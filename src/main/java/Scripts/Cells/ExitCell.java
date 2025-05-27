package Scripts.Cells;

import Scripts.Events.ExitCellActionEvent;
import Scripts.Events.IExitCellActionListener;

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

    public void fireCheckLevelRules(List<Key> playerKeys) {
        for(IExitCellActionListener listener: exitCellListener) {
            ExitCellActionEvent event = new ExitCellActionEvent(listener);
            event.setLevelKeys(_levelKeys);
            event.setCollectedKeys(playerKeys);
            listener.checkLevelRules(event);
        }
    }
}
