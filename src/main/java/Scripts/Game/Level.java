package Scripts.Game;

import Scripts.Cells.AbstractCell;
import Scripts.Cells.Cell;
import Scripts.Cells.ExitCell;
import Scripts.Cells.Key;
import Scripts.Player;
import Scripts.View.HexButton;

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

    private int offsetX = 50; // смещение слева
    private int offsetY = 50; // смещение сверху

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
        Random rand = new Random();

        // Создаем стартовую клетку
        Cell spawnCell = new Cell(0, 0);
        spawnCell.SetPlayer(player);
        field.add(spawnCell);

        // Генерация остальных клеток
        while (field.size() < cellCounter) {
            // Выбираем случайную существующую клетку
            int randIndex = rand.nextInt(field.size());
            AbstractCell currentCell = field.get(randIndex);

            // Определяем случайно, что добавить: ключ или стена
            if (rand.nextBoolean() && currentCell instanceof Cell) {
                setKeyCell((Cell) currentCell);
            } else if (rand.nextBoolean() && currentCell instanceof Cell) {
                currentCell.SetWall();
            }

            // Расширяем сеть соседних клеток
            setNeighboursTo(currentCell);
        }

        // Устанавливаем клетку выхода
        if (!field.isEmpty()) {
            AbstractCell lastCell = field.get(field.size() - 1);
            int index = field.indexOf(lastCell);
            ExitCell exitCell = new ExitCell(keys, lastCell);
            field.set(index, exitCell);
        }

        // Создаем графические кнопки
        drawHexButtons();

        makeNeighboursEnabled(spawnCell);
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

        int[][] directions = {
                {0, -1},    // top
                {1, -1},    // top-right
                {1, 0},     // right
                {0, 1},     // bottom
                {-1, 1},    // bottom-left
                {-1, 0}     // left
        };

        for (int[] dir : directions) {
            int neighborQ = q + dir[0];
            int neighborR = r + dir[1];

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

            // Вместо лямбды добавляем ваш ClickListener
            btn.addActionListener(new ClickListener());


            panel.add(btn);
        }

        panel.revalidate();
        panel.repaint();

        // Отключить все кнопки по умолчанию
        //setAllButtonsEnabled(false);
    }

    private void handleButtonClick(AbstractCell cell, HexButton btn) {
        if (activeButton != null) {
            // Проверка, что кликнули по соседней
            if (!isNeighbor(cell, getCellByButton(activeButton))) {
                return;
            }

            activeButton.setBackground(Color.LIGHT_GRAY);
        }

        activeButton = btn;
        btn.setBackground(Color.YELLOW);

        // Разрешаем только соседние кнопки
        //setAllButtonsEnabled(false);
        for (HexButton neighborBtn : getNeighborButtons(cell)) {
            neighborBtn.setEnabled(true);
        }
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

        int[][] directions = {
                {0, -1}, {1, -1}, {1, 0},
                {0, 1}, {-1, 1}, {-1, 0}
        };

        for (int[] dir : directions) {
            if (q1 + dir[0] == q2 && r1 + dir[1] == r2) {
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

        int[][] directions = {
                {0, -1},  // top
                {1, -1},  // top-right
                {1, 0},   // right
                {0, 1},   // bottom
                {-1, 1},  // bottom-left
                {-1, 0}   // left
        };

        for (int[] dir : directions) {
            int neighborQ = q + dir[0];
            int neighborR = r + dir[1];
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

            makeNeighboursEnabled(cell);

        }


    }
}