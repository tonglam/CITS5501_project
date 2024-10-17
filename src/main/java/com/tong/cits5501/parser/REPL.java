package com.tong.cits5501.parser;

import com.tong.cits5501.domolect.constant.BarrierAction;
import com.tong.cits5501.domolect.constant.Comparison;
import com.tong.cits5501.domolect.constant.State;
import com.tong.cits5501.domolect.device.LightSource;

import java.util.*;

/**
 * REPL (Read-Eval-Print Loop) for the Domolect 2.0 command system.
 * This class provides an interactive interface for parsing and interpreting
 * Domolect commands, including device control and condition settings.
 */
public class REPL {
    /**
     * Stores the grammar rules for different device types
     */
    private final Map<String, List<String>> grammar;

    /**
     * Constructs a new REPL instance and initializes the grammar rules.
     */
    public REPL() {
        grammar = new HashMap<>();
        grammar.put("light_source", Arrays.asList("lamp", "bulb", "neon", "sconce", "brazier"));
        grammar.put("barrier", Arrays.asList("gate", "curtains", "garage-door", "blinds", "window", "shutter", "trapdoor", "portcullis", "drawbridge", "blast-door", "airlock"));
        grammar.put("appliance", Arrays.asList("coffee-maker", "oven", "air-conditioner", "centrifuge", "synchrotron", "laser-cannon"));
        grammar.put("thermal_device", Arrays.asList("oven", "thermostat", "electric-blanket", "incinerator", "reactor-core"));
    }

    /**
     * Parses a given command string and returns the interpretation or error message.
     *
     * @param command The command string to parse
     * @return A string representing the parsed command or an error message
     */
    public String parseCommand(String command) {
        // Print the command
        System.out.println("Executing command: " + command);

        if (command == null || command.trim().isEmpty()) {
            return "Error: Empty command";
        }

        String[] parts = command.trim().split("\\s+");

        if (parts.length < 2) {
            return "Error: Incomplete command";
        }

        // Check for location
        int startIndex = 0;
        if (!isCommandKeyword(parts[0])) {
            startIndex = 1;
            if (parts.length < 3) {
                return "Error: Incomplete command after location";
            }
        }

        // Parse main command
        try {
            if ("turn".equals(parts[startIndex])) {
                return parseTurnCommand(parts, startIndex);
            } else if (isBarrierAction(parts[startIndex])) {
                return parseBarrierCommand(parts, startIndex);
            } else if ("set".equals(parts[startIndex])) {
                return parseSetCommand(parts, startIndex);
            }
        } catch (IndexOutOfBoundsException e) {
            return "Error: Incomplete command parameters";
        }

        // Parse conditions
        int whenIndex = command.indexOf(" when ");
        int untilIndex = command.indexOf(" until ");

        if (whenIndex != -1 || untilIndex != -1) {
            String condition = command.substring(whenIndex != -1 ? whenIndex : untilIndex).trim();
            String conditionType = whenIndex != -1 ? "when" : "until";
            if (condition.startsWith("current-temperature")) {
                return parseTemperatureCondition(condition, conditionType);
            } else {
                return parseTimeCondition(condition, conditionType);
            }
        }

        return "Error: Invalid command";
    }

    /**
     * Parses a 'turn' command for light sources and appliances.
     *
     * @param parts      The split command parts
     * @param startIndex The start index of the actual command (after optional location)
     * @return A string representing the parsed command or an error message
     */
    private String parseTurnCommand(String[] parts, int startIndex) {
        if (parts.length < startIndex + 3) {
            return "Error: Turn command is incomplete";
        }

        if (isValidDevice("light_source", parts[startIndex + 1]) || isValidDevice("appliance", parts[startIndex + 1])) {
            try {
                State state = State.valueOf(parts[startIndex + 2].toUpperCase());
                if (isValidDevice("light_source", parts[startIndex + 1])) {
                    // Create a LightSource object for light sources
                    LightSource lightSource = new LightSource(parts[startIndex + 1]);
                    return "Command recognized: Turn " + lightSource.getName() + " " + state;
                } else {
                    return "Command recognized: Turn " + parts[startIndex + 1] + " " + state;
                }
            } catch (IllegalArgumentException e) {
                return "Error: Invalid state. Use ON or OFF";
            }
        } else {
            return "Error: Invalid device type for 'turn' command";
        }
    }

    /**
     * Parses a barrier action command.
     *
     * @param parts      The split command parts
     * @param startIndex The start index of the actual command (after optional location)
     * @return A string representing the parsed command or an error message
     */
    private String parseBarrierCommand(String[] parts, int startIndex) {
        if (parts.length < startIndex + 2) {
            return "Error: Barrier command is incomplete";
        }

        if (isValidDevice("barrier", parts[startIndex + 1])) {
            BarrierAction action = BarrierAction.valueOf(parts[startIndex].toUpperCase());
            return "Command recognized: " + action + " " + parts[startIndex + 1];
        } else {
            return "Error: Invalid barrier type";
        }
    }

