package Scripts.Events;

import Scripts.Cells.AbstractCell;
import Scripts.View.HexButton;

@FunctionalInterface
public interface LevelInputHandler {
    public void handleCellClick(HexButton btn);
}