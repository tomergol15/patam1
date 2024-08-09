package test;

import java.util.Objects;
import java.util.Random;

public class Tile {

    public final char letter;
    public final int score;

    private Tile(char let, int scr) {
        this.letter = let;
        this.score = scr;
    }

    //equals checking if there in the same place in memory, so we want to do override and check if there are the same things
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        //not the same object
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        //generate an new object
        Tile newTile = (Tile) o;
        return newTile.letter == letter && newTile.score == score;
    }

    //return an int number that repsent the object (more easy to search and find the object)
    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public static class Bag {
        //array of quantity of the letters in bag
        int[] lettersInBag = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        int quantityTilesInBag = 98;

        Tile[] tiles = {
            new Tile('A', 1), //letter and his valuse
            new Tile('B', 3),
            new Tile('C', 3),
            new Tile('D', 2),
            new Tile('E', 1),
            new Tile('F', 4),
            new Tile('G', 2),
            new Tile('H', 4),
            new Tile('I', 1),
            new Tile('J', 8),
            new Tile('K', 5),
            new Tile('L', 1),
            new Tile('M', 3),
            new Tile('N', 1),
            new Tile('O', 1),
            new Tile('P', 3),
            new Tile('Q', 10),
            new Tile('R', 1),
            new Tile('S', 1),
            new Tile('T', 1),
            new Tile('U', 1),
            new Tile('V', 4),
            new Tile('W', 4),
            new Tile('X', 8),
            new Tile('Y', 4),
            new Tile('Z', 10)
        };

        //constructor of the Bag
        private Bag(){}


        //maybe to add a while(true) until we find a tile available
        public Tile getRand() {
            Random random = new Random();
            int index = random.nextInt(26); //choose a random number between 0-26
            if (quantityTilesInBag == 0) {
                return null; //if there no tiles in the bag. we started with 98
            } else if (lettersInBag[index] > 0) {
                lettersInBag[index]--; //the quantity of the letter in the bag
                quantityTilesInBag--; //the quantity of tiles in the bag
                return tiles[index];
            } else {
                return null;
            }
        }

        public Tile getTile(char letter) {
            if (Character.isUpperCase(letter)) { //our tiles's letters with Upper Case
                int index = letter - 'A'; //conver char to int
                if (quantityTilesInBag > 0 && (index >= 0 && index < 26) && lettersInBag[index] > 0) {
                    lettersInBag[index]--;
                    quantityTilesInBag--;
                    return tiles[index];
                }
            }
            return null;
        }

        public void put(Tile t) {
            int index = t.letter - 'A';
            if (index >= 0 && index < 26) {
                // check if it's ok to put in a tile
                if (lettersInBag[index] < getMaxQuantity(t.letter)) {
                    lettersInBag[index]++;
                    quantityTilesInBag++;
                }
            }
        }
        
        //func to check the max-quantity of each letter in the bag, that determinated in the begining 
        private int getMaxQuantity(char letter) {
            switch (letter) {
                case 'A': return 9;
                case 'B': return 2;
                case 'C': return 2;
                case 'D': return 4;
                case 'E': return 12;
                case 'F': return 2;
                case 'G': return 3;
                case 'H': return 2;
                case 'I': return 9;
                case 'J': return 1;
                case 'K': return 1;
                case 'L': return 4;
                case 'M': return 2;
                case 'N': return 6;
                case 'O': return 8;
                case 'P': return 2;
                case 'Q': return 1;
                case 'R': return 6;
                case 'S': return 4;
                case 'T': return 6;
                case 'U': return 4;
                case 'V': return 2;
                case 'W': return 2;
                case 'X': return 1;
                case 'Y': return 2;
                case 'Z': return 1;
                default: return 0;
            }
        }

        public int size(){
            return quantityTilesInBag;
        }

        public int[] getQuantities(){
            return lettersInBag.clone(); //copy of the array
        }

        private static Bag b = null;
        public static Bag getBag(){
            if(b==null){
                b= new Bag();
            }
            return b;
        }

    }
}
