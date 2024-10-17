package com.tong.cits5501;

import com.tong.cits5501.parser.REPL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the REPL (Read-Eval-Print Loop) implementation.
 * This class contains unit tests to verify the correct behavior of the REPL
 * in parsing and executing various Domolect 2.0 commands.
 */
public class REPLTest {

    private REPL repl;

    /**
     * Sets up a new REPL instance before each test.
     */
    @BeforeEach
    void setUp() {
        repl = new REPL();
    }

    /**
     * Tests the REPL's handling of empty commands.
     *
     * @param input The empty input string to test.
     */
    @ParameterizedTest
    @ValueSource(strings = {"   ", "\t", "\n"})
    void testEmptyCommand(String input) {
        assertEquals("Error: Empty command", repl.parseCommand(input));
    }

    /**
     * Tests the REPL's handling of incomplete commands.
     *
     * @param input The incomplete command string to test.
     */
    @ParameterizedTest
    @ValueSource(strings = {"turn", "set", "open", "kitchen turn"})
    void testIncompleteCommand(String input) {
        assertTrue(repl.parseCommand(input).startsWith("Error: Incomplete command"));
    }

    /**
     * Tests valid lighting commands with various light sources and states.
     *
     * @param input               The lighting command to test.
     * @param expectedCommandType The expected command type in the response.
     */
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

    /**
     * Tests valid appliance commands with various appliances and states.
     *
     * @param input               The appliance command to test.
     * @param expectedCommandType The expected command type in the response.
     */
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

    /**
     * Tests valid barrier commands with various barriers and actions.
     *
     * @param input               The barrier command to test.
     * @param expectedCommandType The expected command type in the response.
     */
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

    /**
     * Tests valid thermal device commands with various devices and temperatures.
     *
     * @param input               The thermal device command to test.
     * @param expectedCommandType The expected command type in the response.
     */
    @ParameterizedTest
    @CsvSource({
            "set thermostat to 295K, ThermalDeviceCommand",
            "set oven to 450K, ThermalDeviceCommand",
            "kitchen set electric-blanket to 310K, ThermalDeviceCommand",
            "set incinerator to 1000K, ThermalDeviceCommand",
            "set reactor-core to 5000K, ThermalDeviceCommand",
            "set thermostat to 295 K, ThermalDeviceCommand",
            "kitchen set incinerator to 1000 K, ThermalDeviceCommand",
            "set reactor-core to 5000 K, ThermalDeviceCommand"
    })
    void testValidThermalDeviceCommands(String input, String expectedCommandType) {
        String result = repl.parseCommand(input);
        assertTrue(result.contains("Command recognized: " + expectedCommandType),
                "Expected 'Command recognized: " + expectedCommandType + "' not found in result: " + result);
        assertTrue(result.contains("Simulated execution: Setting"),
                "Simulated execution not found in result: " + result);
    }

    /**
     * Tests the REPL's handling of invalid device types in commands.
     */
    @Test
    void testInvalidDeviceType() {
        assertEquals("Error: Invalid device type for 'turn' command", repl.parseCommand("turn invalid-device on"));
        assertEquals("Error: Invalid barrier type", repl.parseCommand("open invalid-barrier"));
        assertEquals("Error: Invalid thermal device type", repl.parseCommand("set invalid-device to 300K"));
    }

    /**
     * Tests the REPL's handling of invalid states in commands.
     */
    @Test
    void testInvalidState() {
        assertEquals("Error: Invalid state. Use ON or OFF", repl.parseCommand("turn lamp invalid"));
    }

    /**
     * Tests the REPL's handling of invalid temperature formats in commands.
     *
     * @param input The command with an invalid temperature to test.
     */
    @ParameterizedTest
    @ValueSource(strings = {"set thermostat to invalid", "set thermostat to 300", "set thermostat to -100K", "set thermostat to 0K"})
    void testInvalidTemperature(String input) {
        String result = repl.parseCommand(input);
        assertTrue(result.startsWith("Error:"), "Expected error for input: " + input);
    }

    /**
     * Tests valid commands with conditions (when and until).
     *
     * @param input               The command with conditions to test.
     * @param expectedPartsString The expected parts in the response.
     */
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

    /**
     * Tests the REPL's handling of invalid temperature condition formats.
     */
    @Test
    void testInvalidTemperatureCondition() {
        assertEquals("Error: Invalid temperature condition format",
                repl.parseCommand("turn lamp on when current-temperature less-than"));
    }

    /**
     * Tests the REPL's handling of invalid time condition formats.
     *
     * @param input The command with an invalid time condition to test.
     */
    @ParameterizedTest
    @ValueSource(strings = {"turn lamp on until invalid-time", "turn lamp on until 25:00 pm", "turn lamp on until 10:60 am"})
    void testInvalidTimeCondition(String input) {
        String result = repl.parseCommand(input);
        assertTrue(result.startsWith("Error:"), "Expected error for input: " + input);
    }

    /**
     * Tests a command with multiple conditions (both when and until).
     */
    @Test
    void testMultipleConditions() {
        String result = repl.parseCommand("turn lamp on when current-temperature less-than 300K until 10:00 pm");
        assertTrue(result.contains("Command recognized: LightingCommand"));
        assertTrue(result.contains("When condition: TemperatureCondition at 300K, comparison: LESS_THAN"));
        assertTrue(result.contains("Until condition: TimeCondition at 22:00"));
    }

    /**
     * Tests various time conditions in commands.
     *
     * @param input    The command with a time condition to test.
     * @param expected The expected time condition in the response.
     */
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

    /**
     * Tests a complex command with location, device, state, and multiple conditions.
     */
    @Test
    void testComplexCommand() {
        String result = repl.parseCommand("basement turn synchrotron on when current-temperature less-than 273K until 11:59 pm");
        assertTrue(result.contains("Command recognized: ApplianceCommand"));
        assertTrue(result.contains("When condition: TemperatureCondition at 273K, comparison: LESS_THAN"));
        assertTrue(result.contains("Until condition: TimeCondition at 23:59"));
        assertTrue(result.contains("Simulated execution: Turning on the synchrotron at basement"));
    }
}