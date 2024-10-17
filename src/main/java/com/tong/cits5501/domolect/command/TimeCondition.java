package com.tong.cits5501.domolect.command;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a time condition defined by a specific {@link LocalTime}.
 * This condition can be used to determine when a certain time of day is reached.
 */
public final class TimeCondition extends Condition {
  private final LocalTime time;

  /**
   * Constructs a java.com.tong.cits5501.command.TimeCondition with the specified time.
   *
   * @param time the time as a {@link LocalTime}
   */
  public TimeCondition(LocalTime time) {
    this.time = Objects.requireNonNull(time, "time must not be null");
  }

  /**
   * Returns the time associated with this condition.
   *
   * @return the time as a {@link LocalTime}
   */
  public LocalTime getTime() {
    return time;
  }

  /**
   * Returns a string representation of the java.com.tong.cits5501.command.TimeCondition.
   *
   * @return a string describing the condition with its time
   */
  @Override
  public String toString() {
    return "java.com.tong.cits5501.command.TimeCondition at " + time;
  }

  /**
   * Compares this java.com.tong.cits5501.command.TimeCondition to another object for equality.
   *
   * @param o the object to compare to
   * @return true if the other object is a java.com.tong.cits5501.command.TimeCondition with the same time,
   *         false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TimeCondition that = (TimeCondition) o;
    return time.equals(that.time);
  }

  /**
   * Returns a hash code value for this java.com.tong.cits5501.command.TimeCondition.
   *
   * @return a hash code value for this object
   */
  @Override
  public int hashCode() {
    return Objects.hash(time);
  }

  /**
   * Checks if this time condition is satisfied.
   *
   * @return true if the condition is satisfied, false otherwise
   */
  @Override
  public boolean isSatisfied() {
    throw new RuntimeException("The isSatisfied method is not implemented yet.");
  }
}
