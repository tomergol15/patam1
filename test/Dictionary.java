package test;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
public class Dictionary {
    private final CacheManager existingWords;
    private final CacheManager notExistingWords;
    private final BloomFilter bf;
    private final String[] fileNames;

    public Dictionary(String... fileNames) {
        this.existingWords = new CacheManager(400, new LRU());
        this.notExistingWords = new CacheManager(100, new LFU());
        this.bf = new BloomFilter(256, "MD5", "SHA1");
        this.fileNames = fileNames.clone();

        for (String fileName : fileNames) {
            try {
                //paths.get(fileName).touri() create an object that is the way to the file and his place
                Scanner scan = new Scanner(new File(Paths.get(fileName).toUri()));
                while (scan.hasNext()) {
                    bf.add(scan.next());
                }
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
            }
        }
    }

    public boolean query(String word) {
        if (existingWords.query(word)) {
            return true;
        }

        if (notExistingWords.query(word)) {
            return false;
        }

        if (bf.contains(word) == false) {
            notExistingWords.add(word);
            return false;
        } else {
            //existingWords.add(word);
            return true;
        }
    }

    public boolean challenge(String word) {
        //searching in the IOSearch and update in the correct casheManager
        try {
            if (IOSearcher.search(word, fileNames)) {
                existingWords.add(word);
                return true;
            } else {
                notExistingWords.add(word);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}

