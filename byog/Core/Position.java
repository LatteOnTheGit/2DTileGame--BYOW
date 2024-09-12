package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class Position implements Serializable {
  int x;
  int y;
  TETile type;
  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position(int x, int y, TETile type) {
    this.x = x;
    this.y = y;
    this.type = type;
  }

  public void move(char dir) {
    switch (dir) {
      case 'w' :
        y++;
        break;
      case 's' :
        y--;
        break;
      case 'a' :
        x--;
        break;
      case 'd' :
        x++;
        break;
      case 'i' :
        y++;
        break;
      case 'k' :
        y--;
        break;
      case 'j' :
        x--;
        break;
      case 'l' :
        x++;
        break;

    }
  }

  public String toString() {
    return "Position at X = " + x + " , Y = " + y;
  }
}
