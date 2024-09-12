package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class MapGenerator {
  private static final int SEED = 869685544;
  private static final Random RANDOM = new Random(SEED);
  private static final int HEIGHT = 100;
  private static final int WIDTH = 100;
  private static final int ROOMHEIGHT = 4;
  private static final int ROOMEWIDETH = 8;

  private static final int ROOMHEIGHTLOW = 2;
  private static final int ROOMEWIDETHLOW = 4;
  private static final int HALLWAYLEN = 8;
  private static final int HALLWAYLENLOW = 2;
  private static int count = 1000;
  /**
   * probability of generate random hallway is 1/PROBOFHALLWAY.
   */
  private static final int PROBOFHALLWAY = 3;
  /**
   * return world added a rectangle.
   */
//  public static void addRectangle(TETile[][] world, Position p) {
//    int x = p.x;;
//    int y = p.y;
//    int sizeX = RANDOM.nextInt(ROOMEWIDETH - ROOMEWIDETHLOW + 1) + ROOMEWIDETH;
//    int sizeY = RANDOM.nextInt(ROOMHEIGHT - ROOMHEIGHTLOW + 1) + ROOMHEIGHT;
//    if(!isOverlap(x, y, sizeX, sizeY, world)) return;
//    for (int col = x; col < sizeX + x; col++) {
//      for (int row = y; row < sizeY + y; row++) {
//        if (col == x || col == x + sizeX - 1 || row == y || row == sizeY + y - 1) {
//          world[col][row] = world[col][row] == Tileset.NOTHING ? Tileset.WALL : world[col][row];
//          continue;
//        }
//        world[col][row] = Tileset.FLOOR;
//        if (RANDOM.nextInt(PROBOFHALLWAY) == 1) addHallWay(world, new Position(col, row));
//      }
//    }
//  }

  // rectangle could fail because of invalid or overlapping
  // which result in end of expansion.
  public static void addRectangle(TETile[][] world, Position p) {
    int x = p.x;;
    int y = p.y;
    int sizeX = RANDOM.nextInt(ROOMEWIDETH - ROOMEWIDETHLOW + 1) + ROOMEWIDETH;
    int sizeY = RANDOM.nextInt(ROOMHEIGHT - ROOMHEIGHTLOW + 1) + ROOMHEIGHT;
    int dirX = RANDOM.nextBoolean() ? 1 : -1;
    int dirY = RANDOM.nextBoolean() ? 1 : -1;
//    if (!isValid(x + sizeX * dirX, y + sizeY * dirY)) {
//      addRectangle(world, p);
//      return;
//    }
    // when called from hallway, overlapping could kill because starting at a WALL
    if(!isOverlap(x, y, sizeX * dirX, sizeY * dirY, world)) {
      return;
    }
//    count--;
    for (int col = x; col != sizeX * dirX + x; col += dirX) {
      for (int row = y; row < sizeY * dirY + y; row += dirY) {
        if (col == x || col == x + sizeX * dirX - dirX || row == y || row == sizeY * dirY + y - dirY) {
          world[col][row] = world[col][row] == Tileset.NOTHING ? Tileset.WALL : world[col][row];
          continue;
        }
        world[col][row] = Tileset.FLOOR;
//        if (RANDOM.nextInt(PROBOFHALLWAY) == 1) addHallWay(world, new Position(col, row));
      }
    }
  }

  private static void addHallWay(TETile[][] world, Position p) {
    int x = p.x;
    int y = p.y;
    int length = RANDOM.nextInt(HALLWAYLEN - HALLWAYLENLOW + 1) + HALLWAYLENLOW;
    boolean vertical = RANDOM.nextBoolean();
    addHallWayHelper(world, p, length, vertical);
  }

  private static void addHallWayHelper(TETile[][] world, Position p, int length, boolean vertical) {
    int x = p.x;
    int y = p.y;
    int dir = RANDOM.nextBoolean() ? 1 : -1;
    if (vertical) {
      if (!isValid(x, y - 1) || !isValid(x + length * dir, y + 1)) {
        return;
      }
      int i = x;
      while (i != x + length * dir) {
        world[i][y + 1] = world[i][y + 1] == Tileset.NOTHING ? Tileset.WALL : world[i][y + 1];
        world[i][y - 1] = world[i][y - 1] == Tileset.NOTHING ? Tileset.WALL : world[i][y - 1];
        world[i][y] = Tileset.FLOOR;
        //
        randomChoice(world, new Position(i, y));
        i += dir;
      }
      world[i - dir][y] = world[i][y] == Tileset.FLOOR ? world[i - dir][y] : Tileset.WALL;
      randomChoice(world, new Position(i - dir, y));
    } else {
      if (!isValid(x - 1, y) || !isValid(x + 1, y + length * dir)) {
        return;
      }
      int i = y;
      while (i != y + length * dir) {
        world[x + 1][i] = world[x + 1][i] == Tileset.NOTHING ? Tileset.WALL : world[x + 1][i];
        world[x - 1][i] = world[x - 1][i] == Tileset.NOTHING ? Tileset.WALL : world[x - 1][i];
        world[x][i] = Tileset.FLOOR;
        //
        randomChoice(world, new Position(x, i));
        i += dir;
      }
      world[x][i - dir] = world[x][i] == Tileset.FLOOR ? world[x][i - dir] : Tileset.WALL;
      randomChoice(world, new Position(x, i - dir));
    }
  }

  private static void randomChoice(TETile[][] world, Position p) {
    if (count == 0) return;
//    addRectangle(world, p);
    int choice = RANDOM.nextInt(2);
    switch (choice) {
      case 0: {
        addRectangle(world, p);
        break;
      }
      case 1: {
        addHallWay(world, p);
        break;
      }
    }
  }

  private static boolean isValid(int x, int y) {
    return x < WIDTH && y < HEIGHT && x > 0 && y > 0;
  }

  private static boolean isOverlap(int x, int y, int sizeX, int sizeY, TETile[][] world) {
//    sizeY = sizeY - 1;
//    sizeX = sizeX - 1;
    return isValid(x, y)
        && isValid(x + sizeX, y + sizeY)
        && world[x][y] == Tileset.NOTHING
        && world[x + sizeX][y + sizeY] == Tileset.NOTHING
        && world[x + sizeX][y] == Tileset.NOTHING
        && world[x][y + sizeY] == Tileset.NOTHING;
  }

  private static Position[] randomGeneratePos(int num) {
    Position[] res = new Position[num];
    for (int i = 0; i < num; i++) {
      res[i] = new Position(RANDOM.nextInt(WIDTH - 20) + 20, RANDOM.nextInt(HEIGHT - 20) + 20);
    }
    return res;
  }

  private static void createSampleWorld() {
    TERenderer ter = new TERenderer();
    ter.initialize(WIDTH, HEIGHT);
    TETile[][] world = new TETile[WIDTH][HEIGHT];
    for (int x = 0; x < WIDTH; x += 1) {
      for (int y = 0; y < HEIGHT; y += 1) {
        world[x][y] = Tileset.NOTHING;
      }
    }
    Position[] pTest = randomGeneratePos(100);
    for (Position p : pTest) {
      addRectangle(world,p);
    }
//    Position pTest = new Position(50, 50);
//    addRectangle(world, pTest);
    ter.renderFrame(world);
  }

  public static void main(String[] args) {
    createSampleWorld();
  }
}
