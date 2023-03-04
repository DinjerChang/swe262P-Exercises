package Week8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

//Actor Model Thread: sending message around Manager

public class TwentyNine {

    public static void main(String[] args) throws InterruptedException {
        WordFrequencyManager wordFrequencyManager = new WordFrequencyManager();
        StopWordManager stopWordManager = new StopWordManager();
        // stopWordManager actor will send filtered word to wordFrequencyManager
        send(stopWordManager, new Object[]{"init", wordFrequencyManager});

        DataStorageManager dataStorageManager = new DataStorageManager();
        // dataStorageManager actor will send input word to stopWordManager
        send(dataStorageManager, new Object[]{"init", args[0], stopWordManager});

        WordFrequencyController wordFrequencyController = new WordFrequencyController();
        send(wordFrequencyController, new Object[]{"run", dataStorageManager});

        // Wait for the active objects to finish
        wordFrequencyManager.join();
        stopWordManager.join();
        dataStorageManager.join();
        wordFrequencyController.join();
    }

    private static void send(ActiveWFObject receiver, Object[] message) {
        receiver.queue.add(message);
    }

    public static class DataStorageManager extends ActiveWFObject {
        private List<String> data = new ArrayList<>();
        private StopWordManager stopWordManager;

        @Override
        public void dispatch(Object[] message) throws IOException {
            if (message[0].equals("init")) {
                init(message);
            } else if (message[0].equals("send_word_freqs")) {
                processWords(message);
            } else {
                send(stopWordManager, message);
            }
        }

        private void init(Object[] message) throws IOException {
            String filePath = (String) message[1];
            stopWordManager = (StopWordManager) message[2];
            data = Arrays.stream(new String(Files.readAllBytes(Paths.get(filePath))).toLowerCase()
                    .split("[^a-z]+")).filter(word -> word.length() >= 2).collect(Collectors.toList());
        }

        private void processWords(Object[] message) {
            ActiveWFObject recipient = (ActiveWFObject) message[1];
            for (String word : data) {
//                System.out.println("unfiltered: " + word);
                send(stopWordManager, new Object[]{"filter", word});
            }
            send(stopWordManager, new Object[]{"top25", recipient});
        }
    }

    static class StopWordManager extends ActiveWFObject {
        List<String> stopWords = new ArrayList<>();
        private WordFrequencyManager wordFrequencyManager;

        @Override
        public void dispatch(Object[] message) throws IOException {
            if (message[0].equals("init")) {
                init(message);
            } else if (message[0].equals("filter")) {
                filter(message);
            } else {
                // forward top25
                send(wordFrequencyManager, message);
            }
        }

        private void init(Object[] message) throws IOException {
            wordFrequencyManager = (WordFrequencyManager) message[1];
            stopWords = List.of(new String(Files.readAllBytes(Paths.get("stop_words.txt"))).split(","));
        }

        private void filter(Object[] message) throws IOException {
            String word = (String) message[1];
            if (!stopWords.contains(word)) {
                send(wordFrequencyManager, new Object[]{"word", word});
            }
        }
    }

    static class WordFrequencyManager extends ActiveWFObject {
        private HashMap<String, Integer> termFreq = new HashMap<>();

        @Override
        public void dispatch(Object[] message) throws IOException {
            if (message[0].equals("word")) {
                incrementCount(message);
            } else if (message[0].equals("top25")) {
                top25(message);
            }
        }

        private void incrementCount(Object[] message) {
            String filteredWord = (String) message[1];
            termFreq.put(filteredWord, termFreq.getOrDefault(filteredWord, 0) + 1);
        }

        private void top25(Object[] message) {
            WordFrequencyController recipient = (WordFrequencyController) message[1];
            List<Map.Entry<String, Integer>> termFreqList = termFreq.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(25)
                    .collect(Collectors.toList());

            send(recipient, new Object[]{"top25", termFreqList});
        }
    }

    static class WordFrequencyController extends ActiveWFObject {
        private DataStorageManager dataStorageManager;

        @Override
        public void dispatch(Object[] message) throws Exception {
            if (message[0].equals("run")) {
                run(message);
            } else if (message[0].equals("top25")) {
                display(message);
            } else {
                throw new Exception("Message not understood " + message[0]);
            }
        }

        private void run(Object[] message) {
            dataStorageManager = (DataStorageManager) message[1];
            send(dataStorageManager, new Object[]{"send_word_freqs", this});
        }

        private void display(Object[] message) {
            List<Map.Entry<String, Integer>> termFreqList = (ArrayList<Map.Entry<String, Integer>>) message[1];
            for (Map.Entry<String, Integer> entry : termFreqList) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
            send(dataStorageManager, new Object[]{"die"});
            stopMe = true;
        }
    }
}

abstract class ActiveWFObject extends Thread {
    public Boolean stopMe = false;
    public BlockingQueue<Object[]> queue = new LinkedBlockingQueue<>();

    public ActiveWFObject() {
        start();
    }

    public void run() {
        while (!stopMe) {
            Object[] message = queue.poll();
            try {
                if (message != null) {
                    dispatch(message);
                    if (message[0].equals("die")) {
                        stopMe = true;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

        }
    }
    public abstract void dispatch(Object[] message) throws Exception;
}