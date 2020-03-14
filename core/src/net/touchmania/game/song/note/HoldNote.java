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
import net.touchmania.game.round.judge.TailJudgment;
import net.touchmania.game.round.judge.TapJudgment;

public class HoldNote implements JudgeableLengthyNote, ChordNote {
    private double beat;
    private double length;
    private TapJudgment headJudgment;
    private TailJudgment tailJudgment;
    private NoteResolution resolution;

    /**
     * @param beat the note head beat.
     */
    public HoldNote(double beat) {
        this(beat, 0.0D);
    }

    /**
     * @param beat the note head beat.
     * @param length the note length measured in beats.
     */
    public HoldNote(double beat, double length) {
        this.beat = beat;
        this.length = length;
        this.resolution = NoteResolution.valueFromBeat(beat);
    }

    @Override
    public Judgment getJudgment() {
        return headJudgment;
    }

    @Override
    public void setJudgment(Judgment judgment) {
        if(judgment == null) {
            this.headJudgment = null;
        } else if(judgment instanceof TapJudgment) {
            this.headJudgment = (TapJudgment) judgment;
        } else {
            throw new IllegalArgumentException("Judgment must be an instance of TapJudgment!");
        }
    }

    @Override
    public TailJudgment getTailJudgment() {
        return tailJudgment;
    }

    @Override
    public void setTailJudgment(TailJudgment judgment) {
        this.tailJudgment = judgment;
    }

    @Override
    public double getLength() {
        return length;
    }

    @Override
    public double getBeat() {
        return beat;
    }

    /**
     * Gets the note resolution calculated from the note head beat.
     * @return the note resolution.
     */
    public NoteResolution getResolution() {
        return resolution;
    }
}
