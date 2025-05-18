package Scripts.Game;

import Scripts.Events.ExitCellActionEvent;
import Scripts.Events.ExitCellActionListener;
import Scripts.Events.GameActionEvent;
import Scripts.Events.GameActionListener;
import Scripts.View.LevelSelectWindow;
import Scripts.View.MainMenuWindow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private MainMenuWindow mainMenu;

    private boolean isGameRunning = true;
    private int currentLevel = 1;


    private final ExitCellObserver exitCellObserver = new ExitCellObserver();
    private final List<GameActionListener> gameActionListeners = new ArrayList<>();



    public void startGame() {
        mainMenu = new MainMenuWindow(this);
        openMainMenu();

        //isGameRunning = true;
        //startLevel(currentLevel);
    }

    public void openMainMenu()
    {
        mainMenu.showWindow();
    }


    private LevelSelectWindow levelSelectWindow;

    public void openLevelSelect() {
        if (mainMenu != null) mainMenu.dispose();
        levelSelectWindow = new LevelSelectWindow(this);
        levelSelectWindow.showWindow();
    }

    public void startLevel(int levelNum) {
        if (levelSelectWindow != null) levelSelectWindow.dispose();
        // Ваша логика загрузки уровня
        System.out.println("Загрузка уровня " + levelNum);
    }


    public void endGame(boolean isVictory) {
        if (!isGameRunning) return;

        isGameRunning = false;
        fireGameEnded(isVictory);
    }


    private class ExitCellObserver implements ExitCellActionListener {
        @Override
        public boolean fireGameRulesPassed(@NotNull ExitCellActionEvent event) {
            if (isGameRunning) {
                endGame(true);
                return true;
            }
            return false;
        }
    }

    public void addGameActionListener(@NotNull GameActionListener listener) {
        if (!gameActionListeners.contains(listener)) {
            gameActionListeners.add(listener);
        }
    }

    public void removeGameActionListener(@NotNull GameActionListener listener) {
        gameActionListeners.remove(listener);
    }

    private void fireGameEnded(boolean isVictory) {
        GameActionEvent event = isVictory
                ? GameActionEvent.createVictoryEvent(this)
                : GameActionEvent.createDefeatEvent(this);

        event.setLevelCompleted(currentLevel);
    }


    public ExitCellObserver getExitCellObserver() {
        return exitCellObserver;
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

}