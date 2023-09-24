/*
 * the MapReduce functionality implemeted in this program takes a single large text file to map i.e. split it into small chunks and then assign 1 to all the found words
 * then reduces by adding count values to each unique words
 * To build: ./gradlew build
 * To run: ./gradlew run -PchooseMain=io.grpc.filesystem.task2.MapReduce --args="input/pigs.txt output/output-task2.txt"
 */

package io.grpc.filesystem.task2;

import java.util.stream.Collectors;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Map;
import java.util.Timer;

import io.grpc.filesystem.task2.Mapper;

public class MapReduce {

    public static String makeChunks(String inputFilePath) throws IOException {
        int count = 1;
        int size = 500;
        File f = new File(inputFilePath);
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String l = br.readLine();

            while (l != null) {
                File newFile = new File(f.getParent() + "/temp", "chunk"
                        + String.format("%03d", count++) + ".txt");
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile))) {
                    int fileSize = 0;
                    while (l != null) {
                        byte[] bytes = (l + System.lineSeparator()).getBytes(Charset.defaultCharset());
                        if (fileSize + bytes.length > size)
                            break;
                        out.write(bytes);
                        fileSize += bytes.length;
                        l = br.readLine();
                    }
                }
            }
        }
        return f.getParent() + "/temp";

    }

    /**
     * @param inputfilepath
     * @throws IOException
     */
    public static void map(String inputfilepath) throws IOException {

        /*
         * Insert your code here
         * Take a chunk and filter words (you could use "\\p{Punct}" for filtering punctuations and "^[a-zA-Z0-9]"
         * together for filtering the words), then split the sentences to take out words and assign "1" as the initial count.
         * Use the given mapper class to create the unsorted key-value pair.
         * Save the map output in a file named "map-chunk001", for example, in folder
         * path input/temp/map
         */
        File inputFile = new File(inputfilepath);
        // create tempmap folder
        String tempMapFolder = inputFile.getParentFile().getParent() + "/temp/map";
        new File(tempMapFolder).mkdirs();
        String tempMapFileName = "map-" + inputFile.getName();
        File tempMapFile = new File(tempMapFolder, tempMapFileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempMapFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // split line into words
                String[] words = line.split("\\s+"); // \\s+ --> regex for all whitespaces
                for (String word : words) {
                    // remove punctuation and convert to lowercase
                    word = word.replaceAll("\\p{Punct}", " ").toLowerCase();
                    if (word.matches("^[a-zA-Z0-9]*$") && !word.isEmpty()) {
                        writer.write(word + ":1\n");
                    }
                }
            }
        }

    }

    /**
     * @param inputfilepath
     * @param outputfilepath
     * @return
     * @throws IOException
     */
    public static void reduce(String inputfilepath, String outputfilepath) throws IOException {

        /*
         * Insert your code here
         * Take all the files in the map folder and reduce them to one file that shows
         * unique words with their counts as "the:64", for example.
         * Save the output of reduce function as output-task2.txt
         */
        Map<String, Integer> wordCounts = new HashMap<>();
        File inputFolder = new File(inputfilepath + "/map");

        for (File file : inputFolder.listFiles()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    String word = parts[0];
                    int count = Integer.parseInt(parts[1]);
                    wordCounts.put(word, wordCounts.getOrDefault(word, 1) + count);
                }
            }
        }

        List<Entry<String, Integer>> sortedEntries = new ArrayList<>(wordCounts.entrySet());
        sortedEntries.sort(Entry.<String, Integer>comparingByValue().reversed());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputfilepath))) {
            for (Entry<String, Integer> entry : sortedEntries) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        }

    }

    /**
     * Takes a text file as an input and returns counts of each word in a text file
     * "output-task2.txt"
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException { // update the main function if required
        String inputFilePath = args[0];
        String outputFilePath = args[1];
        String chunkpath = makeChunks(inputFilePath);
        File dir = new File(chunkpath);
        File[] directoyListing = dir.listFiles();
        if (directoyListing != null) {
            for (File f : directoyListing) {
                if (f.isFile()) {

                    map(f.getPath());

                }

            }

            reduce(chunkpath, outputFilePath);

        }

    }
}