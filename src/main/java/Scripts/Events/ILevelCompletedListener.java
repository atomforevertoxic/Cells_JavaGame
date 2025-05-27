package Scripts.Events;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface ILevelCompletedListener extends EventListener {

    boolean fireGameRulesPassed(@NotNull ExitCellActionEvent event);

    void GameEnded(@NotNull LevelCompletedEvent event);

    void onGameAction(LevelCompletedEvent event);
}
