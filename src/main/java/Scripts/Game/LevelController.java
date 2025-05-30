package Scripts.Game;

import Scripts.Cells.AbstractCell;
import Scripts.Cells.Cell;
import Scripts.Cells.ExitCell;
import Scripts.Interfaces.ILevelInputHandler;
import Scripts.View.HexButton;

import java.awt.*;
import java.util.List;

public class LevelController implements ILevelInputHandler {
    private final LevelModel model;
    private final LevelView view;
    private final GameManager gameManager;

    public LevelController(LevelModel model, LevelView view, GameManager gameManager) {
        this.model = model;
        this.view = view;
        this.gameManager = gameManager;
        setupExitCellListeners();
        handleCellClick(view.getStartButton());
    }

    private void setupExitCellListeners() {
        model.getField().stream()
                .filter(cell -> cell instanceof ExitCell)
                .forEach(cell -> {
                    ((ExitCell)cell).addExitCellActionListener(
                            gameManager.getExitCellObserver()
                    );
                });
    }

    @Override
    public void handleCellClick(HexButton btn) {
        AbstractCell cell = view.getCellByButton(btn);

        model.movePlayerTo(cell);
        if (cell instanceof Cell c) {
            handleRegularCell(c);
            btn.setCharacter(' '); // очищает ячейку от символа в ней
        }
        else if (cell instanceof ExitCell exitCell) {
            boolean win = exitCell.fireCheckLevelRules(model.getPlayer().GetKeys());
            if (win) view.close();
            return;
        }

        updateViewByCell(btn);
    }

    private void handleRegularCell(Cell cell) {
        if (cell.GetKey() != null) {
            model.getPlayer().TakeKeyFromCell(cell);
        }
        cell.setPassed();
    }

    private void updateViewByCell(HexButton btn) {
        view.setAllButtonsEnable(false);
        view.updateAllButtons();
        enableAdjacentButtons(view.getCellByButton(btn));
    }


    public void enableAdjacentButtons(AbstractCell host) {
        List<AbstractCell> neighbours = host.GetNeighbours();
        for (AbstractCell neighbour : neighbours)
        {
            HexButton btn = view.getButtonMap().get(neighbour.getQ() + "," + neighbour.getR());
            if (neighbour instanceof Cell
                    && (((Cell) neighbour).getPassedInfo()
                    || neighbour.IsWall()))
            {
                continue;
            }

            btn.setEnabled(true);
            btn.setBackground(Color.BLUE);
        }
    }
}