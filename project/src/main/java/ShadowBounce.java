/**
 * Shadow Bounce Game
 * @author StudentId: 1102671
 */

import bagel.*;

public class ShadowBounce extends AbstractGame {

    private final int MAX_BOARD_NUMBER = 4;

    private GameBoard gameBoard;
    private int boardNumber = 0;
    private int shotsRemaining = 20;



    public ShadowBounce() {

        gameBoard = new GameBoard(String.format("res/%d.csv", boardNumber));
        System.out.println(String.format("Shots Remaining: %d", shotsRemaining));
    }


    public static void main(String[] args) {
        ShadowBounce game = new ShadowBounce();
        game.run();
    }

    /**
     * Increments the shots remaining by 1
     */
    public void incrementShotsRemaining(){
        shotsRemaining++;
    }

    /**
     * Decrements the shots remaining by 1
     */
    public void decrementShotsRemaining(){
        shotsRemaining--;
    }

    /**
     * @return Returns the number of shots remaining
     */
    public int getShotsRemaining(){
        return shotsRemaining;
    }
    /**
     * Increments the Board Number by 1 and loads the new board
     */

    public void incrementBoardNumber(){
        boardNumber++;
        if (boardNumber > MAX_BOARD_NUMBER) {
            Window.close();
        } else {
            gameBoard = new GameBoard("res/" + Integer.toString(boardNumber) + ".csv");
        }
    }


    @Override
    public void update(Input input) {

        if (input.wasPressed(MouseButtons.LEFT)) {
            gameBoard.newTurn(this, input);
        }

        gameBoard.drawBoardPieces();
        gameBoard.playGame(this);
    }
}

