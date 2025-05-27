package Scripts.Game;

import Scripts.Cells.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Level {
    private final LevelModel model;
    private final LevelView view;
    private final ILevelController controller;
    private int number;

    public Level(int levelNumber, int rows, int cols,
                 List<Point> wallPositions,
                 List<Point> keyPositions,
                 Point startPosition,
                 Point exitPosition,
                 GameManager gameManager) {

        number = levelNumber;
        gameManager.setCurrentLevel(this);
        this.model = new LevelModel(rows, cols, wallPositions, keyPositions, startPosition, exitPosition);

        JFrame frame = new JFrame("Hexagonal Level - Level " + number);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.view = new LevelView(model, null, frame);
        this.controller = new ILevelController(model, view, gameManager);

        view.renderField(controller);

        frame.setSize(1200, 800);
        frame.setResizable(false);
        frame.add(view.getPanel());
        frame.setVisible(true);


        model.getCell(startPosition.x, startPosition.y).ifPresent(cell -> {
            if (cell instanceof Cell) cell.SetPlayer(model.getPlayer());
            view.updateAllButtons();
            controller.enableAdjacentButtons(cell);
        });
    }

    public int number()
    {
        return number;
    }
}