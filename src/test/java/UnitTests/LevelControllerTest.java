package UnitTests;

import Scripts.Cells.*;
import Scripts.Game.*;
import Scripts.Player;
import Scripts.View.HexButton;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class LevelControllerTest {
    private Level level;
    private LevelModel model;
    private LevelView view;
    private GameManager gameManager;
    private LevelController controller;
    private JFrame testFrame;

    @Before
    public void setUp() {
        List<Point> walls = new ArrayList<>();
        List<Point> keys = List.of(new Point(1, 1));
        Point start = new Point(0, 0);
        Point exit = new Point(2, 2);

        testFrame = new JFrame();
        gameManager = new GameManager();
        level = new Level(1,"1", 3, 3, walls, keys, start, exit, null, gameManager);
        model = new LevelModel(3, 3, walls, keys, start, exit, null);
        view = new LevelView(model, null, testFrame);
        controller = new LevelController(model, view, gameManager);
    }

    @Test
    public void testInitialSetup() {
        AbstractCell startCell = model.getCell(0, 0).orElseThrow();
        assertEquals(startCell, model.getPlayer().GetCell());
        assertEquals(model.getPlayer(), startCell.GetPlayer());
    }

    @Test
    public void testHandleCellClickOnRegularCell() {

        HexButton button = view.getButtonMap().get("0,1");
        AbstractCell cell = view.getCellByButton(button);

        controller.handleCellClick(button);

        assertEquals(cell, model.getPlayer().GetCell());
        assertEquals(model.getPlayer(), cell.GetPlayer());
    }


    @Test
    public void testResearchCellWithKey() {
        Cell keyCell = (Cell) model.getCell(1, 1).orElseThrow();

        boolean result = controller.researchCell(keyCell, model.getPlayer());

        assertFalse(result);
        assertFalse(model.getPlayer().GetKeys().contains(keyCell.GetKey()));
        assertNull(keyCell.GetKey());
    }


    @Test
    public void testExitCellListenerSetup() {
        AbstractCell exitCell = model.getCell(2, 2).orElseThrow();
        assertTrue(exitCell instanceof ExitCell);

        assertNotNull(((ExitCell)exitCell).fireCheckLevelRules(new ArrayList<>()));
    }
}