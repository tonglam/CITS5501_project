package com.tong.cits5501;

import com.tong.cits5501.parser.REPL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class REPLTest {

    private REPL repl;

    @BeforeEach
    void setUp() {
        repl = new REPL();
    }

    @ParameterizedTest
    @ValueSource(strings = {"   ", "\t", "\n"})
    void testEmptyCommand(String input) {
        assertEquals("Error: Empty command", repl.parseCommand(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"turn", "set", "open", "kitchen turn"})
    void testIncompleteCommand(String input) {
        assertTrue(repl.parseCommand(input).startsWith("Error: Incomplete command"));
    }

    @ParameterizedTest
    @CsvSource({
            "turn lamp on, LightingCommand",
            "turn bulb off, LightingCommand",
            "kitchen turn neon on, LightingCommand",
            "living-room turn sconce off, LightingCommand",
            "turn brazier on, LightingCommand"
    })
    void testValidLightingCommands(String input, String expectedCommandType) {
        String result = repl.parseCommand(input);
        assertTrue(result.contains("Command recognized: " + expectedCommandType));
        assertTrue(result.contains("Simulated execution: Turning"));
    }

    @ParameterizedTest
    @CsvSource({
            "turn coffee-maker on, ApplianceCommand",
            "turn oven off, ApplianceCommand",
            "kitchen turn air-conditioner on, ApplianceCommand",
            "lab turn centrifuge off, ApplianceCommand",
            "turn synchrotron on, ApplianceCommand",
            "turn laser-cannon off, ApplianceCommand"
    })
    void testValidApplianceCommands(String input, String expectedCommandType) {
        String result = repl.parseCommand(input);
        assertTrue(result.contains("Command recognized: " + expectedCommandType),
                "Expected 'Command recognized: " + expectedCommandType + "' not found in result: " + result);
        assertTrue(result.contains("Simulated execution: Turning"),
                "Simulated execution not found in result: " + result);
    }

    @ParameterizedTest
    @CsvSource({
            "open gate, BarrierCommand",
            "close garage-door, BarrierCommand",
            "lock trapdoor, BarrierCommand",
            "unlock portcullis, BarrierCommand",
            "open drawbridge, BarrierCommand",
            "close blast-door, BarrierCommand",
            "lock airlock, BarrierCommand"
    })
    void testValidBarrierCommands(String input, String expectedCommandType) {
        String result = repl.parseCommand(input);
        assertTrue(result.contains("Command recognized: " + expectedCommandType),
                "Expected 'Command recognized: " + expectedCommandType + "' not found in result: " + result);
        assertTrue(result.contains("Simulated execution:"),
                "Simulated execution not found in result: " + result);
    }

    @ParameterizedTest
    @CsvSource({
            "set thermostat to 295K, ThermalDeviceCommand",
            "set oven to 450K, ThermalDeviceCommand",
            "kitchen set electric-blanket to 310K, ThermalDeviceCommand",
            "set incinerator to 1000K, ThermalDeviceCommand",
            "set reactor-core to 5000K, ThermalDeviceCommand"
    })
    void testValidThermalDeviceCommands(String input, String expectedCommandType) {
        String result = repl.parseCommand(input);
        assertTrue(result.contains("Command recognized: " + expectedCommandType),
                "Expected 'Command recognized: " + expectedCommandType + "' not found in result: " + result);
        assertTrue(result.contains("Simulated execution: Setting"),
                "Simulated execution not found in result: " + result);
    }

    @Test
    void testInvalidDeviceType() {
        assertEquals("Error: Invalid device type for 'turn' command", repl.parseCommand("turn invalid-device on"));
        assertEquals("Error: Invalid barrier type", repl.parseCommand("open invalid-barrier"));
        assertEquals("Error: Invalid thermal device type", repl.parseCommand("set invalid-device to 300K"));
    }

    @Test
    void testInvalidState() {
        assertEquals("Error: Invalid state. Use ON or OFF", repl.parseCommand("turn lamp invalid"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"set thermostat to invalid", "set thermostat to 300", "set thermostat to -100K", "set thermostat to 0K"})
    void testInvalidTemperature(String input) {
        String result = repl.parseCommand(input);
        assertTrue(result.startsWith("Error:"), "Expected error for input: " + input);
    }

    @ParameterizedTest
    @CsvSource({
            "turn lamp on when current-temperature less-than 300K, When condition: TemperatureCondition at 300K, comparison: LESS_THAN",
            "set thermostat to 295K until 10:00 pm, Until condition: TimeCondition at 22:00",
            "open gate when current-temperature greater-than 305K until 08:30 am, When condition: TemperatureCondition at 305K, comparison: GREATER_THAN;Until condition: TimeCondition at 08:30",
            "turn bulb off when current-temperature greater-than 300 K, When condition: TemperatureCondition at 300K, comparison: GREATER_THAN"
    })
    void testValidCommandsWithConditions(String input, String expectedPartsString) {
        String result = repl.parseCommand(input);
        assertTrue(result.contains("Command recognized:"), "Main command not recognized: " + result);

        String[] expectedParts = expectedPartsString.split(";");
        for (String part : expectedParts) {
            assertTrue(result.contains(part), "Expected part not found: " + part + " in result: " + result);
        }
    }

    @Test
    void testInvalidTemperatureCondition() {
        assertEquals("Error: Invalid temperature condition format",
                repl.parseCommand("turn lamp on when current-temperature less-than"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"turn lamp on until invalid-time", "turn lamp on until 25:00 pm", "turn lamp on until 10:60 am"})
    void testInvalidTimeCondition(String input) {
        String result = repl.parseCommand(input);
        assertTrue(result.startsWith("Error:"), "Expected error for input: " + input);
    }

    @Test
    void testMultipleConditions() {
        String result = repl.parseCommand("turn lamp on when current-temperature less-than 300K until 10:00 pm");
        assertTrue(result.contains("Command recognized: LightingCommand"));
        assertTrue(result.contains("When condition: TemperatureCondition at 300K, comparison: LESS_THAN"));
        assertTrue(result.contains("Until condition: TimeCondition at 22:00"));
    }

    @ParameterizedTest
    @CsvSource({
            "turn lamp on until 9:30 am, Until condition: TimeCondition at 09:30",
            "turn lamp on until 11:45 pm, Until condition: TimeCondition at 23:45",
            "turn lamp on when 1:05 am, When condition: TimeCondition at 01:05",
            "turn lamp on when 12:00 pm, When condition: TimeCondition at 12:00"
    })
    void testTimeConditions(String input, String expected) {
        String result = repl.parseCommand(input);
        assertTrue(result.contains("Command recognized: LightingCommand"));
        assertTrue(result.contains(expected));
    }

    @Test
    void testComplexCommand() {
        String result = repl.parseCommand("basement turn synchrotron on when current-temperature less-than 273K until 11:59 pm");
        assertTrue(result.contains("Command recognized: ApplianceCommand"));
        assertTrue(result.contains("When condition: TemperatureCondition at 273K, comparison: LESS_THAN"));
        assertTrue(result.contains("Until condition: TimeCondition at 23:59"));
        assertTrue(result.contains("Simulated execution: Turning on the synchrotron at basement"));
    }
}