/**
 * The GreedyAI class is a concrete implementation of the AIPlayer class, designed to make moves based on a greedy
 * strategy in the game. The primary goal of this AI is to maximize immediate gains by choosing the move that flips
 * the most discs
 */
import java.util.List;
import java.util.Comparator;


public class GreedyAI extends AIPlayer {

    public GreedyAI(boolean isPlayerOne) {

        super(isPlayerOne);
    }

    /**
     * The makeMove method employing a greedy algorithm to determine the best
     * move based on immediate gains.
     *
     * @param gameStatus represents the current state of the game, providing access to valid moves, flip calculations,
     *                   and player information.
     * @return The move object containing the chosen position and the disc to be placed.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {

        List<Position> validMoves = gameStatus.ValidMoves();//get accesses to the list in the game-logic

        if (validMoves == null || gameStatus.ValidMoves().isEmpty()) {//if there is no moves to do the game is finish

            return null;
        }
        //sort according to the most high value of flips
        //then sort according to the most right in the board
        // and then take the lowest one
        Position bestPosition = validMoves.stream().max(Comparator.comparingInt((Position pos) -> gameStatus.countFlips(pos))
                        .thenComparingInt(Position::col)
                        .thenComparingInt(Position::row))
                .orElse(null);

        if (bestPosition == null) {
            return null; // if there is no move
        }

        Disc disc = new SimpleDisc(isPlayerOne() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer());//if is player
        // one turn so take player1, and player2 otherwise

        return new Move(bestPosition, disc);
    }
}