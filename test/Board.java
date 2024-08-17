package test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private static final int sizeBoard = 15;
    public static Board myBoard = null; //singeltion
    private final Tile[][] board;
    private final int[][] bonusTiles;
    private boolean starUsed;
    private ArrayList<Word> bankWords;


    private Board() {
        board = new Tile[sizeBoard][sizeBoard];
        for (int i = 0; i < sizeBoard; i++) {
            Arrays.fill(board[i], null);
        }
        bonusTiles = new int[sizeBoard][sizeBoard];
        initializeBonusTiles();
        starUsed = false;
        bankWords = new ArrayList<>();
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
        for (int i = 0; i < bonusTiles.length; i++) {
            for (int j = 0; j < bonusTiles.length; j++) {
                bonusTiles[i][j] = 0;
            }
        }
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

        // the center Star
        setBonusTile(7, 7, 2);
    }

    private void setBonusTile(int row, int col, int bonus) {
        bonusTiles[row][col] = bonus;
    }
//    private String generateKey(int row, int col) {
//        return row + "," + col;
//    }

    public Tile[][] getTiles() {
        Tile[][] copy = new Tile[sizeBoard][sizeBoard];
        for (int i = 0; i < sizeBoard; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, sizeBoard);
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
        }

        boolean isFirstMove = isBoardEmpty();
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

    private boolean coversCenterCell(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.getVertical();
        int length = word.getTiles().length;
        int CENTER = sizeBoard / 2;
        if (vertical) {
            return col == CENTER && row <= CENTER && (row + length - 1) >= CENTER;
        } else {
            return row == CENTER && col <= CENTER && (col + length - 1) >= CENTER;
        }
    }

    private boolean hasNeighboringTile(int row, int col) {
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
        if (col < sizeBoard - 1 && board[row][col + 1] != null) {
            return true;
        }
        return false;
    }

    public boolean dictionaryLegal(Word word) {
        return true;
    }

    public ArrayList<Word> getWords(Word word) {
        ArrayList<Word> newWords = new ArrayList<>();

        //built thw complete word we get - without null
        Word completeWord = buildCompleteWord(word);
        if (completeWord != null) {
            newWords.add(completeWord);
        }

        //trying to put into the board the completeWord and makesure it's on the board
        Tile[] tiles = completeWord.getTiles();
        int rowC = completeWord.getRow();
        int colC = completeWord.getCol();
        boolean vertical = completeWord.getVertical();
        //we put the word into the board
        for (int i = 0; i < tiles.length; i++) {
            int currentRow = vertical ? rowC + i : rowC;
            int currentCol = vertical ? colC : colC + i;
            board[currentRow][currentCol] = tiles[i];
        }

        // checks for new words
        for (int i = 0; i < word.getTiles().length; i++) {
            int row = word.getVertical() ? word.getRow() + i : word.getRow();
            int col = word.getVertical() ? word.getCol() : word.getCol() + i;

            Word perpendicularWord = findWordAt(row, col, !word.getVertical());
            if (perpendicularWord != null && perpendicularWord.getTiles().length > 1) {
                newWords.add(perpendicularWord);
            }
        }
        return newWords;
    }

    private Word buildCompleteWord(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.getVertical();
        Tile[] originalTiles = word.getTiles();
        Tile[] newTiles = Arrays.copyOf(originalTiles, originalTiles.length);

        for (int i = 0; i < newTiles.length; i++) {
            int currentRow = vertical ? row + i : row;
            int currentCol = vertical ? col : col + i;

            //if the current tile is null - search the tile in the board
            if (newTiles[i] == null) {
                newTiles[i] = board[currentRow][currentCol];
            }
        }
        return new Word(newTiles, row, col, vertical);
    }


    private Word findWordAt(int row, int col, boolean vertical) {
        int start = vertical ? row : col;
        int end = start;

        // Move backwards to find the start of the word
        while (start > 0 && (vertical ? board[start - 1][col] : board[row][start - 1]) != null) {
            start--;
        }

        // Move forward to find the end of the word
        while (end < sizeBoard - 1 && (vertical ? board[end + 1][col] : board[row][end + 1]) != null) {
            end++;
        }

        // If only a single tile is found, it's not a valid word
        if (start == end) {
            return null;
        }

        // Create the word
        Tile[] newTiles = new Tile[end - start + 1];
        for (int i = start; i <= end; i++) {
            newTiles[i - start] = vertical ? board[i][col] : board[row][i];
        }

        return new Word(newTiles, vertical ? start : row, vertical ? col : start, vertical);
    }
    public int getScore(Word word) {
        // checking if the word is already in the bank
        for (Word w : bankWords) {
            if (w.equals(word)) {
                return 0;
            }
        }

        int score = 0;
        int wordMultiplier = 1;

        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.getVertical();
        Tile[] tiles = word.getTiles();

        for (int i = 0; i < tiles.length; i++) {
            int currentRow = vertical ? row + i : row;
            int currentCol = vertical ? col : col + i;
//        String key = generateKey(currentRow, currentCol);

            Tile currentTile = tiles[i];
            int letterScore = currentTile.score;
            int bonus = bonusTiles[currentRow][currentCol];
            if (bonus == 2) {
                if (currentRow == 7 && currentCol == 7 && starUsed) {
                    score += letterScore;
                    continue;
                } else if (currentRow == 7 && currentCol == 7) {
                    wordMultiplier *= 2;
                } else if (isDoubleWordScore(currentRow, currentCol)) {
                    wordMultiplier *= 2;
                } else {
                    letterScore *= 2;
                }
            } else if (bonus == 3) {
                if (isTripleWordScore(currentRow, currentCol)) {
                    wordMultiplier *= 3;
                } else {
                    letterScore *= 3;
                }
            }
            score += letterScore;
        }
        if (word.getRow() <= sizeBoard / 2 && word.getRow() + tiles.length > sizeBoard / 2 &&
                word.getCol() <= sizeBoard / 2 && word.getCol() + tiles.length > sizeBoard / 2) {
            starUsed = true;
        }
        bankWords.add(word);
        return score * wordMultiplier;
    }

    private boolean isDoubleWordScore(int row, int col) {
        return (row == 1 && col == 1) || (row == 2 && col == 2) ||
                (row == 3 && col == 3) || (row == 4 && col == 4) ||
                (row == 10 && col == 10) || (row == 11 && col == 11) ||
                (row == 12 && col == 12) || (row == 13 && col == 13) ||
                (row == 1 && col == 13) || (row == 2 && col == 12) ||
                (row == 3 && col == 11) || (row == 4 && col == 10) ||
                (row == 10 && col == 4) || (row == 11 && col == 3) ||
                (row == 12 && col == 2) || (row == 13 && col == 1);
    }

    private boolean isTripleWordScore(int row, int col) {
        return (row == 0 && col == 0) || (row == 0 && col == 7) ||
                (row == 0 && col == 14) || (row == 7 && col == 0) ||
                (row == 7 && col == 14) || (row == 14 && col == 0) ||
                (row == 14 && col == 7) || (row == 14 && col == 14);
    }

    public int tryPlaceWord(Word word) {
        //if thw word is overlap the borders or other things
        if (!boardLegal(word)) {
            return 0;
        }
        int totalScore=0;
        ArrayList<Word> myWords = new ArrayList<>();
        myWords = getWords(word);
        //loop on all the words in the array that we got
        for (Word w1 : myWords) {
            //if the word is not allowed
            if (!dictionaryLegal(w1)) {
                return 0;
            }
           totalScore+=getScore(w1);
        }
        //sum all the score
       // totalScore = getScore(myWords.getFirst());
        return totalScore;
    }
}

