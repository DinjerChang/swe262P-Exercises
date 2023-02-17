
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class countNormal implements ICount {

    public Map<String,Integer> count (List<String> validWords){
        Map<String,Integer> termFreq = new HashMap<>();
        for (String word: validWords){
            termFreq.put(word,termFreq.getOrDefault(word,0) + 1);
        }
        return termFreq;
    }
}
