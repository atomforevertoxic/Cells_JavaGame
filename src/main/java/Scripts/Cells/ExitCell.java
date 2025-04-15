package Scripts.Cells;

import Scripts.Events.ExitCellActionEvent;
import Scripts.Events.ExitCellActionListener;

import java.util.ArrayList;
import java.util.List;

public class ExitCell extends AbstractCell
{
    private List<Key> keyCounter;

    public void CheckGameRules(List<Key> playerKeys)
    {
        if (keyCounter.size()!=playerKeys.size()) return;

        for (int i = 0; i<keyCounter.size(); i++)
        {
            if (!keyCounter.get(i).equals(playerKeys.get(i))) return;
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
