package Scripts.Cells;

import Scripts.Events.ExitCellActionEvent;
import Scripts.Events.ExitCellActionListener;

import java.util.ArrayList;
import java.util.List;

public class ExitCell extends AbstractCell
{
    private List<Key> _levelKeys;

    public ExitCell(List<Key> levelKeys)
    {
        _levelKeys = levelKeys;
    }


    public void CheckGameRules(List<Key> playerKeys)
    {
        if (_levelKeys.size()!=playerKeys.size()) return;

        for (Key keyToCheck : _levelKeys)
        {
            if (!playerKeys.contains(keyToCheck))    return;
        }
        fireGameRulePassed();
    }

    //Уведомить игру что условия выполнены

    // -------------------- События --------------------
    private ArrayList<ExitCellActionListener> exitCellListListener = new ArrayList<>();

    public void addExitCellActionListener(ExitCellActionListener listener) {
        exitCellListListener.add(listener);
    }

    public void removeExitCellActionListener(ExitCellActionListener listener) {
        exitCellListListener.remove(listener);
    }

    private void fireGameRulePassed() {
        for(ExitCellActionListener listener: exitCellListListener) {
            ExitCellActionEvent event = new ExitCellActionEvent(listener);
            event.SetKeys(keyCounter);
            listener.fireGameRulesPassed(event);
        }
    }
}
