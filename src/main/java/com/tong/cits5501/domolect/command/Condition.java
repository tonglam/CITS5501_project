package com.tong.cits5501.domolect.command;

/**
 * Represents an abstract condition that can be used in augmented commands
 * to specify when a command should take effect or when it should stop.
 * 
 * <p>
 * This class serves as a base for specific types of conditions, such as 
 * {@link TemperatureCondition} and {@link TimeCondition}, which define 
 * conditions based on temperature and time, respectively.
 * </p>
 * 
 * <p>
 * Subclasses of {@code java.com.tong.cits5501.command.Condition} should implement the logic required to
 * evaluate the specific condition they represent.
 * </p>
 */
public abstract class Condition {

  /**
   * Evaluates whether the condition is satisfied.
   * 
   * @return true if the condition is met; false otherwise.
   */
  public abstract boolean isSatisfied();

}
