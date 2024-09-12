package byog.Core;

import byog.TileEngine.TETile;

import java.io.IOException;

public class StartGame {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    if (args.length == 1 && args[0].equals("visual")) {
      char input = views.welcome();
      switch (input) {
        case 'n':
          views.setFormat();
          String seed = views.controlIn(true);
          Game game = new Game();
          game.playWithKeyboard(seed, false, false);
          break;
        case 'l':
          Game game1 = new Game();
          game1.playWithKeyboard("11111", true, true);
          break;
        case 't':
          views.setFormat();
          String inputS = views.controlIn(false);
          Game game2 = new Game();
          game2.playWithInputString(inputS);
          break;
        case 'd':
          views.setFormat();
          String seed2 = views.controlIn(true);
          Game game3 = new Game();
          game3.playWithKeyboard(seed2, false, true);
          break;
      }

    } else if (args.length == 1) {
      Game game = new Game();
      TETile[][] worldState = game.playWithInputString(args[0]);
      System.out.println(TETile.toString(worldState));
    } else {
      Game game = new Game();
      game.playWithKeyboard("0000L", false, false);
    }
  }
}
