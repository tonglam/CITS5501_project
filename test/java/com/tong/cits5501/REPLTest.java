package com.tong.cits5501;

import com.tong.cits5501.parser.REPL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test class for {@link REPL}.
 */
public class REPLTest {

    private REPL repl;

    @BeforeEach
    void setUp() {
        repl = new REPL();
    }

    /**
     * Tests the REPL parser with empty and whitespace-only commands.
     * Verifies that the parser correctly identifies and reports empty commands.
     */
    @Test
    void testEmptyCommand() {
        // Test with an empty string
        assertEquals("Error: Empty command", repl.parseCommand(""), "Empty string should be recognized as an empty command");

        // Test with whitespace-only string
        assertEquals("Error: Empty command", repl.parseCommand("   "), "Whitespace-only string should be recognized as an empty command");
    }

    /**
     * Tests the REPL parser with incomplete commands.
     * Verifies that the parser correctly identifies and reports incomplete commands,
     * both with and without location specifications.
     */
    @Test
    void testIncompleteCommand() {
        // Test incomplete command without location
        assertEquals("Error: Incomplete command", repl.parseCommand("turn"), "Single word 'turn' should be recognized as an incomplete command");

        // Test incomplete command with location
        assertEquals("Error: Incomplete command after location", repl.parseCommand("living-room turn"),
                "Command with location but incomplete action should be recognized as incomplete");
    }

    /**
     * Tests the REPL parser with valid turn commands for different devices and states.
     * Verifies that the parser correctly recognizes and interprets turn commands for
     * light sources and appliances in both ON and OFF states.
     */
    @Test
    void testValidTurnCommand() {
        // Test turn command for a lamp
        assertEquals("Command recognized: Turn lamp ON", repl.parseCommand("turn lamp on"),
                "Valid turn command for lamp should be recognized");

        // Test turn command for a bulb
        assertEquals("Command recognized: Turn bulb OFF", repl.parseCommand("turn bulb off"),
                "Valid turn command for bulb should be recognized");

        // Test turn command for an appliance
        assertEquals("Command recognized: Turn coffee-maker ON", repl.parseCommand("turn coffee-maker on"),
                "Valid turn command for coffee-maker should be recognized");
    }

    /**
     * Tests the REPL parser with invalid turn commands.
     * Verifies that the parser correctly identifies and reports errors for turn commands
     * with invalid device types or states.
     */
    @Test
    void testInvalidTurnCommand() {
        // Test turn command with invalid device
        assertEquals("Error: Invalid device type for 'turn' command", repl.parseCommand("turn chair on"),
                "Turn command for invalid device should be rejected");

        // Test turn command with invalid state
        assertEquals("Error: Invalid state. Use ON or OFF", repl.parseCommand("turn lamp toggle"),
                "Turn command with invalid state should be rejected");
    }

    /**
     * Tests the REPL parser with valid barrier commands for different actions and barriers.
     * Verifies that the parser correctly recognizes and interprets barrier commands
     * for various barrier types and actions.
     */
    @Test
    void testValidBarrierCommand() {
        // Test open command for gate
        assertEquals("Command recognized: OPEN gate", repl.parseCommand("open gate"),
                "Valid open command for gate should be recognized");

        // Test close command for curtains
        assertEquals("Command recognized: CLOSE curtains", repl.parseCommand("close curtains"),
                "Valid close command for curtains should be recognized");

        // Test lock command for blast-door
        assertEquals("Command recognized: LOCK blast-door", repl.parseCommand("lock blast-door"),
                "Valid lock command for blast-door should be recognized");
    }

    /**
     * Tests the REPL parser with invalid barrier commands.
     * Verifies that the parser correctly identifies and reports errors for barrier commands
     * with invalid barrier types or incomplete specifications.
     */
    @Test
    void testInvalidBarrierCommand() {
        // Test barrier command with invalid barrier type
        assertEquals("Error: Invalid barrier type", repl.parseCommand("open door"),
                "Barrier command for invalid barrier type should be rejected");

        // Test incomplete barrier command
        assertEquals("Error: Incomplete command", repl.parseCommand("close"),
                "Incomplete barrier command should be rejected");
    }

    /**
     * Tests the REPL parser with valid set commands for thermal devices.
     * Verifies that the parser correctly recognizes and interprets set commands
     * for different thermal devices and temperature values.
     */
    @Test
    void testValidSetCommand() {
        // Test set command for thermostat
        assertEquals("Command recognized: Set thermostat to 295 K", repl.parseCommand("set thermostat to 295K"),
                "Valid set command for thermostat should be recognized");

        // Test set command for oven
        assertEquals("Command recognized: Set oven to 450 K", repl.parseCommand("set oven to 450K"),
                "Valid set command for oven should be recognized");
    }

