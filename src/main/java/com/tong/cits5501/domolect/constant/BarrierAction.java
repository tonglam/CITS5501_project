package com.tong.cits5501.domolect.constant;

import com.tong.cits5501.domolect.device.Barrier;

/**
 * Represents the various actions that can be performed on a {@link Barrier}
 * within the Domotopia system. These actions are used to control barriers such
 * as gates, windows, doors, and other access points.
 *
 * <p>The four available actions are:
 * <ul>
 *   <li>{@code LOCK} - Secure the barrier, preventing access.</li>
 *   <li>{@code UNLOCK} - Release the barrier, allowing access.</li>
 *   <li>{@code OPEN} - Open the barrier (e.g., open a gate, lift a trapdoor).</li>
 *   <li>{@code CLOSE} - Close the barrier (e.g., lower a portcullis, close a window).</li>
 * </ul>
 *
 * <p>Example usage:
 *
 * <pre>
 *   // Unlock the front gate using a BarrierCommand
 *   Barrier frontGate = new Barrier("gate");
 *   BarrierCommand unlockCommand = new BarrierCommand(
 *       Location("front-yard"),
 *       frontGate,
 *       BarrierAction.UNLOCK
 *   );
 * </pre>
 */
public enum BarrierAction {

    /**
     * Locks the barrier, preventing access.
     */
    LOCK,

    /**
     * Unlocks the barrier, allowing access.
     */
    UNLOCK,

    /**
     * Opens the barrier, providing access or visibility.
     */
    OPEN,

    /**
     * Closes the barrier, restricting access or visibility.
     */
    CLOSE
}
