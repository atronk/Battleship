package battleship;

import java.util.Set;

public class Ship {
    final int size;
    final String name;
    Set<Coordinate> coordinates;


    Ship(Battlefield.SHIP_TYPE type, Coordinate start, Coordinate end) {
        this.name = type.name;
        this.size = type.size;
        if (!start.onSameAxis(end))
            throw new IllegalArgumentException("Wrong ship location!");
        if (start.shipLenTo(end) != size)
            throw new IllegalArgumentException("Wrong length of the " + this.name + "!");
        this.coordinates = Coordinate.getCoordinatesFromTo(start, end);
    }

    @Override
    public String toString() {
        return this.name;
    }
}

