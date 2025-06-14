package Scripts.Cells;

import Scripts.Game.LevelModel;
import Scripts.Player;
import Scripts.View.HexButton;

import java.awt.*;

public class Wall extends AbstractCell{
    public Wall(int q, int r) {
        super(q, r);
    }

    public Wall(AbstractCell cell) {
        super(cell.getQ(), cell.getR());
    }

    @Override
    public boolean handlePlayerInteraction(Player player, LevelModel model) {
        return false;
    }

    @Override
    public void getButtonAppearance(HexButton btn) {
        btn.setBackground(Color.GRAY);
    }
}
