package test;


import java.util.HashSet;
import java.util.Set;

public class CacheManager {
	private final int maxSizeCashe;
    private final CacheReplacementPolicy crpCashe; //private?
    public Set<String> wordsInCahse;

    public CacheManager(int size, CacheReplacementPolicy crp){
        maxSizeCashe=size;
        crpCashe=crp;
        wordsInCahse = new HashSet<>();
    }
    public boolean query(String word){
        return wordsInCahse.contains(word);

    }
    public void add(String word){
    if(word==null){
        return;
    }

    if(wordsInCahse.size()>=maxSizeCashe){
        String removeWord = crpCashe.remove(); //return the word that we will kick
        wordsInCahse.remove(removeWord);
    }
    crpCashe.add(word);
    wordsInCahse.add(word);
    }
}
