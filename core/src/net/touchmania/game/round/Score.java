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

package net.touchmania.game.round;

public class Score implements JudgmentHandler {

    /**
     * Gets the accuracy value at the given time, where time is relative to the start of the music track.
     * Accuracy is represented as a double value between 0.0 and 1.0.
     * @param time the time in seconds relative to the start of the music track.
     * @return the accuracy value, a double between 1.0 and 0.0 (inclusive).
     */
    public double getAccuracyAt(double time) {
        return 0.0D;
    }

    /**
     * Gets the score value at the given time, where time is relative to the start of the music track.
     * Score is represented as a positive long value.
     * @param time the time in seconds relative to the start of the music track.
     * @return the score value, a long positive value.
     */
    public long getScoreAt(double time) {
        return 0;
    }

    /**
     * Gets the combo value at the given time, where time is relative to the start of the music track.
     * Combo is represented as a positive int value.
     * @param time the time in seconds relative to the start of the music track.
     * @return the combo value, a int positive value.
     */
    public int getComboAt(double time) {
        return 0;
    }

    @Override
    public void handleJudgment(double time, Judgment judgment) {

    }

    @Override
    public void handleMineExplosion(double time) {

    }
}
