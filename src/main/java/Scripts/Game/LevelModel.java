package Scripts.Game;

import Scripts.Cells.*;
import Scripts.Direction;
import Scripts.Player;
import java.awt.Point;
import java.util.*;

public class LevelModel {
    private final List<AbstractCell> field = new ArrayList<>();
    private final List<Key> keys = new ArrayList<>();
    private final Player player = new Player();
    private final int rows;
    private final int cols;
    private final int hexSize = 30;
    private final int offsetX = 150;
    private final int offsetY = 450;
    private AbstractCell startCell;

    private final List<Point> wallPositions;
    private final List<Point> keyPositions;
    private final Point startPosition;
    private final Point exitPosition;
    private final Point teleportPosition;
    private final Point vertTeleportPosition;

    public LevelModel(int rows, int cols,
                      List<Point> wallPositions,
                      List<Point> keyPositions,
                      Point startPosition,
                      Point exitPosition,
                      Point teleportPosition,
                      Point vertTeleportPosition) {
        this.rows = rows;
        this.cols = cols;
        this.wallPositions = wallPositions;
        this.keyPositions = keyPositions;
        this.startPosition = startPosition;
        this.exitPosition = exitPosition;
        this.teleportPosition = teleportPosition;
        this.vertTeleportPosition = vertTeleportPosition;
        initializeField();
    }

    private void initializeField() {

        for (int r = 0; r < rows; r++)
        {
            for (int q = 0; q < cols; q++)
            {
                Point pos = new Point(q,r);

                AbstractCell newCell = setRoleToCell(pos);

                field.add(newCell);
            }
        }

        connectNeighbors();
    }

    private AbstractCell setRoleToCell(Point pos)
    {
        AbstractCell newCell = new Cell(pos);
        if (wallPositions.contains(pos)) {
             newCell = new Wall(pos);
        } else if (keyPositions.contains(pos)) {
            setKeyCell((Cell)newCell);
        } else if (pos.equals(startPosition)) {
            player.SetCell(newCell);
            startCell = newCell;
        } else if (pos.equals(teleportPosition)) {
            newCell = new TeleportCell(pos);
        } else if (pos.equals(vertTeleportPosition)) {
            newCell = new VerticalTeleportCell(pos);
        } else if (pos.equals(exitPosition)) {
            newCell = new ExitCell(keys, pos);
        }

        return newCell;
    }



    private void setKeyCell(Cell cell) {
        Key key = new Key();
        cell.SetKey(key);
        keys.add(key);
    }

    private void connectNeighbors() {
        for (AbstractCell cell : field) {
            connectWithNeighbours(cell);
        }
    }

    private void connectWithNeighbours(AbstractCell cell)
    {
        int q = cell.getQ();
        int r = cell.getR();

        for (Direction dir : Direction.values()) {
            int nq = q + dir.getDQ();
            int nr = r + dir.getDR();

            if (nq >= 0 && nq < cols && nr >= 0 && nr < rows) {
                getCell(nq, nr).ifPresent(cell::SetNeighbour);
            }
        }
    }

    public void reconnectNeighbours(AbstractCell cell)
    {
        cell.GetNeighbours().clear();
        connectWithNeighbours(cell);
    }
    public Optional<AbstractCell> getCell(int q, int r) {
        return field.stream()
                .filter(c -> c.getQ() == q && c.getR() == r)
                .findFirst();
    }

    public Point calculatePixelPosition(AbstractCell cell) {
        double x = hexSize * 3.0 / 2.0 * cell.getQ() + offsetX;
        double y = -hexSize * Math.sqrt(3) * (cell.getR() + cell.getQ() / 2.0) + offsetY;
        return new Point((int)x, (int)y);
    }

    public List<AbstractCell> getField() { return Collections.unmodifiableList(field); }
    public Player getPlayer() { return player; }
    public List<Key> getKeys() { return Collections.unmodifiableList(keys); }
    public int getHexSize() { return hexSize; }


    public AbstractCell getStartPosition()
    {
        return startCell;
    }
}