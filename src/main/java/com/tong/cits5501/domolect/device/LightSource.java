package com.tong.cits5501.domolect.device;

import com.tong.cits5501.domolect.constant.State;

/**
 * Represents a light source in the system.
 *
 * <p>This class represents a light source, identified by its
 * name. It can be used to represent various types of light sources such as
 * lamps, bulbs, or neon lights.</p>
 */
public final class LightSource {
    private final String name;
    private State state;

    /**
     * Constructs a LightSource with the specified name.
     *
     * @param name the name of the light source; must not be empty
     */
    public LightSource(String name, State state) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Light source name cannot be null or empty.");
        }
        this.name = name;
        this.state = state;
    }

    /**
     * Returns the name of the light source.
     *
     * @return the name of the light source
     */
    public String getName() {
        return name;
    }

    /**
     * Turns the light source on.
     * <p>
     * This method sends instructions to the light source hardware.
     * <p>
     * If the light source is already on, an IllegalStateException is thrown.
     */
    public void turnOn() {
        if (State.ON.equals(state)) {
            throw new IllegalStateException("Light source is already on: " + name);
        }
        state = State.ON;
        System.out.println("Turning on the light source: " + name);
    }

    /**
     * Turns the light source off.
     * <p>
     * This method sends instructions to the light source hardware.
     * <p>
     * If the light source is already off, an IllegalStateException is thrown.
     */
    public void turnOff() {
        if (State.OFF.equals(state)) {
            throw new IllegalStateException("Light source is already off: " + name);
        }
        state = State.OFF;
        System.out.println("Turning off the light source: " + name);
    }

    @Override
    public String toString() {
        return "LightSource{name='" + name + "', state=" + state + "}";
    }

}

