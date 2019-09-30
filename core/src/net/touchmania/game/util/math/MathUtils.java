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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author flood2d
 */
public final class MathUtils {
    /**
     * Can be used to restrict a value to a given range.
     *
     * @param <T>
     * @param min range minimum.
     * @param max range maximum.
     * @param value value to clamp.
     * @return min if value < min, max if value > max, value otherwise.</>
     */
    public static <T extends Comparable<T>> T clamp(T min, T max, T value) {
        if (min == null || value == null || max == null) {
            throw new NullPointerException("clamp parameters cannot be null");
        }

        if (value.compareTo(min) < 0) return min;
        if (value.compareTo(max) > 0) return max;
        return value;
    }

    /**
     * <p>Example:
     * <table border>
     * <caption><b>Rounding mode examples</b></caption>
     * <tr valign=top><th>Input Number</th>
     * <th>Input rounded to 1 decimal digit (decimalPlaces = 3)<br>
     * <tr align=right><td>5.55</td>  <td>5.6</td>
     * <tr align=right><td>2.54</td>  <td>2.5</td>
     * <tr align=right><td>1.67</td>  <td>1.7</td>
     * <tr align=right><td>1.02</td>  <td>1.0</td>
     * <tr align=right><td>1.00</td>  <td>1.0</td>
     * </table>
     *
     * @param value the value to round.
     * @param decimalPlaces the decimalPlaces
     * @return the value rounded to n decimal places.
     */
    public static double roundDecimals(double value, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places cannot be negative!");
        }
        StringBuilder pattern = new StringBuilder(decimalPlaces == 0 ? "#" : "#.");
        for (int i = 0; i < decimalPlaces; i++) {
            pattern.append("#");
        }
        DecimalFormat df = new DecimalFormat(pattern.toString());
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        try {
            return NumberFormat.getInstance().parse(df.format(value)).doubleValue();
        } catch (ParseException e) {
            return value;
        }
    }
}
