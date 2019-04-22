package edu.isistan.appender;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AppenderForConnectionEventsToConfFileWriter {

    private String outputFile;
    private String inputFile;
    private String folderOfConnectionEvents;

    AppenderForConnectionEventsToConfFileWriter(String outputFile, String inputFile, String folderOfConnectionEvents) {
        this.outputFile = outputFile;
        this.inputFile = inputFile;
        this.folderOfConnectionEvents = folderOfConnectionEvents;
    }

    public void generateAppendedFile() {
        File folder = new File(this.folderOfConnectionEvents);
        try (FileWriter fileWriter = new FileWriter(new File(this.outputFile));
             Scanner scanner = new Scanner(new File(this.inputFile))
        ) {
            while (scanner.hasNext()) {
                fileWriter.write(scanner.nextLine().trim() + "\n");
            }
            fileWriter.write("\n;connectionRelatedEventsFile\n");
            for (File file : folder.listFiles()) {
                String filePath = file.getAbsolutePath().replace('\\','/');
                String fileRelativePath = "sim_input" + filePath.split("sim_input")[1];
                String fileName = file.getName();
                String nodeName = fileName.substring(0, fileName.length() - 4);
                String line = fileRelativePath + "; " + nodeName + "\n";
                fileWriter.write(line);
            }
        } catch (IOException e) {

            System.out.println("An error has ocurred while appendind the message: " + folderOfConnectionEvents +
                    "\nto the file: " + inputFile +
                    "\nusing as output: " + outputFile);
        }
    }

    public static class Builder {
        private String outputFile;
        private String inputFile;
        private String folderOfConnectionEvents;

        public Builder setOutputFile(String outputFile) {
            this.outputFile = outputFile;
            return this;
        }

        public Builder setInputFile(String inputFile) {
            this.inputFile = inputFile;
            return this;
        }

        public Builder setFolderOfConnectionEvents(String folderOfConnectionEvents) {
            this.folderOfConnectionEvents = folderOfConnectionEvents;
            return this;
        }


        public AppenderForConnectionEventsToConfFileWriter build() {
            return new AppenderForConnectionEventsToConfFileWriter(outputFile, inputFile, folderOfConnectionEvents);
        }
    }
}
