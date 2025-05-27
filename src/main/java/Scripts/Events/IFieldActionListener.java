package Scripts.Events;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface IFieldActionListener extends EventListener
{
    void PLayerMovedInCell(@NotNull FieldActionEvent event);
}
