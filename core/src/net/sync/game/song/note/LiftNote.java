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

package net.sync.game.song.note;

import net.sync.game.round.judge.Judgment;

/**
 * @author Vincenzo Fortunato
 */
public class LiftNote implements JudgeableNote {
    private double beat;
    private net.sync.game.round.judge.Judgment judgment;

    public LiftNote(double beat) {
        this.beat = beat;
    }

    @Override
    public net.sync.game.round.judge.Judgment getJudgment() {
        return judgment;
    }

    @Override
    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }

    @Override
    public double getBeat() {
        return beat;
    }
}
