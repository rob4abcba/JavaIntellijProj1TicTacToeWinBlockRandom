# JavaIntellijProj1TicTacToeWinBlockRandom

This is a Tic Tac Toe game written in Java using IntelliJ.  
The game has both 1-player and 2-player modes.  
In the 1-player mode, the player plays against the computer.  
Originally, the computer just chose random spots to place the Os.  
This version improves the computer's reasoning in deciding which spot to place its next "O".  
The computer will grab the middle square if the human doesn't claim it first.
Afterwards, the computer will try to win with 3 in a row if it can.
If the computer cannot win, then it will see whether it needs to block you to prevent you from winning.
If the computer neither can win, nor needs to block you, it will just randomly choose its next square.