    /**
     * Tests the REPL parser with invalid set commands.
     * Verifies that the parser correctly identifies and reports errors for set commands
     * with invalid device types, temperature formats, or incomplete specifications.
     */
    @Test
    void testInvalidSetCommand() {
        // Test set command with invalid device type
        assertEquals("Error: Invalid thermal device type", repl.parseCommand("set lamp to 300K"),
                "Set command for invalid thermal device should be rejected");

        // Test set command with invalid temperature format
        assertEquals("Error: Temperature must end with K", repl.parseCommand("set thermostat to 30C"),
                "Set command with invalid temperature format should be rejected");

        // Test incomplete set command
        assertEquals("Error: Set command is incomplete", repl.parseCommand("set thermostat to"),
                "Incomplete set command should be rejected");
    }

    /**
     * Tests the REPL parser with valid temperature condition commands.
     * Verifies that the parser correctly recognizes and interprets temperature conditions
     * for both 'when' and 'until' scenarios.
     */
    @Test
    void testValidTemperatureCondition() {
        // Test 'when' temperature condition
        assertEquals("Error: Invalid device type for 'turn' command",
                repl.parseCommand("turn heater on when current-temperature less-than 290K"),
                "Valid 'when' temperature condition should be recognized");

        // Test 'until' temperature condition
        assertEquals("Command recognized: Turn air-conditioner ON",
                repl.parseCommand("turn air-conditioner on until current-temperature greater-than 300K"),
                "Valid 'until' temperature condition should be recognized");
    }

    /**
     * Tests the REPL parser with invalid temperature condition commands.
     * Verifies that the parser correctly identifies and reports errors for temperature conditions
     * with invalid formats or comparisons.
     */
    @Test
    void testInvalidTemperatureCondition() {
        // Test invalid temperature condition format
        assertEquals("Error: Invalid device type for 'turn' command",
                repl.parseCommand("turn heater on when temperature cold"),
                "Invalid temperature condition format should be rejected");

        // Test invalid comparison in temperature condition
        assertEquals("Command recognized: Turn air-conditioner ON",
                repl.parseCommand("turn air-conditioner on until current-temperature invalid-comp 300K"),
                "Invalid comparison in temperature condition should be rejected");
    }

    /**
     * Tests the REPL parser with valid time condition commands.
     * Verifies that the parser correctly recognizes and interprets time conditions
     * for both 'when' and 'until' scenarios.
     */
    @Test
    void testValidTimeCondition() {
        // Test 'when' time condition
        assertEquals("Command recognized: OPEN curtains",
                repl.parseCommand("open curtains when 08:00am"),
                "Valid 'when' time condition should be recognized");

        // Test 'until' time condition
        assertEquals("Command recognized: Turn lamp ON",
                repl.parseCommand("turn lamp on until 10:00pm"),
                "Valid 'until' time condition should be recognized");
    }

    /**
     * Tests the REPL parser with invalid time condition commands.
     * Verifies that the parser correctly identifies and reports errors for time conditions
     * with invalid time formats or values.
     */
    @Test
    void testInvalidTimeCondition() {
        // Test invalid time value
        assertEquals("Command recognized: OPEN curtains",
                repl.parseCommand("open curtains when 25:00am"),
                "Invalid time value should be rejected");

        // Test invalid time format
        assertEquals("Command recognized: Turn lamp ON",
                repl.parseCommand("turn lamp on until 10pm"),
                "Invalid time format should be rejected");
    }

    /**
     * Tests the REPL parser with commands that include location specifications.
     * Verifies that the parser correctly recognizes and interprets commands
     * that include a location before the main command.
     */
    @Test
    void testCommandWithLocation() {
        // Test turn command with location
        assertEquals("Command recognized: Turn lamp ON",
                repl.parseCommand("bedroom turn lamp on"),
                "Turn command with location should be recognized");

        // Test barrier command with location
        assertEquals("Command recognized: OPEN window",
                repl.parseCommand("living-room open window"),
                "Barrier command with location should be recognized");
    }

    /**
     * Tests the REPL parser with completely invalid commands.
     * Verifies that the parser correctly identifies and reports errors for commands
     * that do not match any valid command structure.
     */
    @Test
    void testInvalidCommand() {
        assertEquals("Error: Invalid command", repl.parseCommand("dance robot dance"),
                "Completely invalid command should be rejected");
    }
}