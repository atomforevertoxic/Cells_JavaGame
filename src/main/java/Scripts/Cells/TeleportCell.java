package Scripts.Cells;

import Scripts.Game.LevelModel;
import Scripts.Player;
import Scripts.View.HexButton;

import java.awt.*;

public class TeleportCell extends AbstractCell
{

    public TeleportCell(int q, int r) {
        super(q, r);
    }

    public TeleportCell(AbstractCell cell) {
        super(cell.getQ(), cell.getR());
    }

    public TeleportCell(Point pos) {
        super(pos);
    }


    public void swapWith(AbstractCell cellToSwap)
    {
        int tempQ= getQ();
        int tempR = getR();

        setQ(cellToSwap.getQ());
        setR(cellToSwap.getR());

        cellToSwap.setQ(tempQ);
        cellToSwap.setR(tempR);
    }

    @Override
    public boolean handlePlayerInteraction(Player player, LevelModel model) {
        AbstractCell newSpot = teleportToEnableNeighbour(model);
        if (newSpot!=null)
        {
            newSpot.handlePlayerInteraction(player, model);
        }
        return false;
    }

    @Override
    public void getButtonAppearance(HexButton btn) {
        btn.setBackground(Color.CYAN);
        btn.setCharacter('O');
    }

    private AbstractCell getFirstEnableNeighbour()
    {
        for (AbstractCell neighbour : GetNeighbours()) {
            if (neighbour.shouldEnableCell()) {
                return neighbour;
            }
        }
        return null;
    }

    protected AbstractCell teleportToEnableNeighbour(LevelModel model)
    {
        AbstractCell activeNeighbour = getFirstEnableNeighbour();
        if (activeNeighbour!=null)
        {
            this.swapWith(activeNeighbour);
            model.reconnectNeighbours(activeNeighbour);
            model.reconnectNeighbours(this  );
        }
        return activeNeighbour;
    }
}
