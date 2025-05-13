package Scripts.Game;

import Scripts.Cells.AbstractCell;
import Scripts.Cells.Cell;
import Scripts.Cells.ExitCell;
import Scripts.Cells.Key;
import Scripts.Player;
import Scripts.View.HexButton;
import Scripts.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Level {

    private List<AbstractCell> field = new ArrayList<>();
    private List<Key> keys = new ArrayList<>();
    private Player player;

    private int rows;
    private int cols;

    private int offsetX = 150; // смещение слева
    private int offsetY = 450; // смещение сверху

    private JPanel panel;
    private List<HexButton> allButtons = new ArrayList<>();
    private Map<String, HexButton> buttonMap = new HashMap<>();

    private List<Point> wallPositions;
    private List<Point> keyPositions; // потом и тип ключа сюда можно добавить
    private Point startPosition;
    private Point exitPosition;

    public Level(int rows, int cols,
                 List<Point> wallPositions,
                 List<Point> keyPositions,
                 Point startPosition,
                 Point exitPosition
                 ) {
        this.wallPositions = wallPositions != null ? wallPositions : new ArrayList<>();
        this.keyPositions = keyPositions != null ? keyPositions : new ArrayList<>();
        this.startPosition = startPosition;
        this.exitPosition = exitPosition;
        this.rows = rows;
        this.cols = cols;

        this.player = new Player();

        JFrame frame = new JFrame("Hexagonal Level");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        panel = new JPanel(null);
        frame.add(panel);

        generateField();

        frame.setVisible(true);
    }

    private void generateField() {

        for (int r = 0; r < rows; r++) {
            for (int q = 0; q < cols; q++) {
                Cell cell = new Cell(q, r);
                field.add(cell);
            }
        }

        // Установка стен по заданным позициям
        placeObjectsOnField();

        for (AbstractCell c : field) {
            setNeighboursTo(c);
        }


        drawHexButtons();

    }

    private void placeObjectsOnField()
    {
        for (int i = 0; i < field.size(); i++)
        {
            AbstractCell c = field.get(i);
            if (wallPositions.contains(new Point(c.getQ(), c.getR())))
            {
                c.SetWall();
            }
            else if (keyPositions.contains(new Point(c.getQ(), c.getR())))
            {
                setKeyCell((Cell)c);
            }
            else if (c.getQ() == startPosition.x && c.getR() == startPosition.y)
            {
                ((Cell) c).SetPlayer(player);
            }
            else if(c.getQ() == exitPosition.x && c.getR() == exitPosition.y)
            {
                ExitCell exitCell = new ExitCell(keys, c);
                field.set(i, exitCell);
            }
        }
    }

    private void setKeyCell(Cell keyCell) {
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

            // Проверяем, что сосед внутри границ
            if (neighborQ < 0 || neighborQ >= cols || neighborR < 0 || neighborR >= rows) {
                continue;
            }

            for (AbstractCell c : field) {
                if (c.getQ() == neighborQ && c.getR() == neighborR) {
                    host.SetNeighbour(c);
                    break;
                }
            }
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
                btn.setCharacter('^');
            } else if (cell.IsWall()) {
                btn.setBackground(Color.GRAY);
            } else {
                btn.setBackground(Color.ORANGE);
                if (cell instanceof Cell && ((Cell) cell).GetKey() != null) {
                    btn.setCharacter('l');
                }
            }

            btn.addActionListener(new ClickListener());
            panel.add(btn);
        }

        handleButtonClick(getButtonByPosition(startPosition));

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

    private List<HexButton> getNeighborButtons(AbstractCell cell) {
        List<HexButton> neighbours = new ArrayList<>();
        if (cell == null) return neighbours;

        int q = cell.getQ();
        int r = cell.getR();

        for (Direction dir : Direction.values()) {
            int neighborQ = q + dir.getDQ();
            int neighborR = r + dir.getDR();
            String key = neighborQ + "," + neighborR;
            HexButton neighborBtn = buttonMap.get(key);
            if (neighborBtn != null) {
                neighbours.add(neighborBtn);
            }
        }
        return neighbours;
    }

    private Point axialToPixel(AbstractCell cell, int size) {
        int q = cell.getQ();
        int r = cell.getR();

        double x = size * 3.0 / 2.0 * q + offsetX;
        double y = -size * Math.sqrt(3) * (r + q / 2.0) + offsetY;
        return new Point((int) x, (int) y);
    }

    private HexButton getButtonByPosition(Point position) {
        if (position == null) return null;
        String key = position.x + "," + position.y;
        return buttonMap.get(key);
    }

    private void handleButtonClick(HexButton btn) {
        //btn.click();

        AbstractCell cell = getCellByButton(btn);
        if (cell == null) return;

        if (cell instanceof Cell)
        {
            ((Cell) cell).setPassed();
        }
        setAllButtonsEnable(false);
        btn.setBackground(Color.RED);
        makeNeighboursEnabled(cell);
    }

    private void setAllButtonsEnable(boolean activity) {
        for (HexButton btn : allButtons) {

            if (btn.getBackground()==Color.BLUE
                || btn.getBackground()==Color.RED)
            {
                if (btn.getBackground()==Color.BLUE)
                {
                    btn.setBackground(Color.ORANGE);
                    if (getCellByButton(btn) instanceof ExitCell)
                    {
                        btn.setBackground(Color.GREEN);
                    }
                }
                if (btn.getBackground()==Color.RED)
                {
                    btn.setBackground(Color.LIGHT_GRAY);
                }
                btn.setEnabled(activity);
            }

        }
    }

    private void makeNeighboursEnabled(AbstractCell host) {
        List<HexButton> neighbors = getNeighborButtons(host);
        for (HexButton button : neighbors) {
            //в hexbutton есть cell() - на будущее
            AbstractCell neighbour = getCellByButton(button);

            if (neighbour instanceof Cell
                    && (((Cell) neighbour).getPassedInfo()
                    || neighbour.IsWall()))
            {
                continue;
            }

            button.setEnabled(true);
            button.setBackground(Color.BLUE);
        }
    }

    // ------------------------- Реагируем на действия игрока ----------------------

    private class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            HexButton btn = (HexButton) e.getSource();
            handleButtonClick(btn);
        }
    }
}