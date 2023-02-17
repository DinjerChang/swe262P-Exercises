package exercise2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Five {
    // procedures
    private static Scanner inputFileReader;

    private static void readFile(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Please provide exactly One argument");
            System.exit(1);
        }
        File inputFile = new File(args[0]);
        if (!inputFile.exists()) {
            System.out.println("File Not Found");
            System.exit(1);
        }
        inputFileReader = new Scanner(inputFile);
    }

    private static String[] inputWords;

    private static void generateInputWords() throws FileNotFoundException {

            /* read entire file
            \\Z means the end of the input, containing final line terminator
            */
        inputFileReader.useDelimiter("\\Z");
        String input = inputFileReader.next().toLowerCase();

        inputFileReader.close();

        //[^a-z]+ match one or more characters that are not in the range of 'a' to 'z' (lowercase letters)
        inputWords = input.split("[^a-z]+");
    }

    private static final List<String> list = new ArrayList<>();

    private static void removeStopWords() throws FileNotFoundException {
        File stopFile = new File("stop_words.txt");
        Scanner stopFileReader = new Scanner(stopFile);
        String[] stopWordsArray = stopFileReader.next().split(",");
        HashSet<String> stopWords = new HashSet<>(Arrays.asList(stopWordsArray));
        stopFileReader.close();
        list.addAll(Arrays.asList(inputWords));

        for (String word : inputWords) {
            if (stopWords.contains(word)) {
                list.remove(word);
            }
        }
    }


    private static final HashMap<String, Integer> termFreq = new HashMap<>();

    private static void countFreq() {
        for (String word : list) {
            if (word.length() >= 2) {
                termFreq.put(word, termFreq.getOrDefault(word, 0) + 1);
            }
        }
    }

    private static List<Map.Entry<String, Integer>> freqList;

    private static void sortDescend() {
        freqList = new ArrayList<>(termFreq.entrySet());
        freqList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
    }

    private static void printResult() {
        for (int i = 0; i < 25; i++) {
            System.out.println(freqList.get(i).getKey() + " - " + freqList.get(i).getValue());
        }
    }


    public static void main(String[] args) throws FileNotFoundException {

        readFile(args);
        generateInputWords();
        removeStopWords();
        countFreq();
        sortDescend();
        printResult();


    }
}
