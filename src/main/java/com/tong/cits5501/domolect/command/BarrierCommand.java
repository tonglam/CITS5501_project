package com.tong.cits5501.domolect.command;

import com.tong.cits5501.domolect.constant.BarrierAction;
import com.tong.cits5501.domolect.device.Barrier;

/**
 * Represents a command to perform an action on a {@link Barrier} within the
 * Domotopia system. This class encapsulates the details of the action to be 
 * taken (e.g., locking, unlocking, opening, or closing) and the specific 
 * barrier on which the action will be executed.
 * 
 * <p>Example usage:
 * 
 * <pre>
 *   java.com.tong.cits5501.command.Barrier garageDoor = new java.com.tong.cits5501.command.Barrier("garage-door");
 *   java.com.tong.cits5501.command.BarrierCommand openGarageCommand = new java.com.tong.cits5501.command.BarrierCommand(
 *       new java.com.tong.cits5501.command.Location("garage"),
 *       java.com.tong.cits5501.command.BarrierAction.OPEN,
 *       garageDoor
 *   );
 * </pre>
 */
public final class BarrierCommand extends Command {
  
  private final BarrierAction action;
  private final Barrier barrier;

  /**
   * Constructs a new {@code java.com.tong.cits5501.command.BarrierCommand} with the specified location,
   * action, and barrier.
   * 
   * @param location The location of the barrier in the home automation system.
   *                 May be null if location is inapplicable (e.g. if there
   *                 is only one barrier of that type, then the location is
   *                 unambiguous and therefore not needed).
   * @param action The action to perform on the barrier (e.g., lock, unlock, open, close).
   * @param barrier The barrier on which the action will be performed.
   */
  public BarrierCommand(Location location, BarrierAction action, Barrier barrier) {
    super(location);
    this.action = action;
    this.barrier = barrier;
  }

  /**
   * Returns the action to be performed on the barrier.
   * 
   * @return The {@link BarrierAction} representing the desired action.
   */
  public BarrierAction getAction() {
    return action;
  }

  /**
   * Returns the barrier that this command will act upon.
   * 
   * @return The {@link Barrier} associated with this command.
   */
  public Barrier getBarrier() {
    return barrier;
  }
  
  /**
   * Executes the command by sending instructions to the
   * appropriate java.com.tong.cits5501.command.Barrier.
   */
  @Override
  public void execute() {
    switch (action) {
      case OPEN:
        barrier.open();
        break;
      case CLOSE:
        barrier.close();
        break;
      case LOCK:
        barrier.lock();
        break;
      case UNLOCK:
        barrier.unlock();
        break;
    }
  }

}
