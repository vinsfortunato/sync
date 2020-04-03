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

/**
 * Represents a {@link net.sync.game.util.math.Graph2D} point. Points are interpolated using a specific algorithm to
 * get a function that contains them.
 * @author Vincenzo Fortunato
 */
public class Graph2DPoint {
    /**
     * The point x value
     */
    public final double x;
    /**
     * The point y value.
     */
    public double y;
    /**
     * The value of this variable, if different from 0, it is the jump discontinuity value of
     * the function at {@link #x}. Can be negative.
     */
    public double jump;
    /**
     * It has different meaning based on the type of the point.
     * <li>Point is a jump ({@link #jump} != 0): If true the image calculated at
     * {@link #x} will be {@link #y}, otherwise the image will be {@link #y} + {@link #jump}.</li>
     * <li>Point is a constant segment end point: It is used when inverting the {@link Graph2D}.
     * When the end point of a constant segment is left defined the jump point generated
     * by inverting this constant segment will also be left defined.
     */
    public boolean leftDefined;

    /**
     * Create a point left defined with y and jump that are equal to 0.
     * @param x the point x value.
     */
    public Graph2DPoint(double x) {
        this(x, 0.0D, 0.0D, false);
    }

    /**
     *
     * @param x the point x value.
     * @param y the point y value.
     * @param jump the jump value, 0.0D means no jump.
     * @param leftDefined true if the image at the given x is y, false if the image at the
     *                    given x is y + jump.
     */
    public Graph2DPoint(double x, double y, double jump, boolean leftDefined) {
        this.x = x;
        this.y = y;
        this.jump = jump;
        this.leftDefined = leftDefined;
    }

    /**
     * @return true if jump value is greater than 0.
     */
    public boolean isJump() {
        return jump > 0.0D;
    }
}
