/**
 * The UnflippableDisc class is used to create discs that cannot be flipped, which is a special gameplay mechanic. When
 * a disc of this type is placed on the board, it will stay in its current state and cannot be flipped by the opponent.
 */
public class UnflippableDisc implements Disc{

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
    public UnflippableDisc(Player currentPlayer){
        this.CurrentPlayer=currentPlayer;
    }
    /**
     * The purpose of this method is to assign a player as the owner of the UnflippableDisc . This is useful in scenarios
     * where the disc ownership needs to be updated, such as when a disc is flipped during the game or when a player places
     * the disc on the board.
     * @param player The player who will become the owner of the disc
     */
    @Override
    public void setOwner(Player player) {
        this.CurrentPlayer=player;
    }

    /**
     * @return the type of the UnflippableDisc-disc to recognize it and for using next
     */
    @Override
    public String getType() {
        return "⭕";
    }
}
