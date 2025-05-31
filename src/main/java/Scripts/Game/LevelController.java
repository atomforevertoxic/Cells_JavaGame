package Scripts.Game;

import Scripts.Cells.AbstractCell;
import Scripts.Cells.Cell;
import Scripts.Cells.ExitCell;
import Scripts.Cells.TeleportCell;
import Scripts.Interfaces.ILevelInputHandler;
import Scripts.Player;
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

        Player player = model.getPlayer();

        boolean win = player.researchCell(cell);
        if (win) view.close();

        view.updateViewByCell(cell);
    }






}