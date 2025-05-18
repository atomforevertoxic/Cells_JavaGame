package Scripts.Game;

import Scripts.Events.ExitCellActionEvent;
import Scripts.Events.ExitCellActionListener;
import Scripts.Events.GameActionEvent;
import Scripts.Events.GameActionListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    // Состояние игры
    private boolean isGameRunning = true;
    private int currentLevel = 1;

    // Подсистемы
    private final ExitCellObserver exitCellObserver = new ExitCellObserver();
    private final List<GameActionListener> gameActionListeners = new ArrayList<>();

    // Основные методы управления игрой
    public void startGame() {
        isGameRunning = true;
        startLevel(currentLevel);
    }

    public void startLevel(int level) {
        this.currentLevel = level;
        System.out.println("Starting level " + level);
        // Здесь можно добавить логику загрузки уровня
    }

    public void endGame(boolean isVictory) {
        if (!isGameRunning) return;

        isGameRunning = false;
        fireGameEnded(isVictory);
    }

    // Обработка событий от ExitCell
    private class ExitCellObserver implements ExitCellActionListener {
        @Override
        public boolean fireGameRulesPassed(@NotNull ExitCellActionEvent event) {
            if (isGameRunning) { // Добавляем очки за уровень
                endGame(true); // Победа
                return true;
            }
            return false;
        }
    }

    // Управление слушателями событий
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

    // Геттеры
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