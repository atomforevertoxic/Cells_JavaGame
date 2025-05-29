package Scripts.Game;

import Scripts.Events.*;
import Scripts.Interfaces.IWindowCreator;
import Scripts.Observers.ExitCellObserver;
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

    private ExitCellObserver exitCellObserver = new ExitCellObserver(this);
    private LevelLoader levelLoader = new LevelLoader(this);
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
        isGameRunning = false; // под вопросом. Возможно удалить
        fireLevelCompleted();
        //fire level completed
    }

    public void startLevel(int level) {
        closeCurrentWindow();
        if (isLevelExists(level)) {
            Level currentLevel = levelLoader.startLevelFromJson(level);
            setCurrentLevel(currentLevel);
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



    // -------------------- События --------------------

    private ArrayList<ILevelCompletedListener> levelCompletedListeners = new ArrayList<>();

    public void addLevelCompletedListeners(ILevelCompletedListener listener) {
        levelCompletedListeners.add(listener);
    }

    public void removeLevelCompletedListeners(ILevelCompletedListener listener) {
        levelCompletedListeners.remove(listener);
    }

    public void fireLevelCompleted() {
        for(ILevelCompletedListener listener: levelCompletedListeners) {
            LevelCompletedEvent event = new LevelCompletedEvent(listener);
            event.setLevelCompleted(currentLevel.number());
            event.setMessage("Level " + currentLevel.number() + " completed!");
            listener.showResultWindow(event);
        }
    }


}