//------------------------------------------------------------------//
// Board.java                                                       //
//                                                                  //
// Class used to represent a 2048 game board                        //
//                                                                  //
// Author:  Brandon Williams                                        //
// Date:    1/17/15                                                 //
//------------------------------------------------------------------//

/**     Sample Board
 *
 *      0   1   2   3
 *  0   -   -   -   -
 *  1   -   -   -   -
 *  2   -   -   -   -
 *  3   -   -   -   -
 *
 *  The sample board shows the index values for the columns and rows
 */

/*
 * Name: Jae Song
 * Login: cs8barf
 * Date: Januray 23, 2015
 * File: Board.java
 * Sources of Help: PA 3 pdf, javadocs, PA 4 pdf
 *  
 * This program essentially builds the board and its 
 * functions that will be used for the 2048 game. It can
 * move, check if the game can still go on, add random tiles etc.
 */


import java.util.*;
import java.io.*;

/*
 * Name: Board.java
 * Purpose: This class will essentially create the board that
 *          will be used for the game 2048. 
 */
public class Board
{
    public final int NUM_START_TILES = 2;
    public final int TWO_PROBABILITY = 90;
    public final int GRID_SIZE;


    private final Random random;
    private int[][] grid;
    private int score;
    
    /* Name: Board ( constructor)
     * Purpose: instantiatizes a fresh board given the boardSize
     * Parameter: int boardSize, Random random
     */
    public Board(int boardSize, Random random)
    {
      this.random = random; 
      GRID_SIZE = boardSize;

      this.grid = new int[GRID_SIZE][GRID_SIZE];
      this.score = 0;

      for(int i = 0; i < NUM_START_TILES; i++)
      { 
        //this will add 2 starting tiles randomly
        this.addRandomTile();
      }
    }

    /* Name: Board (constructor)
     * Purpose: instantiatizes a new board given the name of the
     *          input file.
     * Parameters: String inputBoard, Random random
     */
    public Board(String inputBoard, Random random) throws IOException
    {
      this.random = random;
      Scanner input = new Scanner(new File(inputBoard));
      
      //scans the selected file for GRID_SIZE and score
      GRID_SIZE = input.nextInt();
      this.score = input.nextInt();

      this.grid = new int[GRID_SIZE][GRID_SIZE];
        
      for(int row = 0; row < GRID_SIZE; row++)
      {
        for(int column = 0; column < GRID_SIZE; column++)
        { 
          //copies the grid according to the selected file
          this.grid[row][column] = input.nextInt();
        }
      }
    }
    
    /* Name: saveBoard
     * Purpose: saves the current board to a file
     * Parameters: String outputBoard
     * Return: Void
     */    
    public void saveBoard(String outputBoard) throws IOException
    {      
      PrintWriter writer = new PrintWriter(outputBoard);
     
      writer.println(this.GRID_SIZE);
      writer.print(this.score);
      
      for(int row = 0; row < GRID_SIZE; row++)
      {
        //this will make a new line for every new row
        writer.println();        

        for(int column = 0; column < GRID_SIZE; column++)
        { 
          //print number and space
          writer.print(this.grid[row][column] + " ");
        }
      }
      
      //println is used to complement the no new line error when 
      //using the diff method to check
      writer.println();
      writer.close();
    }

    /* Name: addRandomTile
     * Purpose: Adds a random Tile, either 2 or 4 to
     *          a random empty space on the board. There is a
     *          90% chance that it will be a 2 and 10%
     *          chance that it will be a 4. 
     * Parameter: None
     * Return: Void
     */
    public void addRandomTile()
    {
      int count = 0;
      int empty = 0;

      for(int row = 0; row < GRID_SIZE; row++)
      {
        for(int column = 0; column < GRID_SIZE; column++)
        {
          if(this.grid[row][column] == 0)
          {
            //this will count up the number of empty spaces
            count++;
          }
        }
      }
      
      //if no empty spaces, exit
      if(count==0)
      {
        return;
      }
      
      //finds a random empty location
      int location = this.random.nextInt(count);
     
      //determine whether to place a 2 or a 4
      int value = this.random.nextInt(100);
      int tile;
      
      //sets the tile value
      if(value < TWO_PROBABILITY)
      {
        tile = 2;  
      }
      else
      {
        tile = 4;
      }
      
      for(int row = 0; row < GRID_SIZE; row++)
      {
        for(int column = 0; column < GRID_SIZE; column++)
        { 
          //finds the random location and sets the tile there
          if(this.grid[row][column] == 0)
          {
            if(empty == location)
            {
              this.grid[row][column] = tile;
              return;
            }
            //empty is incremented to walk through the empty 
            //locations
            empty++;
          }
        }
      }
    }

