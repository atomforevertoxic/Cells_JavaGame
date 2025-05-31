package Scripts;

import Scripts.Cells.AbstractCell;
import Scripts.Cells.Cell;
import Scripts.Cells.ExitCell;
import Scripts.Cells.Key;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private AbstractCell _cell;

    private List<Key> _keys = new ArrayList<Key>();


    public void SetCell(AbstractCell cell)
    {
        _cell = cell;
    }

    public AbstractCell GetCell()
    {
        return _cell;
    }

    public void TakeKeyFromCell(Cell cell)
    {
        if (cell.GetKey()!=null)
        {
            _keys.add(cell.GetKey());
            cell.DeleteKey();
        }
    }

    public List<Key> GetKeys()
    {
        return _keys;
    }

    public void Move(AbstractCell cell)
    {
        if (_cell!=null) _cell.unsetPlayer();
        SetCell(cell);
        cell.SetPlayer(this);
    }
}
