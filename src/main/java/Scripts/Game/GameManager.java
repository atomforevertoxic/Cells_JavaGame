package Scripts.Game;

import Scripts.Events.ExitCellActionEvent;
import Scripts.Events.ExitCellActionListener;
import Scripts.Events.GameActionEvent;
import Scripts.Events.GameActionListener;
import Scripts.Utils.LevelLoader;
import Scripts.View.LevelSelectWindow;
import Scripts.View.MainMenuWindow;
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

    private final ExitCellObserver exitCellObserver = new ExitCellObserver();
    private final List<GameActionListener> gameActionListeners = new ArrayList<>();

    public ExitCellObserver getExitCellObserver()
    {
        return exitCellObserver;
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

    public void setCurrentActiveWindow(JFrame window) {
        this.currentActiveWindow = window;
    }

    public void endGame(boolean isVictory) {
        if (!isGameRunning) return;
        isGameRunning = false;
        fireGameEnded(isVictory);
    }

    private void fireGameEnded(boolean isVictory) {
        GameActionEvent event = isVictory
                ? GameActionEvent.createVictoryEvent(this)
                : GameActionEvent.createDefeatEvent(this);
        event.setLevelCompleted(currentLevel);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    @FunctionalInterface
    private interface WindowCreator {
        JFrame create();
    }

    public class ExitCellObserver implements ExitCellActionListener {
        @Override
        public boolean fireGameRulesPassed(@NotNull ExitCellActionEvent event) {
            if (isGameRunning) {
                endGame(true);
                return true;
            }
            return false;
        }
    }
}