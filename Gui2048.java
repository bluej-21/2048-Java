/** Gui2048.java */
/** PA8 Release */


/*
 * Name: Jae Song
 * Login: cs8barf
 * Date: 3/5/15
 * File: Gui2048.java
 * Sources of Help: TEXTBOOK, javadocs, PA8 pdf
 *
 * Purpose: this program will implement GUI via javafx onto the
 *          2048 game. It will do everything the previous PA of
 *          2048 will do but on a GUI setting. This program has
 *          three classes, Gui2048, MoveHandler, and Tile
 */


import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;


/*
 * Class Name: Gui2048
 * Purpose: this class is the main implementation of the
 *          GUI onto the game 2048.
 */
public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(
                         238, 228, 218, 0.35);
    private static final Color COLOR_2 = Color.rgb(238, 228, 218);
    private static final Color COLOR_4 = Color.rgb(237, 224, 200);
    private static final Color COLOR_8 = Color.rgb(242, 177, 121);
    private static final Color COLOR_16 = Color.rgb(245, 149, 99);
    private static final Color COLOR_32 = Color.rgb(246, 124, 95);
    private static final Color COLOR_64 = Color.rgb(246, 94, 59);
    private static final Color COLOR_128 = Color.rgb(237, 207, 114);
    private static final Color COLOR_256 = Color.rgb(237, 204, 97);
    private static final Color COLOR_512 = Color.rgb(237, 200, 80);
    private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(
                         238, 228, 218, 0.73);

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(
                         249, 246, 242); // For tiles >= 8
    private static final Color COLOR_VALUE_DARK = Color.rgb(
                         119, 110, 101); // For tiles < 8
  
    //final variables for magic numbers
    private final int LABEL_SIZE = 30;
    private final int SCORE_SIZE = 20;
    private final int OVER_SIZE = 50;
    private final int VALUE_SIZE = 20;

    private final int HBOX_SPACING = 100;
    private final int GRID_GAP = 10;
    private final int PADDING = 10;

    private final int SCREEN_WIDTH = 120;
    private final int SCREEN_HEIGHT = 130;
    private final int RECTANGLE_DIMENSION = 100;

    private final int _2_TILE = 2;
    private final int _4_TILE = 4;
    private final int _8_TILE = 8;
    private final int _16_TILE = 16;
    private final int _32_TILE = 32;
    private final int _64_TILE = 64;
    private final int _128_TILE = 128;
    private final int _256_TILE = 256;
    private final int _512_TILE = 512;
    private final int _1024_TILE = 1024;
    private final int _2048_TILE = 2048;


    /** Add your own Instance Variables here */
    
    //tile for the 2048 board
    private Tile tile;

    //will consist of the score, title, and the board
    private BorderPane borderPane;

    //score 
    private Label score;

    //the overlaying game over screen
    private StackPane gameOver;

    //will have game over screen and the borderPane
    private StackPane stackPane;
    

    /*
     * Name: start
     * Purpose: this game will make the 2048 game. This is 
     *          essentially the game playing part of the
     *          program.
     * Parameter: Stage primaryStage
     * Return: void
     *
     */
    @Override
    public void start(Stage primaryStage)
    {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));
        
        /** Add your Code for the GUI Here */
        
        //make the title
        Label title = new Label("2048");
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD,
                      FontPosture.ITALIC,this.LABEL_SIZE));
       
        //make the score
        this.setScore(new Label("Score: " + 
                                this.getBoard().getScore()));
        this.getScore().setFont(Font.font("Times New Roman", 
                                FontWeight.BOLD,this.SCORE_SIZE));

        //make a horizontal box for title and score
        HBox hBox = new HBox();
        hBox.setSpacing(this.HBOX_SPACING);
        hBox.setPadding(new Insets(this.PADDING,this.PADDING,
                        this.PADDING,this.PADDING));
        hBox.getChildren().add(title);
        hBox.getChildren().add(this.getScore());           
        
        //make the game over screen
        this.setGameOver(new StackPane());

        //text game over
        Text end = new Text("Game Over!");
        end.setFont(Font.font("Times New Roman", FontWeight.BOLD,
                    FontPosture.ITALIC,this.OVER_SIZE));
        
        //the rectangle for the game over
        Rectangle endBox = new Rectangle();
        endBox.setWidth(this.getBoard().GRID_SIZE 
                        * this.SCREEN_WIDTH);
        endBox.setHeight(this.getBoard().GRID_SIZE 
                         * this.SCREEN_HEIGHT);
        endBox.setFill(COLOR_GAME_OVER);
        
        //add the text and rectangle onto the stack pane, gameOver
        this.getGameOver().getChildren().add(endBox);
        this.getGameOver().getChildren().add(end);

        //make the board for the game
        this.setTile(new Tile(this.getBoard().getGrid()));
        this.getTile().setHgap(this.GRID_GAP);
        this.getTile().setVgap(this.GRID_GAP);
        this.getTile().setPadding(new Insets(this.PADDING,this.PADDING
                                  ,this.PADDING,this.PADDING));       
        
        //add the board and the score to the borderPane
        this.setBorderPane(new BorderPane());

        //score and title at top
        this.getBorderPane().setTop(hBox);

        //board in the center
        this.getBorderPane().setCenter(this.getTile());

        //setStyle uses a string so no magic number
        this.getBorderPane().setStyle(
        "-fx-background-color: rgb(187,173,160)");
        
        //eventhandler, will implement the actual move method
        this.getBorderPane().setOnKeyPressed(new MoveHandler());
        
        //make a StackPane, I made a StackPane for the gameOver 
        //screen at the end
        this.setStackPane(new StackPane(this.getBorderPane()));

        //make the scene
        Scene scene = new Scene(this.getStackPane(), 
                                this.getBoard().GRID_SIZE*
                                this.SCREEN_WIDTH, 
                                this.getBoard().GRID_SIZE 
                                * this.SCREEN_HEIGHT);
        
        //set the title and the scene and show
        primaryStage.setTitle("Gui2048");
        primaryStage.setScene(scene);
        primaryStage.show();
     
        //request focus to make multiple moves
        this.getBorderPane().requestFocus();         
    }

    /** Add your own Instance Methods Here */
   
    //simple getter method for score
    private Label getScore()
    {
      return this.score;
    }
    
    //simple setter for score
    private void setScore(Label label)
    {
      this.score = label;
    }

    //simple getter for gameOver screen
    private StackPane getGameOver()
    {
      return this.gameOver;
    }
  
    //simple setter for gameOver
    private void setGameOver(StackPane pane)
    {
      this.gameOver = pane;
    }

    //simple getter for tile
    private Tile getTile()
    {
      return this.tile;
    }
    
    //simple setter for tile
    private void setTile(Tile tile)
    {
      this.tile = tile;
    }

    //simple getter for borderPane
    private BorderPane getBorderPane()
    {
      return this.borderPane;
    }

    //simple setter for borderPane
    private void setBorderPane(BorderPane pane)
    {
      this.borderPane = pane;
    }

    //simple getter of stackPane
    private StackPane getStackPane()
    {
      return this.stackPane;
    }

    //simple for setter stackPane
    private void setStackPane(StackPane pane)
    {
      this.stackPane = pane;
    }
    
    //simple getter for board
    private Board getBoard()
    {
      return this.board;
    }
    
    //simple getter for outputBoard
    private String getOutputBoard()
    {
      return this.outputBoard;
    }



    /*
     * Name: moveDown
     * Parameter: none
     * Return: void
     * Purpose: this is a helper method to implement the move down
     *          method of board but show it on GUI
     */
    private void moveDown()
    {
      //print out the statement
      System.out.println("Moving Down");

      //move down and add random tile
      this.getBoard().move(Direction.DOWN);
      this.getBoard().addRandomTile();

      //make the board again and set the dimensions
      this.setTile(new Tile(this.getBoard().getGrid()));
      this.getTile().setHgap(this.GRID_GAP);
      this.getTile().setVgap(this.GRID_GAP);
      this.getTile().setPadding(new Insets(this.PADDING,this.PADDING,
                                           this.PADDING,this.PADDING));

      //change the text for score
      this.getScore().setText("Score: " + this.board.getScore());

      //make the center the tile again
      this.getBorderPane().setCenter(this.getTile());
      
      //put the gameover screen if the game is over
      if(this.getBoard().isGameOver())
      {
        this.getStackPane().getChildren().add(this.getGameOver());
      }
    }

    /*
     * Name: moveUp
     * Parameter: none
     * Return: void
     * Purpose: this is a helper method to implement the move up
     *          method of board but show it on GUI
     */
    private void moveUp()
    {
      //print out statement
      System.out.println("Moving Up");

      //move the board and add random tile for board
      this.getBoard().move(Direction.UP);
      this.getBoard().addRandomTile();

      //make the GUI version of the board again and set
      //dimensions
      this.setTile(new Tile(this.getBoard().getGrid()));
      this.getTile().setHgap(this.GRID_GAP);
      this.getTile().setVgap(this.GRID_GAP);
      this.getTile().setPadding(new Insets(this.PADDING,this.PADDING,
                                           this.PADDING,this.PADDING));

      //set the score text
      this.getScore().setText("Score: " + this.board.getScore());

      //make the Tile object to the center
      this.getBorderPane().setCenter(this.getTile());

      //put the game over screen if the game is over
      if(this.getBoard().isGameOver())
      {
        this.getStackPane().getChildren().add(this.getGameOver());
      }
    }
    
    /*
     * Name: moveRight
     * Parameter: none
     * Return: void
     * Purpose: this is a helper method to implement the move right
     *          method of board but will show it on GUI
     */
    private void moveRight()
    {
      //print out statement
      System.out.println("Moving Right");

      //move the board and add a tile
      this.getBoard().move(Direction.RIGHT);
      this.getBoard().addRandomTile();

      //make the GUI version of board again and set the dimensions
      this.setTile(new Tile(this.getBoard().getGrid()));
      this.getTile().setHgap(this.GRID_GAP);
      this.getTile().setVgap(this.GRID_GAP);
      this.getTile().setPadding(new Insets(this.PADDING,this.PADDING,
                                           this.PADDING,this.PADDING));

      //change the score text
      this.getScore().setText("Score: " + this.board.getScore());

      //set the board onto the center of borderPane
      this.getBorderPane().setCenter(this.getTile());
    
      //if game is over set the game over screen
      if(this.getBoard().isGameOver())
      {
        this.getStackPane().getChildren().add(this.getGameOver());
      }
    }
 
    /*
     * Name: moveLeft
     * Parameter: none
     * Return: void
     * Purpose: to implement the move left method of board but
     *          make it showable on GUI
     */
    private void moveLeft()
    {
      //print out the statement
      System.out.println("Moving Left");

      //move the board and add a random tile
      this.getBoard().move(Direction.LEFT);
      this.getBoard().addRandomTile();

      //make a new GUI version of board and set the dimensions
      this.setTile(new Tile(this.getBoard().getGrid()));
      this.getTile().setHgap(this.GRID_GAP);
      this.getTile().setVgap(this.GRID_GAP);
      this.getTile().setPadding(new Insets(this.PADDING,this.PADDING,
                                           this.PADDING,this.PADDING));

      //change the score text
      this.getScore().setText("Score: " + this.board.getScore());

      //set the board onto the center of borderPane
      this.getBorderPane().setCenter(this.getTile());
      
      //if the game is over, set the gameOver screen
      if(this.getBoard().isGameOver())
      {
        this.getStackPane().getChildren().add(this.getGameOver());
      } 
    }

    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments 
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument 
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(inputBoard, new Random());
            else
                board = new Board(boardSize, new Random());
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + " was thrown while creating a " +
                               "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                               "Constructor is broken or the file isn't " +
                               "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message 
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be used to save the 2048 board");
        System.out.println("                If none specified then the default \"2048.board\" file will be used");
        System.out.println("  -s [size]  -> Specifies the size of the 2048 board if an input file hasn't been");
        System.out.println("                specified.  If both -s and -i are used, then the size of the board");
        System.out.println("                will be determined by the input file. The default size is 4.");
    }

    /*
     * Class Name: MoveHandler
     * Purpose: this class will take care of the EventHandler
     *          so it will take in user input of arrow keys
     *
     */
    class MoveHandler implements EventHandler<KeyEvent>
    {

      /*
       * Name: handle
       * Parameter: KeyEvent e
       * Return: void
       * Purpose: this method will take in the arrow keys and 
       *          allow for the implementation of move method
       */
      @Override
      public void handle(KeyEvent e)
      {
        //arrow key down...
        if(e.getCode() == KeyCode.DOWN 
           && getBoard().canMoveDown())
        {
          //move down
          moveDown();
        }

        //arrow key up...
        else if(e.getCode() == KeyCode.UP
                && getBoard().canMoveUp())
        {
          //move up
          moveUp();
        }

        //arrow key right...
        else if(e.getCode() == KeyCode.RIGHT
                && getBoard().canMoveRight())
        {
          //move right
          moveRight();
        }

        //arrow key left...
        else if(e.getCode() == KeyCode.LEFT
                && getBoard().canMoveLeft())
        {
          //move left
          moveLeft();
        }

        //press s?
        else if(e.getCode() == KeyCode.S)
        {
          //save the board
          try
          {
            System.out.println("Saving Board to " + getOutputBoard());
            board.saveBoard(getOutputBoard());
          }
          catch(IOException ex)
          {
            //I don't think it should get here, but if so...
            System.out.println("try again");
          }
        }
      }
    }
    
    /*
     * Class Name: Tile
     * Purpose: to make the GUI version of the board. It will
     *          be a GridPane
     *
     */
    class Tile extends GridPane
    {
      //default no-args constructor, shouldn't be used...
      public Tile()
      {}

      /*
       * Tile constructor
       * Parameter: int[][] numbers
       * Purpose: this will take in a 2-d matrix of ints and 
       *          will take the matrix to make the board
       */
      public Tile(int[][] numbers)
      {
        //color for the rectangles
        Color color;
        
        //make the rectangles
        for(int column = 0; column < getBoard().GRID_SIZE; column++)
        {
          for(int row = 0; row < getBoard().GRID_SIZE; row++)
          {
            //make the rectangle and set the dimensions
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(RECTANGLE_DIMENSION);
            rectangle.setHeight(RECTANGLE_DIMENSION);

            //get the color
            //we use [row][column] because javafx coordinates
            //and int[][] coordinates are different.
            color = this.determineColor(numbers[row][column]);

            //set the color and add it.
            rectangle.setFill(color);
            this.add(rectangle,column,row);
          }
        }       
        
        //make the texts
        for(int column = 0; column < getBoard().GRID_SIZE; column++)
        {
          for(int row = 0; row < getBoard().GRID_SIZE; row++)
          { 
            //again int[][] goes row-column while GridPane does
            //column row
            if(numbers[row][column] == 0)
            {
              //if the number is 0 no need for text.
              continue;
            }

            //get the text
            Text text = new Text("" + numbers[row][column]);
            text.setFont(Font.font("Times New Roman",VALUE_SIZE));
            
            //set the text color 
            if(numbers[row][column] < _8_TILE)
            {
              text.setFill(COLOR_VALUE_DARK);  
            }
            else
            {
              text.setFill(COLOR_VALUE_LIGHT);
            }
            
            //add the text
            this.add(text,column,row);

            //set the horizontal alignment
            GridPane.setHalignment(text,HPos.CENTER);
          }
        }                   
      }
      
      /*
       * Name: determineColor
       * Parameter: int number
       * Return: Color
       * Purpose: to determine the color of the tile
       */
      private Color determineColor(int number)
      {
        //given the number...
        switch(number)
        {
          //determine the color
          case 0: return COLOR_EMPTY;
          case _2_TILE: return COLOR_2;
          case _4_TILE: return COLOR_4;
          case _8_TILE: return COLOR_8;
          case _16_TILE: return COLOR_16;
          case _32_TILE: return COLOR_32;
          case _64_TILE: return COLOR_64;
          case _128_TILE: return COLOR_128;          
          case _256_TILE: return COLOR_256;          
          case _512_TILE: return COLOR_512;          
          case _1024_TILE: return COLOR_1024;          
          case _2048_TILE: return COLOR_2048;          
          default: return COLOR_OTHER;          
        }
      }
    }
}
