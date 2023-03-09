package Week8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

// Map-Reduce

public class ThirtyTwo {

    public static class Pair {
        String word;
        int num;

        Pair(String word, int num) {
            this.word = word;
            this.num = num;
        }

        String getKey() {
            return word;
        }

        int getValue() {
            return num;
        }
    }

    // split the file into line chunks and append them to lineList with 200 lines
    public static List<String> partition(String filePath, int number) throws IOException {
        List<String> lineList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String lineStr = reader.readLine();

        StringBuilder sb = new StringBuilder();

        int i = 0;
        while (lineStr != null) {
            sb.append(lineStr).append(" ");
            i++;
            if (i % number == 0) {
                lineList.add(sb.toString());
                sb.setLength(0);
                i = 0;
            }
            lineStr = reader.readLine();
        }
        lineList.add(sb.toString());
        reader.close();
        return lineList;
    }

    public static List<Pair> splitWord(String wordStr) throws IOException {
        // get all the stop words
        Set<String> stopWords = new HashSet<>(Arrays.asList(new String(Files.readAllBytes(Paths.get("stop_words.txt"))).split(",")));

        List<Pair> sortedPairs = new ArrayList<>();
        String[] wordArray = wordStr.toLowerCase().split("[^a-z]+");
        for (String word : wordArray) {
            if (word.length() >= 2 && !stopWords.contains(word))
                sortedPairs.add(new Pair(word, 1));
        }
        return sortedPairs;
    }

    // regroup pairs by inserting the pair according to the first letter:
    // {"a" : {<apple : 1>, <alarm : 1>, <again : 1> }}
    // {"b" : {<banana : 1>, <basket : 1>, <beach : 1> }}
    // .......
    public static Map<String, List<Pair>> regroup(List<List<Pair>> list) {
        Map<String, List<Pair>> map = new HashMap<>();
        for (List<Pair> pairs : list) {
            for (Pair p : pairs) {
                String firstLetter = String.valueOf(p.getKey().charAt(0));
                if (!map.containsKey(firstLetter)) {
                    map.put(firstLetter, new ArrayList<Pair>());
                }
                map.get(firstLetter).add(p);
            }
        }
        return map;
    }

    public static Map<String, Integer> countWord(Map<String, List<Pair>> map) {
        Map<String, Integer> termFreq = new HashMap<>();
        for (String key : map.keySet()) {
            List<Pair> pairs = map.get(key);
            for (Pair p : pairs) {
                termFreq.put(p.getKey(), termFreq.getOrDefault(p.getKey(), 0) + 1);
            }
        }
        return termFreq;
    }

    // sort the termFreq and print the top 25 words
    public static void sort(Map<String, Integer> termFreq) {

        termFreq.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue())
                );
    }


    public static void main(String[] args) throws IOException {
        List<List<Pair>> pairList = new ArrayList<>();
        for (String line : partition(args[0], 200)) {
            pairList.add(splitWord(line));
        }
        sort(countWord(regroup(pairList)));
    }
}
