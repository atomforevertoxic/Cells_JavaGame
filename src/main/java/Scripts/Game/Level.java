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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CancellationException;

public class Level
{

    private int _keyCounter;
    private int _cellCounter;

    private List<AbstractCell> _field = new ArrayList<>();
    private List<Key> _keys = new ArrayList<>();
    private Player _player;


    private int offsetX = 50; // смещение слева
    private int offsetY = 50; // смещение сверху

    // Панель для отображения
    private JPanel panel;

    public Level(int keyCounter, int cellCounter) {
        _keyCounter = keyCounter;
        _cellCounter = cellCounter;

        _player = new Player();

        // Создаем окно
        JFrame frame = new JFrame("Hexagonal Level");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        panel = new JPanel(null); // абсолютное позиционирование
        frame.add(panel);

        GenerateField();

        frame.setVisible(true);
    }

    public void GenerateField() {
        while (_field.size() != _cellCounter) {
            AbstractCell cellToExecute;
            if (_field.isEmpty()) {
                Cell spawnCell = new Cell(0,0);
                SetSpawnCell(spawnCell);
                cellToExecute = spawnCell;
                SetNeighboursTo(cellToExecute);
            } else {
                Random rand = new Random();
                int randIndex = rand.nextInt(_field.size());

                AbstractCell cell = _field.get(randIndex);

                if (cell.GetNeighbours().isEmpty()) SetNeighboursTo(cell);

                //рандомное решение - ключ или стена
                if (rand.nextBoolean() && cell instanceof Cell) SetKeyCell((Cell) cell);
                else if (rand.nextBoolean()) cell.SetWall();
            }
        }

        if (!_field.isEmpty()) {

            AbstractCell lastCell = _field.getLast();
            int index = _field.indexOf(lastCell);
            ExitCell exitCell = new ExitCell(_keys, lastCell);
            _field.set(index, exitCell);
        }

        drawHexButtons();
    }

    private void SetSpawnCell(AbstractCell spawnCell) {
        spawnCell.SetPlayer(_player);
        _field.add(spawnCell);
    }

    private void SetNeighboursTo(AbstractCell host) {
        int q = host.getQ();
        int r = host.getR();

        // направления для соседей
        int[][] directions = {
                {0, -1},    // верх
                {1, -1},    // верх правый
                {1, 0},     // правый
                {0, 1},     // низ
                {-1, 1},    // ниж левый
                {-1, 0}     // левый
        };

        for (int[] dir : directions) {

            if (_field.size()>=_cellCounter) break;

            int neighborQ = q + dir[0];
            int neighborR = r + dir[1];

            AbstractCell neighbor = new AbstractCell(neighborQ, neighborR);

            _field.add(neighbor);
            host.SetNeighbour(neighbor);
        }
    }

    private void SetKeyCell(Cell keyCell) {
        _keyCounter--;
        Key key = new Key();
        keyCell.SetKey(key);
        _keys.add(key);
    }

    public List<AbstractCell> GetField() {
        return _field;
    }


    private void drawHexButtons() {
        // Очистить предыдущие кнопки, если есть
        panel.removeAll();

        int size = 30; // радиус гексагона, можно вынести в поле класса
        int btnSize = size * 2; // диаметр

        for (AbstractCell cell : _field) {
            // Расчет позиции для кнопки
            Point center = axialToPixel(cell, size);
            int x = 300 + (int) center.x;
            int y = 50 + (int) center.y;

            HexButton btn = new HexButton(' ');
            btn.setBounds(x - btnSize / 2, y - btnSize / 2, btnSize, btnSize);

            if (cell.GetPlayer()!=null) { //spawn
                btn.setBackground(Color.GREEN);
            } else if (cell instanceof ExitCell) { //exit
                btn.setBackground(Color.blue);
            }

            // Обработка клика
            btn.addActionListener(e -> {
                btn.click();
                System.out.println("Ячейка: " + cell.toString());
                
            });

            panel.add(btn);
        }

        panel.revalidate();
        panel.repaint();
    }

    private Point axialToPixel(AbstractCell cell, int size) {
        int q = cell.getQ();
        int r = cell.getR();

        double x = size * 3/2.0 * q + offsetX;
        double y = -size * Math.sqrt(3) * (r + q / 2.0) + offsetY;
        return new Point((int) x, (int) y);
    }


}
