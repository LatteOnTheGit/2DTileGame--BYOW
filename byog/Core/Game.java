package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Game {

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard(String seed, boolean relaod, boolean twoPlayer) throws IOException, ClassNotFoundException {
        long newSeed = Long.parseLong(seed);
        gameEngine.playWithKey(newSeed, relaod, twoPlayer);
        // add view that player same and quit
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        long seed;
        String keys = "";
        String seedS = "";
        boolean save = false, reload = false;
        for (char c : input.toCharArray()) {
            if(c == 'l') {
                reload = true;
                continue;
            }
            if (c == 'n' || c == ':') continue;
            if (c == 'q') {
                save = true;
                continue;
            }
            if (Character.isDigit(c))seedS += c;
            keys += c;
        }
        seed = !seedS.equals("") ? Long.parseLong(seedS) : 0000L;
        TETile[][] finalWorldFrame = gameEngine.playWithInput(seed, keys, save, reload);
        return finalWorldFrame;
    }

    private static void testShow(TETile[][] world) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
    }

    /**
     * smoke test on SEED.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Game test = new Game();
        TETile[][] world1 = test.playWithInputString("n1234wasd:q");
        TETile[][] world2 = test.playWithInputString("l:q");
//        System.out.println(TETile.toString(world1));
//        System.out.println("++++++++++++++++++++++");
//        System.out.println(TETile.toString(world2));
        loop:for (int i = 0; i < world1.length; i++) {
            for (int j = 0; j < world1[0].length; j++) {
                if (!world1[i][j].equals(world2[i][j])) {
                    System.out.println("something wrong");
                    break loop;
                }
            }
        }
        System.out.println("check done");
//        testShow(world1);
    }
}
