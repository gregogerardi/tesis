package edu.isistan.experimentsgenerator;

import edu.isistan.NodeFileReader;
import edu.isistan.connectioneventsgenerator.intervalcalculators.IntervalCalculator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ExperimentsGenerator {
    private enum FLAGS {
        NODES_INPUTS("-nodes-inputs", true, new ArgumentHandler() {
            @Override
            public void handleArguments(String... args) {
                nodesInputs = args;
            }
        }),
        LOAD_BALANCINGS("-load-balancing", true, new ArgumentHandler() {
            @Override
            public void handleArguments(String... args) {
                loadBalancings = args;
            }
        }),
        COMPARATORS("-comparators", true, new ArgumentHandler() {
            @Override
            public void handleArguments(String... args) {
                comparatorsFolder = parseLongArgument(args[0]);
            }
        }),
        INTERVARL_CALCULATOR("-ic", 2, true, new ArgumentHandler() {
            @Override
            public void handleArguments(String... args) {
                try {
                    intervalCalculatorClass = (Class<IntervalCalculator>) Class.forName(args[0]);
                    intervalCalculatorArgs = args[1];
                } catch (ClassNotFoundException e) {
                    System.out.println("Class for Interval calculator not found");
                }
            }
        });

        private String flag;
        private int numberOfArguments;
        private boolean required;
        private ArgumentHandler argumentHandler;
        private boolean argumentHandled;

        FLAGS(String flag, int numberOfArguments, boolean required, ArgumentHandler argumentHandler) {
            this.flag = flag;
            this.numberOfArguments = numberOfArguments;
            this.required = required;
            this.argumentHandler = argumentHandler;
        }

        public void parseArguments(String... args) {
            this.argumentHandler.handleArguments(args);
            argumentHandled = true;
        }
    }

    private interface ArgumentHandler {
        void handleArguments(String... args);
    }

    private static String nodesInputFolder;
    private static String loadBalancing;
    private static long comparatorsFolder;
    private static String intervalCalculatorArgs;
    private static Class<IntervalCalculator> intervalCalculatorClass;


    public static void main(String[] args) {
        parseArguments(args);

        try {
            if (nodesInputFolder == null) {
                ExperimentsWriter writer = new ExperimentsWriter.Builder()
                        .setOutputFile(loadBalancing)
                        .setMaxTime(comparatorsFolder)
                        .setIntervalCalculator(intervalCalculatorClass.getConstructor(intervalCalculatorArgs.getClass()).newInstance(intervalCalculatorArgs))
                        .build();

                writer.generateConfigurationFile();

            } else {
                File outputDirectory = new File(loadBalancing);
                if (outputDirectory.exists()) {
                    File[] files = outputDirectory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            file.delete();
                        }
                    }
                    outputDirectory.delete();
                }
                outputDirectory.mkdir();

                NodeFileReader reader = new NodeFileReader(nodesInputFolder);
                List<String> devices = reader.read();
                for (String device : devices) {
                    String output = loadBalancing + "/" + device + ".cnf";

                    ExperimentsWriter writer = new ExperimentsWriter.Builder()
                            .setOutputFile(output)
                            .setMaxTime(comparatorsFolder)
                            .setIntervalCalculator(intervalCalculatorClass.getConstructor(intervalCalculatorArgs.getClass()).newInstance(intervalCalculatorArgs))
                            .build();

                    writer.generateConfigurationFile();

                    System.out.println(output + ";" + device);

                }
            }
        } catch (IOException e) {
            System.out.println("Error writing output file. Aborting program.");
            e.printStackTrace();
            System.exit(1);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.out.println("Class for Interval calculator not instanciated");
        }
    }

    private static void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String argument = args[i];
            boolean argumentParsed = false;
            for (FLAGS flag : FLAGS.values()) {
                if (flag.flag.equals(argument)) {
                    if (flag.argumentHandled) {
                        throw new IllegalArgumentException("Unexpected duplicate argument " + flag.flag + ".");
                    }

                    if (args.length <= i + flag.numberOfArguments) {
                        throw new IllegalArgumentException("Insufficient number of arguments for flag " + flag.flag +
                                ". Expected " + flag.numberOfArguments + ", got " + (args.length - i - 1) + ".");
                    }

                    String[] flagArguments = new String[flag.numberOfArguments];
                    for (int j = 0; j < flag.numberOfArguments; j++) {
                        flagArguments[j] = args[i + j + 1];
                    }

                    flag.parseArguments(flagArguments);
                    i += flag.numberOfArguments;
                    argumentParsed = true;

                    break;
                }
            }

            if (!argumentParsed) {
                throw new IllegalArgumentException("Unrecognized flag " + argument + ".");
            }
        }

        for (FLAGS flag : FLAGS.values()) {
            if (flag.required && !flag.argumentHandled) {
                throw new IllegalArgumentException("Missing required argument " + flag.flag + ".");
            }
        }
    }

    private static long parseLongArgument(String arg) {
        long value = 0;
        try {
            value = Long.parseLong(arg);
        } catch (NumberFormatException e) {
            System.out.println("Could not parse argument " + arg + " into long. Aborting program.");
            System.exit(1);
        }

        return value;
    }

    private static int parseIntArgument(String arg) {
        int value = 0;
        try {
            value = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.out.println("Could not parse argument " + arg + " into int. Aborting program.");
            System.exit(1);
        }
        return value;
    }
}
