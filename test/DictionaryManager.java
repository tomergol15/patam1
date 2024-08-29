package test;

import java.util.HashMap;

public class DictionaryManager {
    private final HashMap<String, Dictionary>  myDic;
    public static DictionaryManager singeltion = null;

    public DictionaryManager(){
        this.myDic = new HashMap<>();
    }

    public boolean query(String... args){
        for (int i = 0; i < args.length - 1; i++) {
            String arg = args[i];
            if(!myDic.containsKey(arg)){
                myDic.put(arg, new Dictionary(arg));
            }
        }

        boolean flag = false;
        for(Dictionary d : myDic.values()){
            if(d.query(args[args.length-1])){
                flag=true;
            }
        }
        return flag;
    }
    public boolean challenge(String... args){
        String word1 = args[args.length - 1];
        boolean flag = false;

        for (int i = 0; i < args.length - 1; i++)
        {
            if (!myDic.containsKey(args[i]))
            {
                myDic.put(args[i], new Dictionary(args[i]));
            }

            if (myDic.get(args[i]).challenge(word1))
            {
                flag = true;
            }
        }

        return flag;
    }

    public int getSize(){
        return myDic.size();
    }

    public static DictionaryManager get(){
        if(singeltion==null){
            singeltion=new DictionaryManager();
        }
        return singeltion;
    }

}
