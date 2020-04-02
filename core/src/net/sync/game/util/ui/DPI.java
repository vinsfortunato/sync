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

package net.sync.game.util.ui;

/**
 * DPI classes. It is used to calculate the appropriate values for
 * pixel density dependent properties.
 */
public enum DPI {
    LOW(320, 0.5f),
    MEDIUM(480, 1.0f),
    HIGH(640, 2.0f);

    public final int dpi;
    public final float scale;

    DPI(int dpi, float scale) {
        this.dpi = dpi;
        this.scale = scale;
    }

    /**
     * Returns the closest dpi class.
     * @param dpi the dpi to fit into a class.
     * @return the closest dpi class.
     */
    public static DPI closest(int dpi) {
        DPI closest = null;
        for(DPI d : values()) {
            if(closest == null || Math.abs(d.dpi - dpi) < Math.abs(closest.dpi - dpi)) {
                closest = d;
            }
        }
        return closest;
    }
}
