package Scripts.Cells;

public class Cell
{
    private Key _key;

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
