package Scripts.Events;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface IExitCellActionListener extends EventListener
{
    boolean checkLevelRules(@NotNull ExitCellActionEvent event);
}
