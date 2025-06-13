package Scripts;

import Scripts.Cells.*;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private AbstractCell _cell;

    private final List<Key> _keys = new ArrayList<>();


    public void SetCell(AbstractCell cell)
    {
        _cell = cell;
        _cell.SetPlayer(this);
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

    public void moveTo(AbstractCell cell)
    {
        if (_cell!=null) _cell.unsetPlayer();
        SetCell(cell);
        cell.SetPlayer(this);
    }


    public void handleRegularCell(Cell cell)
    {
        TakeKeyFromCell(cell);
        cell.setPassed();
    }
}
