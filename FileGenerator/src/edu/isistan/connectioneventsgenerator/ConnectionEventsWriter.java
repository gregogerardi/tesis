package edu.isistan.connectioneventsgenerator;

import edu.isistan.connectioneventsgenerator.intervalcalculators.IntervalCalculator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConnectionEventsWriter {

    private String outputFile;
    private long maxTime;
    private IntervalCalculator intervalCalculator;

    ConnectionEventsWriter(String outputFile, long maxTime, IntervalCalculator intervalCalculator) {
        this.outputFile = outputFile;
        this.maxTime = maxTime;
        this.intervalCalculator = intervalCalculator;
    }

    public void generateConfigurationFile() throws IOException {
        File file = new File(this.outputFile);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);

            long time = 0;

            while (time < this.maxTime) {
                long timestamp = time + intervalCalculator.getNewInterval();
                time = timestamp;

                if (time > maxTime || time < 0) break;

                writeLine(fileWriter, timestamp, intervalCalculator.getTypeOfNextEvent());
            }
        } catch (IOException e) {
            file.delete();
            throw e;
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }

    private void writeLine(FileWriter writer, long timestamp, IntervalCalculator.TypeOfNextEvent typeOfNextEvent) throws IOException {
        switch (typeOfNextEvent) {
            case DISCONNECTION:
                writer.write("ENTER_NETWORK;" + timestamp + ";\n");
                break;
            case CONNECTION:
                writer.write("LEAVE_NETWORK;" + timestamp + ";\n");
                break;
        }
    }


    public static class Builder {
        private String outputFile;
        private long maxTime;
        private IntervalCalculator intervalCalculator;

        public Builder setOutputFile(String outputFile) {
            this.outputFile = outputFile;
            return this;
        }

        public Builder setMaxTime(long maxTime) {
            this.maxTime = maxTime;
            return this;
        }

        public Builder setIntervalCalculator(IntervalCalculator intervalCalculator) {
            this.intervalCalculator = intervalCalculator;
            return this;
        }


        public ConnectionEventsWriter build() {
            return new ConnectionEventsWriter(outputFile, maxTime, intervalCalculator);
        }
    }
}
