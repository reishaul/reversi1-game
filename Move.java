/**
 * The Move class represents an action in the game, encapsulating the position where a disc is placed, the disc itself,
 * and additional state information like flipped positions for undo functionality. This class provides methods for
 * executing moves and retrieving details about a move.
 */

import java.util.ArrayList;

public class Move {

    private Disc disc;
    private Position position;
    private ArrayList<Position> arrayList;

    /**
     * a constructor. build for the Undo methode
     * @param a
     * @param disc
     * @param positions
     */
    public Move(Position a, Disc disc, ArrayList<Position> positions) {//for undo
        this.position=a;
        this.disc=disc;
        this.arrayList=positions;
    }

    /**
     * The makeMove method is responsible for placing a given disc at a specific position on the game board.
     * It directly updates the state of the board to reflect the move.
     * @param board A 2D array representing the game board.
     * @param p The position on the board where the disc should be placed.
     * @param disc The disc to be placed on the board
     */
    public void makeMove(Disc[][] board,Position p, Disc disc){
        board[p.row()][p.col()]=disc;
    }

    /**
     * a constructor for using the properties and implements them.
     * @param position
     * @param disc
     */
    public Move(Position position, Disc disc) {
        this.position = position;
        this.disc = disc;
    }

    /**
     * The position method is a getter that retrieves the position associated with a move
     * @return position
     */
    public Position position(){
        return position;
    }

    /**
     * The position method is a getter that retrieves the disc associated with a move
     * @return disc
     */
    public Disc disc(){
        return disc;

    }
}
