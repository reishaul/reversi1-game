/**
 * Implementation and logic by- Re'i Shaul E-mail: reishaul1@gmail.com
 * This class implements the core functionality and rules of the game. It manages the game board, players, turns,
 * and game-specific operations such as placing discs, validating moves, flipping discs, and handling special disc types
 * like bombs and unflippable discs. It also provides methods to check the game's state, undo moves, and reset the game.
 * As we see it, this is the main class for the game, and also the most important.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {


    private Disc[][] board = new Disc[8][8];//the main board
    private static final int BOARD_SIZE = 8;
    private ArrayList<Position> finalList = new ArrayList<>();//the list that says which discs to flip

    private Player player1;
    private Player player2;

    private List<Position> validMoves = ValidMoves();

    private Stack<Disc[][]> storeDiscs = new Stack<>();//stack for storing the boards
    private Stack<Move> storeMoves = new Stack<>();//stack for storing the moves which was

    private int turnsCounter;//count the number of the turns

    private boolean turn;//using to swich the turns between the players
    private Stack<String> flippingBack = new Stack<>();//stack for printing the flliped disc cause of Undo

    public GameLogic() {
        this.board = new Disc[8][8];//this constructor ensure that the game will start with a new play board and also to do
        // operation of the game
        this.validMoves = new ArrayList<>();
        turn = true;//the first player to play is player one the blue disc
        turnsCounter = 0;
    }

    /**
     * Attempts to place a disc at the specified position on the game board if the move is valid. This method ensures
     * the move adheres to the game rules, updates the game state, and handles special disc types such as bombs and
     * unflippable discs.
     *
     * @param a    , The position on the board where the disc is to be placed
     * @param disc The disc to place at the specified position.
     * @return true if the disc locate successful, false otherwise
     */

    @Override
    public boolean locate_disc(Position a, Disc disc) {

        if (a.row() < 0 || a.row() >= BOARD_SIZE || a.col() < 0 || a.col() >= BOARD_SIZE || board[a.row()][a.col()] != null) {
            return false; // check if the position is not valid or not available
        }

        if (countFlips(a) <= 0) {//check if the position appear in the array for validation and
            //also there is flips because of this turn
            return false;
        }

        if (disc instanceof BombDisc) {
            if (disc.getOwner().getNumber_of_bombs() == 0) {//if the bombs over
                return false;
            } else {
                disc.getOwner().reduce_bomb();
            }//because of the limit of the number the bombs
        }

        if (disc instanceof UnflippableDisc) {
            if (disc.getOwner().getNumber_of_unflippedable() == 0) {
                return false;
            } else {
                disc.getOwner().reduce_unflippedable();
            }
        }
        storeDiscs.push(deepCopyBoard(board));//save the board state
        Move move = new Move(a, disc);
        storeMoves.push(move);//store the previous move for using after
        move.makeMove(board, a, disc);//position the disc on the chosen position

        turnsCounter++;//after move add one turn to the counter

        String playerSt = turn ? "Player 1" : "Player 2";//for printing
        System.out.println(playerSt + " placed a " + disc.getType() + " in (" + a.row() + ", " + a.col() + ")");

        flips(finalList);//send the list of the discs we want to flip.
        turn = !turn; //change the turn

        return true;
    }

    /**
     * @param position The position for which to retrieve the disc.
     * @return The disc present at the specified position.
     */

    @Override
    public Disc getDiscAtPosition(Position position) {
        return board[position.row()][position.col()];
    }

    /**
     * @return the size of the game board. It provides a way to retrieve the dimension of the board
     */
    @Override
    public int getBoardSize() {
        return board.length;
    }

    /**
     * Determines all valid positions on the board where a player can legally place a disc in the current turn.
     *
     * @return A list of positions that represent the valid moves for the current player.
     * Returns an empty list if no valid moves are available.
     */

    @Override
    public List<Position> ValidMoves() {

        ArrayList<Position> listOfMoves = new ArrayList<Position>();

        for (int i = 0; i < BOARD_SIZE; i++) {//for the rows
            for (int j = 0; j < BOARD_SIZE; j++) {//for the cols
                Position position = new Position(i, j);

                if (board[i][j] == null && countFlips(position) > 0) {//if there are fillps cause because of the disc
                    // placed here we want to add it to the list
                    listOfMoves.add(position);
                }
            }
        }

        return listOfMoves;

    }


    /**
     * Determines the number of opponent discs that can be flipped if a disc is placed at the given position.
     *
     * @param a The board position being checked for potential flips.
     * @return The number of discs that would be flipped by placing a disc at the specified position.
     * Returns 0 if the position is invalid or no discs can be flipped
     */

    @Override
    public int countFlips(Position a) {
        finalList.clear();
        if (a.row() < 0 || a.row() >= BOARD_SIZE || a.col() < 0 || a.col() >= BOARD_SIZE) {//checking if the position
            // is in the board
            return 0;
        }
        ArrayList<Position> toFlipFinal = new ArrayList<>();
        int[][] directions = {{-1, 0}, {-1, 1}, {-1, -1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};//for all the directions

        // Iterate over all directions
        for (int[] arr : directions) {
            ArrayList<Position> toflip = new ArrayList<>();//a list of position which need to flip their disc
            int x = a.row() + arr[0];
            int y = a.col() + arr[1];
            // Continue in the direction until we hit the edge or find a valid scenario
            while (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {
                Disc discAtPosition = board[x][y];
                if (discAtPosition == null) {// If we arrived to a null spot so stop
                    break;
                }
                if (discAtPosition.getOwner().isPlayerOne() != turn) {
                    if (!discAtPosition.getType().equals("⭕")) {
                        toflip.add(new Position(x, y));
                    }
                } else {// if we arrived to a disc of the 9current player so
                    if (!toflip.isEmpty()) {// check the temp counter
                        toFlipFinal.addAll(toflip);
                    }
                    break;
                }
                x += arr[0];//continue to the next
                y += arr[1];//continue to the next
            }
        }
        finalList = new ArrayList<>(toFlipFinal);
        for (Position p : toFlipFinal) {//go over the mid list and find bombs
            if (getDiscAtPosition(p).getType().equals("💣")) {//if we find bomb go to a spacial function
                bombFlips(p, toFlipFinal);
            }
        }
        return finalList.size();//also means the number of discs that will be flipped
    }

    /**
     * Flips the discs at the specified positions, changing their ownership to the current player.
     *
     * @param a A list of board positions where discs need to be flipped.
     */

    public void flips(ArrayList<Position> a) {
        Player currentPlayer = turn ? player1 : player2;//if turn is ture so player1, player2 otherwise
        String playerString = turn ? "Player 1" : "Player 2";
        for (Position p : a) {
            getDiscAtPosition(p).setOwner(currentPlayer);

            Disc disc = getDiscAtPosition(p);//for printing
            System.out.println(playerString + " flipped the " + disc.getType() + " in (" + p.row() + ", " + p.col() + ")");


            String forStack = "    Undo: flipping back ⬤ in (" + p.row() + ", " + p.col() + ")";
            flippingBack.push(forStack);

            System.out.println();
        }
    }

    /**
     * Handles the recursive flipping effect caused by a bomb disc. When a bomb disc is flipped, it triggers additional
     * flips in its surrounding positions, potentially chaining further bomb activations.
     *
     * @param position The position of the bomb disc currently being processed.
     * @param list     A list of positions that tracks all discs to be flipped during the recursive operation.
     */

    public void bombFlips(Position position, List<Position> list) {

        int[][] direct = {{-1, 0}, {-1, 1}, {-1, -1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};//for all directions

        for (int[] arr : direct) {
            int x = position.row() + arr[0];
            int y = position.col() + arr[1];
            if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {//chack if it in the board limits
                Position pos = new Position(x, y);
                Disc neighborDisc = board[x][y];//disc around the position

                if (neighborDisc != null && !(finalList.contains(pos)) && neighborDisc.getType().equals("💣") &&
                        neighborDisc.getOwner().isPlayerOne() != turn) {//also attention to the chain reaction

                    finalList.add(pos);
                    bombFlips(pos, finalList);
                } else if (neighborDisc != null && !(finalList.contains(pos)) &&
                        neighborDisc.getOwner().isPlayerOne() != turn) {
                    finalList.add(pos);
                }// also ensure that there
                // is no duplicates
            }
        }
    }

    /**
     * @return The Player object representing the first player.
     */

    @Override
    public Player getFirstPlayer() {
        return player1;
    }

    /**
     * @return The Player object representing the second player.
     */

    @Override
    public Player getSecondPlayer() {
        return player2;
    }

    /**
     * Sets the players for the game.
     *
     * @param player1 The first player.
     * @param player2 The second player.
     */

    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * @return true if is the firs player turn
     */

    @Override
    public boolean isFirstPlayerTurn() {//if turn==false so is the first player turn, else is the second player turn
        return turn;
    }

    /**
     * In fact, if there is no more valid moves to the player which play now, the game is over
     *
     * @return true if the game is finish
     */

    @Override
    public boolean isGameFinished() {
        int player1Discs = 0;
        int player2Discs = 0;
        if (!ValidMoves().isEmpty()) { //check if there is more valid moves to do
            return false;
        }

        for (int i = 0; i < BOARD_SIZE; i++) {//go over the board for counting each player discs to determine the winner
            for (int j = 0; j < BOARD_SIZE; j++) {
                Disc disc = board[i][j];
                if (disc != null) {
                    if (disc.getOwner().isPlayerOne()) {
                        player1Discs++;
                    } else {
                        player2Discs++;
                    }
                }
            }
        }
        if (player1Discs > player2Discs) {
            System.out.printf("Player 1 wins with %d discs! Player 2 had %d discs.\n", player1Discs, player2Discs);
            getFirstPlayer().addWin();

        } else if (player2Discs > player1Discs) {
            System.out.printf("Player 2 wins with %d discs! Player 1 had %d discs.\n", player2Discs, player1Discs);
            getSecondPlayer().addWin();
        } else System.out.printf("It's a tie! Both players have %d discs.\n", player1Discs);

        return true;
    }

    /**
     * Resets the game board to its initial state, preparing the game for a new round.
     */

    @Override
    public void reset() {
        board = new Disc[8][8];//set a new empty board

        board[3][3] = new SimpleDisc(player1);//place the discs as we get when the game start
        board[4][4] = new SimpleDisc(player1);

        board[3][4] = new SimpleDisc(player2);
        board[4][3] = new SimpleDisc(player2);

        turn = true;  //currentPlayer = player1;

        player1.reset_bombs_and_unflippedable();
        player2.reset_bombs_and_unflippedable();

        //start the game with clear information of these structures
        validMoves.clear();
        storeDiscs.clear();
        flippingBack.clear();
        turnsCounter = 0;
        storeMoves.clear();
        finalList.clear();


    }

    /**
     * Undoes the last move made on the game board, reverting the board state to how it was before the last turn.
     * This method also handles updating player states and printing relevant information about the undone move.
     */
    @Override
    public void undoLastMove() {
        System.out.println("Undoing last move:");
        if (!storeMoves.isEmpty() && !storeDiscs.isEmpty() && turnsCounter > 0) {//check the stacks which prepare
            // to save information of the board
            board = storeDiscs.pop();//get the last object(board) was insert

            Move previousM = storeMoves.pop();
            Player previousP = previousM.disc().getOwner();

            System.out.printf("\tUndo: removing %s from (%d, %d)\n", previousM.disc().getType(),
                    previousM.position().row(),
                    previousM.position().col());

            String s = flippingBack.pop();
            System.out.println(s);

            if (previousM.disc().getType().equals("💣")) {
                previousP.increase_bomb();//update the number of bombs
            } else if (previousM.disc().getType().equals("⭕")) {
                previousP.increase_unflippedable();//update the number of unflippedable
            }

            turn = !turn;//change the turn
            turnsCounter--;
            System.out.println();

        } else {
            System.out.println("\tNo previous move available to undo.");
            System.out.println();
        }
    }

    /**
     * Creates a deep copy of the game board, including all discs placed on the board. This method ensures
     * that each disc in the original board is independently copied, avoiding references to the original objects.
     *
     * @param originalBoard The original 2D array representing the game board to be copied. It contains the discs
     *                      placed on the board.
     * @return A new 2D array of discs representing a deep copy of the original board. The copy contains the same state,
     * but the objects are distinct from the originals.
     */
    private Disc[][] deepCopyBoard(Disc[][] originalBoard) {
        Disc[][] copy = new Disc[BOARD_SIZE][BOARD_SIZE];//the copy that will return
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (originalBoard[i][j] != null) {

                    if (originalBoard[i][j].getType().equals("⭕")) {//deep copy for each type of disc
                        copy[i][j] = new UnflippableDisc(originalBoard[i][j].getOwner());
                    } else if (originalBoard[i][j].getType().equals("💣")) {
                        copy[i][j] = new BombDisc(originalBoard[i][j].getOwner());//creat an object respectively

                    } else {
                        copy[i][j] = new SimpleDisc(originalBoard[i][j].getOwner());
                    }

                } else {
                    copy[i][j] = null;
                }
            }
        }
        return copy;//return to the storeDiscs stack

    }

}
