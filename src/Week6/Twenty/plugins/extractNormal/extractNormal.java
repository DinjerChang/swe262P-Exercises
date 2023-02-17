import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class extractNormal implements IExtract {
    @Override
    public List<String> extract(String filePath){
        List<String> validWords = null;
        try {
            Set<String> stopWords = new HashSet<>(Arrays.asList(new Scanner(new File("../../../stop_words.txt")).useDelimiter("\\Z").next().split(",")));

            // read the entire file
            String input = new Scanner(new File(filePath)).useDelimiter("\\Z").next().toLowerCase();

            /* filter the word with lambda expression predicate
            */
            validWords = Arrays.stream(input.split("[^a-z]+"))
                    .filter(word -> word.length() >= 2 && !stopWords.contains(word)).toList();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return validWords;
    }
}
