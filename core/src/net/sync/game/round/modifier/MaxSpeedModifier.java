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

/**
 * Set a maximum speed for notes.
 * If a song has a variable BPM it will match the dominant BPM.
 */
public class MaxSpeedModifier extends SpeedModifier {
    private double speed;

    /**
     * Construct a modifier from timing data and maximum bpm.
     * @param dominantBPM the song dominant bpm.
     * @param maxBpm the max allowed bpm
     */
    public MaxSpeedModifier(double dominantBPM, double maxBpm) {
        this.speed = maxBpm / dominantBPM;
    }

    @Override
    public double getSpeedAt(double beat) {
        return speed;
    }
}
