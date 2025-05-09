package Scripts.Cells;

import Scripts.Player;

import java.util.ArrayList;
import java.util.List;

public class AbstractCell
{

    private int q; // смещение по горизонтали
    private int r; // смещение по диагонали

    private Player _player;
    private boolean _isWall;
    private List<AbstractCell> _neighbours = new ArrayList<>();

    public AbstractCell(int q, int r) {
        this.q = q;
        this.r = r;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public void setR(int r) {
        this.r = r;
    }


    public void SetPlayer(Player player)
    {
        _player = player;
    }

    public Player GetPlayer()
    {
        return _player;
    }

    public void SetWall()
    {
        _isWall = true;
    }

    public boolean IsWall()
    {
        return _isWall;
    }


    public void SetNeighbour(AbstractCell neighbour)
    {
        if (!GetNeighbours().contains(neighbour))
        {
            _neighbours.add(neighbour);
            neighbour.SetNeighbour(this);
        }
    }

    public List<AbstractCell> GetNeighbours()
    {
        return _neighbours;
    }

    public boolean IsNeighbourOf(AbstractCell cell)
    {
        return _neighbours.contains(cell);
    }
}
