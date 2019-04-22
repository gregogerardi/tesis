package edu.isistan;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class NodeFileReader {
    private File file;

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public NodeFileReader(String filePath) {
        this.file = new File(filePath);
    }

    public List<String> read() throws FileNotFoundException {
        ArrayList<String> devices = new ArrayList<>();
        Scanner scanner = new Scanner(this.file);
        while (scanner.hasNext()) {
            String line = scanner.nextLine().trim();

            if (line.indexOf("#") == 0 || line.length() == 0) continue;

            String[] values = line.split(";");
            devices.add(values[0]);
        }
        scanner.close();
        return devices;
    }

    public HashMap<String, String> readDirectory() throws FileNotFoundException {
        return readDirectory(file, "");
    }

    private HashMap<String, String> readDirectory(File file, String outputDirectory) throws FileNotFoundException {
        if (!file.isDirectory()) {
            Scanner scanner = new Scanner(this.file);
            HashMap<String, String> result = new HashMap();
            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();

                if (line.indexOf("#") == 0 || line.length() == 0) continue;

                String[] values = line.split(";");
                result.put(outputDirectory + "/" + values[0] + ".cnf", values[0]);
            }
            scanner.close();
            return result;
        } else {
            HashMap<String, String> results = new HashMap<>();
            for (File f : file.listFiles()) {
                results.putAll(readDirectory(f, outputDirectory + "/" + f.getName()));
            }
            return results;
        }
    }
}