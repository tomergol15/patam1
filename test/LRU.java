package test;

import java.util.LinkedList;
public class LRU implements CacheReplacementPolicy {
LinkedList<String> myWords;

LRU(){
    this.myWords = new LinkedList<>();
}

    @Override
    public void add(String word){
    if(myWords.contains(word)){
        myWords.remove(word);
        myWords.add(word); //add to the end of the list
    }
    else
        myWords.add(word);
    }
    @Override
    public String remove() {
        return myWords.removeFirst();
    }

}
