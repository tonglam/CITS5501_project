package com.tong.cits5501.domolect;

/**
 * Represents an executable action that can be performed on a device.
 *
 * This interface defines a contract for classes that encapsulate commands or
 * actions that can be executed. Implementing classes should provide the specific
 * implementation of the execute method to define the behavior when the action is
 * performed.
 */
public interface Executable {

  /**
   * Executes the defined action or command.
   */
  void execute();
}
