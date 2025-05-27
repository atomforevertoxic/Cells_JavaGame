package Scripts.Events;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface IExitCellActionListener extends EventListener
{
    void checkLevelRules(@NotNull ExitCellActionEvent event);
}
