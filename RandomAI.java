/**
 * The RandomAI class extends the AIPlayer class and represents an artificial intelligence (AI) player that makes random
 * moves in a Reversi game. The AI randomly selects between valid moves on the board, but the type of disc it plays is
 * influenced by the number of available bombs or unflippable discs it has.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer{
    private GameLogic gameStatus = new GameLogic();
    private int randomNumber = 0;


    public RandomAI(boolean isPlayerOne) {//a constructor
        super(isPlayerOne);
    }

    /**
     * The method determines the AI's next move by randomly selecting one of the valid moves available on the game board.
     * It also considers the current number of available bombs and unflippable discs, influencing the type of disc the
     * AI will play
     * @param gameStatus The current state of the game, which provides methods to retrieve valid moves and game information.
     * @return The method returns a Move object that consists of a randomly chosen position on the board and the disc type
     * that the AI will place at that position.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> validMoves = gameStatus.ValidMoves();//access the list from the game-logic

        if (gameStatus.ValidMoves().isEmpty()) {//if there is no moves to do the game is finish

            gameStatus.isGameFinished();
            gameStatus.reset();

        }

        List<Position> valid = new ArrayList<>();
        valid = gameStatus.ValidMoves();
        int randomMove = valid.size();
        Random randomVaildMove = new Random(randomMove);


        Position randomPosition = validMoves.get(randomVaildMove.nextInt(validMoves.size()));//get a random position from the list of valid moves

        Random random = new Random();

        if(this.getNumber_of_bombs()>0&&this.getNumber_of_unflippedable()>0){//if there bombs valid and also un-flip discs
            randomNumber = random.nextInt(3);
            return new Move(randomPosition, randomDisc(randomNumber));
        }
        else if(this.getNumber_of_bombs()>0&&this.getNumber_of_unflippedable()==0){//if the un-flip is over
            randomNumber = random.nextInt(2)+1;
            return new Move(randomPosition, randomDisc(randomNumber));

        }
        else if(this.getNumber_of_bombs()==0&&this.getNumber_of_unflippedable()>0){
            randomNumber = random.nextInt(2);
            return new Move(randomPosition, randomDisc(randomNumber));
        }

        else { randomNumber = 1;//no other option except simpleDisc
            return new Move(randomPosition, randomDisc(randomNumber));
        }
    }

    public Disc randomDisc(int randomNumber){//case for any situation with/with no bombs/Unflippable
        switch (randomNumber) {
            case 0:
                return new UnflippableDisc(this);
            case 1:
                return new SimpleDisc(this);
            case 2:
                return new BombDisc(this);
            default:
                throw new IllegalArgumentException("Invalid disc number: " + randomNumber);
        }
    }

}