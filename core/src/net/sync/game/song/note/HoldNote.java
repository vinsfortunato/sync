/*
 * Copyright (c) 2020 Vincenzo Fortunato.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.sync.game.song.note;

import net.sync.game.round.judge.Judgment;
import net.sync.game.round.judge.TailJudgment;
import net.sync.game.round.judge.TapJudgment;

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
