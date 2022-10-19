package battleship;

import java.util.*;

public class Battlefield {
    final public static int ROWS = Game.SIZE_ROWS;
    final public static int COLS = Game.SIZE_COLS;
    final private char SYM_FOG = '~';
    final private char SYM_SHIP = 'O';
    final private char SYM_HIT = 'X';
    final private char SYM_MISS = 'M';

    final private ArrayList<Ship> ships = new ArrayList<>();
    final private char[][] field;
    final private Set<Coordinate> shotCells = new HashSet<>();

    enum SHIP_TYPE {
        CARRIER(5, "Aircraft Carrier"),
        BATTLESHIP(4, "Battleship"),
        SUBMARINE(3, "Submarine"),
        CRUISER(3, "Cruiser"),
        DESTROYER(2, "Destroyer");
        final int size;
        final String name;

        SHIP_TYPE(int i, String name) {
            this.size = i;
            this.name = name;
        }
    }

    Battlefield() {
        Coordinate.setNeighbourDelta();

        field = new char[ROWS][COLS];
        coverFieldWithFog();
    }

    int getShipsNum() {
        return ships.size();
    }

    private void coverFieldWithFog() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                field[r][c] = SYM_FOG;
            }
        }
    }

    private boolean checkNeighbor(Coordinate c) {
        if (shipAt(c) != null)
            return false;
        for (Coordinate neigh : c.getNeighbours()) {
            if (shipAt(neigh) != null)
                return false;
        }
        return true;
    }

    private void markAt(Coordinate coor, char sym) {
        field[coor.r()][coor.c()] = sym;
    }

    private boolean placeShip(Ship ship) {
        for (Coordinate c : ship.coordinates) {
            if (!checkNeighbor(c)) {
                throw new IllegalArgumentException("You placed it too close to another one.");
            }
        }
        ship.coordinates.forEach(c -> markAt(c, SYM_SHIP));
        return true;
    }

    void placeShips() {
        printBattlefield(false);
        for (SHIP_TYPE type : SHIP_TYPE.values()) {
            System.out.printf("Enter the coordinates of the %s (%d cells):\n\n", type.name, type.size);
            boolean placed = false;
            while (!placed) {
                try {
                    Coordinate c1 = new Coordinate(Game.sc.next().strip());
                    Coordinate c2 = new Coordinate(Game.sc.nextLine().strip());
                    Ship s = new Ship(type, c1, c2);
                    placed = placeShip(s);
                    ships.add(s);
                } catch (IllegalArgumentException exc) {
                    System.out.printf("Error! %s Try again:\n\n", exc.getMessage());
                } catch (Exception e) {
                    System.out.printf("Exception: %s\n\n", e.getMessage());
                    break;
                }
            }
            printBattlefield(false);
        }
    }

    void printBattlefield(boolean fog) {
        System.out.print("\s");
        for (int i = 1; i <= COLS; i++) {
            System.out.print("\s");
            System.out.print(i);
        }
        System.out.println();

        char start = 'A';
        for (int x = 0; x < ROWS; x++) {
            System.out.print(start++);
            for (int y = 0; y < COLS; y++) {
                if (!fog || shotCells.contains(new Coordinate(x, y))) {
                    System.out.print("\s" + field[x][y]);
                } else {
                    System.out.print("\s" + SYM_FOG);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private Ship shipAt(Coordinate c) {
        for (Ship s : ships) {
            if (s.coordinates.contains(c)) {
                return s;
            }
        }
        return null;
    }

    private boolean tryHitShips(Coordinate c) {
        Ship s = shipAt(c);

        if (s != null) {
            s.coordinates.remove(c);
            shotCells.add(c);
            if (s.coordinates.size() == 0) {
                ships.remove(s);
            }
            return true;
        }
        return false;
    }

    private String isAlreadyShot(Coordinate c) {
        if (shotCells.contains(c)) {
            if (field[c.r()][c.c()] == SYM_HIT) {
                return "You hit a ship!";
            }
            if (field[c.r()][c.c()] == SYM_MISS) {
                return "You missed!";
            }
        }
        return null;
    }

    String makeHit(Coordinate c) {
        String msg = isAlreadyShot(c);
        if (msg != null) {
            return msg;
        } else {
            msg = "You missed!";
        }
        int shipsBefore = ships.size();
        boolean hit = tryHitShips(c);


        shotCells.add(c);
        if (hit) {
            markAt(c, SYM_HIT);
            if (ships.size() < shipsBefore) {
                if (ships.size() == 0) {
                    msg = "You sank the last ship. You won. Congratulations!";
                } else {
                    msg = "You sank a ship!";
                }
            } else {
                msg = "You hit a ship!";
            }
        } else {
            markAt(c, SYM_MISS);
        }
        return msg;
    }
}
