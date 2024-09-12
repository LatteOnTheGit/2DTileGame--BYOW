package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Rectangle {
  int x;
  int y;
  int width;
  int height;
  public Rectangle(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public void generateRoom(TETile[][] world) {
    for (int i = x; i <= x + width; i++) {
      for (int m = y; m <= y + height; m ++) {
        if (i == x || m == y || i == x + width || m == y + height) {
          world[i][m] = world[i][m] == gameEngine.BG ? Tileset.WALL : world[i][m];
          continue;
        }
        world[i][m] = Tileset.FLOOR;
      }
    }
//    world[x][y] = Tileset.STAR;
  }

  public void generateHalls(TETile[][] world) {
    for (int m = x; m < x + width; m++)
      for (int n = y; n < y + height; n++) {
        world[m][n] = Tileset.FLOOR;
        surrounded(m, n, world);
      }
  }

  private static void surrounded(int x, int y, TETile[][] world) {
    int[][] dirs = new int[][]{{1, 0}, {-1,0}, {0, 1}, {0, -1}};
    for (int[] dir : dirs) {
      int newX = x + dir[0];
      int newY = y + dir[1];
      world[newX][newY] = world[newX][newY] == gameEngine.BG ? Tileset.WALL : world[newX][newY];
    }
  }
}
