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

package net.sync.game.round.judge;

/**
 * Provides judgment data for a note.
 */
public abstract class Judgment {
    private double genTime;

    /**
     * @param genTime the judgment's generation time relative to the start of the music track.
     */
    public Judgment(double genTime) {
        this.genTime = genTime;
    }

    /**
     * Get the time relative to the music track when the judgment has been generated.
     * @return the judgment's generation time relative to the start of the music track.
     */
    public double getGenTime() {
        return genTime;
    }
}
