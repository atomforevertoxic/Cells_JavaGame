package Scripts.Observers;

import Scripts.Events.ILevelCompletedListener;
import Scripts.Events.LevelCompletedEvent;
import Scripts.View.ResultWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class LevelCompletedObserver implements ILevelCompletedListener
{
    public void showResultWindow(@NotNull LevelCompletedEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResultWindow resultWindow = new ResultWindow(event);
            resultWindow.showResult();
        });
    }
}
