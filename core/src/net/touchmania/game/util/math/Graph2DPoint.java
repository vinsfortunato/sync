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

/**
 * Represents a {@link Graph2D} point. Points are interpolated using a specific algorithm to
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
