package Scripts.Events;

import Scripts.Cells.Key;

import java.util.EventObject;
import java.util.List;

public class ExitCellActionEvent extends EventObject
{
    private List<Key> _levelKeys;

    public void setLevelKeys(List<Key> keys)
    {
        _levelKeys = keys;
    }

    public List<Key> getLevelKeys()
    {
        return _levelKeys;
    }


    private List<Key> _collectedKeys;

    public void setCollectedKeys(List<Key> keys)
    {
        _collectedKeys = keys;
    }

    public List<Key> getCollectedKeys()
    {
        return _collectedKeys;
    }



    public ExitCellActionEvent(Object source) {
        super(source);
    }
}
