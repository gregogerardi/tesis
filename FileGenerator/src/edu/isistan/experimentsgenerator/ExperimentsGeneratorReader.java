package edu.isistan.experimentsgenerator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExperimentsGeneratorReader {
    private BufferedReader conf;
    private String line;
    private String filepath;
    private Boolean networkEnergyManagementFlag;
    private ArrayList<String> nodeFiles = new ArrayList<>();

    public ExperimentsGeneratorReader(String filePath) {
        this.filepath = filePath;
    }

    private BufferedReader getReader(String file) throws FileNotFoundException {
        return new BufferedReader(new FileReader(file));
    }

    private void nextLine() throws IOException {
        this.line = this.conf.readLine();
        if (line == null) return;
        this.line = this.line.trim();
        while (line.startsWith("#") ||
                line.equals("")) {
            this.line = this.conf.readLine();
            if (line == null) return;
            this.line = this.line.trim();
        }
    }

    public List<String> read() throws IOException {

        this.conf = getReader(filepath);
        this.nextLine();
        while (line != null) {
            if (line.startsWith(";loadBalancing")) this.loadScheduler();
            else if (line.startsWith(";GAConfiguration")) this.loadGAConfiguration();
            else if (line.startsWith(";comparator")) this.loadComparator();
            else if (line.startsWith(";policy")) this.loadPolicy();
            else if (line.startsWith(";strategy")) this.loadStragegy();
            else if (line.startsWith(";condition")) this.loadCondition();
            else if (line.startsWith(";link")) this.loadLink();
            else if (line.startsWith(";networkEnergyManagementEnable")) this.loadNetworkEnergyManagementFlag();
            else if (line.startsWith(";devicesStatusNotification")) this.loadDeviceStatusNotificationPolicy();
            else if (line.startsWith(";nodeFile")) this.loadNodes();
            else if (line.startsWith(";batteryFile")) this.loadBatteryFile();
            else if (line.startsWith(";batteryFullCpuUsageFile")) this.loadCpuFullScreenOffBatteryFile();
            else if (line.startsWith(";batteruFullCpuScreenOnFile")) this.loadCpuFullScreenOnBatteryFile();
            else if (line.startsWith(";cpuFile")) this.loadCPUFile();
            else if (line.startsWith(";wifiSignalStrength")) this.loadWifiSignalStrength();
            else if (line.startsWith(";userActivity")) this.loadUserActivity();
            else if (line.startsWith(";connectionRelatedEventsFile")) this.loadConnectionRelatedEventsFile();
            else if (line.startsWith(";networkActivity")) this.loadNetworkActivity();
            else if (line.startsWith(";jobsEvent")) executorService.execute(this.loadJobs());
            else throw new IllegalStateException(this.line + " is not a valid parameter");
        }

        List<String> deviceNames = new ArrayList<>();

        Scanner scanner = new Scanner(this.file);
        while (scanner.hasNext()) {
            String line = scanner.nextLine().trim();

            if (line.indexOf("#") == 0 || line.length() == 0) continue;

            String[] values = line.split(";");
            deviceNames.add(values[0]);
        }
        scanner.close();
        return deviceNames;
    }

    private void loadScheduler() throws IOException {
        this.nextLine();
        while (!line.startsWith(";"))
            schedulers.add(line);
        if (nodeFiles.size()==0){
            System.out.println("at least one node file should be provided");
        }
    }

    private void loadNodes() throws IOException {
        this.nextLine();
        while (!line.startsWith(";"))
            nodeFiles.add(line);
        if (nodeFiles.size()==0){
            System.out.println("at least one node file should be provided");
        }
    }

    private void loadNetworkEnergyManagementFlag() throws IOException {
        String[] lineParts = this.line.split(" ");
        if (lineParts.length > 1) {
            networkEnergyManagementFlag = Boolean.valueOf(lineParts[1]);
        } else System.out.println("Missing networkEnergyManagementFlag");
        nextLine();
    }

}
