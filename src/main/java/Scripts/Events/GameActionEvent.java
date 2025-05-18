package Scripts.Events;

import Scripts.View.ResultWindow;
import org.jetbrains.annotations.NotNull;
import java.util.EventObject;

public class GameActionEvent extends EventObject {
    private boolean isVictory;
    private int levelCompleted;
    private String message;

    /**
     * Constructs a game action event
     * @param source the object that triggered the event (usually GameManager)
     * @throws IllegalArgumentException if source is null
     */
    public GameActionEvent(@NotNull Object source) {
        super(source);
        this.isVictory = false;
        this.levelCompleted = 0;
        this.message = "";
    }

    // ------------------ Getters and Setters ------------------

    public boolean isVictory() {
        return isVictory;
    }

    public void setVictory(boolean victory) {
        isVictory = victory;
    }

    public int getLevelCompleted() {
        return levelCompleted;
    }

    public void setLevelCompleted(int levelCompleted) {
        this.levelCompleted = levelCompleted;
        showResultWindow(); // Автоматический показ окна при установке уровня
    }

    private void showResultWindow() {
        if (this.isVictory) {
            ResultWindow resultWindow = new ResultWindow(this);
            resultWindow.showResult();
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // ------------------ Utility Methods ------------------

    /**
     * Creates victory event with default settings
     */
    public static GameActionEvent createVictoryEvent(Object source) {
        GameActionEvent event = new GameActionEvent(source);
        event.setVictory(true);
        event.setMessage("Level completed!");
        return event;
    }

    /**
     * Creates defeat event with default settings
     */
    public static GameActionEvent createDefeatEvent(Object source) {
        GameActionEvent event = new GameActionEvent(source);
        event.setVictory(false);
        event.setMessage("Game over!");
        return event;
    }

    @Override
    public String toString() {
        return String.format("GameActionEvent[victory=%s, score=%d, level=%d, message='%s']",
                isVictory, levelCompleted, message);
    }
}