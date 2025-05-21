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

        view.renderField(controller::handleCellClick);

        JFrame frame = new JFrame("Hexagonal Level");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setResizable(false);
        frame.add(view.getPanel());
        frame.setVisible(true);


        model.getCell(startPosition.x, startPosition.y).ifPresent(cell -> {
            if (cell instanceof Cell) ((Cell)cell).SetPlayer(model.getPlayer());
            view.updateAllButtons();
            controller.enableNeighborButtons();
        });
    }
}