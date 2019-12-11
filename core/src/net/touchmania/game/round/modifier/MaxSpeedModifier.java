/*
 * Copyright 2019 Vincenzo Fortunato
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

package net.touchmania.game.round.modifier;

import net.touchmania.game.song.TimingData;

import java.util.HashMap;
import java.util.Map;

/**
 * Set a maximum speed for notes.
 * If a song has a variable BPM it will match the predominant BPM.
 */
public class MaxSpeedModifier extends SpeedModifier {
    private double speed;

    /**
     * Construct a modifier from timing data and maximum bpm.
     * @param data the timing data
     * @param bpm the max allowed bpm
     */
    public MaxSpeedModifier(TimingData data, double bpm) {
        double maxBPM = 1;

        //TODO predominantBPM instead of maxBPM but we need song length to calculate accurate BPM durations
        //Find maxBPM
        for(Map.Entry<Double, Double> entry : data.bpms.entrySet()) {
            if(entry.getValue() > maxBPM) {
                maxBPM = entry.getValue();
            }
        }

        //Calculate speed
        speed = bpm / maxBPM;
    }

    @Override
    public double getSpeedAt(double beat) {
        return speed;
    }
}
