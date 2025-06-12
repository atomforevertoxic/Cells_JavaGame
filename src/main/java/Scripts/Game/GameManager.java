package Scripts.Game;

import Scripts.Events.*;
import Scripts.Observers.ExitCellObserver;
import Scripts.Utils.LevelLoader;
import Scripts.View.LevelSelectWindow;
import Scripts.View.MainMenuWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final boolean[] levelStates;

    private Level currentLevel;

    private final ExitCellObserver exitCellObserver = new ExitCellObserver(this);
    private final LevelLoader levelLoader = new LevelLoader(this);
    private final GameView gameView = new GameView();

    public GameManager()
    {
        // [TODO] сохранение прогресса пройденных уровней - потом переписать
        levelStates = new boolean[]{true, false, false, false, false};
    }


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
        gameView.switchWindow(() -> {
            return new MainMenuWindow(this);
        });
    }

    public void openLevelSelect() {
        gameView.switchWindow(() -> {
            return new LevelSelectWindow(this);
        });
    }




    public void endCurrentLevel() {
        unlockLevel(currentLevel.number());
        fireLevelCompleted();
    }

    public void startLevel(int level) {
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

    public boolean isLevelExists(int levelId) {
        List<LevelLoader.LevelConfig> levels = LevelLoader.loadLevels();
        return levels != null && levels.stream().anyMatch(l -> l.id == levelId);
    }



    // -------------------- События --------------------

    private final ArrayList<ILevelCompletedListener> levelCompletedListeners = new ArrayList<>();

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
            listener.showResultWindow(event, this);
        }
    }

    public void unlockLevel(int level)
    {
        if (level< levelStates.length)
        {
            levelStates[level] = true;
        }
    }

    public boolean getLevelState(int levelNumber)
    {
        return levelStates[levelNumber];
    }

}