package UnitTests;

import Scripts.Cells.*;
import Scripts.Events.IExitCellActionListener;
import Scripts.Game.GameManager;
import Scripts.Game.LevelModel;
import Scripts.Observers.ExitCellObserver;
import Scripts.Player;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class ExitTest {
    private Player player;
    private LevelModel model;
    private List<Key> requiredKeys;
    private ExitCell exitCell;
    private boolean winEventFired;

    @Before
    public void setUp() {
        player = new Player();
        model = new LevelModel(5, 5, new ArrayList<>(), new ArrayList<>(),
                new Point(0, 0), new Point(1, 1), new Point(2, 2));
        requiredKeys = new ArrayList<>();
        exitCell = new ExitCell(requiredKeys, new Cell(1, 1));

        // Мок-слушатель для проверки событий
        exitCell.addExitCellActionListener(event -> {
            return winEventFired = event.getCollectedKeys().containsAll(event.getLevelKeys());
        });

        winEventFired = false;
    }

    @Test
    public void testEnterWithoutKeys() {
        boolean result = exitCell.handlePlayerInteraction(player, model);
        assertFalse("Не должно быть победы без ключей", result);
        assertFalse("Не должно быть события победы", winEventFired);
    }

    @Test
    public void testEnterWithSomeKeys() {
        requiredKeys.add(new Key());
        requiredKeys.add(new Key());
        player.GetKeys().add(new Key());

        boolean result = exitCell.handlePlayerInteraction(player, model);
        assertFalse("Не должно быть победы без всех ключей", result);
        assertFalse("Не должно быть события победы", winEventFired);
    }

    @Test
    public void testEnterWithAllKeys() {
        Key key1 = new Key();
        Key key2 = new Key();
        requiredKeys.add(key1);
        requiredKeys.add(key2);
        player.GetKeys().add(key1);
        player.GetKeys().add(key2);

        boolean result = exitCell.handlePlayerInteraction(player, model);
        assertTrue("Должна быть победа со всеми ключами", result);
        assertTrue("Должно быть событие победы", winEventFired);
    }

    @Test
    public void testReEnterAfterFirstVisit() {
        requiredKeys.add(new Key());
        player.GetKeys().add(requiredKeys.get(0));

        boolean firstResult = exitCell.handlePlayerInteraction(player, model);
        assertTrue(firstResult);

        winEventFired = false;
        boolean secondResult = exitCell.handlePlayerInteraction(player, model);
        assertTrue("Повторное попадание должно тоже завершаться победой", secondResult);
        assertTrue("Должно быть событие победы", winEventFired);
    }

    @Test
    public void testReEnterAfterFailedAttempt() {
        requiredKeys.add(new Key());

        boolean firstResult = exitCell.handlePlayerInteraction(player, model);
        assertFalse(firstResult);

        player.GetKeys().add(requiredKeys.get(0));
        boolean secondResult = exitCell.handlePlayerInteraction(player, model);
        assertTrue("После сбора ключей должна быть победа", secondResult);
    }

    @Test
    public void testExitCellRemainsActiveAfterVisit() {
        assertTrue("Клетка должна быть активна изначально", exitCell.shouldEnableCell());

        exitCell.handlePlayerInteraction(player, model);
        assertTrue("Клетка должна оставаться активной после посещения", exitCell.shouldEnableCell());
    }

    @Test
    public void testExitCellRemainsActiveAfterWin() {
        requiredKeys.add(new Key());
        player.GetKeys().add(requiredKeys.get(0));

        exitCell.handlePlayerInteraction(player, model);
        assertTrue("Клетка должна оставаться активной даже после победы", exitCell.shouldEnableCell());
    }

    @Test
    public void testTeleportationWhenNoKeys() {
        Cell neighborCell = new Cell(1, 2);

        List<Key> keys = new ArrayList<Key>();
        keys.add(new Key());

        ExitCell tempExit = new ExitCell(keys, new Cell(0,0));
        tempExit.SetNeighbour(neighborCell);
        tempExit.handlePlayerInteraction(player, model);
        assertEquals("Игрок должен телепортироваться на соседнюю клетку", neighborCell, player.GetCell());
    }

    @Test
    public void testNoTeleportationWhenBlocked() {

        List<Key> keys = new ArrayList<Key>();
        keys.add(new Key());

        ExitCell tempExit = new ExitCell(keys, new Cell(0,0));
        player.moveTo(tempExit);
        boolean result = tempExit.handlePlayerInteraction(player, model);
        assertFalse(result);
        assertEquals("Игрок должен остаться на клетке выхода", tempExit, player.GetCell());
    }

    @Test
    public void testListenerRemoval() {
        IExitCellActionListener listener = new ExitCellObserver(new GameManager());
        exitCell.addExitCellActionListener(listener);
        exitCell.removeExitCellActionListener(listener);

        exitCell.handlePlayerInteraction(player, model);
    }


    @Test
    public void testEmptyKeyList() {
        boolean result = exitCell.handlePlayerInteraction(player, model);
        assertTrue("С пустым списком ключей должна быть победа", result);
    }

    @Test
    public void testNullKeyInList() {
        requiredKeys.add(null);
        player.GetKeys().add(null);

        boolean result = exitCell.handlePlayerInteraction(player, model);
        assertTrue("Должна быть победа (null считается как собранный ключ)", result);
    }
}