package io.grpc.filesystem.test;

import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/*
 * Using this file you can check your output against the expected output file. For both the tasks 2 and 3 the answers are expected to be the same.
 * Please use the this file to compare your output against the expected output available at output/output.txt.
 * It's possible that the word arrangement with the same count in the text file provides incorrect results.
 * Please go over your complete output and try to rectify it.
 */

public class CheckOutput {
    public static void main(String[] args) throws IOException{
        String outputFilePath = args[0];
        String l1;
        String l2;

        // Read and map the original file
        Map<String, Integer> original = new HashMap<>();
        BufferedReader bf1 = new BufferedReader(new FileReader("output/output.txt"));
        while((l1=bf1.readLine()) != null) {
            if(!l1.isEmpty()) {
                String[] subStrings = l1.split(":");
                original.put(subStrings[0], Integer.parseInt(subStrings[1]));
            }
        }

        // Read and map the file that needs to be compared
        Map<String, Integer> copy = new HashMap<>();
        BufferedReader bf2 = new BufferedReader(new FileReader(outputFilePath));
        while((l2=bf2.readLine()) != null) {
            if(!l2.isEmpty()) {
                String[] subStrings = l2.split(":");
                copy.put(subStrings[0], Integer.parseInt(subStrings[1]));
            }
        }

        // Print the verdict
        if(original.equals(copy)){
            System.out.println("Both files are the same!");
        }else{
            System.out.println("Both files are NOT the same!");
        }

        bf1.close();
        bf2.close();

    }
}
