package UnitTests;

import Scripts.Cells.*;
import Scripts.Game.LevelModel;
import Scripts.Player;
import org.junit.Before;
import org.junit.Test;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FieldTest {
    private LevelModel model;
    private Player player;

    @Before
    public void setUp() {
        player = new Player();
        model = new LevelModel(5, 5, new ArrayList<>(), new ArrayList<>(),
                new Point(0, 0), new Point(1, 1), new Point(2, 2));
    }

    @Test
    public void testSetKeyOnEmptyCell() {
        Cell cell = new Cell(0, 0);
        Key key = new Key();

        assertTrue("Ключ должен устанавливаться на пустую клетку", cell.SetKey(key));
        assertEquals(key, cell.GetKey());
    }

    @Test
    public void testSetKeyOnOccupiedCell() {
        Cell cell = new Cell(0, 0);
        Key key1 = new Key();
        Key key2 = new Key();

        cell.SetKey(key1);
        assertFalse("Нельзя установить второй ключ", cell.SetKey(key2));
        assertEquals(key1, cell.GetKey());
    }

    @Test
    public void testWallReplacement() {
        Cell cell = new Cell(0, 0);
        Wall wall = new Wall(cell);

        assertTrue("Стена должна блокировать перемещение", !wall.shouldEnableCell());
    }

    @Test
    public void testWallWithPlayer() {
        Cell cell = new Cell(0, 0);
        cell.SetPlayer(player);
        Wall wall = new Wall(cell);

        assertNull("Игрок должен быть удален при замене на стену", wall.GetPlayer());
    }

    @Test
    public void testExitCellCreation() {
        List<Key> keys = new ArrayList<>();
        Cell cell = new Cell(0, 0);
        ExitCell exitCell = new ExitCell(keys, cell);

        assertTrue("Клетка выхода должна быть активной", exitCell.shouldEnableCell());
    }

    @Test
    public void testExitCellWithKeys() {
        Key key = new Key();
        List<Key> keys = new ArrayList<>();
        keys.add(key);

        ExitCell exitCell = new ExitCell(keys, new Cell(0, 0));
        assertFalse("Клетка выхода должна проверять ключи", exitCell.fireCheckLevelRules(keys));
    }

    @Test
    public void testFieldInitialization() {
        assertEquals("Неверное количество клеток", 25, model.getField().size());
    }

    @Test
    public void testNeighborConnections() {
        AbstractCell cell = model.getCell(2, 2).orElseThrow();
        int expectedNeighbors = 6;

        assertEquals("Неверное количество соседей", expectedNeighbors, cell.GetNeighbours().size());
    }


    @Test
    public void testMultipleWallPlacement() {
        Point position = new Point(1, 1);
        List<Point> walls = List.of(position, position, position); // Дубликаты

        LevelModel modelWithWalls = new LevelModel(3, 3, walls, new ArrayList<>(),
                new Point(0, 0), new Point(2, 2), null);

        long wallCount = modelWithWalls.getField().stream()
                .filter(c -> c instanceof Wall)
                .count();

        assertEquals("Должна быть только одна стена", 1, wallCount);
    }

    @Test
    public void testStartPositionWithPlayer() {
        AbstractCell startCell = model.getStartPosition();
        assertNotNull("Игрок должен быть на стартовой позиции", startCell.GetPlayer());
    }

    @Test
    public void testFieldConsistencyAfterModifications() {
        AbstractCell cell = model.getCell(2, 2).orElseThrow();
        int originalNeighborCount = cell.GetNeighbours().size();

        cell.GetNeighbours().forEach(neighbor -> {
            model.reconnectNeighbours(neighbor);
        });

        assertEquals("Количество соседей не должно измениться",
                originalNeighborCount, cell.GetNeighbours().size());
    }

}
