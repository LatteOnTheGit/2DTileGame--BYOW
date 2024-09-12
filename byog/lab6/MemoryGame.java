package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"试试", "算你运气好",
                                                   "蒙对了", "马上就没了", "实在是菜",
                                                   "对你太难了", "放弃吧"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(80, 80, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        this.rand = new Random(seed);

    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(CHARACTERS[rand.nextInt(25)]);
        }
        return sb.toString();
    }

    public void drawFrame(String s, boolean status) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.cyan);
        StdDraw.setPenRadius(0.008);
        StdDraw.line(0, 0.90 * (double)height, width, 0.90 * (double)height);
        StdDraw.setPenColor(Color.RED);
        StdDraw.text(0.1 * width, 0.95 * height, "round: " + round);
        if (status) StdDraw.text(0.5 * width, 0.95 * height, "好好看, 别乱按");
        else StdDraw.text(0.5 * width, 0.95 * height, "抓紧写 倒计时10s");
        StdDraw.textRight(0.9 * width, 0.95 * height, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
        StdDraw.text(0.5 * width, 0.5 * height, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (char c : letters.toCharArray()) {
            drawFrame(Character.toString(c), playerTurn);
            StdDraw.pause(1000);
            drawFrame("", playerTurn);
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        String input = "";
        drawFrame("Its time to type", playerTurn);
        StdDraw.pause(500);
        while (input.length() < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            input += StdDraw.nextKeyTyped();
            drawFrame(input, playerTurn);
        }
        StdDraw.pause(500);
        return input;
    }

    public void start() {
        String input = "";
        drawFrame("随便按开始游戏", playerTurn);
        while(!StdDraw.hasNextKeyTyped()) {
            continue;
        }
        StdDraw.nextKeyTyped();
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        round = 0;
        gameOver = false;
        start();
        //TODO: Establish Game loop
        while (!gameOver) {
            round++;
            playerTurn = true;
            drawFrame("Welcome to ROUND" + round,playerTurn);
            StdDraw.pause(800);
            String q = generateRandomString(round);
            flashSequence(q);
            playerTurn = false;
            String answer = solicitNCharsInput(round);
            gameOver = !q.equals(answer);
        }
        playerTurn = true;
        drawFrame("拿铁都比你强 菜狗", playerTurn);
    }

}
