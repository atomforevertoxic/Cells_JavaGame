package Scripts;

public enum Direction {
    TOP(0, -1),
    TOP_RIGHT(1, -1),
    RIGHT(1, 0),
    BOTTOM(0, 1),
    BOTTOM_LEFT(-1, 1),
    LEFT(-1, 0);

    private final int dq;
    private final int dr;

    Direction(int dq, int dr) {
        this.dq = dq;
        this.dr = dr;
    }

    public int getDQ() {
        return dq;
    }

    public int getDR() {
        return dr;
    }
}
