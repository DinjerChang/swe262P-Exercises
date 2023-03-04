package Week8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// Tuple Space Thread => Consumer & Producer
public class Thirty {

    // two share data space
    private static BlockingQueue<String> wordSpace = new LinkedBlockingQueue<>();
    private static BlockingQueue<HashMap<String, Integer>> freqSpace = new LinkedBlockingQueue<>();

    private static Set<String> stopWords;


    public static void main(String[] args) throws IOException {
        // generate stop words
        stopWords = new HashSet<>(List.of((new String(
                Files.readAllBytes(Paths.get("stop_words.txt")))
                .toLowerCase().split(","))));

        // produce word space
        Arrays.stream(new String(Files.readAllBytes(Paths.get(args[0])))
                        .toLowerCase().split("[^a-z]+"))
                .filter(word -> word.length() >= 2).forEach(
                        word -> wordSpace.add(word));

        // initialize five workers
        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            workers.add(new Thread(Thirty::processWords));
        }
        // workers start working
        workers.forEach(thread -> thread.start());

        // wait until the worker finish the job
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        Map<String, Integer> termFreq = new HashMap<>();
        while (!freqSpace.isEmpty()) {
            HashMap<String, Integer> partialFreq = freqSpace.poll();
            partialFreq.forEach((k, v) -> termFreq.merge(k, v, Integer::sum));
        }

        termFreq.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue())
                );
    }

    /*
     Worker function that consumes words from word space
     and sends partial results to freq space
     consume the data from wordSpace
     */
    private static void processWords() {
        HashMap<String, Integer> partialWordFreq = new HashMap<>();
        while (true) {
            try {
                String word = wordSpace.poll(1, TimeUnit.SECONDS);
                if (word == null) {
                    break;
                }
                if (!stopWords.contains(word)) {
                    partialWordFreq.put(word, partialWordFreq.getOrDefault(word, 0) + 1);
                }
            } catch (InterruptedException e) {
                System.out.println(e);
                Thread.currentThread().interrupt();
                System.exit(1);
            }
        }
        freqSpace.add(partialWordFreq);
    }
}
