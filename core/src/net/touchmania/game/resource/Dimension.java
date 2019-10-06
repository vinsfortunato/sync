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

package net.touchmania.game.resource;

import net.touchmania.game.util.ui.DPI;

public class Dimension {
    private final float baseValue;

    public Dimension(float baseValue) {
        this.baseValue = baseValue;
    }

    /**
     * Gets the base value.
     * @return the dimension base value
     */
    public float getBaseValue() {
        return baseValue;
    }

    /**
     * Gets the value relative to device screen density.
     * @return the dimension value.
     */
    public float getValue() {
        return baseValue * DPI.getDeviceDPI().scale;
    }

    /**
     * Gets the value relative to device screen density rounded to an integer.
     * @return the dimension value as a rounded int.
     */
    public int getIntValue() {
        return (int) getValue();
    }

    public Dimension copy() {
        return new Dimension(baseValue);
    }
}
