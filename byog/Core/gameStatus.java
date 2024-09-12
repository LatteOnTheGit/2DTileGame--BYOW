package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class gameStatus implements Serializable {
  TETile[][] world;
  Position player;
  Position player1;
//  Position door;

  public gameStatus(TETile[][] world, Position player) {
    this.world = world;
    this.player = player;
    this.player1 = null;
  }

  public gameStatus(TETile[][] world, Position player, Position player1) {
    this.world = world;
    this.player = player;
    this.player1 = player1;
  }
}
