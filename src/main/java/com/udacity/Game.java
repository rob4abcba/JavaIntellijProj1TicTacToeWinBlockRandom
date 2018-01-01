package com.udacity;

import java.util.Arrays;

/**
 * Created by udacity 2016
 * The Main class containing game logic and backend 2D array
 */
public class Game {

    private char turn; // who's turn is it, 'x' or 'o' ? x always starts
    private boolean twoPlayer; // true if this is a 2 player game, false if AI playing
    private char [][] grid; // a 2D array of chars representing the game grid
    private int freeSpots; // counts the number of empty spots remaining on the board (starts from 9  and counts down)
    private static GameUI gui;

    //RL: Add variables to store last i and j played.
    private int lastxi;
    private int lastxj;
    private int lastoi;
    private int lastoj;

    //RL: Add boolean to indicate who took center spot.  These are in case I decide to use these to simplify or speed up operation.
    private boolean xTookCenter; // true if x took center spot
    private boolean oTookCenter; // true if o took center spot

    /**
     * Create a new single player game
     *
     */
    public Game() {
        newGame(false);
    }

    /**
     * Create a new game by clearing the 2D grid and restarting the freeSpots counter and setting the turn to x
     * @param twoPlayer: true if this is a 2 player game, false if playing against the computer
     */
    public void newGame(boolean twoPlayer){
        //sets a game to one or two player
        this.twoPlayer = twoPlayer;

        // initialize all chars in 3x3 game grid to '-'
        grid = new char[3][3];
        //fill all empty slots with -
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                grid[i][j] = '-';
            }
        }
        //start with 9 free spots and decrement by one every time a spot is taken
        freeSpots = 9;
        //x always starts
        turn = 'x';
    }


    /**
     * Gets the char value at that particular position in the grid array
     * @param i the x index of the 2D array grid
     * @param j the y index of the 2D array grid
     * @return the char value at the position (i,j):
     *          'x' if x has played here
     *          'o' if o has played here
     *          '-' if no one has played here
     *          '!' if i or j is out of bounds
     */
    public char gridAt(int i, int j){
        if(i>=3||j>=3||i<0||j<0)
            return '!';
        return grid[i][j];
    }

    /**
     * Places current player's char at position (i,j)
     * Uses the variable turn to decide what char to use
     * @param i the x index of the 2D array grid
     * @param j the y index of the 2D array grid
     * @return boolean: true if play was successful, false if invalid play
     */
    public boolean playAt(int i, int j){
        //check for index boundries
        if(i>=3||j>=3||i<0||j<0)
            return false;
        //check if this position is available
        if(grid[i][j] != '-'){
            return false; //bail out if not available
        }
        //update grid with new play based on who's turn it is
        grid[i][j] = turn;
        System.out.println("inside playAt1");
        System.out.println("Just put a "+turn+" at position ["+i+"]["+j+"]");
        lastxi = i;
        lastxj = j;

        //update free spots
        freeSpots--;
        return true;
    }


    /**
     * Override
     * @return string format for 2D array values
     */
    public String toString(){
        return Arrays.deepToString(this.grid);
    }

    /**
     * Performs the winner check and displays a message if game is over
     * @return true if game is over to start a new game
     */
    public boolean doChecks() {
        //check if there's a winner or tie ?
        String winnerMessage = checkGameWinner(grid);
        if (!winnerMessage.equals("None")) {
            gui.gameOver(winnerMessage);
            newGame(false);
            return true;
        }
        return false;
    }

    /**
     * Allows computer to play in a single player game or switch turns for 2 player game
     */
    public void nextTurn(){
        //check if single player game, then let computer play turn
        System.out.println("inside nextTurn1");
        if(!twoPlayer){
            System.out.println("Mode = 1 player vs. computer");
            if(freeSpots == 0){
                System.out.println("No free spots.  Nobody won.");
                return ; //bail out if no more free spots
            }
            int ai_i, ai_j;

            /* RL0: Comment out computer choosing random spots.  Instead give computer some brains.
            do {
                //randomly pick a position (ai_i,ai_j)
                ai_i = (int) (Math.random() * 3);
                ai_j = (int) (Math.random() * 3);
            }while(grid[ai_i][ai_j] != '-'); //keep trying if this spot was taken
            RL0 */

            if(grid[1][1] == '-'){
                System.out.println("Computer thanks you for leaving it the center spot.");
                ai_i = 1;
                ai_j = 1;
                xTookCenter = false;
                oTookCenter = true;
            }
            else {
                System.out.println("Center spot is occupied by "+grid[1][1]);


                System.out.println("Inside nextTurn: Opponent just put a "+turn+" at position ["+lastxi+"]["+lastxj+"]");


                //System.out.println("Win if you can win at either diagonal.");
                if (grid[0][0] == 'o' & grid[1][1] == 'o' & grid[2][2] == '-') {
                    ai_i = 2;
                    ai_j = 2;
                }
                else if (grid[0][0] == 'o' & grid[1][1] == '-' & grid[2][2] == 'o') {
                    ai_i = 1;
                    ai_j = 1;
                }
                else if (grid[0][0] == '-' & grid[1][1] == 'o' & grid[2][2] == 'o') {
                    ai_i = 0;
                    ai_j = 0;
                }

                else if (grid[0][2] == 'o' & grid[1][1] == 'o' & grid[2][0] == '-') {
                    ai_i = 2;
                    ai_j = 0;
                }
                else if (grid[0][2] == 'o' & grid[1][1] == '-' & grid[2][0] == 'o') {
                    ai_i = 1;
                    ai_j = 1;
                }
                else if (grid[0][2] == '-' & grid[1][1] == 'o' & grid[2][0] == 'o') {
                    ai_i = 0;
                    ai_j = 2;
                }

                //System.out.println("Win if you can win at column "+lastoi+" or row "+lastoj);
                else if (grid[lastoi][0] == 'o' & grid[lastoi][1] == 'o' & grid[lastoi][2] == '-') {
                    ai_i = lastoi;
                    ai_j = 2;
                }
                else if (grid[lastoi][0] == 'o' & grid[lastoi][1] == '-' & grid[lastoi][2] == 'o') {
                    ai_i = lastoi;
                    ai_j = 1;
                }
                else if (grid[lastoi][0] == '-' & grid[lastoi][1] == 'o' & grid[lastoi][2] == 'o') {
                    ai_i = lastoi;
                    ai_j = 0;
                }
                else if (grid[0][lastoj] == 'o' & grid[1][lastoj] == 'o' & grid[2][lastoj] == '-') {
                    ai_i = 2;
                    ai_j = lastoj;
                }
                else if (grid[0][lastoj] == 'o' & grid[1][lastoj] == '-' & grid[2][lastoj] == 'o') {
                    ai_i = 1;
                    ai_j = lastoj;
                }
                else if (grid[0][lastoj] == '-' & grid[1][lastoj] == 'o' & grid[2][lastoj] == 'o') {
                    ai_i = 0;
                    ai_j = lastoj;
                }

                //System.out.println("Cannot win, so block opponent from completing either diagonal.");
                else if (grid[0][0] == 'x' & grid[1][1] == 'x' & grid[2][2] == '-') {
                    ai_i = 2;
                    ai_j = 2;
                }
                else if (grid[0][0] == 'x' & grid[1][1] == '-' & grid[2][2] == 'x') {
                    ai_i = 1;
                    ai_j = 1;
                }
                else if (grid[0][0] == '-' & grid[1][1] == 'x' & grid[2][2] == 'x') {
                    ai_i = 0;
                    ai_j = 0;
                }

                else if (grid[0][2] == 'x' & grid[1][1] == 'x' & grid[2][0] == '-') {
                    ai_i = 2;
                    ai_j = 0;
                }
                else if (grid[0][2] == 'x' & grid[1][1] == '-' & grid[2][0] == 'x') {
                    ai_i = 1;
                    ai_j = 1;
                }
                else if (grid[0][2] == '-' & grid[1][1] == 'x' & grid[2][0] == 'x') {
                    ai_i = 0;
                    ai_j = 2;
                }


                //System.out.println("Cannot win, so block opponent from completing column "+lastxi+" or row "+lastxj);
                else if (grid[lastxi][0] == 'x' & grid[lastxi][1] == 'x' & grid[lastxi][2] == '-') {
                    ai_i = lastxi;
                    ai_j = 2;
                }
                else if (grid[lastxi][0] == 'x' & grid[lastxi][1] == '-' & grid[lastxi][2] == 'x') {
                        ai_i = lastxi;
                        ai_j = 1;
                    }
                else if (grid[lastxi][0] == '-' & grid[lastxi][1] == 'x' & grid[lastxi][2] == 'x') {
                    ai_i = lastxi;
                    ai_j = 0;
                }
                else if (grid[0][lastxj] == 'x' & grid[1][lastxj] == 'x' & grid[2][lastxj] == '-') {
                    ai_i = 2;
                    ai_j = lastxj;
                }
                else if (grid[0][lastxj] == 'x' & grid[1][lastxj] == '-' & grid[2][lastxj] == 'x') {
                    ai_i = 1;
                    ai_j = lastxj;
                }
                else if (grid[0][lastxj] == '-' & grid[1][lastxj] == 'x' & grid[2][lastxj] == 'x') {
                    ai_i = 0;
                    ai_j = lastxj;
                }
                else {
                    System.out.println("Nothing to block, so randomly pick any remaining spot.");
                    do {
                        //randomly pick a position (ai_i,ai_j)
                        ai_i = (int) (Math.random() * 3);
                        ai_j = (int) (Math.random() * 3);
                    } while (grid[ai_i][ai_j] != '-'); //keep trying if this spot was taken
                }

                /* RL1
                System.out.println("Center spot taken, check for and take corner spot if available.");
                if (grid[0][0] == '-' || grid[0][2] == '-' || grid[2][2] == '-' || grid[2][0] == '-') {
                    do {
                        //randomly pick a corner (ai_i,ai_j)
                        int ai_ii, ai_jj;
                        ai_ii = (int) (Math.random() * 2);
                        ai_jj = (int) (Math.random() * 2);
                        ai_i = ai_ii * 2;
                        ai_j = ai_jj * 2;
                    } while (grid[ai_i][ai_j] != '-'); //keep trying if this spot was taken
                } else {
                    System.out.println("Center spot & Corner spots all taken, so randomly pick any remaining spot.");
                    do {
                        //randomly pick a position (ai_i,ai_j)
                        ai_i = (int) (Math.random() * 3);
                        ai_j = (int) (Math.random() * 3);
                    } while (grid[ai_i][ai_j] != '-'); //keep trying if this spot was taken
                }
                RL1 */

            }
                    //update grid with new play, computer is always o
            lastoi = ai_i;
            lastoj = ai_j;

            grid[ai_i][ai_j] = 'o';

            //update free spots
            freeSpots--;
        }
        else{
            System.out.println("Mode = 2 players");
            //change turns (RL: If twoPlayer == true, then increment to next player's turn.)
            if(turn == 'x'){
                turn = 'o';
            }
            else{
                turn = 'x';
            }
        }
        return;
    }


    /**
     * Checks if the game has ended either because a player has won, or if the game has ended as a tie.
     * If game hasn't ended the return string has to be "None",
     * If the game ends as tie, the return string has to be "Tie",
     * If the game ends because there's a winner, it should return "X wins" or "O wins" accordingly
     * @param grid 2D array of characters representing the game board
     * @return String indicating the outcome of the game: "X wins" or "O wins" or "Tie" or "None"
     */
    public String checkGameWinner(char [][]grid){
        String result = "None";
        //Student code goes here ...
        System.out.println("inside checkGameWinner1");
        for (int i=0; i<3; i++) {
            if (grid[i][0] == 'x' & grid[i][1] == 'x' & grid[i][2] == 'x') {
                return "x completed a column"; // x completed a full column
            }
        }
        for (int j=0; j<3; j++) {
            if (grid[0][j] == 'x' & grid[1][j] == 'x' & grid[2][j] == 'x') {
                return "x completed a row"; // x completed a full row
            }
        }

        if (grid[0][0] == 'x' & grid[1][1] == 'x' & grid[2][2] == 'x') {
            return "x completed bwd slash diagonal"; // x completed bwd slash diagonal
            }
        if (grid[0][2] == 'x' & grid[1][1] == 'x' & grid[2][0] == 'x') {
            return "x completed fwd slash diagonal"; // x completed fwd slash diagonal
        }


        for (int i=0; i<3; i++) {
            if (grid[i][0] == 'o' & grid[i][1] == 'o' & grid[i][2] == 'o') {
                return "o completed a column"; // o completed a full column
            }
        }
        for (int j=0; j<3; j++) {
            if (grid[0][j] == 'o' & grid[1][j] == 'o' & grid[2][j] == 'o') {
                return "o completed a row"; // o completed a full row
            }
        }

        if (grid[0][0] == 'o' & grid[1][1] == 'o' & grid[2][2] == 'o') {
            return "o completed bwd slash diagonal"; // o completed bwd slash diagonal
        }
        if (grid[0][2] == 'o' & grid[1][1] == 'o' & grid[2][0] == 'o') {
            return "o completed fwd slash diagonal"; // o completed fwd slash diagonal
        }

        //System.out.println("inside checkGameWinner2");
        if(freeSpots == 0){
            System.out.println("no more free spots");
            return "No more free spots. Nobody won."; //bail out if no more free spots
        }
        return result;
    }

    /**
     * Main function
     * @param args command line arguments
     */
    public static void main(String args[]){
        Game game = new Game();
        gui = new GameUI(game);
    }

}