    /*
     * Name: isGameOver
     * Purpose: to see if there are any more valid moves left
     *          in this game.
     * Parameter: None
     * Return: boolean. It will return whether or not the
     *         game has any more moves left or not
     *
     */    
    public boolean isGameOver()
    {
      //check if any valid moves are left
      //the condition is derived from De Morgan's law
      if(!canMove(Direction.UP) && !canMove(Direction.DOWN) &&
         !canMove(Direction.RIGHT) && !canMove(Direction.LEFT))
      {
        //System.out.println("Game Over!");
        return true;
      }                    
      else
      {
        return false;
      }
    }

    /*
     * Name: canMove
     * Purpose: to see if the user's input direction is a valid
     *          move. 
     * Parameter: Direction direction
     * Return: boolean. It will return true if the user input
     *         will result in a valid move or false if the
     *         game cannot be moved in the user's input 
     *         direction.
     *
     */
    public boolean canMove(Direction direction)
    {
      if(direction.equals(Direction.UP))
      {
        return this.canMoveUp();
      }
      else if(direction.equals(Direction.DOWN))
      {
        return this.canMoveDown();
      }
      else if(direction.equals(Direction.RIGHT))
      {
        return this.canMoveRight();
      }
      else if(direction.equals(Direction.LEFT))
      {
        return this.canMoveLeft();
      }
      else
      {
        System.out.println("the direction is an invalid one");
        return false;
      }
    }

    /*
     * Name: canMoveUp
     * Purpose: helper method for canMove, will determined if the
     *          tiles can move up
     *  Parameter: none
     *  Return: boolean. true if you can move up, false if you can't
     *
     */
    protected boolean canMoveUp()
    {
      //loops to go through every element

      //row will start from the bottom as I want to see if I can
      //move up
      for(int row = GRID_SIZE - 1; row > 0; row--)
      {
        for(int column = 0; column < GRID_SIZE; column++)
        {
          //check will check every single element above the
          //current row
          for(int check = row - 1; check >= 0; check--)
          {             
            //if the bottom element is not 0 and any element
            //above it is 0 then you can always move up.
            if(this.grid[row][column] != 0 
              && this.grid[check][column] == 0)
            {
              return true;
            }           
          }
          /* if the previous condtion was not met, then that means
           * there are no zeros in the current column.
           * so the only way it could move up is having the same 
           * number the one below and one above.
           */
          if(this.grid[row][column] != 0
            && this.grid[row-1][column] == this.grid[row][column])
          {
            return true;
          }
        }         
      }        
      return false;
    }

    /*
     * Name: canMoveDown
     * Purpose: similiar concept as canMoveUp, except it
     *          checks if you can move down
     * Parameter: none
     * Return: boolean, true if can move down, false if not
     */
    protected boolean canMoveDown()
    {
      //same ideology as canMoveUp but instead of bottom, start
      //from top and stop one before because the bottom row
      //cannot move down.
      for(int row = 0; row < GRID_SIZE - 1; row++)
      {
        for(int column = 0; column < GRID_SIZE; column++)
        {
          for(int check = row + 1; check < GRID_SIZE; check++)
          {
            //checks if there are any 0's in the column
            if(this.grid[row][column] != 0
              && this.grid[check][column] == 0)
            {
              return true;
            }              
          }

          //since it's made to here, there are no zeros in 
          //the column, if the two adjacent ones are equal 
          // then can move
          if(this.grid[row][column] != 0
            && this.grid[row+1][column] == this.grid[row][column])
          {
            return true;
          }
        }
      }        
      return false;
    }

