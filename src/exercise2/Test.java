package exercise2;

import java.io.*;
import java.util.*;

public class Test {
    // the global list of [word, frequency] pairs
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide exactly One argument");
        }
        System.out.println(Character.isAlphabetic('\n'));

        try {
            File stopFile = new File("stop_words.txt");
            BufferedReader reader = new BufferedReader(new FileReader("stop_words.txt"));
            HashSet<String> stopWords = new HashSet<>();
            String stopWord;
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {

                for (char ch : line.toLowerCase().toCharArray()) {
                    if (stringBuilder.isEmpty()) {
                        // find the start index of the word
                        if (Character.isAlphabetic(ch)) {
                            stringBuilder.append(ch);
                        }
                    } else {
                        if (Character.isAlphabetic(ch)) {
                            stringBuilder.append(ch);
                        } else {
                            stopWord = stringBuilder.toString();
                            stopWords.add(stopWord);
                            stringBuilder.setLength(0);
                        }
                    }
                }
            }

//            for (char c = 'a'; c < 'z'; c++) {
//                stopWords.add(Character.toString(c));
//            }
            System.out.println(stopWords);
            System.out.println(stopWords.size());


            File inputFile = new File(args[0]);
            Scanner inputReader = new Scanner(inputFile);
            Map<String, Integer> termFreq = new HashMap<>();
            String inputWord;
            stringBuilder.setLength(0);
            int total = 0;
            while (inputReader.hasNextLine()) {
                line = inputReader.next();

//                int startIndex = -1;
//                int i = 0;
//                for (char ch : line.toCharArray()) {
//                    if (startIndex == -1) {
//                        //find the start index of the word
//                        if (Character.isAlphabetic(ch)) {
//                            startIndex = i;
//                        }
//                    } else {
//                        // find the end index if the word
//                        if (!Character.isAlphabetic(ch)) {
//                            word = line.substring(startIndex, i);
//
//                            if (!stopWords.contains(word) && word.length() >= 2) {
//                                termFreq.put(word, termFreq.getOrDefault(word, 0)+1);
//                            }
//                            startIndex = -1;
//                        }
//
//                    }
//                    i++;
//                }
                for (char ch : line.toLowerCase().toCharArray()) {
                    //find the start index of the word
                    if (Character.isAlphabetic(ch)) {
                        stringBuilder.append(ch);

                    } else {
//                        if (!stringBuilder.isEmpty()) {

                            // find the end index if the word
                            inputWord = stringBuilder.toString();

                            if (!stopWords.contains(inputWord) && inputWord.length() >= 2) {
                                termFreq.put(inputWord, termFreq.getOrDefault(inputWord, 0) + 1);
                            }
                            stringBuilder.setLength(0);
//                        }

                    }

                }

            }
            reader.close();

            List<Map.Entry<String, Integer>> freqList = new ArrayList<>(termFreq.entrySet());
            freqList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // reverse order
            for (int i = 0; i < 25; i++) {
                System.out.println(freqList.get(i).getKey() + " - " + freqList.get(i).getValue());
            }
            System.out.println(total);

        } catch (IOException e) {
            System.out.println("File not found");
        }

    }
}