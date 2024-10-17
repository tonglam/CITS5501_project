package com.tong.cits5501.domolect.device;

import com.tong.cits5501.domolect.constant.State;

/**
 * The {@code Appliance} class represents an appliance within the
 * Domotopia automation system.
 * <p>
 * An appliance is a type of device
 * that can be controlled using simple commands like turning the appliance
 * "on" or "off".
 *
 *
 * <h2>Example usage:</h2>
 * <pre>
 * Appliance coffeeMaker = new Appliance("coffee-maker");
 * System.out.println(coffeeMaker.getName()); // Output: coffee-maker
 * </pre>
 */
public final class Appliance {

    /**
     * The name of the appliance.
     * <p>
     * This name must be a valid appliance identifier
     * (e.g., "coffee-maker", "oven", "air-conditioner").
     */
    private final String name;
    private State state;

    /**
     * Constructs an {@code Appliance} with the specified name.
     *
     * @param name The name of the appliance, must be a non-null, non-empty
     *             string corresponding to a valid appliance name.
     */
    public Appliance(String name, State state) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Appliance name cannot be null or empty.");
        }
        this.name = name;
        this.state = state;
    }

    /**
     * Returns the name of the appliance.
     *
     * <p>This method returns the name of the appliance.
     *
     * @return the name of the appliance
     */
    public String getName() {
        return name;
    }

    /**
     * Turns the appliance on.
     * <p>
     * This method sends a command to the appliance hardware which
     * turns the appliance on.
     * <p>
     * If the appliance is already on, an IllegalStateException is thrown.
     */
    public void turnOn() {
        if (State.ON.equals(state)) {
            throw new IllegalStateException("Appliance is already on: " + name);
        }
        state = State.ON;
        System.out.println("Turning on the appliance: " + name);
    }

    /**
     * Turns the appliance off.
     * <p>
     * This method sends a command to the appliance hardware which
     * turns the appliance off.
     * <p>
     * If the appliance is already off, an IllegalStateException is thrown.
     */
    public void turnOff() {
        if (State.OFF.equals(state)) {
            throw new IllegalStateException("Appliance is already off: " + name);
        }
        state = State.OFF;
        System.out.println("Turning off the appliance: " + name);
    }

    @Override
    public String toString() {
        return "Appliance{name='" + name + "', state=" + state + "}";
    }

}
