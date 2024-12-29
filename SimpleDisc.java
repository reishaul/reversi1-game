/**
 *The SimpleDisc class is used to represent a standard disc that can be placed on the board. It stores information about
 * the player who owns the disc and provides methods to access or modify the disc's owner. The disc is represented visually
 * by the symbol "⬤" (filled circle) on the game board.
 */
public class SimpleDisc implements Disc{

    private Player CurrentPlayer;

    /**
     * a getter for the current owner of the player
     * @return the player who own the disc
     */
    @Override
    public Player getOwner() {

        return CurrentPlayer;
    }

    /**
     * constructor for the player
     * @param currentPlayer
     */
    public SimpleDisc(Player currentPlayer){
        this.CurrentPlayer=currentPlayer;
    }

    /**
     * The purpose of this method is to assign a player as the owner of the SimpleDisc. This is useful in scenarios where
     * the disc ownership needs to be updated, such as when a disc is flipped during the game or when a player places the
     * disc on the board.
     * @param player The player who will become the owner of the disc
     */
    @Override
    public void setOwner(Player player) {
        this.CurrentPlayer=player;
    }

    /**
     * @return the type of the simple-disc to recognize it and for using next
     */
    @Override
    public String getType() {

        return "⬤";
    }
}
