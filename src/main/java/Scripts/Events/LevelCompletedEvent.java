package Scripts.Events;

import Scripts.Game.GameManager;
import Scripts.View.ResultWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
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