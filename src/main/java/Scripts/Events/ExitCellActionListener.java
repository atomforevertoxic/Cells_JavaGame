package Scripts.Events;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface ExitCellActionListener extends EventListener
{
    boolean fireGameRulesPassed(@NotNull ExitCellActionEvent event);
}