    /*
     * Name: canMoveRight
     * Purpose: similar with canMoveUp and canMoveDown
     * Parameter: none
     * return: boolean, true if can move right, false if not
     */
    protected boolean canMoveRight()
    {
      //go through all the elements
      for(int row = 0; row < GRID_SIZE; row++)
      {

        //this time start with the leftmost element
        for(int column = 0; column < GRID_SIZE - 1; column++)
        {
          //checks all the elements to the right of it
          for(int check = column + 1; check < GRID_SIZE; check++)
          {
            
            //checks if there are any 0's
            if(this.grid[row][column] != 0
              && this.grid[row][check] == 0)
            {
              return true;
            }
          } 
          
          //if no zero's are found, can only move if the
          //adjacent tiles are equal
          if(this.grid[row][column] != 0
            && this.grid[row][column+1] == this.grid[row][column])
          {
            return true;
          }
        }
      }        
      return false;
    }

    /*
     * Name: canMoveLeft
     * Purpose: similiar to canMoveUp, Down, Right, checks
     *          if  you can move left
     * Parameter: none
     * return: boolean, true if you can move left
     */
    protected boolean canMoveLeft()
    {
      //loops through all the elements
      for(int row = 0; row < GRID_SIZE; row++)
      {
        
        //start with the rightmost element
        for(int column = GRID_SIZE - 1; column > 0; column--)
        {
            
          //check all the element to the left of it
          for(int check = column - 1; check >= 0; check--)
          {
              
            //checks if there are any zeros to the left of
            //the element
            if(this.grid[row][column] != 0
              && this.grid[row][check] == 0)
            {
              return true;
            }             
          }

          //since there are no 0's only can move if the 
          //two adjacent tiles are equal.
          if(this.grid[row][column] != 0
            && this.grid[row][column-1] == this.grid[row][column])
          {
            return true;
          }
        }
      }        
      return false;
    }
         

    /*
     * Name: move
     * Purpose: the implementation of the game, 2048
     *          it will move the tiles according to the direction
     *          given by the user.
     * Parameter: Direction direction
     * Return: it will return true if the move was successful,
     *         it will return false if the direction given was 
     *         not a valid one.
     *
     */    
    public boolean move(Direction direction)
    {
      //helper methods will be used according to the direction
      if(direction.equals(Direction.UP))
      {         
        return this.moveUp();
      }
      else if(direction.equals(Direction.DOWN))
      {
        return this.moveDown();
      }
      else if(direction.equals(Direction.RIGHT))
      {
        return this.moveRight();
      }
      else if(direction.equals(Direction.LEFT))
      {
        return this.moveLeft();
      }
      
      //if the direction is not a valid one, then it will print out
      //an error statement and return false.
      else
      {
        System.out.println("direction is not valid");
        return false;
      }      
    }

    /*
     * Name: makeEmptySet
     * Purpose: helper method for the helper methods. It will be
     *          used by every single one of the main helper methods.
     *          it will make an ArrayList of all 0's according to the
     *          size of the GRID_SIZE.
     * Parameter: None
     * Return: ArrayList<Integer> it will return an ArrayList of
     *         all 0's.
     *
     */
    private ArrayList<Integer> makeEmptySet()
    {
      ArrayList<Integer> emptySet = new ArrayList<Integer>();
      
      //simply add zeros to the number of GRID_SIZE
      for(int emptyI = 0; emptyI < GRID_SIZE; emptyI++)
      {
        emptySet.add(0);
      }

      //return the ArrayList
      return emptySet;
    }

    /*
     * Name: removeZero
     * Purpose: It will remove all the zero's in a given
     *          ArrayList which will move all the nonzeros to 
     *          Next to each other, and return the count of 
     *          zeros that were removed
     * Parameter: ArrayList<Integer> list
     * Return: int
     *
     */
    private int removeZero(ArrayList<Integer> list)
    {
      //count of how many zeros are in list
      int count = 0;

      for(int check = 0; check< list.size(); check++)
      { 
        //if it is 0 then remove, and increment count by one
        if(list.get(check).equals(0))
        {
          list.remove(check);
          
          //check is set to -1 because it will be incremented by 1
          //at the end of the loop and I want it to be 0 again
          //to iterate from the beginning
          check = -1;
          count++;
        }
      }
      //return how many zeros were there
      return count;
    }

