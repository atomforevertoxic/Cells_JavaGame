package Scripts;

import Scripts.Cells.Cell;
import Scripts.Cells.Key;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private Cell _cell;

    private List<Key> _keys = new ArrayList<Key>();


    public void SetCell(Cell cell)
    {
        _cell = cell;
    }

    public Cell GetCell()
    {
        return _cell;
    }

    public boolean CanMove(Cell cell)
    {
        return true;
    }

    public void TakeKey(Cell cell)
    {
        if (cell.GetKey()!=null)
        {
            _keys.add(cell.GetKey());
        }
    }

    public List<Key> GetKeys()
    {
        return _keys;
    }



}
