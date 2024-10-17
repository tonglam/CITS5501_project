package com.tong.cits5501.domolect.command;

import com.tong.cits5501.domolect.constant.Comparison;

import java.util.Objects;

/**
 * This condition can be used to determine if the current temperature
 * is less than, greater than, or equal to some target temperature.
 * <p>
 * The condition is considered satisfied when the actual
 * temperature holds this relation to the target temperature.
 * <p>
 * For example, if the comparison is LESS_THAN, then
 * the condition is fulfilled when the actual temperature
 * is less than the target temperature.
 */
public final class TemperatureCondition extends Condition {

    private final int kelvin;
    private final Comparison comparison;

    /**
     * Constructs a TemperatureCondition with the specified temperature in Kelvin
     * and the desired comparison.
     *
     * @param kelvin     the temperature in Kelvin; must be a positive value
     * @param comparison the comparison result to be applied (LESS_THAN, EQUAL_TO, GREATER_THAN)
     */
    public TemperatureCondition(int kelvin, Comparison comparison) {
        if (kelvin <= 0) {
            throw new IllegalArgumentException("Temperature must be a positive value.");
        }
        this.kelvin = kelvin;
        this.comparison = comparison;
    }

    /**
     * Returns the target temperature in Kelvin.
     *
     * @return the target temperature in Kelvin
     */
    public int getKelvin() {
        return kelvin;
    }

    /**
     * Returns the comparison result for this condition.
     *
     * @return the comparison result (LESS_THAN, EQUAL_TO, GREATER_THAN)
     */
    public Comparison getComparison() {
        return comparison;
    }

    /**
     * Returns a string representation of the TemperatureCondition.
     *
     * @return a string describing the condition with its temperature in Kelvin
     */
    @Override
    public String toString() {
        return "TemperatureCondition at " + kelvin + "K, comparison: " + comparison;
    }

    /**
     * Compares this TemperatureCondition to another object for equality.
     *
     * @param o the object to compare to
     * @return true if the other object is a TemperatureCondition
     * with the same Kelvin value and comparison result, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemperatureCondition that = (TemperatureCondition) o;
        return kelvin == that.kelvin && comparison == that.comparison;
    }

    /**
     * Returns a hash code value for this TemperatureCondition.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(kelvin, comparison);
    }

    /**
     * Checks if this temperature condition is satisfied, by sending
     * queries to appropriate sensors.
     *
     * @return true if the condition is satisfied, false otherwise
     */
    @Override
    public boolean isSatisfied() {
        throw new RuntimeException("The isSatisfied method is not implemented yet.");
    }
}
