package edu.isistan.jobs;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class JobGeneratorInputRelatedFromFile {
    private File file;

    public JobGeneratorInputRelatedFromFile(String filePath) {
        this.file = new File(filePath);
    }

    public void generateFiles(String outputRootPath) throws FileNotFoundException {

        Scanner scanner = new Scanner(this.file);
        while (scanner.hasNext()) {
            String line = scanner.nextLine().trim();
            if (line.indexOf("#") == 0 || line.length() == 0) continue;
            generateFile(line, outputRootPath);
        }
        scanner.close();
    }

    private void generateFile(String line, String outputRootFolder) {
        String args[] = line.split(";");
        StringBuffer output = new StringBuffer();
        int nJobs = Integer.parseInt(args[0]);
        long minTime = Long.parseLong(args[1]);
        long maxTime = Long.parseLong(args[2]);
        String opsFunction = args[3].split(":")[1];//given the parameter format ParamName:Function, instantiate the opsFunction variable with Function value
        long maxFlop = Long.parseLong(args[4]);
        int minInput = Integer.parseInt(args[5]);
        int maxInput = Integer.parseInt(args[6]);
        int minOutput = Integer.parseInt(args[7]);
        int maxOutput = Integer.parseInt(args[8]);
        output.append("#input: numberOfJobs JobsArriveMinTime(millis) JobsArriveMaxTime(millis) opsFunction:[nlogn|n_2|n_3|all] maxFlop JobsMinInput(bytes) JobsMaxInput(bytes) JobsMinOutput(bytes) JobsMaxOutput(bytes)");
        output.append("#input: " + nJobs + " " + minTime + " " + maxTime + " " + opsFunction + " " + maxFlop + " " + minInput + " " + maxInput + " " + minOutput + " " + maxOutput);
        output.append("#output: id;ops;time;input;output");
        JobGeneratorInputRelated jg = new JobGeneratorInputRelated();
        InputStream is = jg.generateJobs(nJobs, minTime, maxTime, opsFunction, maxFlop, minInput, maxInput, minOutput, maxOutput);
        OutputStream os = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        try {
            int readed = is.read(bytes);
            while (readed == 1024) {
                os.write(bytes);
                readed = is.read(bytes);
            }
            if (readed != -1) {
                os.write(bytes, 0, readed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        output.append(new String(((ByteArrayOutputStream) os).toByteArray()));
        String outputFile = outputRootFolder + "/" + nJobs + "-" + minTime + "-" + maxTime + "-" + opsFunction + "-" + maxFlop + "-" + minInput + "-" + maxInput + "-" + minOutput + "-" + maxOutput;
        File file = new File(outputFile);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(output.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        /**
         * @param args
         */
        public static void main (String[]args){
            if (args.length < 2) {
                System.out.println("input file and output directory are required");
                return;
            }

            JobGeneratorInputRelatedFromFile reader = new JobGeneratorInputRelatedFromFile(args[0]);
            try {
                reader.generateFiles(args[1]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public InputStream generateJobs ( int cant, long minTime, long maxTime, String opsFunction,long maxFlop,
        int minInput, int maxInput, int minOutput, int maxOutput){
            List<JobInformation> jobs = new ArrayList<JobInformation>(cant);
            for (int i = 0; i < cant; i++) {
                jobs.add(this.generateNewJob(i, opsFunction, maxFlop, minTime, maxTime, minInput, maxInput, minOutput, maxOutput));
            }
            Collections.sort(jobs);
            byte[] memory = this.transToMemory(jobs);
            return new ByteArrayInputStream(memory);
        }

        private JobInformation generateNewJob ( int id, String opsFunction,long maxFlop,
        long minTime, long maxTime, int minInput, int maxInput,
        int minOutput, int maxOutput){

            long time = ((long) (Math.random() * (maxTime - minTime))) + minTime;
            int input = ((int) (Math.random() * (maxInput - minInput))) + minInput;
            int output = ((int) (Math.random() * (maxOutput - minOutput))) + minOutput;

            long inputEntries = (long) (input / 1024);
            long ops = getOps(opsFunction, inputEntries);

            while (ops >= maxFlop) ops = getOps(opsFunction, inputEntries);

            return new JobInformation(time, id, ops, input, output);
        }

        private long getOps (String opsFunction,long inputEntries){

            if (opsFunction.compareTo("none") == 0)
                return 0;
            if (opsFunction.compareTo("fixed") == 0)
                return 83629400;
            if (opsFunction.compareTo("nlogn") == 0)
                return (long) ((long) Math.log(inputEntries) * inputEntries);

            if (opsFunction.startsWith("n_")) {
                String[] opsFunctionComponents = opsFunction.split("_");
                int exp = Integer.parseInt(opsFunctionComponents[1]);
                return (long) ((long) Math.pow(inputEntries, exp));
            }

            if (opsFunction.compareTo("all") == 0) {
                int function = ((int) (Math.random() * 10000000)) % 3;
                switch (function) {
                    case 0:
                        return (long) ((long) Math.log(inputEntries) * inputEntries);
                    case 1:
                        return (long) ((long) Math.pow(inputEntries, 2));
                    case 2:
                        return (long) ((long) Math.pow(inputEntries, 3));
                }
            }
            return -1;
        }

        private byte[] transToMemory (List < JobInformation > jobs) {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(bo));
            try {
                while (jobs.size() > 0) {
                    writer.write(jobs.remove(0).toString() + "\n");
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return bo.toByteArray();
        }

    }
