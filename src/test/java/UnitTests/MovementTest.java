package UnitTests;

import Scripts.Cells.AbstractCell;
import Scripts.Cells.Cell;
import Scripts.Cells.ExitCell;
import Scripts.Cells.Key;
import Scripts.Player;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MovementTest
{
    Player player = new Player();
    @Test
    public void BaseTest()
    {
        Cell currentCell = new Cell();
        player.SetCell(currentCell);

        Cell nextCell = new Cell();

        currentCell.SetNeighbour(nextCell);

        player.Move(nextCell);

        assertEquals(player.GetCell(), nextCell);
    }

    @Test
    public void MoveToKeyCell()
    {
        Cell currentCell = new Cell();
        player.SetCell(currentCell);

        Cell nextCell = new Cell();
        nextCell.SetKey(new Key());

        currentCell.SetNeighbour(nextCell);

        player.Move(nextCell);

        assertEquals(player.GetCell(), nextCell);
    }

    @Test
    public void MoveToExitCell()
    {
        Cell currentCell = new Cell();
        player.SetCell(currentCell);

        ExitCell nextCell = new ExitCell();

        currentCell.SetNeighbour(nextCell);

        player.Move(nextCell);

        assertEquals(player.GetCell(), nextCell);
    }
}
