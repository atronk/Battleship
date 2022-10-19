package battleship;

import java.util.Scanner;

public class Game {
    final static Scanner sc = new Scanner(System.in);
    final static int SIZE_ROWS = 10;
    final static int SIZE_COLS = 10;

    static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                Runtime.getRuntime().exec(new String[]{"cls"}).waitFor();
            } else {
                Runtime.getRuntime().exec(new String[]{"clear"}).waitFor();
            }
        } catch (final Exception e) {
            System.out.printf("Exception %s", e.getMessage());
        }
    }

    private void passTheMove() {
        clearConsole();
        System.out.println("Press Enter ...");
        sc.nextLine();
    }

    boolean makeTurn(Player p1, Player p2) {
        p1.attack(p2);
        if (p2.shipsLeft() == 0) {
            p1.win(p2);
            return true;
        }
        return false;
    }

    void start() {
        Player p1 = new Player("Player 1");
        Player p2 = new Player("Player 2");

        p1.fillBattlefield();
        passTheMove();
        p2.fillBattlefield();
        passTheMove();

        while (true) {
            if (makeTurn(p1, p2))
                break;
            passTheMove();
            if (makeTurn(p2, p1))
                break;
            passTheMove();
        }
    }
}
