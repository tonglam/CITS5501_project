package com.tong.cits5501.domolect.command;

import com.tong.cits5501.domolect.Executable;

import java.util.Objects;

/**
 * Represents an augmented command that is executed based on specified conditions.
 * This class is new to the Domotopia 2.0 version of the system.
 * <p>
 * It incorporates a base java.com.tong.cits5501.command.Command, but augments it with
 * "when" conditions and "until" conditions. Either or both of these
 * can be null. If the "when" condition is null, the command is executed immediately.
 * <p>
 * If the "when" condition is non-null, the system stores the command,
 * and constantly monitors
 * its sensors and clocks to determine when the "when" conditions have been met.
 * Once they have been met, the command is executed.
 * <p>
 * If the "until" condition is non-null, then the system likewise stores
 * the command and monitors its sensors and clocks. When the appropriate
 * condition has been met, the command is reversed. (For instance, if the
 * command was to turn a coffee-maker on, then the "until" condition
 * becoming satisfied will cause the system to turn the coffee-maker off).
 */
public final class AugmentedCommand implements Executable {
    private final Condition whenCondition;
    private final Condition untilCondition;
    private final Command command;

    /**
     * Constructs an AugmentedCommand with the specified conditions and command.
     *
     * @param whenCondition  the condition that triggers the execution of the command; may be null
     * @param untilCondition the condition that ceases the execution of the command; may be null
     * @param command        the command to be executed
     */
    public AugmentedCommand(Condition whenCondition, Condition untilCondition, Command command) {
        this.command = Objects.requireNonNull(command, "command must not be null");
        this.untilCondition = untilCondition;
        this.whenCondition = whenCondition;
    }

    /**
     * Returns the condition that specifies when the command should be executed.
     *
     * @return the when condition
     */
    public Condition getWhenCondition() {
        return whenCondition;
    }

    /**
     * Returns the condition that specifies until when the command should be executed.
     *
     * @return the until condition
     */
    public Condition getUntilCondition() {
        return untilCondition;
    }

    /**
     * Returns the command that is associated with this augmented command.
     *
     * @return the command to be executed
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Returns a string representation of the AugmentedCommand.
     *
     * @return a string describing the augmented command, including its conditions and command
     */
    @Override
    public String toString() {
        return "AugmentedCommand [whenCondition=" + whenCondition +
                ", untilCondition=" + untilCondition +
                ", command=" + command + "]";
    }

    /**
     * Compares this AugmentedCommand to another object for equality.
     *
     * @param o the object to compare to
     * @return true if the other object is an AugmentedCommand with the same conditions and command, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AugmentedCommand that = (AugmentedCommand) o;
        return whenCondition.equals(that.whenCondition) &&
                untilCondition.equals(that.untilCondition) &&
                command.equals(that.command);
    }

    /**
     * Returns a hash code value for this AugmentedCommand.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(whenCondition, untilCondition, command);
    }

    /**
     * Sends appropriate instructions to hardware devices
     * to execute the base command.
     */
    @Override
    public void execute() {
        this.command.execute();
    }
}
