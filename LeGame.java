import java.util.Scanner;
import java.lang.Math;

public class LeGame {
  static int MAP_WIDTH = 20;
  static int MAP_HEIGHT = 20;
  static int MAX_FOOD_AMOUNT = 15;
  static int MAX_OBSTACLE_AMOUNT = 20;
  static final int X = 1;
  static final int Y = 0;
  static final int BLANK = 0;
  static final int PLAYER = 1;
  static final int FOOD = 2;
  static final int OBSTACLE = 3;
  static int[][] map = new int[MAP_HEIGHT][MAP_WIDTH];
  static final String[] symbols = {" ", "P", "O", "-"};
  static int score = 0;

  public static void moveCursor(int line, int column) {
    System.out.print("\033[" + line + ";" + column + "f");
  }

  public static void resetCursor() {
    moveCursor(0, 0);
  }

  public static void eraseLine() {
    System.out.println("\033[2K");;
  }

  public static void cursorNextLine() {
    System.out.print("\033[1B");
    System.out.flush();
  }

  public static void cursorLineBeginning() {
    System.out.print("\033[0G");
  }

  public static void clearScreen() {
    System.out.print("\033[2J");
    System.out.flush();
  }

  public static void printHelp() {
    moveCursor(1, 24);
    System.out.print("commands:");
    moveCursor(2, 24);
    System.out.print("movements:");
    moveCursor(3, 24);
    System.out.print("    | W |");
    moveCursor(4, 24);
    System.out.print("| A | S | D |");
    moveCursor(5, 24);
    System.out.print("Q: quit");
    moveCursor(6, 24);
    System.out.print("Press enter to");
    moveCursor(7, 24);
    System.out.print("confirm the commands");
    System.out.flush();
  }

  public static void printScore() {
    moveCursor(8, 24);
    System.out.print("score: " + score);
  }

  public static void printWin() {
    clearScreen();
    moveCursor(10, 5);
    System.out.print("WIN!!");
    moveCursor(11, 5);
    System.out.print("PRESS_ENTER");
  }

  public static void renderFrame() {
    moveCursor(0, 1);
    for(int i = 0; i <= 20; i++) {
      System.out.print("_");
    }
    for(int i = 0; i < 22; i++) {
      moveCursor(i, 1);
      System.out.print("|");
    }
    for(int i = 0; i < 22; i++) {
      moveCursor(i, 22);
      System.out.print("|");
    }
    System.out.flush();
  }

  public static void renderMap() {
    for(int i = 0; i < MAP_HEIGHT; i++) {
      moveCursor(i + 2, 2);
      for(int j = 0; j < MAP_WIDTH; j++) {
        System.out.print(symbols[map[i][j]]);
      }
    }
    System.out.flush();
  }

  public static void fillMap(int amount, int type) {
    int x = 0;
    int y = 0;
    for(int i = 0; i < amount; i++) {

      do {
        x = (int)(Math.random() * (MAP_HEIGHT - 1)) + 1;
        y = (int)(Math.random() * (MAP_WIDTH - 1)) + 1;
      } while (map[y][x] != BLANK);
      map[y][x] = type;
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    System.out.println("This program only works on terminals that support ANSI escape codes! (UNIX shells, cygwin, ANSICON) or just use linux");
    System.out.println("The window size shouldn't be changed during the runtime of this program");
    System.out.println("Make sure the window size is more than 30x25");
    System.out.print("PRESS ENTER");
    input.nextLine();

    clearScreen();

    int[] playerCoord = new int[2];

    int foodAmount =(int) (Math.random() * (MAX_FOOD_AMOUNT)) + 1;
    int obstacleAmount =(int) (Math.random() * (MAX_OBSTACLE_AMOUNT - 9)) + 10;

    fillMap(foodAmount, FOOD);
    fillMap(obstacleAmount, OBSTACLE);

    printHelp();

    String command;
    boolean running = true;
    map[0][0] = PLAYER;
    int x;
    int y;


    renderFrame();
    while(running) {
      x = playerCoord[X];
      y = playerCoord[Y];
      renderMap();

      moveCursor(22, 0);
      System.out.print("Enter the command: ");
      System.out.flush();
      command = input.nextLine();
      moveCursor(22, 0);
      eraseLine();
      if(command.length() == 0) {
        continue;
      }

      switch(command.charAt(0)){
        case 'A':
          x = Math.floorMod((playerCoord[X] - 1), MAP_WIDTH);
          break;
        case 'D':
          x = Math.floorMod((playerCoord[X] + 1), MAP_WIDTH);
          break;
        case 'S':
          y = Math.floorMod((playerCoord[Y] + 1), MAP_HEIGHT);
          break;
        case 'Q':
          running = false;
          break;
        case 'W':
          y = Math.floorMod((playerCoord[Y] - 1), MAP_HEIGHT);
          break;
      }

      if(map[y][x] == OBSTACLE) {
        continue;
      }
      if(map[y][x] == FOOD){
        score++;
        printScore();
      }
      map[playerCoord[Y]][playerCoord[X]] = BLANK;
      playerCoord[Y] = y;
      playerCoord[X] = x;
      map[playerCoord[Y]][playerCoord[X]] = PLAYER;


      if(score == foodAmount) {
        running = false;
        printWin();
        input.nextLine();
      }
    }
    input.close();
  }
}
