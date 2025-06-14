package UnitTests;

import Scripts.Cells.*;
import Scripts.Game.LevelModel;
import org.junit.Before;
import org.junit.Test;
import java.awt.Point;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;

public class LevelModelTest {
    private LevelModel model;
    private final int TEST_ROWS = 3;
    private final int TEST_COLS = 3;

    @Before
    public void setUp() {
        List<Point> walls = List.of(new Point(1, 1));
        List<Point> keys = List.of(new Point(0, 1));
        Point start = new Point(0, 0);
        Point exit = new Point(2, 2);
        List<Point> teleports = List.of(new Point(1, 0));


        model = new LevelModel(TEST_ROWS, TEST_COLS, walls, keys, start, exit, teleports);
    }

    @Test
    public void testModelInitialization() {
        assertEquals("Неверное количество строк", TEST_ROWS, model.getField().size() / TEST_COLS);
        assertEquals("Неверное количество столбцов", TEST_COLS, TEST_COLS);
    }

    @Test
    public void testWallPlacement() {
        Optional<AbstractCell> wallCell = model.getCell(1, 1);
        assertTrue("Клетка должна быть стеной", wallCell.get() instanceof Wall);
    }

    @Test
    public void testKeyPlacement() {
        Optional<AbstractCell> keyCell = model.getCell(0, 1);
        assertTrue("Клетка должна содержать ключ", ((Cell)keyCell.get()).GetKey() != null);
        assertEquals("Неверное количество ключей в модели", 1, model.getKeys().size());
    }

    @Test
    public void testStartPosition() {
        AbstractCell startCell = model.getStartPosition();
        assertEquals("Неверная стартовая позиция X", 0, startCell.getQ());
        assertEquals("Неверная стартовая позиция Y", 0, startCell.getR());
        assertNotNull("Игрок должен быть на стартовой клетке", startCell.GetPlayer());
    }

    @Test
    public void testExitPlacement() {
        Optional<AbstractCell> exitCell = model.getCell(2, 2);
        assertTrue("Клетка должна быть выходом", exitCell.get() instanceof ExitCell);
    }

    @Test
    public void testTeleportPlacement() {
        Optional<AbstractCell> teleportCell = model.getCell(1, 0);
        assertTrue("Клетка должна быть телепортом", teleportCell.get() instanceof TeleportCell);
    }

    @Test
    public void testNeighborConnections() {
        AbstractCell centerCell = model.getCell(1, 1).get();
        assertEquals("У центральной клетки должно быть 6 соседей", 6, centerCell.GetNeighbours().size());

        AbstractCell cornerCell = model.getCell(0, 0).get();
        assertEquals("У угловой клетки должно быть 2 соседа", 2, cornerCell.GetNeighbours().size());
    }

    @Test
    public void testPlayerInitialization() {
        assertNotNull("Игрок должен быть инициализирован", model.getPlayer());
        assertEquals("Игрок должен быть на стартовой позиции",
                model.getStartPosition(), model.getPlayer().GetCell());
    }

    @Test
    public void testKeyCollectionInModel() {
        assertEquals("В модели должен быть 1 ключ", 1, model.getKeys().size());
        assertSame("Ключ в модели и в клетке должны совпадать",
                model.getKeys().get(0),
                ((Cell)model.getCell(0, 1).get()).GetKey());
    }

    @Test
    public void testReconnectNeighbors() {
        AbstractCell cell = model.getCell(0, 0).get();
        int originalNeighborCount = cell.GetNeighbours().size();

        model.reconnectNeighbours(cell);

        assertEquals("Количество соседей не должно измениться",
                originalNeighborCount, cell.GetNeighbours().size());
    }

    @Test
    public void testPixelPositionCalculation() {
        AbstractCell cell = model.getCell(0, 0).get();
        Point pos = model.calculatePixelPosition(cell);

        assertTrue("X координата должна быть положительной", pos.x > 0);
        assertTrue("Y координата должна быть положительной", pos.y > 0);
    }

    @Test
    public void testFieldImmutability() {
        List<AbstractCell> field = model.getField();
        try {
            field.add(new Cell(10, 10));
            fail("Должно быть брошено исключение при изменении поля");
        } catch (UnsupportedOperationException e) {
            // Ожидаемое поведение
        }
    }
}