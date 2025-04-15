package Scripts.Cells;

public class Cell extends AbstractCell
{
    private Key _key;
    private boolean _isPassed = true;

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


}
