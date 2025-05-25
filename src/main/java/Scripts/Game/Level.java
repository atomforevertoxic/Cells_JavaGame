package Scripts.Game;

import Scripts.Cells.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Level {
    private final LevelModel model;
    private final LevelView view;
    private final LevelController controller;

    public Level(int rows, int cols,
                 List<Point> wallPositions,
                 List<Point> keyPositions,
                 Point startPosition,
                 Point exitPosition,
                 GameManager gameManager) {

        this.model = new LevelModel(rows, cols, wallPositions, keyPositions, startPosition, exitPosition);
        this.view = new LevelView(model, null);
        this.controller = new LevelController(model, view, gameManager);

        view.renderField(controller);

        JFrame frame = new JFrame("Hexagonal Level - Level " + gameManager.getCurrentLevel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Добавляем обработчик закрытия окна
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // При закрытии уровня возвращаемся в главное меню
                gameManager.openMainMenu();
            }
        });

        frame.setSize(1200, 800);
        frame.setResizable(false);
        frame.add(view.getPanel());
        frame.setVisible(true);


        model.getCell(startPosition.x, startPosition.y).ifPresent(cell -> {
            if (cell instanceof Cell) ((Cell)cell).SetPlayer(model.getPlayer());
            view.updateAllButtons();
            controller.enableAdjacentButtons(cell);
        });
    }
}