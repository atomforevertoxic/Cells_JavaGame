package Scripts.Cells;

import Scripts.Events.ExitCellActionEvent;
import Scripts.Events.IExitCellActionListener;
import Scripts.Game.LevelModel;
import Scripts.Player;

import java.util.ArrayList;
import java.util.List;

public class ExitCell extends AbstractCell
{
    private List<Key> _levelKeys;

    public ExitCell(List<Key> levelKeys, AbstractCell cell)
    {
        super(cell.getQ(), cell.getR());
        _levelKeys = levelKeys;
    }


    // -------------------- События --------------------

    private ArrayList<IExitCellActionListener> exitCellListener = new ArrayList<>();

    public void addExitCellActionListener(IExitCellActionListener listener) {
        exitCellListener.add(listener);
    }

    public void removeExitCellActionListener(IExitCellActionListener listener) {
        exitCellListener.remove(listener);
    }

    public boolean fireCheckLevelRules(List<Key> playerKeys) {
        boolean win = false;
        for(IExitCellActionListener listener: exitCellListener) {
            ExitCellActionEvent event = new ExitCellActionEvent(listener);
            event.setLevelKeys(_levelKeys);
            event.setCollectedKeys(playerKeys);
            win = listener.checkLevelRules(event);
        }
        return win;
    }

    @Override
    public boolean handlePlayerInteraction(Player player, LevelModel model) {
        return fireCheckLevelRules(player.GetKeys());
    }
}
