package Scripts.Events;

import org.jetbrains.annotations.NotNull;

import java.util.EventObject;

public class GameActionEvent extends EventObject {
    // ------------------ Robot ------------------

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public GameActionEvent(Object source) {
        super(source);
    }
}