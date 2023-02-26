package Week7;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Iterators {
    public static void main(String[] args) throws IOException {
        Iterator<String> getFiles = new readFiles(args[0]);
        Iterator<String> getWord = new allWords(getFiles);
        Iterator<String> getFilteredWords = new filterWords(getWord);
        Iterator<ArrayList<Map.Entry<String, Integer>>> CountAndSort = new countAndSort(getFilteredWords);
        int iteration = 1;
        ArrayList<Map.Entry<String, Integer>> termFreqList;
        while (CountAndSort.hasNext()) {
            System.out.println("---- Updated Iteration " + iteration + " -----");
            termFreqList = CountAndSort.next();
            int i = 0;
            for (Map.Entry<String, Integer> entry : termFreqList) {
                if (i == 25) break;
                System.out.println(entry.getKey() + " - " + entry.getValue());
                i++;
            }
            iteration++;
        }

    }
}

class readFiles implements Iterator<String> {
    private final BufferedReader reader;

    public readFiles(String filePath) throws IOException {
        reader = new BufferedReader(new FileReader(filePath));
    }

    @Override
    public boolean hasNext() {
        boolean res = false;
        try {
            res = reader.ready();
        } catch (IOException e) {
            System.out.println(e);
        }
        return res;
    }

    @Override
    public String next() {
        String line = "";
        try {
            do {
                line = reader.readLine();
            } while (line == null);

        } catch (IOException e) {
            System.out.println(e);
        }
//        System.out.println(line);
        return line.toLowerCase();

    }
}

class allWords implements Iterator<String> {

    private Iterator<String> prior;

    private String[] words;
    private int pos = 0;

    allWords(Iterator i) {
        prior = i;
        words = prior.next().split("[^a-z]+");
//        System.out.println(words.length);
//        for (String word: words){
//            System.out.println(word);
//        }
    }


    @Override
    public boolean hasNext() {

        if (prior.hasNext() || (pos < words.length && words.length > 0)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String next() {
        if (pos < words.length) {
            pos++;
//            System.out.println("bb " + words[pos - 1]);
//            System.out.println("pos " + pos);
            return words[pos - 1];
        } else {
            words = prior.next().split("[^a-z]+");
            pos = 1;
            // non-words line
            if (words.length == 0) return this.next();
//            System.out.println("after " + words[0]);
            return words[0];
        }
    }
}

class filterWords implements Iterator<String> {

    private Iterator<String> prior;
    private Set<String> stopWords;

    filterWords(Iterator i) throws IOException {
        prior = i;
        stopWords = new HashSet<>(Arrays.asList(new String(Files.readAllBytes(Paths.get("stop_words.txt")))
                .split(",")));
    }

    @Override
    public boolean hasNext() {
        return prior.hasNext();
    }

    @Override
    public String next() {
        String word;
        do {
            word = prior.next();

        } while (stopWords.contains(word) || word.length() < 2);

        return word;
    }
}

class countAndSort implements Iterator<ArrayList<Map.Entry<String, Integer>>> {
    private Iterator<String> prior;
    private Map<String, Integer> termFreq = new HashMap<>();
    private int count = 0;

    countAndSort(Iterator i) {
        prior = i;
    }

    @Override
    public boolean hasNext() {
        return prior.hasNext();
    }

    @Override
    public ArrayList<Map.Entry<String, Integer>> next() {
        while ((count == 0 || count % 5000 != 0) && prior.hasNext()) {
            String word = prior.next();

            termFreq.put(word, termFreq.getOrDefault(word, 0) + 1);
            count ++;
        }

        ArrayList<Map.Entry<String, Integer>> termFreqlist = new ArrayList<>(termFreq.entrySet());
        termFreqlist.sort((word1, word2) -> Integer.compare(word2.getValue(),word1.getValue())) ;

        // set the count back to 0, for the next 5000 words
        count = 0;
        return termFreqlist;
    }
}
