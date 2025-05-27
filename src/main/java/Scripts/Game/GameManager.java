package Scripts.Game;

import Scripts.Cells.ExitCell;
import Scripts.Events.ExitCellActionEvent;
import Scripts.Events.ExitCellActionListener;
import Scripts.Events.LevelCompletedEvent;
import Scripts.Events.LevelCompletedListener;
import Scripts.Observers.ExitCellObserver;
import Scripts.Utils.LevelLoader;
import Scripts.View.LevelSelectWindow;
import Scripts.View.MainMenuWindow;
import Scripts.View.ResultWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private MainMenuWindow mainMenu;
    private LevelSelectWindow levelSelectWindow;
    private JFrame currentActiveWindow;

    private boolean isGameRunning = true;
    private int currentLevel = 1;

    private ExitCellObserver exitCellObserver = new ExitCellObserver(this);
    private final List<LevelCompletedListener> levelCompletedListeners = new ArrayList<>();

    public ExitCellActionListener getExitCellObserver()
    {
        return (ExitCellActionListener) exitCellObserver;
    }

    public void startGame() {
        openMainMenu();
    }

    public void openMainMenu() {
        switchWindow(() -> {
            mainMenu = new MainMenuWindow(this);
            return mainMenu;
        });
    }

    public void openLevelSelect() {
        switchWindow(() -> {
            levelSelectWindow = new LevelSelectWindow(this);
            return levelSelectWindow;
        });
    }

    public void startLevelFromJson(int levelId) {
        closeCurrentWindow();
        this.currentLevel = levelId;

        LevelLoader.LevelConfig config = loadLevelConfig(levelId);
        if (config == null) {
            openMainMenu(); // Возвращаем в меню при ошибке
            return;
        }

        new Level(
                config.rows,
                config.cols,
                convertPoints2(config.walls),
                convertPoints(config.keys),
                new Point(config.start.q, config.start.r),
                new Point(config.exit.q, config.exit.r),
                this
        );
    }

    private LevelLoader.LevelConfig loadLevelConfig(int levelId) {
        List<LevelLoader.LevelConfig> levels = LevelLoader.loadLevels();
        if (levels == null) {
            System.err.println("Не удалось загрузить уровни!");
            return null;
        }

        LevelLoader.LevelConfig config = levels.stream()
                .filter(l -> l.id == levelId)
                .findFirst()
                .orElse(null);

        if (config == null) {
            System.err.println("Уровень с ID " + levelId + " не найден!");
        }

        return config;
    }

    // переписать
    private List<Point> convertPoints(List<LevelLoader.KeyPosition> positions) {
        return positions.stream()
                .map(w -> new Point(w.q, w.r))
                .toList();
    }

    private List<Point> convertPoints2(List<LevelLoader.WallPosition> positions) {
        return positions.stream()
                .map(w -> new Point(w.q, w.r))
                .toList();
    }

    private void switchWindow(WindowCreator creator) {
        closeCurrentWindow();
        JFrame newWindow = creator.create();
        currentActiveWindow = newWindow;
        newWindow.setVisible(true);
    }

    private void closeCurrentWindow() {
        if (currentActiveWindow != null) {
            currentActiveWindow.dispose();
            currentActiveWindow = null;
        }
    }


    public void endCurrentLevel() {
        isGameRunning = false;
        fireLevelEnded();
        //fire level completed
    }


    public int getCurrentLevel() {
        return currentLevel;
    }

    public void startNextLevel() {
        currentLevel++;
        if (isLevelExists(currentLevel)) {
            startLevelFromJson(currentLevel);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Поздравляем! Вы прошли все уровни!",
                    "Игра завершена",
                    JOptionPane.INFORMATION_MESSAGE);
            openMainMenu();
        }
    }

    private boolean isLevelExists(int levelId) {
        List<LevelLoader.LevelConfig> levels = LevelLoader.loadLevels();
        return levels != null && levels.stream().anyMatch(l -> l.id == levelId);
    }

    public void handleLevelCompletion() {
        LevelCompletedEvent event = LevelCompletedEvent.createVictoryEvent(this, currentLevel);

        fireGameEvent(event);
        showResultWindow(event);
    }

    private void fireGameEvent(LevelCompletedEvent event) {
        // Оповещаем всех слушателей
        levelCompletedListeners.forEach(listener -> listener.onGameAction(event));
    }

    private void showResultWindow(LevelCompletedEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResultWindow resultWindow = new ResultWindow(event);
            resultWindow.showResult();
        });
    }

    private void fireLevelEnded() {
        LevelCompletedEvent event = LevelCompletedEvent.createVictoryEvent(this, currentLevel);

        event.setLevelCompleted(currentLevel);
    }


    @FunctionalInterface
    private interface WindowCreator {
        JFrame create();
    }
}