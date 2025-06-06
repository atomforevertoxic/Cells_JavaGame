package Scripts.Cells;

import Scripts.Game.LevelModel;
import Scripts.Game.LevelView;
import Scripts.Player;
import Scripts.View.HexButton;

import java.awt.*;

public class VerticalTeleportCell extends TeleportCell{
    public VerticalTeleportCell(int q, int r) {
        super(q, r);
    }

    public VerticalTeleportCell(Point pos) {
        super(pos);
    }

    @Override
    protected AbstractCell teleportToEnableNeighbour(LevelModel model) {

        AbstractCell topNeighbour = getFirstEnableNeighbour();

        if (topNeighbour != null) {
            swapWith(topNeighbour);
            model.reconnectNeighbours(topNeighbour);
            model.reconnectNeighbours(this);
        }
        return topNeighbour;
    }

    @Override
    protected AbstractCell getFirstEnableNeighbour()
    {
        for (AbstractCell neighbour : GetNeighbours()) {
            if (neighbour.getR() > this.getR() && neighbour.getQ()==this.getQ()) {
                return neighbour;
            }
        }
        return null;
    }


    @Override
    public void getButtonAppearance(HexButton btn) {
        btn.setBackground(Color.MAGENTA);
        btn.setCharacter('↑');
    }

    @Override
    public boolean handlePlayerInteraction(Player player, LevelModel model) {

        return false;
    }

    public boolean isUpstairsNeighbourEnable()
    {
        return getFirstEnableNeighbour() != null;
    }

    public boolean tryToLeaveCell(LevelModel model, Player player)
    {
        AbstractCell newSpot = teleportToEnableNeighbour(model);
        if (newSpot!=null)
        {
            return newSpot.handlePlayerInteraction(player, model);
        }
        return false;
    }
}
