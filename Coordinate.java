package battleship;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Coordinate {
    final private static Set<int[]> neighbourDelta = new HashSet<>();
    private int r;
    private int c;

    public Coordinate(Coordinate that) {
        this.r = that.r;
        this.c = that.c;
    }

    public int r() {
        return this.r;
    }

    public int c() {
        return this.c;
    }

    private int parseR(String coor) {
        char ch = coor.toUpperCase().charAt(0);
        if (ch < 'A' || ch >= 'A' + Battlefield.ROWS) {
            throw new IllegalArgumentException("Coordinate (" + coor + ") is wrong.");
        }
        return ch - 'A';
    }

    private int parseC(String coor) {
        int col;
        try {
            col = Integer.parseInt(coor.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Coordinate (" + coor + ") is wrong.");
        }
        if (col < 1 || col > Battlefield.COLS) {
            throw new IllegalArgumentException("Coordinate (" + coor + ") is wrong.");
        }
        return col - 1;
    }

    Coordinate(int r, int c) {
        if (r < 0 || c < 0 || r > Battlefield.ROWS || c > Battlefield.COLS) {
            throw new IndexOutOfBoundsException();
        }
        this.r = r;
        this.c = c;
    }

    Coordinate(String coor) {
        if (coor == null || coor.length() < 2) {
            throw new IllegalArgumentException("Coordinate (" + coor + ") is wrong.");
        }
        r = parseR(coor);
        c = parseC(coor);
    }

    private boolean isSameRow(Coordinate that) {
        return this.r == that.r;
    }

    private boolean isSameCol(Coordinate that) {
        return this.c == that.c;
    }

    public boolean onSameAxis(Coordinate c) {
        return isSameRow(c) || isSameCol(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return isSameRow(that) && isSameCol(that);
    }

    @Override
    public String toString() {
        return String.format("%c%d", 'A' + r, 1 + c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }

    public int shipLenTo(Coordinate that) {
        return Math.abs(this.r - that.r) + Math.abs(this.c - that.c) + 1;
    }

    public void moveTo(Coordinate that) {
        if (this.r != that.r) {
            if (this.r < that.r)
                ++this.r;
            else
                --this.r;
        } else if (this.c != that.c) {
            if (this.c < that.c)
                ++this.c;
            else
                --this.c;
        }
    }

    public static Set<Coordinate> getCoordinatesFromTo(Coordinate c1, Coordinate c2) {
        Set<Coordinate> set = new HashSet<>();

        for (Coordinate c = new Coordinate(c1); !c.equals(c2); c.moveTo(c2)) {
            set.add(new Coordinate(c));
        }
        set.add(c2);
        return set;
    }

    public Set<Coordinate> getNeighbours() {
        Set<Coordinate> neigh = new HashSet<>();
        for (int[] del : neighbourDelta) {
            int dr = r + del[0];
            int dc = c + del[1];
            if (dr >= 0 && dr < Battlefield.ROWS && dc >= 0 && dc < Battlefield.COLS) {
                neigh.add(new Coordinate(dr, dc));
            }
        }
        return neigh;
    }

    static void setNeighbourDelta() {
        neighbourDelta.add(new int[]{0, 1});
        neighbourDelta.add(new int[]{0, -1});
        neighbourDelta.add(new int[]{1, 0});
        neighbourDelta.add(new int[]{-1, 0});
    }

}
