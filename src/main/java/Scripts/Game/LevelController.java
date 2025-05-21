package Scripts.Game;

import Scripts.Cells.AbstractCell;
import Scripts.Cells.Cell;
import Scripts.Cells.ExitCell;
import Scripts.Events.ExitCellActionEvent;
import Scripts.Events.ExitCellActionListener;
import Scripts.Events.LevelInputHandler;
import Scripts.View.HexButton;

import java.awt.*;
import java.util.List;

public class LevelController implements LevelInputHandler {
    private final LevelModel model;
    private final LevelView view;
    private final GameManager gameManager;

    public LevelController(LevelModel model, LevelView view, GameManager gameManager) {
        this.model = model;
        this.view = view;
        this.gameManager = gameManager;
        setupExitCellListeners();
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
    public void handleCellClick(AbstractCell cell) {
        if (cell instanceof Cell c) {
            handleRegularCell(c);
        } else if (cell instanceof ExitCell exitCell) {
            exitCell.CheckGameRules(model.getPlayer().GetKeys());
        }
        updateView();
    }

    private void handleRegularCell(Cell cell) {
        if (cell.GetKey() != null) {
            model.getPlayer().TakeKeyFromCell(cell);
        }
        cell.setPassed();
    }

    private void updateView() {
        view.updateAllButtons();
        enableNeighborButtons();
    }

    void enableNeighborButtons() {
        model.getField().forEach(cell -> {
            if (cell.GetPlayer() != null) {
                HexButton btn = view.getButtonMap().get(cell.getQ() + "," + cell.getR());
                if (btn != null) {
                    btn.setBackground(Color.RED);
                    enableAdjacentButtons(cell);
                }
            }
        });
    }

    private void enableAdjacentButtons(AbstractCell cell) {
        cell.GetNeighbours().forEach(neighbor -> {
            HexButton btn = view.getButtonMap().get(neighbor.getQ() + "," + neighbor.getR());
            if (btn != null && !neighbor.IsWall() &&
                    (!(neighbor instanceof Cell) || !((Cell)neighbor).getPassedInfo())) {
                btn.setEnabled(true);
                btn.setBackground(Color.BLUE);
            }
        });
    }
}