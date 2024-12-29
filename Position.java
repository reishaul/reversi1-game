/**
 * The Position class represents a coordinate on the game board, defined by a row and column. It provides methods for
 * retrieving these values and a mechanism to compare positions for equality.
 */
public class Position {

    private int col,row;

    /**
     * a constructor whom build for using the of this class
     * @param row (x, )-coordinate
     * @param col ( ,y)-coordinate
     */
    public Position(int row, int col) {
        this.col = col;
        this.row = row;
    }

    /**
     * a getter of the ( ,y)-coordinate
     * @return col
     */
    public int col(){
        return col;
    }
    /**
     * a getter of the (x, )-coordinate
     * @return row
     */
    public int row(){
        return row;
    }

    /**
     * The equals method is used to compare two Position objects to determine if they represent the same location on the
     * game board. This is essential for checking whether two positions have the same row and column coordinates.
     * @param o The object to compare with the current Position.
     * @return true if the current Position object and the provided object o have the same row and col values.
     * returns false otherwise.
     */
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Position)) return false;
        Position p= (Position) o;
        return this.row==p.row && this.col==p.col;
    }
}


