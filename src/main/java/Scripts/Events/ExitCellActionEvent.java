package Scripts.Events;

import Scripts.Cells.Key;

import java.util.EventObject;
import java.util.List;

public class ExitCellActionEvent extends EventObject
{
    private List<Key> _keys;

    public void SetKeys(List<Key> keys)
    {
        _keys = keys;
    }

    public List<Key> GetKeys()
    {
        return _keys;
    }
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
