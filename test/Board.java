package test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private static final int sizeBoard = 15;
    public static Board myBoard = null; //singeltion
    private final Tile[][] board;
    private final Map<String, Integer> bonusTiles; // מפה שמכילה את הבונוסים של המשבצות

    private Board() {
        board = new Tile[sizeBoard][sizeBoard];
         for (int i = 0; i < sizeBoard; i++) {
            Arrays.fill(board[i], null);
        }
         bonusTiles = new HashMap<>();
         initializeBonusTiles();
    }
    //singelton
    public static Board getBoard() {
        if (myBoard == null) {
            myBoard = new Board();
        }
        return myBoard;
    }

    private void initializeBonusTiles() {
        // Triple Word Score (אדום)
        setBonusTile(0, 0, 3);
        setBonusTile(0, 7, 3);
        setBonusTile(0, 14, 3);
        setBonusTile(7, 0, 3);
        setBonusTile(7, 14, 3);
        setBonusTile(14, 0, 3);
        setBonusTile(14, 7, 3);
        setBonusTile(14, 14, 3);

        // Double Word Score (צהוב)
        setBonusTile(1, 1, 2);
        setBonusTile(2, 2, 2);
        setBonusTile(3, 3, 2);
        setBonusTile(4, 4, 2);
        setBonusTile(10, 10, 2);
        setBonusTile(11, 11, 2);
        setBonusTile(12, 12, 2);
        setBonusTile(13, 13, 2);
        setBonusTile(1, 13, 2);
        setBonusTile(2, 12, 2);
        setBonusTile(3, 11, 2);
        setBonusTile(4, 10, 2);
        setBonusTile(10, 4, 2);
        setBonusTile(11, 3, 2);
        setBonusTile(12, 2, 2);
        setBonusTile(13, 1, 2);

        // Triple Letter Score (כחול כהה)
        setBonusTile(1, 5, 3);
        setBonusTile(1, 9, 3);
        setBonusTile(5, 1, 3);
        setBonusTile(5, 5, 3);
        setBonusTile(5, 9, 3);
        setBonusTile(5, 13, 3);
        setBonusTile(9, 1, 3);
        setBonusTile(9, 5, 3);
        setBonusTile(9, 9, 3);
        setBonusTile(9, 13, 3);
        setBonusTile(13, 5, 3);
        setBonusTile(13, 9, 3);

        // Double Letter Score (תכלת)
        setBonusTile(0, 3, 2);
        setBonusTile(0, 11, 2);
        setBonusTile(2, 6, 2);
        setBonusTile(2, 8, 2);
        setBonusTile(3, 0, 2);
        setBonusTile(3, 7, 2);
        setBonusTile(3, 14, 2);
        setBonusTile(6, 2, 2);
        setBonusTile(6, 6, 2);
        setBonusTile(6, 8, 2);
        setBonusTile(6, 12, 2);
        setBonusTile(7, 3, 2);
        setBonusTile(7, 11, 2);
        setBonusTile(8, 2, 2);
        setBonusTile(8, 6, 2);
        setBonusTile(8, 8, 2);
        setBonusTile(8, 12, 2);
        setBonusTile(11, 0, 2);
        setBonusTile(11, 7, 2);
        setBonusTile(11, 14, 2);
        setBonusTile(12, 6, 2);
        setBonusTile(12, 8, 2);
        setBonusTile(14, 3, 2);
        setBonusTile(14, 11, 2);

        // המרכז (כוכב)
        setBonusTile(7, 7, 2); // הכוכב נמצא במרכז ומכפיל את ערך המילה
    }
    private void setBonusTile(int row, int col, int bonus) {
        String key = generateKey(row, col);
        bonusTiles.put(key, bonus); // מיקום הלוח עם ערך הבונוס
    }
    private String generateKey(int row, int col) {
        return row + "," + col;
    }

    public Tile[][] getTiles() {
        Tile[][] copy = new Tile[sizeBoard][sizeBoard];
        for(int i=0; i < sizeBoard; i++)
        {
            System.arraycopy(board[i],0,copy[i],0,sizeBoard);
        }
        return copy;
    }

    public boolean boardLegal(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        Tile[] tiles = word.getTiles();
        boolean vertical = word.getVertical();
        //Bounds check
        if (vertical) {
            if (row < 0 || row + tiles.length > sizeBoard || col < 0 || col >= sizeBoard) {
                return false;
            }
        } else {//horizontal
            if (row < 0 || row >= sizeBoard || col < 0 || col + tiles.length > sizeBoard) {
                return false;
            }
        }
        boolean hasAdjacent = false;
        boolean overlapExistingTile = false; //if the word overlap on another word

        for (int i = 0; i < tiles.length; i++) {
            int currentRow = vertical ? row + i : row;
            int currentCol = vertical ? col : col + i;

            Tile boardTile = board[currentRow][currentCol];
            Tile wordTile = tiles[i];
            if (boardTile != null) {
                if (wordTile == null) {
                    overlapExistingTile = true;
                } else if (boardTile.letter != wordTile.letter) {
                    return false;
                } else {
                    overlapExistingTile = true;
                }

            } else {
                if (wordTile == null) {
                    return false;
                }
            }
            if (!hasAdjacent) {
                if (hasNeighboringTile(currentRow, currentCol)) {
                    hasAdjacent = true;
                }
            }
            Board isFirstMove = isBoardEmpty();
            if (isFirstMove) {
                if (!coversCenterCell(word)) {
                    return false;
                }
            } else {
                if (!hasAdjacent && !overlapExistingTile) {
                    return false;
                }
            }
            return true;
        }
        private boolean isBoardEmpty() {
            for (int i = 0; i < sizeBoard; i++) {
                for (int j = 0; j < sizeBoard; j++) {
                    if (board[i][j] != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        //check where the center of the board
        private boolean coversCenterCell(Word word)
        {
            int row1 = word.getRow();
            int col1 = word.getCol();
            boolean verical = word.getVertical();
            int length = word.getTiles().length;
            int CENTER = sizeBoard / 2;
            if (vertical) {
                return col1 == CENTER && row1 <= CENTER && (row1 + length - 1) >= CENTER;
            } else {
                return row1 == CENTER && col1 <= CENTER && (col1 + length - 1) >= CENTER;
            }
        }
        private boolean hasNeighboringTile(int row,int col)
        {
            //Up
            if (row > 0 && board[row - 1][col] != null) {
                return true;
            }
            //Down
            if (row < sizeBoard - 1 && board[row + 1][col] != null) {
                return true;
            }
            //Left
            if (col > 0 && board[row][col - 1] != null) {
                return true;
            }
            //Right
            if (col > sizeBoard - 1 && board[row][col + 1] != null) {
                return true;
            }
            return false;
        }
    }
    public boolean dictionaryLegal(Word word) {
        return true;
    }
    public ArrayList<Word> getWords(Word word){
        ArrayList<Word> newWords=new ArrayList<>();
        int row=word.getRow();
        int col=word.getCol();
        boolean vertical = word.getVertical();

        Tile[] tiles=word.getTiles();
        for(int i=0; i<tiles.length;i++)
        {
            int currentRow= vertical ? row+i : row;
            int currentCol= vertical ? col :col +i;
            Word newWord =findNewWord(currentRow,currentCol,!vertical);
            if(newWord!=null)
            {
                newWords.add(newWord);
            }
        }
        return newWords;
    }

    //function that help to find the new word
    private Word findNewWord(int row,int col, boolean notVertical){
        int start = notVertical ? row : col;
        int end = start;
        int check_start=notVertical ? start-1 : row;
        int check_end=notVertical ? col : start-1;

        while(start > 0 && board[check_start][check_end]!=null)
        {
            start --;
            check_start=notVertical ? start-1 : row;
            check_end=notVertical ? col : start-1;
        }

        check_start=notVertical ? end+1 : row;
        check_end=notVertical ? col : end+1;
        while(end <sizeBoard -1 && board[check_start][check_end]!=null)
        {
            end++;
            check_start=notVertical ? end+1 : row;
            check_end=notVertical ? col : end+1;
        }
        if (start==end) //don't have a new word
        {
            return null;
        }
        Tile[] newTiles=new Tile[end-start+1];
        for(int i=start;i<=end;i++){
            newTiles[i-start]=board[notVertical ? i: row][notVertical ? col :i];
        }
        return new Word(newTiles,notVertical ? start : row, notVertical ? col : start , notVertical);
    }

    public int getScore(Word word) {
    int score=0;
    int wordMultiplier = 1;

    int row = word.getRow();
    int col = word.getCol();
    boolean vertical = word.getVertical();
    Tile[] tiles = word.getTiles();

    for(int i=0;i< tiles.length;i++) {
        int currentRow = vertical ? row + i : row;
        int currentCol = vertical ? col : col + i;
        String key = generateKey(currentRow, currentCol);

        Tile currentTile = tiles[i];
        int letterScore = currentTile.score;

        if (bonusTiles.containsKey(key)) {
            int bonus = bonusTiles.get(key);
            if (bonus == 2 || bonus == 3) {
                wordMultiplier *= bonus;
            } else {
                letterScore *= bonus;
            }
        }
        score += letterScore;
    }
        return score*wordMultiplier;
    }

    public int tryPlaceWord(Word word){
        //if thw word is overlap the borders or other things
        if(!boardLegal(word)){
            return 0;
        }

        ArrayList<Word> myWords = getWords(word);
        //loop on all the words in the array that we got
        for(Word w1 : myWords)
        {
            //if the word is not allowed
            if(!dictionaryLegal(w1)){
                return 0;
            }
        }

        //sum all the score
        int totalScore = 0;
        for(Word w1 : myWords){
            totalScore += getScore(w1);
        }

        //we get the data members of the word
        Tile[] tiles = word.getTiles();
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.getVertical();

        //we put the word into the board
        for (int i = 0; i < tiles.length; i++) {
            int currentRow = vertical ? row + i : row;
            int currentCol = vertical ? col : col + i;
            board[currentRow][currentCol] = tiles[i];
        }

        return totalScore;
    }

}

