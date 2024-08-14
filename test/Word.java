package test;

import java.util.Arrays;
import java.util.Objects;

public class Word {
    private final Tile[] tiles; //final ???
    private final int row, col;
    private final boolean vertical;	

    public Word(Tile[] tileArr, int row, int col, boolean v){
        this.tiles = tileArr;
        this.row = row;
        this.col = col;
        this.vertical = v;
    }

    public int getRow(){
        return this.row;
    }
    public int getCol(){
        return this.col;
    }
    public boolean getVertical(){
        return this.vertical;
    }
    public Tile[] getTiles(){
        return this.tiles;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word newWord = (Word) o;
        return newWord.row==row && newWord.col==col && newWord.vertical==vertical && Arrays.equals(tiles, newWord.tiles);
    }

    // @Override
    // public int hashCode() {
    //     return Objects.hash(tiles, row, col, vertical);
    // }


}


//example:
// Tile[] tiles = {new Tile('H', 4), new Tile('E', 1), new Tile('L', 1), new Tile('L', 1), new Tile('O', 1)};
// Word word = new Word(tiles, 7, 7, false); 
