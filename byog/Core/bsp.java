package byog.Core;

import byog.TileEngine.TETile;
import org.w3c.dom.css.Rect;

import java.util.LinkedList;
import java.util.Random;

public class bsp {
  int x;
  int y;
  int width;
  int height;
  Rectangle room;
  LinkedList<Rectangle> halls = new LinkedList<>();
  bsp leftChild;
  bsp rightChild;

  static final int MINEDGE = gameEngine.MINEDGE;
  static final int MAXEDGE = gameEngine.MAXEDGE;

  static final int MINROOMSIZE = 3;
//  static final int SEED = 123445;
  final Random RANDOM;

  public bsp(int x, int y, int width, int height, Random RANDOM) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.RANDOM = RANDOM;
  }

  public boolean split() {
    if (leftChild != null || rightChild != null) return false;
    boolean splitH = RANDOM.nextBoolean();
    // if width 25% greater than height, split width.
    if (width > height && (double)width / height > 1.25) {
      splitH = false;
    } else if (height > width && (double)height / width > 1.25) {
      splitH = true;
    }
    int max = (splitH ? height : width) - MINEDGE;
    // if split could not generate bsp greater than MINEDGE, abort.
    if (max <= MINEDGE) return false;
    int splitPoint = RANDOM.nextInt(max - MINEDGE + 1) + MINEDGE;
    if (splitH) {
      leftChild = new bsp(x, y, width, splitPoint, RANDOM);
      rightChild = new bsp(x, y + splitPoint, width,height - splitPoint, RANDOM);
    } else {
      leftChild = new bsp(x, y, splitPoint, height, RANDOM);
      rightChild = new bsp(x + splitPoint, y, width - splitPoint, height, RANDOM);
    }
    return true;
  }

  public void createRooms(TETile[][] world) {
    if (leftChild != null || rightChild != null) {
      if (leftChild != null) {
        leftChild.createRooms(world);
      }
      if (rightChild != null) {
        rightChild.createRooms(world);
      }
      if (leftChild != null && rightChild != null) {
        createHallways(leftChild.getRoom(), rightChild.getRoom(), world);
      }
    } else {
      Position size = new Position(RANDOM.nextInt(width - 2 - MINROOMSIZE + 1) + MINROOMSIZE, RANDOM.nextInt(height - 2 - MINROOMSIZE + 1) + MINROOMSIZE);
      Position pos = new Position(RANDOM.nextInt(width - size.x - 1) + 1, RANDOM.nextInt(height - size.y - 1) + 1);
      room = new Rectangle(x + pos.x, y + pos.y, size.x, size.y);
      room.generateRoom(world);
    }
  }

  public Rectangle getRoom() {
    if (room != null) return room;
    Rectangle leftRoom = null;
    Rectangle rightRoom = null;
    if (leftChild != null) leftRoom = leftChild.getRoom();
    if (rightChild != null) rightRoom = rightChild.getRoom();
    if (leftRoom == null && rightRoom == null) return null;
    if (leftRoom == null) return rightRoom;
    if (rightRoom == null) return leftRoom;
    if (RANDOM.nextBoolean()) return leftRoom;
    else return rightRoom;
  }

  public void createHallways(Rectangle l, Rectangle r, TETile[][] world) {
    // random select point1 in l, point 2 in r.
    Position point1 = new Position(RANDOM.nextInt(l.width - 1) + l.x + 1, RANDOM.nextInt(l.height - 1) + l.y + 1);
    Position point2 = new Position(RANDOM.nextInt(r.width - 1) + r.x + 1, RANDOM.nextInt(r.height - 1) + r.y + 1);
    // decide relative position of point1 and point2.
    int w = point2.x - point1.x;
    int h = point2.y - point1.y;
    boolean choice = RANDOM.nextBoolean();
    // 2 is to the left of 1.
    if (w < 0) {
      // 2 is under 1.
      if (h < 0) {
        // random choice between 2 path.
        if (choice) {
          halls.offer(new Rectangle(point2.x, point2.y, 1, -h));
          halls.offer(new Rectangle(point2.x, point1.y, -w, 1));
        } else {
          halls.offer(new Rectangle(point2.x, point2.y, -w, 1));
          halls.offer(new Rectangle(point1.x, point2.y, 1, -h));
        }
      } else if (h > 0) {
        if (choice) {
          halls.offer(new Rectangle(point2.x, point2.y, 1-w, 1));
          halls.offer(new Rectangle(point1.x, point1.y, 1, h+1));
        } else {
          halls.offer(new Rectangle(point2.x, point1.y, 1, h));
          halls.offer(new Rectangle(point2.x, point1.y, -w, 1));
        }
      } else {
          halls.offer(new Rectangle(point2.x, point2.y, -w,1));
      }
    } else if (w > 0) {
      if (h < 0) {
        if (choice) {
          halls.offer(new Rectangle(point1.x, point1.y, w + 1, 1));
          halls.offer(new Rectangle(point2.x, point2.y, 1, 1-h));
        } else {
          halls.offer(new Rectangle(point1.x, point2.y, 1, -h));
          halls.offer(new Rectangle(point1.x, point2.y, w,1));
        }
      } else if (h > 0) {
        if (choice) {
          halls.offer(new Rectangle(point1.x, point1.y, 1, h ));
          halls.offer(new Rectangle(point1.x, point2.y, w,1));
        } else {
          halls.offer(new Rectangle(point1.x, point1.y, w,1));
          halls.offer(new Rectangle(point2.x, point1.y, 1, h));
        }
      } else {
        halls.offer(new Rectangle(point1.x, point1.y, w,1));
      }
    } else {
      if (h > 0) {
        halls.offer(new Rectangle(point1.x, point1.y, 1, h));
      } else {
        halls.offer(new Rectangle(point2.x, point2.y, 1, -h));
      }
    }
    for (Rectangle hall : halls) {
      hall.generateHalls(world);
    }
  }

}
