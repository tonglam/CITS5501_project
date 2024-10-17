package com.tong.cits5501.domolect.command;

/**
 * Represents a physical location within the premises where commands can be executed.
 * Each location is identified by a unique name.
 */
public final class Location {
  private final String name;

  /**
   * Constructs a new Location with the specified name.
   *
   * @param name the name of the location, which must be unique within the system.
   */
  public Location(String name) {
    this.name = name;
  }

  /**
   * Retrieves the name of this location.
   *
   * @return the name of the location.
   */
  public String getName() {
    return name;
  }

  /**
   * Checks whether this Location is equal to another object.
   * Two Locations are considered equal if they have the same name.
   * (Note, however, that since names must be unique, if a Location
   * compares equal to another Location, it follows it must in fact
   * be the same object.)
   *
   * @param obj the object to compare with this Location.
   * @return true if the given object is a Location with the same name; false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Location)) {
      return false; 
    }
    Location other = (Location) obj;
    return name.equals(other.name);
  }

  /**
   * Returns a hash code value for this Location.
   *
   * @return a hash code value for this Location, based on its name.
   */
  @Override
  public int hashCode() {
    return name.hashCode(); // Use the name's hash code
  }
}
