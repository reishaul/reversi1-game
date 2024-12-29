import java.util.Comparator;
import java.util.List;

public class MultiAI extends AIPlayer{

    public MultiAI(boolean isPlayerOne) {

        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {

        List<Position> validMoves = gameStatus.ValidMoves();//get accesses to the list in the game-logic

        if (validMoves == null || gameStatus.ValidMoves().isEmpty()) {//if there is no moves to do the game is finish

            return null;
        }
        //sort according to the most high value of flips
        // and then take the lowest one
        Position multiPosition = validMoves.stream().max(Comparator.comparingInt((Position pos) -> gameStatus.countFlips(pos))
                        .thenComparingInt(Position::row)).orElse(null);

        if (multiPosition == null) {
            return null; // if there is no move
        }

        Disc disc = new SimpleDisc(isPlayerOne() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer());//if is player
        // one turn so take player1, and player2 otherwise

        return new Move(multiPosition, disc);
    }
}
