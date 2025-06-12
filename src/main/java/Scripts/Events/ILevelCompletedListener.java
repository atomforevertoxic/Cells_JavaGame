package Scripts.Events;

import Scripts.Game.GameManager;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface ILevelCompletedListener extends EventListener {

    void showResultWindow(@NotNull LevelCompletedEvent event, GameManager gm);
}
