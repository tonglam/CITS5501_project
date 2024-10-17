package com.tong.cits5501.domolect.command;

import com.tong.cits5501.domolect.Executable;

/**
 * Represents a base class for commands within the Domotopia system. 
 * This class serves as a foundation for all specific command types 
 * (e.g., appliance commands, barrier commands, etc.).
 * 
 * <p>Commands are location-specific, meaning they can optionally include a 
 * {@link Location}. If no location is 
 * specified, the command applies globally to the entire premises.
 * 
 * <p>Concrete subclasses must implement the specific behavior of the 
 * command and define the context in which it operates.
 */
public abstract class Command implements Executable {
  
  private final Location location;

  /**
   * Constructs a new {@code java.com.tong.cits5501.command.Command} with the specified location.
   * 
   * @param location The location where the command is applicable. 
   *                 Can be null for commands that apply globally.
   */
  public Command(Location location) {
    this.location = location;
  }

  /**
   * Returns the location where this command is applicable.
   * 
   * @return The {@link Location} associated with this command, 
   *         or null if the command applies globally.
   */
  public Location getLocation() {
    return location;
  }

  /**
   * Executes the command. The specific implementation will be defined 
   * in subclasses of {@code java.com.tong.cits5501.command.Command}.
   */
  @Override
  public abstract void execute();

  @Override
  public String toString() {
  	return "java.com.tong.cits5501.command.Command [location=" + location + "]";
  }
  
}
