/*
 * Copyright 2020 Vincenzo Fortunato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sync.game.util.math;

import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a step function, a piecewise constant function having only finitely
 * many pieces. For a given x the {@link #f(X)} will be computed by getting the
 * step value associated to the interval that starts from the greatest key x
 * less than or equal to the given x.
 * @param <X> the function abscissa type
 * @param <Y> the function ordinate type
 */
public class StepFunction<X extends Comparable<X>, Y> implements MathFunction<X, Y> {
    private Y defaultStep;
    private TreeMap<X, Y> steps = new TreeMap<>();

    /**
     * Construct a step function with single interval covering the entire domain and step value
     * equal to the given defaultStep.
     * @param defaultStep
     */
    public StepFunction(Y defaultStep) {
        this.defaultStep = defaultStep;
    }

    /**
     * Construct a step function with the given intervals, constant step values and default step.
     * The default step is the step value of the interval outside the given intervals defined by
     * the given x and y arrays.
     * @param defaultStep the default step.
     * @param x the intervals bounds
     * @param y the step value of the interval that starts from the corresponding x.
     */
    public StepFunction(Y defaultStep, X[] x, Y[] y) {
        if(x.length != y.length)
            throw new IllegalArgumentException("x array length must equal y array length.");

        this.defaultStep = defaultStep;

        for(int i = 0; i < x.length; i++) {
            putStep(x[i], y[i]);
        }
    }

    /**
     * Put a new interval starting from the given x and with the given y step value.
     * @param x the start of the constant step interval.
     * @param y the step value associated to the interval.
     */
    public void putStep(X x, Y y) {
        steps.put(x, y);
    }

    /**
     * Remove the interval starting from the given x.
     * @param x the start of the constant step interval to remove.
     * @return the step value associated to the removed interval, or null
     * if there was no interval starting at the given x.
     */
    public Y removeStep(X x) {
        return steps.remove(x);
    }

    /**
     * Calculates the image of the given x. It is calculated by getting step value of
     * the interval starting from the greatest key x less than or equal to the given x.
     * @param x the x variable of the function.
     * @return the image of x, the step value associated to the interval that contains the given x.
     */
    @Override
    public Y f(X x) {
        Map.Entry<X, Y> entry = steps.floorEntry(x);
        if(entry != null) {
            return entry.getValue();
        } else {
            return defaultStep;
        }
    }

    @Override
    public boolean isDefined(X x) {
        return true;
    }

    @Override
    public boolean isInvertible() {
        return false;
    }

    @Override
    public MathFunction<Y, X> invert() {
        throw new IllegalStateException("Step function has no inverse.");
    }
}
