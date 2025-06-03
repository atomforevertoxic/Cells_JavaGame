package UnitTests;

import Scripts.Cells.*;
import Scripts.Game.LevelModel;
import Scripts.Player;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MovementTest {
    private Player player;
    private LevelModel model;

    @Before
    public void setUp() {
        player = new Player();
        model = new LevelModel(5, 5, new ArrayList<>(), new ArrayList<>(),
                new Point(0, 0), new Point(1, 1), new Point(2, 2));
    }

    @Test
    public void testBasicMovement() {
        Cell startCell = new Cell(0, 0);
        Cell targetCell = new Cell(0, 1);
        startCell.SetNeighbour(targetCell);

        player.moveTo(startCell);
        player.moveTo(targetCell);

        assertEquals(targetCell, player.GetCell());
        assertNull(startCell.GetPlayer());
        assertEquals(player, targetCell.GetPlayer());
    }

    @Test
    public void testKeyCollection() {
        Cell cellWithKey = new Cell(1, 1);
        Key key = new Key();
        cellWithKey.SetKey(key);

        player.moveTo(cellWithKey);
        cellWithKey.handlePlayerInteraction(player, model);

        assertTrue(player.GetKeys().contains(key));
        assertNull(cellWithKey.GetKey());
        assertTrue(cellWithKey.getPassedInfo());
    }

    @Test
    public void testWallBlocking() {
        Cell startCell = new Cell(0, 0);
        Wall wallCell = new Wall(0, 1);
        startCell.SetNeighbour(wallCell);

        player.moveTo(startCell);
        boolean result = wallCell.handlePlayerInteraction(player, model);

        assertFalse(result);
        assertEquals(startCell, player.GetCell());
    }

    @Test
    public void testExitCellWithoutKeys() {
        List<Key> requiredKeys = new ArrayList<>();
        requiredKeys.add(new Key());

        ExitCell exitCell = new ExitCell(requiredKeys, new Cell(1, 1));
        // Игрок НЕ собрал ключи

        boolean result = exitCell.handlePlayerInteraction(player, model);
        assertFalse(result); // Уровень не должен завершиться
    }

    @Test
    public void testCellPassedState() {
        Cell cell = new Cell(2, 2);
        player.moveTo(cell);
        cell.handlePlayerInteraction(player, model);

        assertTrue(cell.getPassedInfo());
    }

    @Test
    public void testNeighborConnections() {
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(0, 1);

        cell1.SetNeighbour(cell2);

        assertTrue(cell1.IsNeighbourOf(cell2));
        assertTrue(cell2.IsNeighbourOf(cell1));
        assertEquals(1, cell1.GetNeighbours().size());
        assertEquals(1, cell2.GetNeighbours().size());
    }


    @Test
    public void testMultipleKeysCollection() {
        Cell cell1 = new Cell(1, 1);
        Cell cell2 = new Cell(1, 2);
        Key key1 = new Key();
        Key key2 = new Key();

        cell1.SetKey(key1);
        cell2.SetKey(key2);

        player.moveTo(cell1);
        cell1.handlePlayerInteraction(player, model);

        player.moveTo(cell2);
        cell2.handlePlayerInteraction(player, model);

        assertEquals(2, player.GetKeys().size());
        assertTrue(player.GetKeys().contains(key1));
        assertTrue(player.GetKeys().contains(key2));
    }

    @Test
    public void testTeleportToWall() {
        TeleportCell teleportCell = new TeleportCell(1, 1);
        Wall wallCell = new Wall(1, 2);
        teleportCell.SetNeighbour(wallCell);

        player.moveTo(teleportCell);
        boolean result = teleportCell.handlePlayerInteraction(player, model);

        assertFalse(result);
        // Игрок должен остаться на телепорте, так как стена непроходима
        assertEquals(teleportCell, player.GetCell());
    }

    @Test
    public void testExitCellTeleportation() {
        List<Key> keys = new ArrayList<>();
        ExitCell exitCell = new ExitCell(keys, new Cell(1, 1));
        Cell neighborCell = new Cell(1, 2);
        exitCell.SetNeighbour(neighborCell);

        player.moveTo(exitCell);
        exitCell.handlePlayerInteraction(player, model);

        // При отсутствии ключей игрок должен телепортироваться
        assertEquals(neighborCell, player.GetCell());
    }
}