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
 * Represents a relation between a set of inputs and a set of permissible
 * outputs with the property that each input is related to exactly one output.
 * @author Vincenzo Fortunato
 */
public interface MathFunction<X, Y> {
    /**
     * Calculates the image of the given x.
     * @param x the x variable of the function.
     * @return the image of the given x.
     */
    Y f(X x);

    /**
     * Check if the image of the given x exists.
     * @param x the x variable of the function.
     * @return true if the image exists, false otherwise.
     */
    boolean isDefined(X x);

    /**
     * @return true if the function can be inverted, false otherwise.
     */
    boolean isInvertible();

    /**
     * Try to get the inverse function. Can throw an IllegalStateException. Be sure to
     * check if it's invertible by using {@link #isInvertible()} before calling this
     * method.
     * @return the inverse function.
     * @throws IllegalStateException if the function cannot be inverted.
     */
    MathFunction<Y, X> invert();
}
