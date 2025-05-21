package Scripts.Events;

import Scripts.Cells.AbstractCell;

@FunctionalInterface
public interface LevelInputHandler {
    void handleCellClick(AbstractCell cell);
}