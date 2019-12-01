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

public final class MathUtils {
    /**
     * Restrict a value to a given range.
     * @param <T> a comparable type.
     * @param min range minimum.
     * @param max range maximum.
     * @param value value to clamp.
     * @return min if value is less than min, max if value is greater than max, value otherwise.</>
     */
    public static <T extends Comparable<T>> T clamp(T min, T max, T value) {
        if (min == null || value == null || max == null) {
            throw new NullPointerException("clamp parameters cannot be null");
        }

        if (value.compareTo(min) < 0) return min;
        if (value.compareTo(max) > 0) return max;
        return value;
    }
}
