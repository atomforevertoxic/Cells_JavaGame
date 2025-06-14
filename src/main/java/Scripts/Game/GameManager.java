package Scripts.Game;

import Scripts.Events.*;
import Scripts.Observers.ExitCellObserver;
import Scripts.Observers.LevelCompletedObserver;
import Scripts.Utils.LevelLoader;
import Scripts.View.LevelSelectWindow;
import Scripts.View.MainMenuWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameManager {
    private final LevelProgressService progressService;
    private Level currentLevel;

    private final ExitCellObserver exitCellObserver = new ExitCellObserver(this);
    private final LevelLoader levelLoader = new LevelLoader(this);
    private final GameView gameView = new GameView();

    public GameManager() {
        int totalLevels = Objects.requireNonNull(LevelLoader.loadLevels()).size();
        this.progressService = new LevelProgressService(totalLevels, true);
        addLevelCompletedListeners(new LevelCompletedObserver());
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
        gameView.switchWindow(() -> new MainMenuWindow(this));
    }

    public void openLevelSelect() {
        gameView.switchWindow(() -> new LevelSelectWindow(this));
    }


    public void endCurrentLevel() {
        progressService.completeLevel(currentLevel.number()-1);
        fireLevelCompleted();
    }

    public boolean isLevelUnlocked(int levelId) {
        return progressService.isLevelUnlocked(levelId);
    }

    public void unlockLevel(int levelId) {
        progressService.completeLevel(levelId-1);
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

}