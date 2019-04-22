package edu.isistan.appender;

public class AppenderForLine {
    private enum FLAGS {
        INPUT_FILE("-i", 1, true, new ArgumentHandler() {
            @Override
            public void handleArguments(String... args) {
                inputFile = args[0];
            }
        }),
        OUTPUT_FILE("-o", 1, true, new ArgumentHandler() {
            @Override
            public void handleArguments(String... args) {
                outputFile = args[0];
            }
        }),
        MESSAGE_TO_APPEND("-m", 1, true, new ArgumentHandler() {
            @Override
            public void handleArguments(String... args) {
                messajeToAppend = args[0];
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

    private static String inputFile;
    private static String outputFile;
    private static String messajeToAppend;

    public static void main(String[] args) {
        parseArguments(args);
                AppenderForLineWriter writer = new AppenderForLineWriter.Builder().setMessajeToAppend(messajeToAppend)
                        .setInputFile(inputFile)
                        .setOutputFile(outputFile)
                        .build();
                writer.generateAppendedFile();
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

}
