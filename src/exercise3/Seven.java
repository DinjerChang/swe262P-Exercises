package exercise3;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Seven {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide exactly one argument");
            System.exit(1);
        }
        try {
            Set<String> stopWords = new HashSet<>(Arrays.asList(new Scanner(new File("stop_words.txt")).useDelimiter("\\Z").next().split(",")));

            // read the entire file
            String input = new Scanner(new File(args[0])).useDelimiter("\\Z").next().toLowerCase();

            /* filter the word with lambda expression predicate
            collect the result in the container
            groupingBy groups the words by word and counts the occurrences of each word.*/
            Map<String, Long> termFreq = Arrays.stream(input.split("[^a-z]+"))
                    .filter(word -> word.length() >= 2 && !stopWords.contains(word))
                    .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

            /*
            */

            termFreq.entrySet().stream()
                    .sorted(Map.Entry.<String,Long>comparingByValue().reversed())
                    .limit(25)
                    .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
