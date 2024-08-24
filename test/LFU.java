package test;

import java.util.HashMap;
import java.util.Map;
public class LFU implements CacheReplacementPolicy {
   private final Map<String,Integer> freqMap;

    public LFU(){
        this.freqMap = new HashMap<>();
    }

    @Override
    public void add(String word){
        if(freqMap.containsKey(word)){
            int count = freqMap.get(word);
            freqMap.replace(word,count+1);
        }
        else{
            freqMap.put(word,1);
        }
    }
    @Override
    public String remove() {
    if(freqMap.isEmpty()){
        return null;
    }
        String removeWord = null;
        int minFreq = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
            if (entry.getValue() < minFreq) {
                minFreq = entry.getValue();
                removeWord = entry.getKey();
            }
        }

        if (removeWord != null) {
            freqMap.remove(removeWord);
        }

        return removeWord;
    }

}
