package Scripts.Events;

import org.jetbrains.annotations.NotNull;

import java.util.EventObject;

public class LevelCompletedEvent extends EventObject {
    private String message;
    private int levelCompleted;


    public LevelCompletedEvent(@NotNull Object source) { super(source); }

    // ------------------ Getters and Setters ------------------

    public int getLevelCompleted() {
        return levelCompleted;
    }

    public void setLevelCompleted(int levelCompleted) {
        this.levelCompleted = levelCompleted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}