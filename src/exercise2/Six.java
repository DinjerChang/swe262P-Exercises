package exercise2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Six {


    private static Scanner readFile(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Please provide exactly One argument");
            System.exit(1);
        }
        File inputFile = new File(args[0]);
        if (!inputFile.exists()) {
            System.out.println("File Not Found");
            System.exit(1);
        }
        Scanner inputFileReader = new Scanner(inputFile);
        return inputFileReader;
    }


    private static String[] generateInputWords(Scanner inputFileReader) {

            /* read entire file
            \\Z means the end of the input, containing final line terminator
            */
        inputFileReader.useDelimiter("\\Z");
        String input = inputFileReader.next().toLowerCase();

        inputFileReader.close();

        //[^a-z]+ match one or more characters that are not in the range of 'a' to 'z' (lowercase letters)
        String[] inputWords = input.split("[^a-z]+");
        return inputWords;
    }

    private static List<String> removeStopWords(String[] inputWords) throws FileNotFoundException {
        File stopFile = new File("stop_words.txt");
        Scanner stopFileReader = new Scanner(stopFile);
        String[] stopWordsArray = stopFileReader.next().split(",");
        HashSet<String> stopWords = new HashSet<>(Arrays.asList(stopWordsArray));
        stopFileReader.close();
        List<String> list = new ArrayList<>(Arrays.asList(inputWords));

        for (String word : inputWords) {
            if (stopWords.contains(word)) {
                list.remove(word);
            }
        }
        return list;
    }


    private static HashMap<String, Integer> countFreq(List<String> list) {
        HashMap<String, Integer> termFreq = new HashMap<>();
        for (String word : list) {
            if (word.length() >= 2) {
                termFreq.put(word, termFreq.getOrDefault(word, 0) + 1);
            }
        }
        return termFreq;
    }


    private static ArrayList<Map.Entry<String, Integer>> sortDescend(HashMap<String, Integer> termFreq) {
        ArrayList<Map.Entry<String, Integer>> freqList = new ArrayList<>(termFreq.entrySet());
        freqList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return freqList;
    }

    private static void printResult(ArrayList<Map.Entry<String, Integer>> freqList) {
        for (int i = 0; i < 25; i++) {
            System.out.println(freqList.get(i).getKey() + " - " + freqList.get(i).getValue());
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        printResult(sortDescend(countFreq(removeStopWords(generateInputWords(readFile(args))))));

    }
}
