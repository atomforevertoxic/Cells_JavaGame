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

    public LevelModel(int rows, int cols,
                      List<Point> wallPositions,
                      List<Point> keyPositions,
                      Point startPosition,
                      Point exitPosition) {
        this.rows = rows;
        this.cols = cols;
        initializeField(wallPositions, keyPositions, startPosition, exitPosition);
    }

    private void initializeField(List<Point> walls, List<Point> keys, Point start, Point exit) {

        for (int r = 0; r < rows; r++) {
            for (int q = 0; q < cols; q++) {
                field.add(new Cell(q, r));
            }
        }


        placeObjects(walls, keys, start, exit);
        connectNeighbors();
    }

    private void placeObjects(List<Point> walls, List<Point> keyPositions, Point start, Point exit) {
        for (int i = 0; i < field.size(); i++) {
            AbstractCell c = field.get(i);
            Point pos = new Point(c.getQ(), c.getR());

            if (walls.contains(pos)) {
                c.SetWall();
            } else if (keyPositions.contains(pos)) {
                setKeyCell((Cell)c);
            } else if (pos.equals(start)) {
                ((Cell)c).SetPlayer(player);
                startCell = c;
            } else if (pos.equals(exit)) {
                ExitCell exitCell = new ExitCell(keys, c);
                field.set(i, exitCell);
            }
        }
    }

    private void setKeyCell(Cell cell) {
        Key key = new Key();
        cell.SetKey(key);
        keys.add(key);
    }

    private void connectNeighbors() {
        for (AbstractCell cell : field) {
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


    public void movePlayerTo(AbstractCell cell)
    {
        player.Move(cell);
    }

    public AbstractCell getStartPosition()
    {
        return startCell;
    }
}