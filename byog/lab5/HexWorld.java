package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
  private static final int sideLength = 3;
  private static final int WIDTH = 100;
  private static final int HEIGHT = 100;
  private static final long SEED = 3112323;
  private static final Random RANDOM = new Random(SEED);

  public static TETile[][] addHexagon(center p, int s, TETile[][] word, TETile type) {
    // height(y) would be s, from 0 to s-1.
    // width start with s, s + 2 ... s + (s - 1) * 2 ....., s + 2, s.
    // leftMost position(x) p -1, -2, -3... -(s - 1)...-2, -1.
    int x = p.x;
    int y = p.y;
    for (int i = y; i < y + 2 * s; i++) {
      int[] feats = featsOfTile(i - y, s, x);
      int numOfTile = feats[1];
      int newX = feats[0];

      for(int n = 0; n < numOfTile; n++) {
//        word[newX + n][i] = TETile.colorVariant(type, 1000, 1000, 1000, new Random(n));
        word[newX + n][i] = type;
      }
    }
    return word;
  }

  private static int[] featsOfTile(int absHeight, int s, int x) {
    if (absHeight < s) return new int[]{x - absHeight,absHeight * 2 + s};
    if (absHeight > s) return new int[]{x - (s - 1) + (absHeight - s),(s - 1) * 2 + s - (absHeight - s) * 2};
    return new int[]{x - (s - 1), absHeight * 2 + s - 2};
  }

  private static center[] centersCol(center bottom, int s, int colNum) {
    int x = bottom.x;
    int y = bottom.y;
    int num = colNum < s ? s + colNum : 2 * s - 1 - (colNum - s + 1);
    center[] res = new center[num];
    for (int n = 0; n < num; n++) {
      res[n] = new center(x, y + n * 2 * s);
    }
    return res;
  }

  private static center[] centersRow(int x, int y, int s) {
    int length = 2 * s - 1;
    center[] res = new center[length];
    for (int n = 0; n < length; n++) {
      int newX = x + n * length;
      int newY = n < s ? y - n * s : y - (s - 1) * s + (n - s + 1) * s;
      res[n] = new center(newX, newY);
    }
    return res;
  }

  public static TETile[][] tesselationOfHex(TETile[][] world, int x, int y, int s) {
    center[] row = centersRow(x, y, s);
    for (int n = 0; n < row.length; n++) {
      center[] col = centersCol(row[n], s, n);
      for (center c : col) {
        world = addHexagon(c, s, world, randomTile());
      }
    }
    return world;
  }

  private static TETile randomTile() {
    int tileNum = RANDOM.nextInt(5);
    switch (tileNum) {
      case 0: return Tileset.WALL;
      case 1: return Tileset.FLOWER;
      case 2: return Tileset.GRASS;
      case 3: return Tileset.LOCKED_DOOR;
      case 4: return Tileset.TREE;
      default: return Tileset.STAR;
    }
  }

  public static void main(String[] args) {
    TERenderer ter = new TERenderer();
    ter.initialize(WIDTH, HEIGHT);
    TETile[][] world = new TETile[WIDTH][HEIGHT];
    for (int x = 0; x < WIDTH; x += 1) {
      for (int y = 0; y < HEIGHT; y += 1) {
        world[x][y] = Tileset.NOTHING;
      }
    }
    world = tesselationOfHex(world, 50,50,4);
    ter.renderFrame(world);
  }
}
