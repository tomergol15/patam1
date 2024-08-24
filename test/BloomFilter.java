package test;

import java.util.BitSet;
import java.security.MessageDigest;
import java.math.BigInteger;

public class BloomFilter {
    BitSet bs;
    int size;
    MessageDigest[] md;

    public BloomFilter(int size, String... algs){
        this.size = size;
        this.bs = new BitSet(size);
        this.md = new MessageDigest[algs.length];

        for (int i = 0; i < algs.length; i++) {
            try {
                this.md[i] = MessageDigest.getInstance(algs[i]);
            } catch (Exception e) {
                throw new RuntimeException("Invalid algorithm: " + algs[i]);
            }
        }
    }

    public void add(String string) {
        for (MessageDigest digest : md) {
            int index = getHashIndex(string, digest, size);
            bs.set(index);
        }
    }

    public boolean contains(String word) {
        for (MessageDigest digest : md) {
            int index = getHashIndex(word, digest, size);
            if (!bs.get(index)) {
                return false; // if one of the bits is not set, the word is not found
            }
        }
        return true; // all bits are open, the word might be found
    }

    public String toString(){
        //object that help to build string
        StringBuilder sb = new StringBuilder();
        for (int i= 0; i <bs.length(); i++){
            if(bs.get(i))
                sb.append("1");
            else
                sb.append("0");
        }
        return sb.toString();
    }

    private int getHashIndex(String input, MessageDigest digest, int size) {
        //the func digest calculate the hash of the word (that turned into byte's array) - return it as array of bytes
        byte[] hashBytes = digest.digest(input.getBytes());
        //returned the result into a bitInteger
        BigInteger hashInt = new BigInteger(hashBytes);

        //turn the bigInteger into a regular int and used his absolute value.
//      //make sure it in the range of the bitset's size
        int index = Math.abs(hashInt.intValue()) % size;

        return index;
    }


}




//    public void add(String string){
//        for(MessageDigest digest : md){
//            byte[] hashBytes = digest.digest(string.getBytes());
//            BigInteger hashInt = new BigInteger(hashBytes);
//            int index = Math.abs(hashInt.intValue()) % size;
//            bs.set(index);
//        }
//    }
//
//    public boolean contains(String word) {
//        for (MessageDigest digest : md) {
//            //the func digest calculate the hash of the word (that turned into byte's array - return it as array of bytes
//            byte[] hashBytes = digest.digest(word.getBytes());
//            //returned the result into a bitInteger
//            BigInteger hashInt = new BigInteger(hashBytes);
//            //turn the bigInteger into a regular int and used his absolute value.
//            //make sure it in the range of the bitset's size
//            int index = Math.abs(hashInt.intValue()) % size;
//            if (!bs.get(index)) {
//                return false; // if one of the bits closed - the word is not found
//            }
//        }
//        return true; // all the bits are open, possible to found
//    }