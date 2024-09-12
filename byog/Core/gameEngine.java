package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.*;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class gameEngine {
  static final int GAMEWIDTH = Game.WIDTH;
  static final int GAMEHEIGHT = Game.HEIGHT;
  // issue with 2345234
  static long SEED = 456784;
  static Random RANDOM = new Random(SEED);
  static TERenderer ter;
  static bsp rootLeaf;
  static final int MINEDGE = 5;
  static final int MAXEDGE = 15;
  static final TETile BG = Tileset.NOTHING;

  private static TETile[][] worldInit() {
    TETile[][] world = new TETile[GAMEWIDTH][GAMEHEIGHT];
    for (int x = 0; x < GAMEWIDTH; x++) {
      for (int y = 0; y < GAMEHEIGHT; y++) {
        world[x][y] = BG;
      }
    }
    return world;
  }

  private static void initBsp(TETile[][] world) {
    rootLeaf = new bsp(0, 0, GAMEWIDTH, GAMEHEIGHT, RANDOM);
    LinkedList<bsp> q = new LinkedList<>();
    q.offer(rootLeaf);
    while (!q.isEmpty()) {
      bsp cur = q.removeFirst();
      // visual test
//      drawLine(world, cur.x, cur.y);
      if (cur.leftChild == null && cur.rightChild == null) {
        if (cur.width > MAXEDGE || cur.height > MAXEDGE || RANDOM.nextInt(100) > 25) {
          // if split success, push sub-leaf to q.
          if (cur.split()) {
            q.offer(cur.leftChild);
            q.offer(cur.rightChild);
          }
        }
      }
    }
    rootLeaf.createRooms(world);
  }

  /**
   * helper method draw all leaf segment line
   */
  private static void drawLine(TETile[][] word, int x, int y) {
    for (int m = 0; m < GAMEWIDTH; m++) {
      word[m][y] = Tileset.LINEH;
    }
    for (int n = 0; n < GAMEHEIGHT; n++) {
      word[x][n] = Tileset.LINE;
    }
  }

  static void resetSeed(long seed) {
    gameEngine.SEED = seed;
    gameEngine.RANDOM = new Random(SEED);
  }

  public static TETile[][] generateWorld() {
    TETile[][] res = worldInit();
    initBsp(res);
    return res;
  }

  // This part support interactive and player feats.

  /**
   * randomly generate a position within a room.
   */
  private static Position getAPos(TETile[][] world, TETile type) {
    int x = RANDOM.nextInt(GAMEWIDTH);
    int y = RANDOM.nextInt(GAMEHEIGHT);
    TETile searchTile = type.equals(Tileset.PLAYER) || type.equals(Tileset.PLAYER1) ? Tileset.FLOOR : Tileset.WALL;
    while(!world[x][y].equals(searchTile)) {
      x = RANDOM.nextInt(GAMEWIDTH);
      y = RANDOM.nextInt(GAMEHEIGHT);
    }
    world[x][y] = type;
    return new Position(x, y, type);
  }

  private final static HashMap<Character, Character> dirs = new HashMap<>();
  static {
    dirs.put('w', 's');
    dirs.put('s', 'w');
    dirs.put('a', 'd');
    dirs.put('d', 'a');
    dirs.put('j', 'l');
    dirs.put('l', 'j');
    dirs.put('i', 'k');
    dirs.put('k', 'i');
  }

  private static TETile[][] validMove(TETile[][] world, Position player, char dir) {
    world[player.x][player.y] = Tileset.FLOOR;
    player.move(dir);
    if (player.x < 0 || player.x >= GAMEWIDTH || player.y < 0 || player.y >= GAMEHEIGHT || world[player.x][player.y].equals(Tileset.WALL)) {
      player.move(dirs.get(dir));
    }
    if (world[player.x][player.y].equals(Tileset.LOCKED_DOOR)) views.winGame();
    world[player.x][player.y] = player.type;
    return world;
  }

  public static TETile[][] playWithInput(long seed, String keys, boolean save, boolean reload) {
    TETile[][] world;
    Position player;
    Position door;
    if (reload) {
      gameStatus saves = reload();
      world = saves.world;
      player = saves.player;
    } else {
      resetSeed(seed);
      world = generateWorld();
      player = getAPos(world, Tileset.PLAYER);
      door = getAPos(world, Tileset.LOCKED_DOOR);
    }

    for (char c : keys.toCharArray()) {
      world = validMove(world,player, c);
    }
    if (save) {
      gameStatus game = new gameStatus(world, player);
      saveGame(game);
    }
    return world;
  }

  private static void saveGame(gameStatus game) {
    try {
      FileOutputStream file = new FileOutputStream("./save.txt");
      ObjectOutputStream objectOut = new ObjectOutputStream(file);
      objectOut.writeObject(game);
    } catch (IOException a) {

    }

  }

  private static gameStatus reload() {
    try {
      FileInputStream fileIn = new FileInputStream("./save.txt");
      try {
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        gameStatus game = (gameStatus) objectIn.readObject();
        return game;
      } catch (ClassNotFoundException c) {

      }

    } catch (IOException a){

    }
//    FileInputStream fileIn = new FileInputStream("./save.txt");
//    ObjectInputStream objectIn = new ObjectInputStream(fileIn);
//    gameStatus game = (gameStatus) objectIn.readObject();
//    return game;
    return null;
  }

  public static void playWithKey(long seed, boolean reload, boolean twoPlayer) throws IOException, ClassNotFoundException {
    TETile[][] world;
    Position player;
    Position player1 = null;
    Position door;
    if (reload) {
      gameStatus game = reload();
      world = game.world;
      player = game.player;
      if (game.player1 != null) player1 = game.player1;
    } else {
      resetSeed(seed);
      world = generateWorld();
      player = getAPos(world, Tileset.PLAYER);
      door = getAPos(world, Tileset.LOCKED_DOOR);
      // add feats allowed 2 players.
      if (twoPlayer) {
        player1 = getAPos(world, Tileset.PLAYER1);
//        getAPos(world, Tileset.LOCKED_DOOR);
      }
    }
    ter = new TERenderer();
    ter.initialize(GAMEWIDTH, GAMEHEIGHT + 4, 0, 2);
    ter.renderFrame(world);
    boolean end = false;
    while (!end) {
      if (StdDraw.hasNextKeyTyped()) {
        char dir = StdDraw.nextKeyTyped();
        if (dir == 'q') end = true;
        if (dir == 'm') {
          StartGame.main(new String[]{"visual"});
          System.exit(0);
        }
        if (twoPlayer && (dir == 'j' || dir == 'k' || dir == 'l' || dir == 'i')) {
          world = validMove(world, player1, dir);
        } else world = validMove(world, player, dir);
        ter.renderFrame(world);
      }
      if (StdDraw.isMousePressed()) {
        int x = (int)StdDraw.mouseX();
        int y = (int)StdDraw.mouseY();
        if (x < GAMEWIDTH && x >= 0 && y < GAMEHEIGHT && y >= 0) {
          String mess = world[x][y].description();
          ter.renderFrame(world, mess);
        }

      }

    }
    if (twoPlayer) saveGame(new gameStatus(world, player, player1));
    else saveGame(new gameStatus(world, player));
    views.quit();
    System.exit(0);
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException {
//    ter = new TERenderer();
//    ter.initialize(GAMEWIDTH, GAMEHEIGHT);
//    TETile[][] world = playWithInput(123412354L, "a", true, true);
//
//    ter.renderFrame(world);
    playWithKey(123412354L, false, true);

  }
}
