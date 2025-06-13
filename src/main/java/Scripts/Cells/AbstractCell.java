package Scripts.Cells;

import Scripts.Game.LevelModel;
import Scripts.Interfaces.IInteractable;
import Scripts.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCell implements IInteractable
{

    private int q; // смещение по горизонтали
    private int r; // смещение по диагонали

    private Player _player;
    private final List<AbstractCell> _neighbours = new ArrayList<>();

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

    public void unsetPlayer()
    {
        _player = null;
    }

    public Player GetPlayer()
    {
        return _player;
    }

    // Приглядеться!!!!
    public void SetNeighbour(AbstractCell neighbour)
    {
        if (!GetNeighbours().contains(neighbour))
        {
            _neighbours.add(neighbour);
            neighbour.SetNeighbour(this);
        }
    }

    public boolean shouldEnableCell() {
        return this instanceof ExitCell || this instanceof TeleportCell ||
                (this instanceof Cell cell && !cell.getPassedInfo());
    }

    public List<AbstractCell> GetNeighbours()
    {
        return _neighbours;
    }

    public boolean IsNeighbourOf(AbstractCell cell)
    {
        return _neighbours.contains(cell);
    }

    public abstract boolean handlePlayerInteraction(Player player, LevelModel model);
}
