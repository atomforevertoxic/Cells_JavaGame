package Scripts.Observers;

import Scripts.Events.ILevelCompletedListener;
import Scripts.Events.LevelCompletedEvent;
import Scripts.Game.GameManager;
import Scripts.View.ResultWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class LevelCompletedObserver implements ILevelCompletedListener
{
    public void showResultWindow(@NotNull LevelCompletedEvent event, GameManager gm) {
        SwingUtilities.invokeLater(() -> {
            ResultWindow resultWindow = new ResultWindow(event, gm);
            resultWindow.showResult();
        });
    }

}
