package com.tong.cits5501.domolect.command;

/**
 * Represents a command to control a thermal device.
 * This command works by sending instructions to the relevant
 * thermal device (for instance, an oven or thermostat) which
 * set the device's target temperature.
 */
public final class ThermalDeviceCommand extends Command {
  private final String thermalDevice;
  private final int temperature;

  /**
   * Constructs a java.com.tong.cits5501.command.ThermalDeviceCommand with the specified location,
   * thermal device name, and desired temperature.
   * 
   * A location can be left off, if the device name is unambiguous.
   * (For instance, if the device name is "oven", and there is only one
   * oven on the premises.)
   * However, if the location is left off and the device name is ambiguous
   * because there are devices with that name in multiple locations,
   * an IllegalArgumentException is thrown.
   * 
   * @param location the location where the command is to be executed; 
   *                 can be null if the command applies globally
   * @param thermalDevice the name of the thermal device to control
   * @param temperature the desired temperature to be set on the device,
   *                    in Kelvin
   */
  public ThermalDeviceCommand(Location location, String thermalDevice, int temperature) {
    super(location);
    this.thermalDevice = thermalDevice;
    this.temperature = temperature;
  }

  /**
   * Returns the name of the thermal device associated with this command.
   * 
   * @return the name of the thermal device
   */
  public String getThermalDevice() {
    return thermalDevice;
  }

  /**
   * Returns the desired temperature for the thermal device.
   * 
   * @return the temperature to be set on the thermal device
   */
  public int getTemperature() {
    return temperature;
  }

  /**
   * Executes the command by sending instructions to the specified device.
   */
  @Override
  public void execute() {
	 throw new RuntimeException("The turnOff method is not implemented yet.");
  }
}
