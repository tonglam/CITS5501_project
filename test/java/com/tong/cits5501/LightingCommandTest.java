package com.tong.cits5501;

import com.tong.cits5501.domolect.command.LightingCommand;
import com.tong.cits5501.domolect.command.Location;
import com.tong.cits5501.domolect.constant.State;
import com.tong.cits5501.domolect.device.LightSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LightingCommand}.
 * Contains unit tests for the LightingCommand constructor based on ISP analysis.
 */
public class LightingCommandTest {

    /**
     * Test ID: TC001
     * Tests the LightingCommand constructor with a non-null Location, valid LightSource, and ON state.
     */
    @Test
    void testLightingCommandWithNonNullLocationLampOn() {
        // Arrange
        Location location = new Location("Living Room");
        LightSource lightSource = new LightSource("Lamp");
        State state = State.ON;

        // Act
        LightingCommand command = new LightingCommand(location, lightSource, state);

        // Assert
        assertNotNull(command, "LightingCommand object should be successfully created");
        assertEquals(location, command.getLocation(), "getLocation() should return the 'Living Room' Location object");
        assertEquals(lightSource, command.getLightSource(), "getLightSource() should return the 'Lamp' LightSource object");
        assertEquals(State.ON, command.getState(), "getState() should return State.ON");
    }

    /**
     * Test ID: TC002
     * Tests the LightingCommand constructor with a null Location, valid LightSource, and OFF state.
     */
    @Test
    void testLightingCommandWithNullLocationBulbOff() {
        // Arrange
        Location location = null;
        LightSource lightSource = new LightSource("Bulb");
        State state = State.OFF;

        // Act
        LightingCommand command = new LightingCommand(location, lightSource, state);

        // Assert
        assertNotNull(command, "LightingCommand object should be successfully created");
        assertNull(command.getLocation(), "getLocation() should return null");
        assertEquals(lightSource, command.getLightSource(), "getLightSource() should return the 'Bulb' LightSource object");
        assertEquals(State.OFF, command.getState(), "getState() should return State.OFF");
    }

}