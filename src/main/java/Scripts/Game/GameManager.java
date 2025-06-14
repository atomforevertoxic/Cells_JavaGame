package Scripts.Game;

import Scripts.Events.*;
import Scripts.Observers.ExitCellObserver;
import Scripts.Utils.LevelLoader;
import Scripts.View.LevelSelectWindow;
import Scripts.View.MainMenuWindow;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final boolean[] levelStates = new boolean[]{true, false, false, false, false};

    private Level currentLevel;

    private final ExitCellObserver exitCellObserver = new ExitCellObserver(this);
    private final LevelLoader levelLoader = new LevelLoader(this);
    private final GameView gameView = new GameView();

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
        gameView.switchWindow(() -> new MainMenuWindow(this));
    }

    public void openLevelSelect() {
        gameView.switchWindow(() -> new LevelSelectWindow(this));
    }


    public void endCurrentLevel() {
        unlockLevel(currentLevel.number());
        fireLevelCompleted();
    }

    public void startLevel(int level) {
        Level currentLevel = levelLoader.startLevelFromJson(level);
        setCurrentLevel(currentLevel);
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