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

package net.sync.game.round.modifier;

import com.google.common.base.Preconditions;

/**
 * A speed modifier that provides a constant speed value.
 */
public class MultiplySpeedModifier extends SpeedModifier {
    private double speed;

    /**
     * Construct a modifier with a constant speed.
     * @param speed the constant speed.
     */
    public MultiplySpeedModifier(double speed) {
        Preconditions.checkArgument(speed > 0.0, "Speed must greater than 0.0!");
        this.speed = speed;
    }

    @Override
    public double getSpeedAt(double beat) {
        return speed;
    }
}
