package Week7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Stream {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Please provide exactly one argument");
            System.exit(1);
        }

        Set<String> stopWords = new HashSet<>(List.of((new String(
                Files.readAllBytes(Paths.get("stop_words.txt")))
                .toLowerCase().split(","))));

        Arrays.stream(new String(Files.readAllBytes(Paths.get(args[0]))).toLowerCase()
                .split("[^a-z]+"))
                .filter( word -> word.length() >=2 && !stopWords.contains(word))
                // collect as Map<String, Long>
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
                .entrySet().stream()
                // sort
                .sorted(Map.Entry.<String,Long>comparingByValue().reversed())
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue())
                );
    }
}
