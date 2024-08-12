package test;
import java.util.Arrays;

public class Board {
    private int sizeBoard = 15;
    public static Board myBoard = null;
    private Tile[][] board;

    private Board(){
        board = new Tile[sizeBoard][sizeBoard];
        for (int i = 0; i < sizeBoard; i++) {
            Arrays.fill(board[i], null);
        }
    }
    public Board getBoard(){
        if (myBoard==null){
            myBoard = new Board();
        }
        return myBoard;
    }

//    public Board getTiles() {
//        return myBoard.clone();
//    }
}
