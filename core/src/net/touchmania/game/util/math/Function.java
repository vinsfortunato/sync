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
 * Represents a relation between a set of inputs and a set of permissible
 * outputs with the property that each input is related to exactly one output.
 * @author flood2d
 */
public interface Function<X, Y> {
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
    Function<Y, X> invert();
}
