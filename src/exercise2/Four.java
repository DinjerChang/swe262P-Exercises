package exercise2;


import java.io.File;
import java.io.FileNotFoundException;

import java.util.*;

public class Four {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide exactly One argument");
        }
        try {
            File stopFile = new File("stop_words.txt");
            Scanner stopReader = new Scanner(stopFile);

            HashSet<String> stopWords = new HashSet<>();
            String stopWord;

            while (stopReader.hasNextLine()) {
                String line = stopReader.nextLine();

                int startIndex = -1;
                int i = 0;

                for (char ch : line.toLowerCase().toCharArray()) {
                    if (startIndex == -1) {
                        // find the start index of the word
                        if (Character.isAlphabetic(ch)) {
                            startIndex = i;
                        }
                    } else {
                        // find the last index of the word
                        if (!Character.isAlphabetic(ch)) {
                            stopWord = line.substring(startIndex, i);
                            stopWords.add(stopWord);
                            startIndex = -1;
                        } else {
                            // the last character in line is an alphabet
                            if (i == line.length() - 1) {
                                stopWord = line.substring(startIndex);
                                stopWords.add(stopWord);
                                startIndex = -1;
                            }
                        }
                    }
                    i++;
                }
            }
            stopReader.close();

            File inputFile = new File(args[0]);
            Scanner inputReader = new Scanner(inputFile);

            Map<String, Integer> termFreq = new HashMap<>();
            String word;
            while (inputReader.hasNext()) {
                String line = inputReader.nextLine().toLowerCase();

                int startIndex = -1;
                int i = 0;
                for (char ch : line.toCharArray()) {
                    if (startIndex == -1) {
                        //find the start index of the word
                        if (Character.isAlphabetic(ch)) {
                            startIndex = i;
                        }
                    } else {

                        // find the end index if the word
                        if (!Character.isAlphabetic(ch)) {

                            word = line.substring(startIndex, i);

                            if (!stopWords.contains(word) && word.length() >= 2) {
                                termFreq.put(word, termFreq.getOrDefault(word, 0)+1);
                            }
                            startIndex = -1;
                        }else{
                            // the las character in line is an alphabet
                            if (i == line.length()-1){
                                word = line.substring(startIndex);
                                if (!stopWords.contains(word) && word.length() >= 2) {
                                    termFreq.put(word, termFreq.getOrDefault(word, 0)+1);
                                }
                                startIndex = -1;
                            }
                        }

                    }
                    i++;
                }

            }
            inputReader.close();

            List<Map.Entry<String, Integer>> freqList = new ArrayList<>(termFreq.entrySet());
            // sorting
            for (int i = 0; i < freqList.size() - 1; i++) {
                for (int j = 0; j < freqList.size() - i - 1; j++) {
                    // reverse order
                    if (freqList.get(j).getValue() < freqList.get(j + 1).getValue()) {
                        // Swap the value
                        Map.Entry<String, Integer> temp = freqList.get(j);
                        freqList.set(j, freqList.get(j + 1));
                        freqList.set(j + 1, temp);
                    }
                }
            }
            freqList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
            for (int i=0; i <25; i++){
                System.out.println(freqList.get(i).getKey() + " - " + freqList.get(i).getValue());
            }


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

    }
}
