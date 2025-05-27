package Scripts.Interfaces;

import Scripts.View.HexButton;

@FunctionalInterface
public interface ILevelInputHandler {
    void handleCellClick(HexButton btn);
}