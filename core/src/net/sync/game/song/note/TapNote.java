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
import net.sync.game.round.judge.TapJudgment;

public class TapNote implements JudgeableNote, ChordNote {
    private double beat;
    private net.sync.game.round.judge.TapJudgment judgment;
    private NoteResolution resolution;

    public TapNote(double beat) {
        this.beat = beat;
        this.resolution = NoteResolution.valueFromBeat(beat);
    }

    @Override
    public net.sync.game.round.judge.TapJudgment getJudgment() {
        return judgment;
    }

    @Override
    public void setJudgment(Judgment judgment) {
        if(judgment == null) {
            this.judgment = null;
        } else if(judgment instanceof net.sync.game.round.judge.TapJudgment) {
            this.judgment = (TapJudgment) judgment;
        } else {
            throw new IllegalArgumentException("Judgment must be an instance of TapJudgment!");
        }
    }

    @Override
    public double getBeat() {
        return beat;
    }

    /**
     * Gets the note resolution calculated from the note beat.
     * @return the note resolution.
     */
    public NoteResolution getResolution() {
        return resolution;
    }
}
