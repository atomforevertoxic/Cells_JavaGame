package Scripts.Events;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface GameActionListener extends EventListener {

    void GameEnded(@NotNull GameActionEvent event);

}
