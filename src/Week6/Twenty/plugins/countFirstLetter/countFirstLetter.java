import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class countFirstLetter implements ICount{
    @Override
    public Map<String, Integer> count(List<String> validWords){
        Map <String, Integer> termFreq = new HashMap<>();
        for (String word: validWords){
            String firstLetter = String.valueOf(word.charAt(0));
            termFreq.put(firstLetter,termFreq.getOrDefault(firstLetter,0)+1);
        }
        return termFreq;
    }
}
