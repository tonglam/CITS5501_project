package com.tong.cits5501.domolect.command;

import com.tong.cits5501.domolect.constant.State;
import com.tong.cits5501.domolect.device.LightSource;

/**
 * Represents a command to control a light source.
 */
public final class LightingCommand extends Command {
    private final LightSource lightSource;
    private final State state;

    /**
     * Constructs a LightingCommand with the specified location, light source,
     * and desired state.
     *
     * @param location    the location where the command should be executed; may be null
     * @param lightSource the light source to be controlled
     * @param state       the desired state of the light source
     */
    public LightingCommand(Location location, LightSource lightSource, State state) {
        super(location);
        this.lightSource = lightSource;
        this.state = state;
    }

    /**
     * Returns the light source associated with this command.
     *
     * @return the light source to be controlled
     */
    public LightSource getLightSource() {
        return lightSource;
    }

    /**
     * Returns the desired state of the light source.
     *
     * @return the desired state, which can be either "on" or "off"
     */
    public State getState() {
        return state;
    }


    /**
     * Executes the command by sending instructions to the
     * appropriate LightSource.
     */
    @Override
    public void execute() {
        switch (getState()) {
            case ON:
                getLightSource().turnOn();
                break;
            case OFF:
                getLightSource().turnOff();
                break;
        }
    }

    @Override
    public String toString() {
        return "LightingCommand [lightSource=" + getLightSource().getName() + ", state=" + getState() + ", location=" + getLocation() + "]";
    }

}


