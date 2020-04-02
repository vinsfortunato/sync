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

package net.sync.game.round;

import net.sync.game.round.judge.Judgment;
import net.sync.game.round.judge.JudgmentHandler;

public class Life implements JudgmentHandler {
    /**
     * Gets the life value at the given time, where time is relative to the start of the music track.
     * Life is represented as a double value between 0.0 (no life) and 1.0 (max life).
     * @param time the time in seconds relative to the start of the music track.
     * @return the life value, a double between 1.0 and 0.0 (inclusive).
     */
    public double getLifeAt(double time) {
        return 0.0D;
    }

    @Override
    public void handleJudgment(double time, Judgment judgment) {

    }
}
