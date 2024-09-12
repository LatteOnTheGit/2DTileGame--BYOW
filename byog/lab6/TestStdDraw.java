package byog.lab6;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class TestStdDraw {
  static int width = 40;
  static int height = 40;

  static void test(String s) {
//    StdDraw.clear(Color.BLACK);
//    StdDraw.setPenColor(Color.cyan);
//    StdDraw.setPenRadius(0.008);
//    StdDraw.line(0, 0.90 * (double)height, width, 0.90 * (double)height);
//    StdDraw.setPenColor(Color.RED);
//    StdDraw.text(20, 20, input);
//    StdDraw.show();
//    StdDraw.pause(800);
//    StdDraw.clear(Color.BLACK);
//    StdDraw.show();
    StdDraw.clear(Color.BLACK);
    StdDraw.setPenColor(Color.cyan);
    StdDraw.setPenRadius(0.008);
    StdDraw.line(0, 0.90 * (double)height, width, 0.90 * (double)height);
    StdDraw.setPenColor(Color.RED);
    StdDraw.text(0.5 * width, 0.95 * height, "round 5");
    StdDraw.text(0.5 * width, 0.5 * height, s);
    StdDraw.show();
  }

  static void change() {
    StdDraw.pause(1000);
    test("");
    StdDraw.pause(500);
  }

  public static String solicitNCharsInput(int n) {
    String input = "";
    while (input.length() < n) {
      if (!StdDraw.hasNextKeyTyped()) {
        continue;
      }
      input += StdDraw.nextKeyTyped();
      test(input);
    }
    return input;
  }

  public static void main(String[] args) {

    StdDraw.enableDoubleBuffering();

    StdDraw.setCanvasSize(width * 16, height * 16);
    Font font = new Font("Monaco", Font.BOLD, 30);
    StdDraw.setFont(font);
    StdDraw.setXscale(0, width);
    StdDraw.setYscale(0, height);
    test("test");
    change();
    test("A");
    change();
    test("B");
    change();
    test("please input");
    String answer = solicitNCharsInput(2);
    if (answer.equals("AB")) test("success");
    test("fail, ");
  }
}
