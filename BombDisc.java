/**
 * This class build for a bomb disc, different type of disc with different properties and logic.
 */
public class BombDisc implements Disc{

    private Player CurrentPlayer;

    /**
     * @return the player who locate the disc
     */
    @Override
    public Player getOwner() {
        return CurrentPlayer;
    }

    /**
     * The BombDisc constructor initializes a bomb disc with a specific owner (player). This disc has unique behavior
     * and is associated with a player who places it on the board.
     * @param currentPlayer The player who owns and places the bomb disc on the board. This parameter assigns ownership
     * to the bomb disc, determining which player controls it.
     */
    public BombDisc(Player currentPlayer){
        this.CurrentPlayer=currentPlayer;
    }

    /**
     * set the player to own the disc
     * @param player some player we want to own the disc
     */
    @Override
    public void setOwner(Player player) {
        this.CurrentPlayer=player;
    }

    /**
     * @return the type of the bomb disc to recognize it and for using next
     */
    @Override
    public String getType() {
        return "💣";
    }

}
