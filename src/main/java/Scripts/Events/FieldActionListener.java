package Scripts.Events;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface FieldActionListener extends EventListener
{
    void PLayerMovedInCell(@NotNull FieldActionEvent event);
}
