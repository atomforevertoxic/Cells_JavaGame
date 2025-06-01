package Scripts.Cells;

import Scripts.Game.LevelModel;
import Scripts.Player;

public class Cell extends AbstractCell
{
    private Key _key;
    private boolean _isPassed = false;

    public Cell(int q, int r) {
        super(q, r);
    }

    public boolean SetKey(Key key)
    {
        if (_key!=null) return false;

        _key = key;
        return true;
    }

    public boolean DeleteKey()
    {
        if (_key==null) return false;

        _key=null;
        return true;
    }

    public Key GetKey()
    {
        return _key;
    }


    public void setPassed()
    {
        _isPassed = true;
    }

    public boolean getPassedInfo()
    {
        return _isPassed;
    }

    @Override
    public boolean handlePlayerInteraction(Player player, LevelModel model) {
        player.handleRegularCell(this);
        return false;
    }
}
