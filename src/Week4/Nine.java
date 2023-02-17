package Week4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Nine {
    // continuation passing style
    // using interface

    private interface IFunction{
        void call(Object arg, IFunction func);
    }

    public static void readFile(String[] args, IFunction func) throws FileNotFoundException {
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
        inputFileReader.useDelimiter("\\Z");
        String input = inputFileReader.next();
        inputFileReader.close();

        func.call(input,new removeStopWords());
    }

    private static class generateInputWords implements IFunction{
          public void call(Object arg, IFunction func){
            String[] inputWords = ((String) arg).toLowerCase().split("[^a-z]+");
            func.call(inputWords, new countFreq());
        }
    }

    private static class removeStopWords implements IFunction{
        public void call(Object arg, IFunction func) {
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

            func.call(list, new sortDescend());
        }
    }

    @SuppressWarnings("unchecked")
    private static class countFreq implements IFunction{
        public void call(Object arg, IFunction func){
            Map<String,Integer> termFreq = new HashMap<>();
            for (String word: (List<String>)arg){
                if (word.length() >=2) {
                    termFreq.put(word, termFreq.getOrDefault(word, 0) + 1);
                }
            }
            func.call(termFreq, new printResult());
        }
    }


    @SuppressWarnings("unchecked")
    private static class sortDescend implements IFunction{
        public void call(Object arg, IFunction func){
            ArrayList<Map.Entry<String, Integer>> freqList = new ArrayList<>(((HashMap<String, Integer>)arg).entrySet());
            freqList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
            func.call(freqList, new noOperate());
        }
    }

    @SuppressWarnings("unchecked")
    private static class printResult implements IFunction {
        public void call(Object arg, IFunction func) {
            ArrayList<Map.Entry<String,Integer>> freqMap = (ArrayList<Map.Entry<String,Integer>>)arg;
            for (int i =0; i<25; i++){
                System.out.println(freqMap.get(i).getKey()+ " - " + freqMap.get(i).getValue());
            }
        }
    }

    private static class noOperate implements IFunction{
        public void call(Object arg, IFunction func) {
        }
    }

    public static void main(String[] args) throws IOException {

        readFile( args,new generateInputWords());
    }
}
