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

import net.sync.game.song.Timing;
import net.sync.game.util.math.StepFunction;

import java.util.Map;

/**
 * A speed modifier that keeps BPM at a constant given value.
 */
public class ConstantSpeedModifier extends SpeedModifier {
    private net.sync.game.util.math.StepFunction<Double, Double> speedFunction;

    /**
     * Construct the modifier from timing data and the constant bpm.
     * @param timing the timing
     * @param bpm the constant bpm
     */
    public ConstantSpeedModifier(Timing timing, double bpm) {
        speedFunction = new StepFunction<>(1.0D);

        //Init speed function
        for(Map.Entry<Double, Double> entry : timing.getTimingData().bpms.entrySet()) {
            speedFunction.putStep(entry.getKey(), bpm / entry.getValue());
        }
    }

    @Override
    public double getSpeedAt(double beat) {
        return speedFunction.f(beat);
    }
}
