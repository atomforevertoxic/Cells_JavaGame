package UnitTests;
import Scripts.Cells.Cell;
import Scripts.Cells.Key;
import Scripts.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PickupTest
{
    private Player player;
    private Cell cellWithKey;
    private Cell emptyCell;
    private Key testKey;

    @Before
    public void setUp() {
        player = new Player();
        testKey = new Key();
        cellWithKey = new Cell(0, 0);
        cellWithKey.SetKey(testKey);
        emptyCell = new Cell(1, 1);
    }

    @Test
    public void testPickupKeyFromCell() {
        player.moveTo(cellWithKey);
        cellWithKey.handlePlayerInteraction(player, null);

        assertTrue("Ключ должен быть у игрока", player.GetKeys().contains(testKey));
        assertNull("Ключ должен быть удален из клетки", cellWithKey.GetKey());
    }

    @Test
    public void testPickupFromEmptyCell() {
        player.moveTo(emptyCell);
        emptyCell.handlePlayerInteraction(player, null);

        assertTrue("У игрока не должно быть ключей", player.GetKeys().isEmpty());
    }

    @Test
    public void testCellMarkedAsPassedAfterPickup() {
        player.moveTo(cellWithKey);
        cellWithKey.handlePlayerInteraction(player, null);

        assertTrue("Клетка должна быть помечена как пройденная", cellWithKey.getPassedInfo());
    }

    @Test
    public void testMultipleKeyPickups() {
        Key secondKey = new Key();
        Cell secondCellWithKey = new Cell(2, 2);
        secondCellWithKey.SetKey(secondKey);

        player.moveTo(cellWithKey);
        cellWithKey.handlePlayerInteraction(player, null);

        player.moveTo(secondCellWithKey);
        secondCellWithKey.handlePlayerInteraction(player, null);

        assertEquals("У игрока должно быть 2 ключа", 2, player.GetKeys().size());
        assertTrue("Должен быть первый ключ", player.GetKeys().contains(testKey));
        assertTrue("Должен быть второй ключ", player.GetKeys().contains(secondKey));
    }


    @Test
    public void testKeyRemovalFromCell() {
        player.moveTo(cellWithKey);
        cellWithKey.handlePlayerInteraction(player, null);

        assertFalse("Клетка должна вернуть false при попытке удалить несуществующий ключ",
                cellWithKey.DeleteKey());
    }

    @Test
    public void testKeySetToOccupiedCell() {
        cellWithKey.SetKey(testKey);
        Key newKey = new Key();

        boolean result = cellWithKey.SetKey(newKey);

        assertFalse("Нельзя установить ключ в клетку, где уже есть ключ", result);
        assertEquals("В клетке должен остаться старый ключ", testKey, cellWithKey.GetKey());
    }

    @Test
    public void testKeyEquality() {
        Key key1 = new Key();
        Key key2 = key1;

        assertTrue("Ключи должны быть равны между собой", key1.equals(key2));
        assertTrue("Равенство должно быть симметричным", key2.equals(key1));
    }

    @Test
    public void testKeyPickupAfterLeavingCell() {
        player.moveTo(cellWithKey);
        cellWithKey.handlePlayerInteraction(player, null);

        player.moveTo(emptyCell);
        player.moveTo(cellWithKey);
        cellWithKey.handlePlayerInteraction(player, null);

        assertEquals("Не должно быть нового ключа", 1, player.GetKeys().size());
    }
}
