import java.io.File;
import java.io.IOException;
import java.util.*;

public class extractZ implements IExtract {
    @Override
    public List<String> extract(String filePath) {
        List<String> validWords = null;
        try {
            // read the stop words
            Set<String> stopWords = new HashSet<>(Arrays.asList(new Scanner(new File("../../../stop_words.txt"))
                    .useDelimiter("\\Z").next().split(",")));
            // read the entire file
            String input = new Scanner(new File(filePath)).useDelimiter("\\Z").next().toLowerCase();

            /* filter the word with lambda expression predicate
            word.contains("z") to extract word with z letter
             */
            validWords = Arrays.stream(input.split("[^a-z]+"))
                    .filter(word -> word.length() >=2 && !stopWords.contains(word) && word.contains("z")).toList();
        } catch (IOException e) {
            System.out.println(e);
        }
        return validWords;

    }
}
