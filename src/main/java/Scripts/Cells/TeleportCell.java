package Scripts.Cells;

public class TeleportCell extends AbstractCell
{

    public TeleportCell(int q, int r) {
        super(q, r);
    }

    public TeleportCell(AbstractCell cell) {
        super(cell.getQ(), cell.getR());
    }



}
