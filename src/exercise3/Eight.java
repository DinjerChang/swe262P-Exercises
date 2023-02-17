package exercise3;

import java.io.*;
import java.util.*;

public class Eight {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide exactly one argument");
            System.exit(1);
        }
        try {
            // stop words
            Scanner stopFileReader = new Scanner(new File("stop_words.txt"));
            Set<String> stopWords = new HashSet<>(Arrays.asList(stopFileReader.next().split(",")));
            stopFileReader.close();

            FileReader inputFileReader = new FileReader(args[0]);

            // record tem freq
            Map<String, Integer> termFreq = new HashMap<>();
            List<String> res = parse(inputFileReader, new ArrayList<>(), stopWords);
            inputFileReader.close();

            for (String word : res) {
                termFreq.put(word, termFreq.getOrDefault(word, 0) + 1);
            }

            // sort and print top 25
            termFreq.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(25)
                    .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    private static List<String> parse(Reader reader, List<String> nonStopWords, Set<String> stopWords)
            throws IOException {
        StringBuilder wordBuilder = new StringBuilder();
        while (reader.ready()) {
            // read character by character
            Character ch = (char) reader.read();

            if (Character.isAlphabetic(ch)) {
                wordBuilder.append(ch);
            } else {
                // reach word end
                String word = wordBuilder.toString().toLowerCase();
                // append ch if the ch is [a-z] continuously, if not, then it form a word
                if (!stopWords.contains(word) && word.length() > 1) {
                    nonStopWords.add(word);
                }

                parse(reader, nonStopWords, stopWords);
            }
        }
        return nonStopWords;
    }
}
