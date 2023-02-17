package exercise1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TermFrequency {
    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Please provide exactly One argument");
            System.exit(1);
        }
        try{

            // load stopWord File and Input File by Scanner & File
            File stopFile = new File("stop_words.txt");
            Scanner stopFileReader = new Scanner(stopFile);
            String [] stopWordsArray = stopFileReader.next().split(",");
            stopFileReader.close();
            Set<String> stopWords = new HashSet<>(Arrays.asList(stopWordsArray));

            Scanner inputFileReader = new Scanner(new File(args[0]));
            /* read entire file
            \\Z means the end of the input, containing final line terminator
            */
            inputFileReader.useDelimiter("\\Z");
            String input = inputFileReader.next().toLowerCase();

            inputFileReader.close();

            //[^a-z]+ match one or more characters that are not in the range of 'a' to 'z' (lowercase letters)
            String[] inputWordsArray = input.split("[^a-z]+");

            // count freq
            HashMap<String, Integer> termFreq = new HashMap<>();
            for (String inputWord: inputWordsArray){
                if (inputWord.length() >= 2 && !stopWords.contains(inputWord) ){
                    termFreq.put(inputWord, termFreq.getOrDefault(inputWord,0)+ 1);
                    }
                }

            // print out top 25
            List<Map.Entry<String, Integer>> freqList = new ArrayList<>(termFreq.entrySet());
            freqList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // reverse order
            for (int i=0; i <25; i++){
                System.out.println(freqList.get(i).getKey() + " - " + freqList.get(i).getValue());
            }
        }catch(FileNotFoundException e){
            System.out.println("File Not Found");
        }
    }
}
