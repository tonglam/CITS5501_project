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

public class REPL {

    private final Map<String, List<String>> grammar;
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{2})\\s*(am|pm)", Pattern.CASE_INSENSITIVE);
    private static final Pattern TEMPERATURE_PATTERN = Pattern.compile("current-temperature\\s+(less-than|equal-to|greater-than)\\s+(\\d+)\\s*K");

    public REPL() {
        grammar = new HashMap<>();
        grammar.put("light_source", Arrays.asList("lamp", "bulb", "neon", "sconce", "brazier"));
        grammar.put("barrier", Arrays.asList("gate", "curtains", "garage-door", "blinds", "window", "shutter", "trapdoor", "portcullis", "drawbridge", "blast-door", "airlock"));
        grammar.put("appliance", Arrays.asList("coffee-maker", "oven", "air-conditioner", "centrifuge", "synchrotron", "laser-cannon"));
        grammar.put("thermal_device", Arrays.asList("oven", "thermostat", "electric-blanket", "incinerator", "reactor-core"));
    }

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
        if (!isCommandKeyword(parts[0])) {
            location = new Location(parts[0]);
            startIndex = 1;
            if (parts.length < 3) {
                return "Error: Incomplete command after location";
            }
        }

        try {
            Command mainCommand = parseMainCommand(parts, startIndex, location);
            Condition whenCondition = parseCondition(command, "when");
            Condition untilCondition = parseCondition(command, "until");

            AugmentedCommand augmentedCommand = new AugmentedCommand(whenCondition, untilCondition, mainCommand);
            return simulateExecution(augmentedCommand);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    private Command parseMainCommand(String[] parts, int startIndex, Location location) {
        return switch (parts[startIndex].toLowerCase()) {
            case "turn" -> parseTurnCommand(parts, startIndex, location);
            case "open", "close", "lock", "unlock" -> parseBarrierCommand(parts, startIndex, location);
            case "set" -> parseSetCommand(parts, startIndex, location);
            default -> throw new IllegalArgumentException("Invalid command type");
        };
    }

    private Command parseTurnCommand(String[] parts, int startIndex, Location location) {
        if (parts.length < startIndex + 3) {
            throw new IllegalArgumentException("Incomplete turn command");
        }

        String deviceName = parts[startIndex + 1];
        String deviceType = isValidDevice("light_source", deviceName) ? "light_source" :
                isValidDevice("appliance", deviceName) ? "appliance" : null;

        if (deviceType == null) {
            throw new IllegalArgumentException("Invalid device type for 'turn' command");
        }

        State state = parseState(parts[startIndex + 2]);

        if ("light_source".equals(deviceType)) {
            return new LightingCommand(location, new LightSource(deviceName, null), state);
        } else {
            return new ApplianceCommand(location, new Appliance(deviceName, null), state);
        }
    }

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

    private Command parseSetCommand(String[] parts, int startIndex, Location location) {
        if (parts.length < startIndex + 4) {
            throw new IllegalArgumentException("Incomplete set command");
        }

        String deviceName = parts[startIndex + 1];
        if (!isValidDevice("thermal_device", deviceName)) {
            throw new IllegalArgumentException("Invalid thermal device type");
        }

        int temperature = parseTemperature(parts[startIndex + 3]);

        return new ThermalDeviceCommand(location, deviceName, temperature);
    }

    private Condition parseCondition(String command, String conditionType) {
        String conditionStr = extractCondition(command, conditionType);
        if (conditionStr == null) {
            return null;
        }

        if (conditionStr.startsWith("current-temperature")) {
            return parseTemperatureCondition(conditionStr);
        } else {
            return parseTimeCondition(conditionStr);
        }
    }

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

    private String simulateExecution(AugmentedCommand command) {
        StringBuilder result = new StringBuilder();
        result.append("Command recognized: ").append(command.getCommand().getClass().getSimpleName()).append("\n");

        Condition whenCondition = command.getWhenCondition();
        if (whenCondition != null) {
            result.append("When condition: ").append(whenCondition).append("\n");
        }

        Condition untilCondition = command.getUntilCondition();
        if (untilCondition != null) {
            result.append("Until condition: ").append(untilCondition).append("\n");
        }

        result.append("Simulated execution: ").append(simulateCommandExecution(command.getCommand()));

        return result.toString();
    }

    private String simulateCommandExecution(Command command) {
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

    private String extractCondition(String command, String conditionType) {
        int index = command.indexOf(" " + conditionType + " ");
        if (index != -1) {
            String remainingCommand = command.substring(index + conditionType.length() + 2).trim();
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

    private boolean isValidDevice(String deviceType, String deviceName) {
        return grammar.containsKey(deviceType) && grammar.get(deviceType).contains(deviceName);
    }

    private int parseTemperature(String temp) {
        if (!temp.endsWith("K")) {
            throw new IllegalArgumentException("Temperature must end with K");
        }
        try {
            int temperature = Integer.parseInt(temp.substring(0, temp.length() - 1).trim());
            if (temperature <= 0) {
                throw new IllegalArgumentException("Temperature must be a positive value");
            }
            return temperature;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid temperature format");
        }
    }

    private State parseState(String stateStr) {
        try {
            return State.valueOf(stateStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid state. Use ON or OFF");
        }
    }

    private Comparison parseComparison(String comparisonStr) {
        return switch (comparisonStr.toLowerCase()) {
            case "less-than" -> Comparison.LESS_THAN;
            case "equal-to" -> Comparison.EQUAL_TO;
            case "greater-than" -> Comparison.GREATER_THAN;
            default -> throw new IllegalArgumentException("Invalid comparison: " + comparisonStr);
        };
    }

    private boolean isCommandKeyword(String word) {
        return "turn".equals(word) || "set".equals(word) || isBarrierAction(word);
    }

    private boolean isBarrierAction(String word) {
        try {
            BarrierAction.valueOf(word.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

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

    public static void main(String[] args) {
        new REPL().start();
    }
}