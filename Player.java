package battleship;

public class Player {
    final private Battlefield bf;
    final private String name;

    Player(String name) {
        this.bf = new Battlefield();
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    private void showBattlefield() {
        this.bf.printBattlefield(true);
    }

    int shipsLeft() {
        return this.bf.getShipsNum();
    }

    private void watchBattlefields(Player other) {
        other.showBattlefield();
        System.out.println("---------------------");
        this.bf.printBattlefield(false);
    }

    void fillBattlefield() {
        System.out.printf("%s, place your ships on the game field\n\n", this);
        this.bf.placeShips();
    }

    private String takeShot(Coordinate c) {
        return this.bf.makeHit(c);
    }

    void attack(Player other) {
        watchBattlefields(other);

        System.out.printf("%s, it's your turn:\n\n", this);
        boolean shot = false;
        while (!shot) {
            try {
                Coordinate c = new Coordinate(Game.sc.nextLine().strip());
                String msg = other.takeShot(c);
                shot = true;
                System.out.printf("%s\n\n", msg);
            } catch (IllegalArgumentException | IndexOutOfBoundsException exc) {
                System.out.printf("Error! %s Try again:\n\n", exc.getMessage());
            }
        }
    }

    void win(Player p2) {
        p2.showBattlefield();
        System.out.printf("%s has won!", this);
    }
}
