/*
 * Copyright 2018 Vincenzo Fortunato
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

package net.touchmania.game.util.math;

import com.google.common.base.Preconditions;

/**
 * Represents a straight-line with function y = mx + q, where m is the slope or gradient of the line and q
 * is the y-intercept of the line.
 *
 * @author Vincenzo Fortunato
 */
public class Line2D implements MathFunction<Double, Double> {
    private final double m;
    private final double q;

    /**
     * @param m the slope or gradient of the line.
     * @param q the y-intercept of the line.
     */
    public Line2D(double m, double q) {
        this.m = m;
        this.q = q;
    }

    @Override
    public Double f(Double x) {
        if(x == null) {
            return null;
        }
        return m * x + q;
    }

    @Override
    public boolean isDefined(Double x) {
        return true;
    }

    @Override
    public boolean isInvertible() {
        return Double.compare(m, 0.0D) != 0;
    }

    @Override
    public Line2D invert() {
        Preconditions.checkState(isInvertible(), "The function cannot be inverted.");
        double newM = 1.0D / m;
        double newQ = -q / m;
        return new Line2D(newM, newQ);
    }

    /**
     * @return the slope or gradient of the line.
     */
    public double getM() {
        return m;
    }

    /**
     * @return the y-intercept of the line.
     */
    public double getQ() {
        return q;
    }

    /**
     * Create a line from two distinct points.
     * @param x1 the first point x value.
     * @param y1 the first point y value.
     * @param x2 the second point x value.
     * @param y2 the second point y value.
     * @return the line.
     * @throws IllegalArgumentException if x1 equals x2
     */
    public static Line2D from(double x1, double y1, double x2, double y2) {
        if(Double.compare(x1, x2) == 0) {
            throw new IllegalArgumentException("x1 can not be equal to x2.");
        }
        double m = (y2 - y1) / (x2 - x1);
        double q = -m * x1 + y1;
        return new Line2D(m, q);
    }
}
