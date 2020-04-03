/*
 * Copyright (c) 2020 Vincenzo Fortunato.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.sync.game.util.math;

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
