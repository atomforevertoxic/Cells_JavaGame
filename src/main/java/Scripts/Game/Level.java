package Scripts.Game;

import Scripts.Observers.LevelCompletedObserver;
import Scripts.Game.GameMenuBar;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Level {
    private final LevelModel model;
    private final LevelView view;
    private final int number;
    private final GameManager gameManager;
    private final JFrame frame;

    public Level(int levelNumber, String name, int rows, int cols,
                 List<Point> wallPositions,
                 List<Point> keyPositions,
                 Point startPosition,
                 Point exitPosition,
                 List<Point> teleportPositions,
                 int xOffset, int yOffset,
                 GameManager gameManager) {

        this.number = levelNumber + 1;
        this.gameManager = gameManager;
        gameManager.setCurrentLevel(this);

        this.model = new LevelModel(rows, cols, wallPositions, keyPositions,
                startPosition, exitPosition, teleportPositions);

        this.frame = new JFrame(name + " - Level " + number);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        GameMenuBar menuBar = new GameMenuBar(
                this::restartLevel,
                this::goToMenu,
                () -> System.exit(0)
        );
        frame.setJMenuBar(menuBar);

        this.view = new LevelView(model, null, frame, xOffset, yOffset);
        LevelController controller = new LevelController(model, view, gameManager);

        view.renderField(controller);
        frame.setSize(1200, 800);
        frame.setResizable(false);
        frame.add(view.getPanel());
        frame.setVisible(true);

        model.getCell(startPosition.x, startPosition.y).ifPresent(cell -> {
            cell.SetPlayer(model.getPlayer());
            view.update(model.getPlayer());
        });
    }

    private void goToMenu()
    {
        if (frame != null) {
            frame.dispose();
            gameManager.openMainMenu();
        }
    }

    private void restartLevel() {
        if (frame != null) {
            frame.dispose();
            gameManager.startLevel(number-1);
        }
    }

    public int number() {
        return number;
    }

    public LevelView getView() {
        return view;
    }
}