    /*
     * Name: moveUp
     * Purpose: this method will essentially move the tiles up and
     *          add the tiles together if they are equal and either
     *          adjacent or 0's in between
     * Parameter: none
     * Return: true after its implementation.
     */
    private boolean moveUp()
    {
      //an ArrayList for the numbers in a single column
      ArrayList<Integer> list;

      int count;
      
      //look at helper method makeEmptySet
      ArrayList<Integer> emptySet = this.makeEmptySet();
      

      for(int column = 0; column < GRID_SIZE; column++)
      { 
        //I want a new list every column because we're looking to
        //move up so each column by itself makes it more simple
        list = new ArrayList<Integer>();

        //copy the grid onto the arraylist
        for(int row = 0; row < GRID_SIZE; row++)
        {
          list.add(this.grid[row][column]);                 
        }

        //if the list is all 0's then just skip to the next column
        if(list.equals(emptySet))
        {
          continue;  
        }      
        
        //see helper method removeZero
        count = this.removeZero(list);
        
        //add zeros to the end of the Arraylist because we're 
        //moving up according to how many zero's we took out.
        for(int index = 0; index < count; index++)
        {
          //this will ensure all the nonzeros are at the
          //top of the list so we can compare it easily
          list.add(0);
        }              

        //now since all the zeros are gone, if the two adjacent
        //tiles, starting with the top one, because top has priority,
        //then we double the number and remove the lower tile
        //so the next tile moves up and if that tile has another
        //adjacent then we double that number too and remove
        for(int adjacent = 0; adjacent < GRID_SIZE - 1; adjacent++)
        {
          if(list.get(adjacent).equals(list.get(adjacent+1)))
          {            
            // increase the score before tiles change
            this.score += list.get(adjacent) * 2;
            list.set(adjacent, list.get(adjacent) * 2);

            //remove the tile that is "added" on
            list.remove(adjacent+1);

            //I can simply add a 0 because we're moving up, so the
            //zeros would be at the bottom
            list.add(0);

            //do not change the adjacent value because a tile
            //can be changed only once per turn
          }
        }        
        
        //this will copy the list to the grid to basically to
        //implement the move we just did. notice we are changing
        //the rows because we are moving up, so each list
        //will be each column
        for(int copyRow = 0; copyRow < GRID_SIZE; copyRow++)
        {
          this.grid[copyRow][column] = list.get(copyRow);
        }
      }      
      return true;
    }

    /*
     * Name: moveDown
     * Purpose: to move the tiles down and while doing so it will add
     *          the two tiles together if they are equal and either 
     *          adjacent or have zero's in between.
     * Parameter: none
     * Return: true after the move is successful
     */
    private boolean moveDown()
    {
      /* this method is basically the same as moveUp method
       * so less comment will be on it, the differences
       * will be commented on!
       */
      ArrayList<Integer> list;

      int count;
      
      //look at helper method makeEmptySet
      ArrayList<Integer> emptySet = this.makeEmptySet();
       
      for(int column = 0; column < GRID_SIZE; column++)
      {         
        list = new ArrayList<Integer>();

        for(int row = 0; row < GRID_SIZE; row++)
        {
          list.add(this.grid[row][column]);                 
        }

        if(list.equals(emptySet))
        {
          continue;  
        }      
        
        //see helper method removeZero
        count = this.removeZero(list);
        
        for(int index = 0; index < count; index++)
        {
          //here instead of just adding the 0's to the back
          //we add them to the front because we're moving down
          //therefore the 0's would be at the top, "front"
          list.add(0,0);
        }

        //we will start with the last one because we moved down
        //so all the nonzeros will be at the bottom
        for(int adjacent = GRID_SIZE - 1; adjacent > 0; adjacent--)
        {
          //instead of adjacent + 1 we do adjacent - 1, because
          //we're looking at the one above the current one
          if(list.get(adjacent).equals(list.get(adjacent-1)))
          {
            //score is increased before tiles change
            this.score += list.get(adjacent) * 2;

            list.set(adjacent, list.get(adjacent) * 2);
            list.remove(adjacent-1);

            //again, we add the zero to the front not the back
            //because we're moving down
            list.add(0,0);

            //do not change the adjacent value because
            //the tile is only increased once per turn
          }
        }        
        
        for(int copyRow = 0; copyRow < GRID_SIZE; copyRow++)
        {
          this.grid[copyRow][column] = list.get(copyRow);
        }
      }      
      return true;
    }
    
