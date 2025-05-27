package Scripts.Observers;
import Scripts.Cells.Key;
import Scripts.Events.ExitCellActionEvent;
import Scripts.Events.IExitCellActionListener;
import Scripts.Game.GameManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExitCellObserver implements IExitCellActionListener {

    private GameManager gameManager;

    public ExitCellObserver(GameManager gm)
    {
        gameManager = gm;
    }


    @Override
    public void checkLevelRules(@NotNull ExitCellActionEvent event) {
        List<Key> levelKeys = event.getLevelKeys();
        List<Key> collectedKeys = event.getCollectedKeys();
        if (levelKeys.size()!=collectedKeys.size()) return ;

        for (Key keyToCheck : levelKeys)
        {
            if (!collectedKeys.contains(keyToCheck))    return ;
        }
        gameManager.endCurrentLevel();
    }

}