    /**
     * Parses a 'set' command for thermal devices.
     *
     * @param parts      The split command parts
     * @param startIndex The start index of the actual command (after optional location)
     * @return A string representing the parsed command or an error message
     */
    private String parseSetCommand(String[] parts, int startIndex) {
        if (parts.length < startIndex + 4) {
            return "Error: Set command is incomplete";
        }

        if (isValidDevice("thermal_device", parts[startIndex + 1])) {
            try {
                int temperature = parseTemperature(parts[startIndex + 3]);
                return "Command recognized: Set " + parts[startIndex + 1] + " to " + temperature + " K";
            } catch (NumberFormatException e) {
                return "Error: Invalid temperature format";
            } catch (IllegalArgumentException e) {
                return "Error: " + e.getMessage();
            }
        } else {
            return "Error: Invalid thermal device type";
        }
    }

    /**
     * Checks if a given device name is valid for the specified device type.
     *
     * @param deviceType The type of the device
     * @param deviceName The name of the device
     * @return true if the device is valid, false otherwise
     */
    private boolean isValidDevice(String deviceType, String deviceName) {
        return grammar.containsKey(deviceType) && grammar.get(deviceType).contains(deviceName);
    }

    /**
     * Parses a temperature condition.
     *
     * @param condition     The condition string to parse
     * @param conditionType The type of condition ("when" or "until")
     * @return A string representing the parsed condition or an error message
     */
    private String parseTemperatureCondition(String condition, String conditionType) {
        String[] parts = condition.split("\\s+");
        if (parts.length != 4) {
            return "Error: Invalid temperature condition format";
        }

        try {
            Comparison comparison = parseComparison(parts[1] + "-" + parts[2]);
            int temperature = parseTemperature(parts[3]);
            return "Condition recognized: " + conditionType + " temperature is " + comparison + " " + temperature + " K";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Parses a time condition.
     *
     * @param condition     The condition string to parse
     * @param conditionType The type of condition ("when" or "until")
     * @return A string representing the parsed condition or an error message
     */
    private String parseTimeCondition(String condition, String conditionType) {
        String[] parts = condition.split("\\s+");
        if (parts.length != 2) {
            return "Error: Invalid time condition format";
        }

        try {
            int[] time = parseTime(parts[1]);
            return "Condition recognized: " + conditionType + " time is " + String.format("%02d:%02d", time[0], time[1]);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Parses a temperature string into an integer value.
     *
     * @param temp The temperature string to parse
     * @return The temperature as an integer
     * @throws IllegalArgumentException if the temperature format is invalid
     */
    private int parseTemperature(String temp) {
        if (!temp.endsWith("K")) {
            throw new IllegalArgumentException("Temperature must end with K");
        }

        try {
            return Integer.parseInt(temp.substring(0, temp.length() - 1).trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid temperature format");
        }
    }

    /**
     * Parses a time string into an array of integers representing hours and minutes.
     *
     * @param timeStr The time string to parse
     * @return An array of two integers: [hours, minutes]
     * @throws IllegalArgumentException if the time format is invalid
     */
    private int[] parseTime(String timeStr) {
        String[] parts = timeStr.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid time format");
        }

        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1].substring(0, 2));
            String amPm = parts[1].substring(2).trim().toLowerCase();

            // Validate time components
            if (hours < 1 || hours > 12 || minutes < 0 || minutes > 59 || (!amPm.equals("am") && !amPm.equals("pm"))) {
                throw new IllegalArgumentException("Invalid time");
            }

            // Convert to 24-hour format
            if (amPm.equals("pm") && hours != 12) {
                hours += 12;
            } else if (amPm.equals("am") && hours == 12) {
                hours = 0;
            }

            return new int[]{hours, minutes};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid time format");
        }
    }

    /**
     * Parses a comparison string into a Comparison enum value.
     *
     * @param comparisonStr The comparison string to parse
     * @return The corresponding Comparison enum value
     * @throws IllegalArgumentException if the comparison string is invalid
     */
    private Comparison parseComparison(String comparisonStr) {
        return switch (comparisonStr.toLowerCase()) {
            case "less-than" -> Comparison.LESS_THAN;
            case "equal-to" -> Comparison.EQUAL_TO;
            case "greater-than" -> Comparison.GREATER_THAN;
            default -> throw new IllegalArgumentException("Invalid comparison: " + comparisonStr);
        };
    }

    /**
     * Checks if a given word is a command keyword.
     *
     * @param word The word to check
     * @return true if the word is a command keyword, false otherwise
     */
    private boolean isCommandKeyword(String word) {
        return "turn".equals(word) || "set".equals(word) || isBarrierAction(word);
    }

    /**
     * Checks if a given word is a valid barrier action.
     *
     * @param word The word to check
     * @return true if the word is a valid barrier action, false otherwise
     */
    private boolean isBarrierAction(String word) {
        try {
            BarrierAction.valueOf(word.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Starts the REPL, continuously reading user input and parsing commands.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Domolect 2.0 REPL. Type 'exit' to quit.");

        while (true) {
            System.out.print(">>> ");
            String command = scanner.nextLine();
            if ("exit".equalsIgnoreCase(command)) {
                break;
            }
            System.out.println(parseCommand(command));
        }
        scanner.close();
    }

    /**
     * Main method to run the REPL.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        new REPL().start();
    }
}