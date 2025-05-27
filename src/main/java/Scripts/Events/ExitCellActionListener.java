package Scripts.Events;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface ExitCellActionListener extends EventListener
{
    void checkLevelRules(@NotNull ExitCellActionEvent event);
}
