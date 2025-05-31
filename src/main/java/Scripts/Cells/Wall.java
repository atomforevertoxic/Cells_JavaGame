package Scripts.Cells;

public class Wall extends AbstractCell{
    public Wall(int q, int r) {
        super(q, r);
    }

    public Wall(AbstractCell cell) {
        super(cell.getQ(), cell.getR());
    }
}
