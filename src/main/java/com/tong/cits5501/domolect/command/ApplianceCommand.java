package com.tong.cits5501.domolect.command;

import com.tong.cits5501.domolect.constant.State;
import com.tong.cits5501.domolect.device.Appliance;

/**
 * Represents a command to control an {@link Appliance} within the
 * Domotopia system. This command specifies a target {@link Appliance}
 * and the desired {@link State}, such as turning an appliance on or off.
 *
 * <p>Commands are location-specific, meaning they can optionally include a
 * {@link Location} where the appliance is located. If no location is
 * specified, the command applies globally to the entire premises.
 *
 * <p>Example usage:
 *
 * <pre>
 *   // Turn on the coffee maker in the kitchen
 *   cmd = new ApplianceCommand(
 *       Location("kitchen"),
 *       Appliance("coffee-maker"),
 *       State.ON
 *   );
 * </pre>
 */
public final class ApplianceCommand extends Command {

    /**
     * The {@link Appliance} that this command applies to, such as a coffee maker
     * or air conditioner.
     */
    private final Appliance appliance;

    /**
     * The desired {@link State} of the appliance, turning it on or off.
     */
    private final State state;

    /**
     * Constructs a new {@code ApplianceCommand} to control the specified
     * appliance at the given location and set it to the desired state.
     *
     * @param location  The {@link Location} where the appliance is situated.
     *                  This can be null if the command applies
     *                  globally to the entire premises.
     * @param appliance The {@link Appliance} that is the target of this
     *                  command.
     * @param state     The desired {@link State} for the appliance.
     */
    public ApplianceCommand(Location location, Appliance appliance, State state) {
        super(location);
        this.appliance = appliance;
        this.state = state;
    }

    /**
     * Returns the {@link Appliance} that this command controls.
     *
     * @return The {@link Appliance} for this command.
     */
    public Appliance getAppliance() {
        return appliance;
    }

    /**
     * Returns the desired {@link State} of the appliance, such as whether
     * it should be turned on or off.
     *
     * @return The {@link State} for this command.
     */
    public State getState() {
        return state;
    }

    /**
     * Executes the command by sending instructions to the
     * appropriate Appliance.
     */
    @Override
    public void execute() {
        if (state == State.ON) {
            appliance.turnOn();
        } else {
            appliance.turnOff();
        }
    }

    @Override
    public String toString() {
    	return "ApplianceCommand [appliance=" + appliance.getName() + ", state=" + state + ", location=" + getLocation() + "]";
    }

}
