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
import net.sync.game.round.judge.TapJudgment;

public class TapNote implements JudgeableNote, ChordNote {
    private double beat;
    private TapJudgment judgment;
    private NoteResolution resolution;

    public TapNote(double beat) {
        this.beat = beat;
        this.resolution = NoteResolution.valueFromBeat(beat);
    }

    @Override
    public TapJudgment getJudgment() {
        return judgment;
    }

    @Override
    public void setJudgment(Judgment judgment) {
        if(judgment == null) {
            this.judgment = null;
        } else if(judgment instanceof TapJudgment) {
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
