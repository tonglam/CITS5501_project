package com.tong.cits5501.domolect.device;

/**
 * Represents a light source in the system.
 *
 * <p>This class represents a light source, identified by its
 * name. It can be used to represent various types of light sources such as
 * lamps, bulbs, or neon lights.</p>
 *
 */
public final class LightSource {
  private final String name;

  /**
   * Constructs a java.com.tong.cits5501.command.LightSource with the specified name.
   *
   * @param name the name of the light source; must not be empty
   */
  public LightSource(String name) {
    this.name = name;
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
   * 
   * This method sends instructions to the light source hardware.
   * 
   * If the light source is already on, an IllegalStateException is thrown.
   */
  public void turnOn() {
    throw new RuntimeException("The turnOn method is not implemented yet.");
  }

  /**
   * Turns the light source off.
   * 
   * This method sends instructions to the light source hardware.
   * 
   * If the light source is already off, an IllegalStateException is thrown.
   */
  public void turnOff() {
    throw new RuntimeException("The turnOff method is not implemented yet.");
  }
}

