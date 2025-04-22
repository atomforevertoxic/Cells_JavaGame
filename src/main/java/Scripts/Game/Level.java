package Scripts.Game;

import Scripts.Cells.AbstractCell;
import Scripts.Cells.Cell;
import Scripts.Cells.ExitCell;
import Scripts.Cells.Key;
import Scripts.Player;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CancellationException;

public class Level
{

    private int _keyCounter;
    private int _cellCounter;

    private List<AbstractCell> _field = new ArrayList<>();
    private List<Key> _keys;
    private Player _player;

    public Level(int keyCounter, int cellCounter)
    {
        _keyCounter = keyCounter;
        _cellCounter = cellCounter;

        _player = new Player();

        GenerateField();
    }



    public void GenerateField()
    {
        while (_field.size()!=_cellCounter)
        {

            AbstractCell cellToExecute;
            if (_field.isEmpty())
            {
                Cell spawnCell = new Cell();
                SetSpawnCell(spawnCell);

                cellToExecute = spawnCell;
            }
            else
            {
                Random rand = new Random();
                int randIndex = rand.nextInt(_field.size());

                AbstractCell cell = _field.get(randIndex);

                if (cell.GetNeighbours().isEmpty()) SetNeighboursTo(cell);

                if (rand.nextBoolean()) SetKeyCell((Cell)cell);
                else if (rand.nextBoolean()) cell.SetWall();;
            }
        }



    }


    private void SetSpawnCell(AbstractCell spawnCell)
    {
        spawnCell.SetPlayer(_player);
        _field.add(spawnCell);
    }

    private void SetNeighboursTo(AbstractCell host)
    {
        for (int i = 0 ; i<6; i++)
        {
            AbstractCell neighbour = new AbstractCell();

            if ((_field.size()-1)==_cellCounter) neighbour = new ExitCell();

            _field.add(neighbour);
            host.SetNeighbour(neighbour);
        }
    }


    private void SetKeyCell(Cell keyCell)
    {
        _keyCounter--;
        Key key = new Key();
        keyCell.SetKey(key);
        _keys.add(key);
    }


    public List<AbstractCell> GetField()
    {
        return _field;
    }
}
