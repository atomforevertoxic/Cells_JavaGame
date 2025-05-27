package Scripts.Game;

import Scripts.Events.IExitCellActionListener;
import Scripts.Events.IWindowCreator;
import Scripts.Events.LevelCompletedEvent;
import Scripts.Events.ILevelCompletedListener;
import Scripts.Observers.IExitCellObserver;
import Scripts.Utils.LevelLoader;
import Scripts.View.LevelSelectWindow;
import Scripts.View.MainMenuWindow;
import Scripts.View.ResultWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private MainMenuWindow mainMenu;
    private LevelSelectWindow levelSelectWindow;
    private JFrame currentActiveWindow;

    private boolean isGameRunning = true;
    private Level currentLevel;

    private IExitCellObserver exitCellObserver = new IExitCellObserver(this);
    private final List<ILevelCompletedListener> ILevelCompletedListeners = new ArrayList<>();


    public void setCurrentLevel(Level level)
    {
        currentLevel = level;
    }

    public IExitCellActionListener getExitCellObserver()
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


    private void switchWindow(IWindowCreator creator) {
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
        ILevelCompletedListeners.forEach(listener -> listener.onGameAction(event));
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

}