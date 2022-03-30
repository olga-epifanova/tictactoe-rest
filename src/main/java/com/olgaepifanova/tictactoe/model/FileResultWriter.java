package com.olgaepifanova.tictactoe.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileResultWriter {

    private static final String FILE_NAME = "rating.txt";

    public static void writeFile(String text) {

        try (FileWriter writer = new FileWriter(FILE_NAME, true); BufferedWriter bufferWriter = new BufferedWriter(writer)) {
            bufferWriter.write(text + "\n");
        } catch (IOException e) {
            System.out.println(e);
        }

    }

}
