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

package net.touchmania.game.song.note;

import net.touchmania.game.round.judge.Judgment;
import net.touchmania.game.round.judge.MineJudgment;

/**
 * @author Vincenzo Fortunato
 */
public class MineNote implements JudgeableNote {
    private double beat;
    private MineJudgment judgment;

    public MineNote(double beat) {
        this.beat = beat;
    }

    @Override
    public void setJudgment(Judgment judgment) {
        if(judgment == null) {
            this.judgment = null;
        } else if(judgment instanceof MineJudgment) {
            this.judgment = (MineJudgment) judgment;
        } else {
            throw new IllegalArgumentException("Judgment must be an instance of MineJudgment!");
        }
    }

    @Override
    public MineJudgment getJudgment() {
        return judgment;
    }

    @Override
    public double getBeat() {
        return beat;
    }
}
