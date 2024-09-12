package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.HashSet;

public class views {
  private static final int WIDTH = 50;
  private static final int HEIGHT = 50;
  private static final Font fontG = new Font("Monaco", Font.BOLD, 30);
  private static final Font fontC = new Font("Monaco", Font.TYPE1_FONT, 15);
  private static HashSet<Character> choices = new HashSet<>();

  static {
    choices.add('n');
    choices.add('l');
    choices.add('t');
    choices.add('q');
    choices.add('d');
  }

  public static void setFormat() {
    StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
    StdDraw.clear(Color.BLACK);
    StdDraw.setFont(fontG);
    StdDraw.setXscale(0, WIDTH);
    StdDraw.setYscale(0, HEIGHT);
    StdDraw.enableDoubleBuffering();
    StdDraw.setPenColor(Color.cyan);
  }

  public static void quit() {
    setFormat();
    StdDraw.text(0.5 * WIDTH, 0.5 * HEIGHT, "Game saved and quit");
    StdDraw.show();
    StdDraw.pause(1500);
  }

  public static char welcome() {
    setFormat();
    StdDraw.text(0.5 * WIDTH, 0.8 * HEIGHT, "Welcome to MyWorld demo");
    StdDraw.setFont(fontC);
    StdDraw.text(0.5 * WIDTH, 0.7 * HEIGHT, "press N to start a SINGLE game");
    StdDraw.text(0.5 * WIDTH, 0.6 * HEIGHT, "press D to start a TWO PLAYER game");
    StdDraw.text(0.5 * WIDTH, 0.5 * HEIGHT, "press L to reload last game");
    StdDraw.text(0.5 * WIDTH, 0.4 * HEIGHT, "press T for test ONLY");
    StdDraw.setPenColor(Color.RED);
    StdDraw.text(0.5 * WIDTH, 0.2 * HEIGHT, "press Q to leave");
    StdDraw.show();
    char input = '.';
    while (!choices.contains(input)) {
      if (!StdDraw.hasNextKeyTyped()) continue;
      input = StdDraw.nextKeyTyped();
    }
    if (input == 'q') {
      quit();
      System.exit(0);
    }
    return input;
  }

  public static void inputSeed(String input) {
    StdDraw.clear(Color.BLACK);
    StdDraw.setPenColor(Color.cyan);
    StdDraw.setFont(fontG);
    StdDraw.text(0.5 * WIDTH, 0.8 * HEIGHT, "Please Input SEED");
    StdDraw.text(0.5 * WIDTH, 0.2 * HEIGHT, "START GAME");
    StdDraw.setPenColor(Color.ORANGE);
    StdDraw.setFont(fontC);
    StdDraw.text(0.5 * WIDTH, 0.5 * HEIGHT, "Your seed is : " + input);
    StdDraw.show();
  }

  public static String controlIn(boolean seedOrC) {
    inputSeed("");
    String seed = "";
    boolean start = false;
    while (!start) {
      if (StdDraw.hasNextKeyTyped()) {
        seed += StdDraw.nextKeyTyped();
        if (seedOrC) inputSeed(seed);
        else  stringIn(seed);
      }
      if (StdDraw.isMousePressed()) {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        if (x > 0.5 * WIDTH - 5 && x < 0.5 * WIDTH + 5 && y > 0.2 * HEIGHT - 1 && y < 0.2 * HEIGHT + 1) {
          if (seed.equals("")) {
            if (seedOrC) inputSeed("seed must be entered");
            else stringIn("commend must be entered");
            continue;
          }
          start = true;
        }

      }
    }
    return seed;
  }

  public static void stringIn(String input) {
    setFormat();
    StdDraw.setPenColor(Color.cyan);
    StdDraw.setFont(fontG);
    StdDraw.text(0.5 * WIDTH, 0.8 * HEIGHT, "Please Input commend");
    StdDraw.text(0.5 * WIDTH, 0.2 * HEIGHT, "START GAME");
    StdDraw.setPenColor(Color.ORANGE);
    StdDraw.setFont(fontC);
    StdDraw.text(0.5 * WIDTH, 0.5 * HEIGHT, "Your commend is : " + input);
    StdDraw.show();
  }

  public static void winGame() {
    setFormat();
    StdDraw.setPenColor(Color.ORANGE);
    StdDraw.text(0.5 * WIDTH, 0.5 * HEIGHT, "You WIN !!!!!");
    StdDraw.show();
    StdDraw.pause(2000);
    System.exit(0);
  }

  public static void main(String[] args) {
//    setFormat();
//    controlSeedIn();
    welcome();
  }
}