//for new - put this in a commit because i fill the board
//            Tile[] tiles = w1.getTiles();
//            int row = w1.getRow();
//            int col = w1.getCol();
//            boolean vertical = w1.getVertical();
//            //we put the word into the board
//            for (int i = 0; i < tiles.length; i++) {
//                int currentRow = vertical ? row + i : row;
//                int currentCol = vertical ? col : col + i;
//                board[currentRow][currentCol] = tiles[i];
//            }



//        //we get the data members of the word
//        Tile[] tiles = word.getTiles();
//        int row = word.getRow();
//        int col = word.getCol();
//        boolean vertical = word.getVertical();
//
//        //we put the word into the board
//        for (int i = 0; i < tiles.length; i++) {
//            int currentRow = vertical ? row + i : row;
//            int currentCol = vertical ? col : col + i;
//            board[currentRow][currentCol] = tiles[i];
//        }

        //}

//commit for a getscore that didnt work:
//            if (bonus != 0) {
//                if (bonus == 3) {
//                    wordMultiplier *= bonus;
//                    letterScore *= wordMultiplier;
//                    score += letterScore;
//                    wordMultiplier = 1;
//                    continue;
//                } else if (bonus == 2) {
//                    // בדיקה אם מדובר בכוכב במרכז
//                    if (currentRow == sizeBoard / 2 && currentCol == sizeBoard / 2 && !starUsed) {
//                        wordMultiplier *= bonus;
//                    }
// else {
//                    letterScore *= bonus;
//                }
//                }
//            }