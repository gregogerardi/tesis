package edu.isistan.appender;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AppenderForLineWriter {

    private String outputFile;
    private String inputFile;
    private String messajeToAppend;

    AppenderForLineWriter(String outputFile, String inputFile, String messajeToAppend) {
        this.outputFile = outputFile;
        this.inputFile = inputFile;
        this.messajeToAppend = messajeToAppend;
    }

    public void generateAppendedFile() {
        try (FileWriter fileWriter = new FileWriter(new File(this.outputFile));
             Scanner scanner = new Scanner(new File(this.inputFile))
        ) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();
                if (line.indexOf("#") == 0 || line.length() == 0) continue;
                line += messajeToAppend + "\n";
                fileWriter.write(line);
            }
        } catch (IOException e) {
            System.out.println("An error has ocurred while appendind the message: " + messajeToAppend +
                    "\nto the file: " + inputFile +
                    "\nusing as output: " + outputFile);
        }
    }

    public static class Builder {
        private String outputFile;
        private String inputFile;
        private String messajeToAppend;

        public Builder setOutputFile(String outputFile) {
            this.outputFile = outputFile;
            return this;
        }

        public Builder setInputFile(String inputFile) {
            this.inputFile = inputFile;
            return this;
        }

        public Builder setMessajeToAppend(String messajeToAppend) {
            this.messajeToAppend = messajeToAppend;
            return this;
        }


        public AppenderForLineWriter build() {
            return new AppenderForLineWriter(outputFile, inputFile, messajeToAppend);
        }
    }
}
