package UnitTests;

import Scripts.Cells.Cell;
import Scripts.Cells.Key;
import Scripts.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PickupTest
{
//    private boolean CompareKeyLists(List<Key> actual, List<Key> expected)
//    {
//        if (actual.size()!=expected.size()) return false;
//        for (int i = 0; i< actual.size(); i++)
//        {
//            if (!actual.get(i).equals(expected.get(i))) return false;
//        }
//        return true;
//    }
//
//
//    @Test
//    public void BaseTest()
//    {
//        Player player = new Player();
//
//        Cell cell = new Cell();
//        Key key = new Key();
//        cell.SetKey(key);
//
//        player.TakeKeyFromCell(cell);
//
//        List<Key> expectedKeyList = new ArrayList<>();
//        expectedKeyList.add(key);
//
//        assertTrue(CompareKeyLists(player.GetKeys(), expectedKeyList));
//    }
//
//
//    @Test
//    public void test_TakeKeyFromEmptyCell()
//    {
//        Player player = new Player();
//
//        Cell cell = new Cell();
//
//        player.TakeKeyFromCell(cell);
//
//        List<Key> expectedKeyList = new ArrayList<>();
//
//        assertTrue(CompareKeyLists(player.GetKeys(), expectedKeyList));
//    }
//
//
//    @Test
//    public void test_TakeKeyFromWallCell()
//    {
//        Player player = new Player();
//
//        Cell cell = new Cell();
//        // cell.SetWall(); заменить на класс Wall
//
//        player.TakeKeyFromCell(cell);
//
//        List<Key> expectedKeyList = new ArrayList<>();
//
//        assertTrue(CompareKeyLists(player.GetKeys(), expectedKeyList));
//    }
//
//    @Test
//    public void test_TakeSomeKeys()
//    {
//        Player player = new Player();
//
//        //Добавление первого ключа
//        Cell cell1 = new Cell();
//        Key key1 = new Key();
//        cell1.SetKey(key1);
//
//        player.TakeKeyFromCell(cell1);
//
//        //Добавление второго ключа
//        Cell cell2 = new Cell();
//        Key key2 = new Key();
//        cell2.SetKey(key2);
//
//        player.TakeKeyFromCell(cell2);
//
//        List<Key> expectedKeyList = new ArrayList<>();
//        expectedKeyList.add(key1);
//        expectedKeyList.add(key2);
//
//        assertTrue(CompareKeyLists(player.GetKeys(), expectedKeyList));
//    }
//
//    @Test
//    public void test_TakeKeyFromCellTwice()
//    {
//        Player player = new Player();
//
//        Cell cell = new Cell();
//        Key key = new Key();
//        cell.SetKey(key);
//
//        // Игрок берет ключ первый раз
//        player.TakeKeyFromCell(cell);
//
//        // Игрок пытается взять ключ второй раз
//        player.TakeKeyFromCell(cell);
//
//        List<Key> expectedKeyList = new ArrayList<>();
//        expectedKeyList.add(key);
//
//        assertTrue(CompareKeyLists(player.GetKeys(), expectedKeyList));
//    }
//



}
