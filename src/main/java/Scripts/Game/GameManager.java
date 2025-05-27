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
    private Level currentLevel;

    private ExitCellObserver exitCellObserver = new ExitCellObserver(this);
    private final List<LevelCompletedListener> levelCompletedListeners = new ArrayList<>();


    public void setCurrentLevel(Level level)
    {
        currentLevel = level;
    }

    public ExitCellActionListener getExitCellObserver()
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

    public void startNextLevel() {
        if (isLevelExists(currentLevel.number()+1)) {
            //startLevelFromJson(currentLevel);
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
        LevelCompletedEvent event = LevelCompletedEvent.createVictoryEvent(this, currentLevel.number());

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
        LevelCompletedEvent event = LevelCompletedEvent.createVictoryEvent(this, currentLevel.number());

        event.setLevelCompleted(currentLevel.number());
    }


    @FunctionalInterface
    private interface WindowCreator {
        JFrame create();
    }
}