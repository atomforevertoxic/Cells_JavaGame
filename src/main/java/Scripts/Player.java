package Scripts;

import Scripts.Cells.*;

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

    public boolean researchCell(AbstractCell cell)
    {
        boolean win = false;

        Move(cell);
        if (cell instanceof Cell c) {
            handleRegularCell(c);
        }
//        else if (cell instanceof TeleportCell teleportCell)
//        {
//            for (AbstractCell neighbour: teleportCell.GetNeighbours())
//            {
//                if (!(neighbour.IsWall() && neighbour instanceof ExitCell && neighbour.))
//                {
//
//                }
//            }
//
//        }
        else if (cell instanceof ExitCell exitCell) {
            win = exitCell.fireCheckLevelRules(GetKeys());
        }
        return win;
    }

    private void handleRegularCell(Cell cell)
    {
        TakeKeyFromCell(cell);
        cell.setPassed();
    }
}