    /*
     * Name: moveRight
     * Purpose: to move the tiles to the right, and to add the tiles
     *          that are adjacent or have zero's in between
     * Parameter: none
     * Return: true after the move is implemented
     */
    private boolean moveRight()
    { 
      /* Again this is similiar to moveUp and moveDown
       * but some details are different and those will be
       * explicitly commented
       */

      ArrayList<Integer> list;

      int count;
      
      //look at helper method makeEmptySet
      ArrayList<Integer> emptySet = this.makeEmptySet();
            
      for(int row = 0; row < GRID_SIZE; row++)
      { 
        //this time we make a new list for every row, because we're
        //moving to the right we want every list to be a 
        //new one of new digits.
        list = new ArrayList<Integer>();
        
        //copies the numbers in a single row
        for(int column = 0; column < GRID_SIZE; column++)
        {
          list.add(this.grid[row][column]);                 
        }

        //again, if the list is all 0's skip to next row.
        if(list.equals(emptySet))
        {
          continue;  
        }      
        
        //see helper method removeZero
        count = this.removeZero(list);
        
        //similar to moveDown we want to add all the zero's 
        //to the beginning of the list, or the most left
        //that will mean all the non-zeros will be moved to 
        //the right
        for(int index = 0; index < count; index++)
        {
          list.add(0,0);
        }
        
        //start with the most right tile because those have priority
        //and check if there are any tiles with the same value
        for(int adjacent = GRID_SIZE - 1; adjacent > 0; adjacent--)
        {
          if(list.get(adjacent).equals(list.get(adjacent-1)))
          {
            //score is increased before the tiles change
            this.score += list.get(adjacent) * 2;
            
            //double the tile value
            list.set(adjacent, list.get(adjacent) * 2);

            //remove the tile that will be "added" on
            list.remove(adjacent-1);

            //add another 0 to the front because we moved right, or
            //the end
            list.add(0,0);

            //we do not change adjacent to 0 again because 
            //a tile is only increased once per turn.
          }
        }        

        //this will copy the list onto the grid, and notice
        //that we are moving through the columns because
        //we are moving right, each row will be each list.
        for(int copyColumn = 0; copyColumn < GRID_SIZE; copyColumn++)
        {
          this.grid[row][copyColumn] = list.get(copyColumn);
        }
      }    
      return true;  
    }

    /*
     * Name: moveLeft
     * Purpose: to move the tiles to the left, and to add the tiles
     *          together if they are equal and if either they
     *          are adjacent to each other or they have 0's
     *          in between
     * Parameter: none
     * Return: true after the move is implemented
     */
    private boolean moveLeft()
    {
      /* I say it again but this method is super similar to moveRight
       * and moveUp. So there will be less comments on explaining
       * things but there will be comments on stuff that's different
       *
       */
      
      ArrayList<Integer> list;

      int count;
      
      //look at helper method makeEmptySet
      ArrayList<Integer> emptySet = this.makeEmptySet();
            
      for(int row = 0; row < GRID_SIZE; row++)
      {
        //new list for every row like moveRight
        list = new ArrayList<Integer>();

        for(int column = 0; column < GRID_SIZE; column++)
        {
          list.add(this.grid[row][column]);                 
        }

        if(list.equals(emptySet))
        {
          continue;  
        }      
        
        //see helper method removeZero
        count = this.removeZero(list);
        
        //add zeros according to how many we took out
        for(int index = 0; index < count; index++)
        {
          list.add(0);
        }
        
        //we start from the beginning or the most left because
        //we moved all the non-zeros to the left.
        for(int adjacent = 0; adjacent < GRID_SIZE-1; adjacent++)
        {          
          if(list.get(adjacent).equals(list.get(adjacent+1)))
          {
            //increase the score
            this.score += list.get(adjacent) * 2;
            list.set(adjacent, list.get(adjacent) * 2);

            //remove its tile to the right because that's the one
            //that is "added"
            list.remove(adjacent+1);

            //add a 0 to the back because that will add it to the
            //rightmost tile
            list.add(0);
          }
        }        
        
        for(int copyColumn = 0; copyColumn < GRID_SIZE; copyColumn++)
        {
          this.grid[row][copyColumn] = list.get(copyColumn);
        }
      }    
      return true;
    }
     

    // Return the reference to the 2048 Grid
    public int[][] getGrid()
    {
        return grid;
    }

    // Return the score
    public int getScore()
    {
      return this.score;
    }

    @Override
    public String toString()
    {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -" :
                                    String.format("%5d", grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }
}
