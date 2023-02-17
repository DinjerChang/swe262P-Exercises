package Week4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Ten {

    //Monadic Identity


    private interface IFunction{
        Object call(Object arg);
    }

    private static class TFTheOne{
        private  Object value;
        TFTheOne(Object v){ this.value = v;}

        public TFTheOne bind(IFunction func){
            this.value = func.call(this.value);
            return this;
        }
        public void printMe(){ //debug
            System.out.println(value);
        }
    }

    public static void main(String[] args) {
        TFTheOne top25 = new TFTheOne(args);
        top25.bind(new readFile()).bind(new generateInputWords()).bind(new removeStopWords())
                .bind(new countFreq()).bind(new sortDescend()).bind(new printResult());
    }

    private static class readFile implements  IFunction{
        public Object call(Object arg){
            String [] args = (String []) arg;
            String input = null;
            if (args.length != 1) {
                System.out.println("Please provide exactly One argument");
                System.exit(1);
            }
            File inputFile = new File(args[0]);
            if (!inputFile.exists()) {
                System.out.println("File Not Found");
                System.exit(1);
            }
            try{
                Scanner inputFileReader = new Scanner(inputFile);
                inputFileReader.useDelimiter("\\Z");
                input= inputFileReader.next();
                inputFileReader.close();
            }catch (IOException e){
                System.out.println(e);
                System.exit(1);
            }
            return input;
        }
    }

    private static class generateInputWords implements IFunction {
        public Object call(Object arg){
            String[] inputWords = ((String) arg).toLowerCase().split("[^a-z]+");
            return inputWords;
        }
    }

    private static class removeStopWords implements IFunction {
        public Object call(Object arg) {
            File stopFile = new File("stop_words.txt");
            List<String> list = null;
            try {
                Scanner stopFileReader = new Scanner(stopFile);
                String[] stopWordsArray = stopFileReader.next().split(",");
                HashSet<String> stopWords = new HashSet<>(Arrays.asList(stopWordsArray));
                stopFileReader.close();
                list = new ArrayList<>(Arrays.asList((String[]) arg));

                for (String word : (String[]) arg) {
                    if (stopWords.contains(word)) {
                        list.remove(word);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println(e);
                System.exit(1);
            }
            return list;
        }
    }

    @SuppressWarnings("unchecked")
    private static class countFreq implements IFunction {
        public Object call(Object arg){
            Map<String,Integer> termFreq = new HashMap<>();
            for (String word: (List<String>)arg){
                if (word.length() >=2) {
                    termFreq.put(word, termFreq.getOrDefault(word, 0) + 1);
                }
            }
            return termFreq;
        }
    }

    @SuppressWarnings("unchecked")
    private static class sortDescend implements IFunction {

        public Object call(Object arg){
            ArrayList<Map.Entry<String, Integer>> freqList = new ArrayList<>(((HashMap<String, Integer>)arg).entrySet());
            freqList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
            return freqList;
        }
    }

    @SuppressWarnings("unchecked")
    private static class printResult implements IFunction {
        public Object call(Object arg) {
            ArrayList<Map.Entry<String,Integer>> freqMap = (ArrayList<Map.Entry<String,Integer>>)arg;
            for (int i =0; i<25; i++){
                System.out.println(freqMap.get(i).getKey()+ " - " + freqMap.get(i).getValue());
            }
            return null;
        }
    }


}
