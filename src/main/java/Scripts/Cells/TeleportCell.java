package Scripts.Cells;

import Scripts.Game.LevelModel;
import Scripts.Player;

public class TeleportCell extends AbstractCell
{

    public TeleportCell(int q, int r) {
        super(q, r);
    }

    public TeleportCell(AbstractCell cell) {
        super(cell.getQ(), cell.getR());
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
        AbstractCell activeNeighbour = getFirstEnableNeighbour();
        if (activeNeighbour!=null)
        {
            this.swapWith(activeNeighbour);
            model.reconnectNeighbours(this);
            activeNeighbour.handlePlayerInteraction(player, model);
            //player.handleRegularCell((Cell) activeNeighbour);
        }
        return false;
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
}
