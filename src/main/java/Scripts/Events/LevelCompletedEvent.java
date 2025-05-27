package Scripts.Events;

import Scripts.Game.GameManager;
import Scripts.View.ResultWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EventObject;

public class LevelCompletedEvent extends EventObject {
    private String message = "";
    private int levelCompleted;
    private GameManager gameManager = new GameManager();


    public LevelCompletedEvent(@NotNull Object source, int levelCompleted) {
        super(source);
        this.levelCompleted = levelCompleted;
    }

    // ------------------ Getters and Setters ------------------

    public int getLevelCompleted() {
        return levelCompleted;
    }

    public void setLevelCompleted(int levelCompleted) {
        this.levelCompleted = levelCompleted;
        showResultWindow();
    }

    private void showResultWindow() {
        ResultWindow resultWindow = new ResultWindow(this);
        resultWindow.showResult();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static LevelCompletedEvent createVictoryEvent(Object source, int levelCompleted) {
        LevelCompletedEvent event = new LevelCompletedEvent(source, levelCompleted);
        event.setMessage("Level " + event.levelCompleted + " completed!");
        return event;
    }

}