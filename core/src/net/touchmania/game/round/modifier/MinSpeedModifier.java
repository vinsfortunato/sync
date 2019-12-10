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

/**
 * Set a minimum speed for notes. If a song has a variable BPM
 * it will match the minimum BPM (e.g. if your MMod is m600 and the song goes
 * from 150 to 300 bpm, note speed will be from 600 to 750).
 */
public class MinSpeedModifier extends SpeedModifier {
    private double speed;

    /**
     * Construct a modifier from timing data and minimum bpm.
     * @param data the timing data
     * @param bpm the max allowed bpm
     */
    public MinSpeedModifier(TimingData data, double bpm) {
        double minBPM = data.bpms.firstEntry().getValue();

        //Calculate speed
        speed = bpm / minBPM;
    }

    @Override
    public double getSpeedAt(double beat) {
        return speed;
    }
}
