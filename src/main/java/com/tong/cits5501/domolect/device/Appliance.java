package com.tong.cits5501.domolect.device;

/**
 * The {@code java.com.tong.cits5501.command.Appliance} class represents an appliance within the
 * Domotopia automation system.
 *
 * An appliance is a type of device 
 * that can be controlled using simple commands like turning the appliance 
 * "on" or "off".
 * 
 * 
 * <h2>Example usage:</h2>
 * <pre>
 * java.com.tong.cits5501.command.Appliance coffeeMaker = new java.com.tong.cits5501.command.Appliance("coffee-maker");
 * System.out.println(coffeeMaker.getName()); // Output: coffee-maker
 * </pre>
 */
public final class Appliance {

  /**
   * The name of the appliance.
   * 
   * This name must be a valid appliance identifier 
   * (e.g., "coffee-maker", "oven", "air-conditioner").
   */
  private final String name;

  /**
   * Constructs an {@code java.com.tong.cits5501.command.Appliance} with the specified name.
   * 
   * @param name The name of the appliance, must be a non-null, non-empty 
   *             string corresponding to a valid appliance name.
   */
  public Appliance(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("java.com.tong.cits5501.command.Appliance name cannot be null or empty.");
    }
    this.name = name;
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
   * 
   * This method sends a command to the appliance hardware which
   * turns the appliance on.
   * 
   * If the appliance is already on, an IllegalStateException is thrown.
   */
  public void turnOn() {
    throw new RuntimeException("The turnOn method is not implemented yet.");
  }

  /**
   * Turns the appliance off.
   * 
   * This method sends a command to the appliance hardware which
   * turns the appliance off.
   * 
   * If the appliance is already off, an IllegalStateException is thrown.
   */
  public void turnOff() {
    throw new RuntimeException("The turnOff method is not implemented yet.");
  }


}
