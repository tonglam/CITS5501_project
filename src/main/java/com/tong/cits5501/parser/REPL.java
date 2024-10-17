package com.tong.cits5501.parser;

import com.tong.cits5501.domolect.command.*;
import com.tong.cits5501.domolect.constant.BarrierAction;
import com.tong.cits5501.domolect.constant.Comparison;
import com.tong.cits5501.domolect.constant.State;
import com.tong.cits5501.domolect.device.Appliance;
import com.tong.cits5501.domolect.device.Barrier;
import com.tong.cits5501.domolect.device.LightSource;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Read-Eval-Print Loop (REPL) for the Domolect 2.0 command language.
 * This class parses and simulates the execution of Domolect commands.
 */
public class REPL {

    private final Map<String, List<String>> grammar;
    // Initialize grammar with valid device types and names
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{2})\\s*(am|pm)", Pattern.CASE_INSENSITIVE);
    private static final Pattern TEMPERATURE_PATTERN = Pattern.compile("current-temperature\\s+(less-than|equal-to|greater-than)\\s+(\\d+)\\s*K");

    /**
     * Constructs a new REPL instance and initializes the grammar.
     */
    public REPL() {
        grammar = new HashMap<>();
        grammar.put("light_source", Arrays.asList("lamp", "bulb", "neon", "sconce", "brazier"));
        grammar.put("barrier", Arrays.asList("gate", "curtains", "garage-door", "blinds", "window", "shutter", "trapdoor", "portcullis", "drawbridge", "blast-door", "airlock"));
        grammar.put("appliance", Arrays.asList("coffee-maker", "oven", "air-conditioner", "centrifuge", "synchrotron", "laser-cannon"));
        grammar.put("thermal_device", Arrays.asList("oven", "thermostat", "electric-blanket", "incinerator", "reactor-core"));
    }

    /**
     * Parses a given command string and simulates its execution.
     *
     * @param command The command string to parse and execute.
     * @return A string describing the parsed command and its simulated execution.
     */
    public String parseCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return "Error: Empty command";
        }

        String[] parts = command.trim().split("\\s+");
        if (parts.length < 2) {
            return "Error: Incomplete command";
        }

        int startIndex = 0;
        Location location = null;
        // Check if the command starts with a location
        if (!isCommandKeyword(parts[0])) {
            location = new Location(parts[0]);
            startIndex = 1;
            if (parts.length < 3) {
                return "Error: Incomplete command after location";
            }
        }

        try {
            Command mainCommand = parseMainCommand(parts, startIndex, location);
            // Parse optional 'when' and 'until' conditions
            Condition whenCondition = parseCondition(command, "when");
            Condition untilCondition = parseCondition(command, "until");

            AugmentedCommand augmentedCommand = new AugmentedCommand(whenCondition, untilCondition, mainCommand);
            return simulateExecution(augmentedCommand);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Parses the main command from the given parts array.
     *
     * @param parts      The array of command parts.
     * @param startIndex The starting index for parsing the command.
     * @param location   The location for the command, if specified.
     * @return A Command object representing the parsed main command.
     * @throws IllegalArgumentException If the command type is invalid.
     */
    private Command parseMainCommand(String[] parts, int startIndex, Location location) {
        // Determine the command type based on the first keyword
        return switch (parts[startIndex].toLowerCase()) {
            case "turn" -> parseTurnCommand(parts, startIndex, location);
            case "open", "close", "lock", "unlock" -> parseBarrierCommand(parts, startIndex, location);
            case "set" -> parseSetCommand(parts, startIndex, location);
            default -> throw new IllegalArgumentException("Invalid command type");
        };
    }

    /**
     * Parses a turn command for light sources or appliances.
     *
     * @param parts      The array of command parts.
     * @param startIndex The starting index for parsing the command.
     * @param location   The location for the command, if specified.
     * @return A Command object representing the parsed turn command.
     * @throws IllegalArgumentException If the command is incomplete or invalid.
     */
    private Command parseTurnCommand(String[] parts, int startIndex, Location location) {
        if (parts.length < startIndex + 3) {
            throw new IllegalArgumentException("Incomplete turn command");
        }

        String deviceName = parts[startIndex + 1];
        // Determine if the device is a light source or an appliance
        String deviceType = isValidDevice("light_source", deviceName) ? "light_source" :
                isValidDevice("appliance", deviceName) ? "appliance" : null;

        if (deviceType == null) {
            throw new IllegalArgumentException("Invalid device type for 'turn' command");
        }

        State state = parseState(parts[startIndex + 2]);

        // Create the appropriate command based on the device type
        if ("light_source".equals(deviceType)) {
            return new LightingCommand(location, new LightSource(deviceName, null), state);
        } else {
            return new ApplianceCommand(location, new Appliance(deviceName, null), state);
        }
    }

    /**
     * Parses a barrier command (open, close, lock, unlock).
     *
     * @param parts      The array of command parts.
     * @param startIndex The starting index for parsing the command.
     * @param location   The location for the command, if specified.
     * @return A BarrierCommand object representing the parsed barrier command.
     * @throws IllegalArgumentException If the command is incomplete or invalid.
     */
    private Command parseBarrierCommand(String[] parts, int startIndex, Location location) {
        if (parts.length < startIndex + 2) {
            throw new IllegalArgumentException("Incomplete barrier command");
        }

        String barrierName = parts[startIndex + 1];
        if (!isValidDevice("barrier", barrierName)) {
            throw new IllegalArgumentException("Invalid barrier type");
        }

        BarrierAction action = BarrierAction.valueOf(parts[startIndex].toUpperCase());
        return new BarrierCommand(location, action, new Barrier(barrierName));
    }

    /**
     * Parses a set command for thermal devices.
     *
     * @param parts      The array of command parts.
     * @param startIndex The starting index for parsing the command.
     * @param location   The location for the command, if specified.
     * @return A ThermalDeviceCommand object representing the parsed set command.
     * @throws IllegalArgumentException If the command is incomplete or invalid.
     */
    private Command parseSetCommand(String[] parts, int startIndex, Location location) {
        if (parts.length < startIndex + 4) {
            throw new IllegalArgumentException("Incomplete set command");
        }

        String deviceName = parts[startIndex + 1];
        if (!isValidDevice("thermal_device", deviceName)) {
            throw new IllegalArgumentException("Invalid thermal device type");
        }

        // Handle temperature with or without a space before 'K'
        String temperatureStr = parts[startIndex + 3];
        if (parts.length > startIndex + 4 && parts[startIndex + 4].equalsIgnoreCase("K")) {
            temperatureStr += " " + parts[startIndex + 4];
        }

        int temperature = parseTemperature(temperatureStr);

        return new ThermalDeviceCommand(location, deviceName, temperature);
    }

    /**
     * Parses a condition (when or until) from the command string.
     *
     * @param command       The full command string.
     * @param conditionType The type of condition ("when" or "until").
     * @return A Condition object representing the parsed condition, or null if no condition is present.
     * @throws IllegalArgumentException If the condition format is invalid.
     */
    private Condition parseCondition(String command, String conditionType) {
        String conditionStr = extractCondition(command, conditionType);
        if (conditionStr == null) {
            return null;
        }

        // Determine if it's a temperature or time condition
        if (conditionStr.startsWith("current-temperature")) {
            return parseTemperatureCondition(conditionStr);
        } else {
            return parseTimeCondition(conditionStr);
        }
    }

    /**
     * Parses a temperature condition.
     *
     * @param condition The temperature condition string.
     * @return A TemperatureCondition object representing the parsed condition.
     * @throws IllegalArgumentException If the temperature condition format is invalid.
     */
    private TemperatureCondition parseTemperatureCondition(String condition) {
        Matcher matcher = TEMPERATURE_PATTERN.matcher(condition);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid temperature condition format");
        }

        String comparisonStr = matcher.group(1);
        int temperature = Integer.parseInt(matcher.group(2));

        Comparison comparison = parseComparison(comparisonStr);
        return new TemperatureCondition(temperature, comparison);
    }

    /**
     * Parses a time condition.
     *
     * @param condition The time condition string.
     * @return A TimeCondition object representing the parsed condition.
     * @throws IllegalArgumentException If the time condition format is invalid.
     */
    private TimeCondition parseTimeCondition(String condition) {
        Matcher matcher = TIME_PATTERN.matcher(condition);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid time condition format");
        }

        int hours = Integer.parseInt(matcher.group(1));
        int minutes = Integer.parseInt(matcher.group(2));
        String amPm = matcher.group(3).toLowerCase();

        if (hours < 1 || hours > 12 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Invalid time");
        }

        // Convert to 24-hour format
        if (amPm.equals("pm") && hours != 12) {
            hours += 12;
        } else if (amPm.equals("am") && hours == 12) {
            hours = 0;
        }

        return new TimeCondition(java.time.LocalTime.of(hours, minutes));
    }

    /**
     * Simulates the execution of an AugmentedCommand and generates a description.
     *
     * @param command The AugmentedCommand to simulate.
     * @return A string describing the simulated execution of the command.
     */
    private String simulateExecution(AugmentedCommand command) {
        StringBuilder result = new StringBuilder();
        result.append("Command recognized: ").append(command.getCommand().getClass().getSimpleName()).append("\n");

        // Add when condition if present
        Condition whenCondition = command.getWhenCondition();
        if (whenCondition != null) {
            result.append("When condition: ").append(whenCondition).append("\n");
        }

        // Add until condition if present
        Condition untilCondition = command.getUntilCondition();
        if (untilCondition != null) {
            result.append("Until condition: ").append(untilCondition).append("\n");
        }

        result.append("Simulated execution: ").append(simulateCommandExecution(command.getCommand()));

        return result.toString();
    }

    /**
     * Simulates the execution of a specific Command and generates a description.
     *
     * @param command The Command to simulate.
     * @return A string describing the simulated execution of the command.
     */
    private String simulateCommandExecution(Command command) {
        // Use pattern matching to determine the command type and generate appropriate description
        if (command instanceof LightingCommand cmd) {
            return String.format("Turning %s the %s%s",
                    cmd.getState().toString().toLowerCase(),
                    cmd.getLightSource().getName(),
                    cmd.getLocation() != null ? " at " + cmd.getLocation().getName() : "");
        } else if (command instanceof ApplianceCommand cmd) {
            return String.format("Turning %s the %s%s",
                    cmd.getState().toString().toLowerCase(),
                    cmd.getAppliance().getName(),
                    cmd.getLocation() != null ? " at " + cmd.getLocation().getName() : "");
        } else if (command instanceof BarrierCommand cmd) {
            return String.format("%s the %s%s",
                    cmd.getAction().toString().toLowerCase(),
                    cmd.getBarrier().getName(),
                    cmd.getLocation() != null ? " at " + cmd.getLocation().getName() : "");
        } else if (command instanceof ThermalDeviceCommand cmd) {
            return String.format("Setting %s to %d K%s",
                    cmd.getThermalDevice(),
                    cmd.getTemperature(),
                    cmd.getLocation() != null ? " at " + cmd.getLocation().getName() : "");
        }
        return "Unknown command type";
    }

    /**
     * Extracts a specific condition (when or until) from the command string.
     *
     * @param command       The full command string.
     * @param conditionType The type of condition to extract ("when" or "until").
     * @return The extracted condition string, or null if not found.
     */
    private String extractCondition(String command, String conditionType) {
        int index = command.indexOf(" " + conditionType + " ");
        if (index != -1) {
            String remainingCommand = command.substring(index + conditionType.length() + 2).trim();
            // Find the next condition keyword, if any
            int nextConditionIndex = Math.min(
                    !remainingCommand.contains(" when ") ? Integer.MAX_VALUE : remainingCommand.indexOf(" when "),
                    !remainingCommand.contains(" until ") ? Integer.MAX_VALUE : remainingCommand.indexOf(" until ")
            );
            if (nextConditionIndex == Integer.MAX_VALUE) {
                return remainingCommand;
            } else {
                return remainingCommand.substring(0, nextConditionIndex).trim();
            }
        }
        return null;
    }

    /**
     * Checks if a given device name is valid for the specified device type.
     *
     * @param deviceType The type of device to check.
     * @param deviceName The name of the device to validate.
     * @return true if the device name is valid for the given type, false otherwise.
     */
    private boolean isValidDevice(String deviceType, String deviceName) {
        return grammar.containsKey(deviceType) && grammar.get(deviceType).contains(deviceName);
    }

    /**
     * Parses a temperature string into an integer value.
     *
     * @param temp The temperature string to parse.
     * @return The parsed temperature as an integer.
     * @throws IllegalArgumentException If the temperature format is invalid or the value is not positive.
     */
    private int parseTemperature(String temp) {
        String trimmedTemp = temp.trim().toUpperCase();
        if (!trimmedTemp.endsWith("K")) {
            throw new IllegalArgumentException("Temperature must end with K");
        }
        try {
            // Extract the numeric part of the temperature
            String numberPart = trimmedTemp.substring(0, trimmedTemp.length() - 1).trim();
            int temperature = Integer.parseInt(numberPart);
            if (temperature <= 0) {
                throw new IllegalArgumentException("Temperature must be a positive value");
            }
            return temperature;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid temperature format");
        }
    }

    /**
     * Parses a state string into a State enum value.
     *
     * @param stateStr The state string to parse.
     * @return The parsed State enum value.
     * @throws IllegalArgumentException If the state string is invalid.
     */
    private State parseState(String stateStr) {
        try {
            return State.valueOf(stateStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid state. Use ON or OFF");
        }
    }

    /**
     * Parses a comparison string into a Comparison enum value.
     *
     * @param comparisonStr The comparison string to parse.
     * @return The parsed Comparison enum value.
     * @throws IllegalArgumentException If the comparison string is invalid.
     */
    private Comparison parseComparison(String comparisonStr) {
        // Use switch expression for cleaner code
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
     * @param word The word to check.
     * @return true if the word is a command keyword, false otherwise.
     */
    private boolean isCommandKeyword(String word) {
        return "turn".equals(word) || "set".equals(word) || isBarrierAction(word);
    }

    /**
     * Checks if a given word is a valid barrier action.
     *
     * @param word The word to check.
     * @return true if the word is a valid barrier action, false otherwise.
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
     * Starts the REPL, continuously reading user input and parsing commands until 'exit' is entered.
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
            String result = parseCommand(command);
            System.out.println(result);
        }
        scanner.close();
    }

    /**
     * Main method to run the REPL.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new REPL().start();
    }
}