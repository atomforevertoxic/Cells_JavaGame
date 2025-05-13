package Scripts.Game;

import Scripts.Cells.AbstractCell;
import Scripts.Cells.Cell;
import Scripts.Cells.ExitCell;
import Scripts.Cells.Key;
import Scripts.Player;
import Scripts.View.HexButton;
import Scripts.Direction;

import javax.print.attribute.standard.PresentationDirection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Level {

    private int keyCounter;
    private int cellCounter;

    private List<AbstractCell> field = new ArrayList<>();
    private List<Key> keys = new ArrayList<>();
    private Player player;

    private int offsetX = 150; // смещение слева
    private int offsetY = 450; // смещение сверху

    private JPanel panel;
    private HexButton activeButton = null;
    private List<HexButton> allButtons = new ArrayList<>();
    private Map<String, HexButton> buttonMap = new HashMap<>();

    public Level(int keyCounter, int cellCounter) {
        this.keyCounter = keyCounter;
        this.cellCounter = cellCounter;

        this.player = new Player();

        JFrame frame = new JFrame("Hexagonal Level");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        panel = new JPanel(null); // абсолютное позиционирование
        frame.add(panel);

        generateField();

        frame.setVisible(true);


    }

    private void generateField() {
        int rows = 5; // количество строк
        int cols = 7; // количество ячеек в строке

        // Создаем клетки построчно
        for (int r = 0; r < rows; r++) {
            for (int q = 0; q < cols; q++) {
                Cell cell = new Cell(q, r);
                field.add(cell);
            }
        }



        for (AbstractCell c : field) {
            if (c.getQ() == cols - 1 && c.getR() == rows - 1) {
                int index = field.indexOf(c);
                ExitCell exitCell = new ExitCell(keys, c);
                field.set(index, exitCell);
                break;
            }
        }

        drawHexButtons();

        for (AbstractCell c : field) {
            if (c.getQ() == 0 && c.getR() == 0) {
                makeNeighboursEnabled(c);
                break;
            }
        }
    }

    private void setKeyCell(Cell keyCell) {
        if (keyCounter <= 0) return;
        keyCounter--;
        Key key = new Key();
        keyCell.SetKey(key);
        keys.add(key);
    }

    private void setNeighboursTo(AbstractCell host) {
        int q = host.getQ();
        int r = host.getR();

        for (Direction dir : Direction.values()) {
            int neighborQ = q + dir.getDQ();
            int neighborR = r + dir.getDR();

            // Проверка, что такой клетки еще нет в списке
            String key = neighborQ + "," + neighborR;
            boolean exists = false;
            for (AbstractCell c : field) {
                if (c.getQ() == neighborQ && c.getR() == neighborR) {
                    exists = true;
                    break;
                }
            }
            if (exists || field.size() >= cellCounter) continue;

            // Создаем новую клетку
            Cell neighborCell = new Cell(neighborQ, neighborR);
            field.add(neighborCell);
            host.SetNeighbour(neighborCell);
        }
    }

    private void drawHexButtons() {
        panel.removeAll();
        allButtons.clear();
        buttonMap.clear();

        int size = 30; // радиус гексагона
        int btnSize = size * 2;

        for (AbstractCell cell : field) {
            Point center = axialToPixel(cell, size);
            int x = 300 + (int) center.x;
            int y = 50 + (int) center.y;


            HexButton btn = new HexButton(' ');
            btn.setBounds(x - btnSize / 2, y - btnSize / 2, btnSize, btnSize);

            btn.setEnabled(false);
            String key = cell.getQ() + "," + cell.getR();
            buttonMap.put(key, btn);
            allButtons.add(btn);

            if (cell.GetPlayer() != null) {
                btn.setBackground(Color.RED);
            } else if (cell instanceof ExitCell) {
                btn.setBackground(Color.GREEN);
            } else if (cell.IsWall())
            {
                btn.setBackground(Color.LIGHT_GRAY);
            }
            else {
                btn.setBackground(Color.ORANGE);
                if (cell instanceof Cell && ((Cell) cell).GetKey()!=null)
                {
                    btn.setCharacter('l');
                }
            }

            btn.addActionListener(new ClickListener());


            panel.add(btn);
        }

        panel.revalidate();
        panel.repaint();

    }


    private AbstractCell getCellByButton(HexButton btn) {
        for (AbstractCell cell : field) {
            String key = cell.getQ() + "," + cell.getR();
            if (buttonMap.get(key) == btn) {
                return cell;
            }
        }
        return null;
    }

    private boolean isNeighbor(AbstractCell c1, AbstractCell c2) {
        if (c1 == null || c2 == null) return false;
        int q1 = c1.getQ(), r1 = c1.getR();
        int q2 = c2.getQ(), r2 = c2.getR();

        for (Direction dir : Direction.values()) {
            if (q1 + dir.getDQ() == q2 && r1 + dir.getDR() == r2) {
                return true;
            }
        }
        return false;
    }

    private List<HexButton> getNeighborButtons(AbstractCell cell) {
        List<HexButton> neighbors = new ArrayList<>();
        if (cell == null) return neighbors;

        int q = cell.getQ();
        int r = cell.getR();

        for (Direction dir : Direction.values()) {
            int neighborQ = q + dir.getDQ();
            int neighborR = r + dir.getDR();
            String key = neighborQ + "," + neighborR;
            HexButton neighborBtn = buttonMap.get(key);
            if (neighborBtn != null) {
                neighbors.add(neighborBtn);
            }
        }
        return neighbors;
    }

    private Point axialToPixel(AbstractCell cell, int size) {
        int q = cell.getQ();
        int r = cell.getR();

        double x = size * 3.0 / 2.0 * q + offsetX;
        double y = -size * Math.sqrt(3) * (r + q / 2.0) + offsetY;
        return new Point((int) x, (int) y);
    }

    private void setAllButtonsEnable(boolean activity)
    {
        for (HexButton btn : allButtons)
        {
            btn.setBackground(Color.ORANGE);
            btn.setEnabled(activity);
        }
    }

    private void makeNeighboursEnabled(AbstractCell host)
    {
        List<HexButton> neighbours = getNeighborButtons(host);

        for (HexButton button : neighbours)
        {
            button.setEnabled(true);
            button.setBackground(Color.BLUE);
        }
    }

    // ------------------------- Реагируем на действия игрока ----------------------

    private class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            HexButton btn = (HexButton) e.getSource();
            btn.click();

            AbstractCell cell = getCellByButton(btn);

            if (cell==null) return;

            setAllButtonsEnable(false);

            makeNeighboursEnabled(cell);

        }


    }
}