package Week4;


import java.io.*;
import java.util.*;
import java.util.function.*;


/*
Pub-Sub Style

run -> load & start -> word -> validWord -> eof -> stop -> printResult
 */

public class Sixteen {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Please provide exactly One argument");
            System.exit(1);
        }

        EventManager em = new EventManager();
        new DataStorage(em);
        new StopWordFilter(em);
        new TermFrequencyCounter(em);
        new TermFrequencyApplication(em);
        em.publish(new String[]{"run", args[0]});
    }

    /*
    The event management substrate
    */

    private static class EventManager {
        private static HashMap<String, ArrayList<Consumer<String[]>>> subs = new HashMap<>();

        // subscribe an event and the corresponding handler function
        private static void subscribe(String eventType, Consumer<String[]> handler) {
            if (subs.containsKey(eventType)) {
                subs.get(eventType).add(handler);
            } else {
                subs.put(eventType, new ArrayList<>(Arrays.asList(handler)));
            }

        }

        // publish an event and execute the handler function
        private static void publish(String[] event) {
            String event_type = event[0];
            if (subs.containsKey(event_type)) {
                for (Consumer<String[]> h : subs.get(event_type)) {
                    // accept takes in the parameter
                    h.accept(event);
                }
            }
        }
    }

    private static class DataStorage {
        private static EventManager eventManager;
        private static List<String> inputWords;

        DataStorage(EventManager em) {
            eventManager = em;
            eventManager.subscribe("load", DataStorage::load);
            eventManager.subscribe("start", DataStorage::generateWords);
        }

        // split the file and save all the words in terms list
        private static void load(String[] event) {
            String filePath = event[1];
            // read inputFile
            try {
                Scanner inputFileReader = new Scanner(new File(filePath));
                inputFileReader.useDelimiter("\\Z");
                String input = inputFileReader.next().toLowerCase();
                inputFileReader.close();
                inputWords = Arrays.asList(input.split("[^a-z]+"));

            } catch (IOException e) {
                System.out.println(e);
                System.exit(1);
            }
        }

        //publish each term of the terms list -> check whether it is a stop word,
        private static void generateWords(String[] event) {
            for (String word : inputWords) {
                eventManager.publish(new String[]{"word", word});
            }
            eventManager.publish(new String[]{"eof"});
        }
    }

    private static class StopWordFilter {
        private static EventManager eventManager;
        private static HashSet<String> stopWords;

        StopWordFilter(EventManager em) {
            eventManager = em;
            eventManager.subscribe("load", (String[] event) -> load());
            eventManager.subscribe("word", StopWordFilter::validWord);
        }

        // split the file and save all the stop words in stop_words list
        private static void load() {
            File stopFile = new File("stop_words.txt");
            try {
                Scanner stopFileReader = new Scanner(stopFile);
                String[] stopWordsArray = stopFileReader.next().split(",");
                stopWords = new HashSet<>(Arrays.asList(stopWordsArray));
                stopFileReader.close();
            } catch (IOException e) {
                System.out.println(e);
                System.exit(1);
            }
        }

        // check if the word is valid
        private static void validWord(String[] event) {
            String word = event[1];
            if (!stopWords.contains(word) && word.length() >= 2) {
                eventManager.publish(new String[]{"validWord", word});
            }
        }
    }

    private static class TermFrequencyCounter {
        private static EventManager eventManager;
        private static HashMap<String, Integer> termFreq = new HashMap<>();

        TermFrequencyCounter(EventManager em) {
            eventManager = em;
            eventManager.subscribe("validWord", TermFrequencyCounter::count);
            eventManager.subscribe("print", (String[] event) -> printResult());
        }

        // if the word is valid, update the map
        private static void count(String[] event) {
            String word = event[1];
            termFreq.put(word, termFreq.getOrDefault(word, 0) + 1);
        }


        private static void printResult() {
            ArrayList<Map.Entry<String, Integer>> freqList = new ArrayList<>(termFreq.entrySet());
            freqList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
            for (int i = 0; i < 25; i++) {
                System.out.println(freqList.get(i).getKey() + " - " + freqList.get(i).getValue());
            }
        }
    }

    private static class TermFrequencyApplication {
        private static EventManager eventManager;

        TermFrequencyApplication(EventManager em) {
            eventManager = em;
            eventManager.subscribe("run", TermFrequencyApplication::run);
            eventManager.subscribe("eof", TermFrequencyApplication::stop);
        }

        private static void run(String[] event) {
            String filePath = event[1];
            eventManager.publish(new String[]{"load", filePath});
            eventManager.publish(new String[]{"start"});
        }

        private static void stop(String[] event) {
            eventManager.publish(new String[]{"print"});
        }
    }

}