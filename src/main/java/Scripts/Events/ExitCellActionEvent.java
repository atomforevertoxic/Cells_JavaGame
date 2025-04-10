package Scripts.Events;

import Scripts.Player;

import java.util.EventObject;

public class ExitCellActionEvent extends EventObject
{
    private Player _player;

    public void SetPlayer(Player player) { _player = player; }

    public Player GetPlayer() { return _player; }


    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ExitCellActionEvent(Object source) {
        super(source);
    }
}
