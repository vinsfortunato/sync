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

import com.badlogic.gdx.utils.ObjectSet;
import com.google.common.math.DoubleMath;

import java.math.RoundingMode;

/**
 * @author Vincenzo Fortunato
 */
public enum NoteResolution {
    NOTE_4TH(4),
    NOTE_8TH(8),
    NOTE_12TH(12),
    NOTE_16TH(16),
    NOTE_24TH(24),
    NOTE_32ND(32),
    NOTE_48TH(48),
    NOTE_64TH(64),
    NOTE_192ND(192);

    /**
     * The minimum distance in beat between notes of the same resolution.
     */
    public final double noteDistance;

    /**
     * The number of notes of the same resolution in a measure (4.000 beats).
     */
    public final int notesInMeasure;

    /**
     * The number of notes of the same resolution in beat (1.000 beats).
     */
    public final int notesInBeat;

    NoteResolution(int notesInMeasure) {
        this.noteDistance = 4.0D / notesInMeasure;
        this.notesInMeasure = notesInMeasure;
        this.notesInBeat = notesInMeasure / 4;
    }

    /**
     * Gets a note resolution from a beat.
     * @param beat the note beat.
     * @return the note resolution.
     */
    public static NoteResolution valueFromBeat(double beat) {
        long result = Math.round((beat - DoubleMath.roundToInt(beat, RoundingMode.FLOOR)) * NOTE_192ND.notesInBeat);
        for(NoteResolution resolution : values()) {
            if(result % (NOTE_192ND.notesInBeat / resolution.notesInBeat) == 0) {
                return resolution;
            }
        }
        return NOTE_192ND;
    }

    /**
     * Get all the <code>NoteResolution</code> used by the given beats.
     * A <code>NoteResolution</code> will be added to the returned set if
     * {@link #valueFromBeat(double)} return it for at least one of the given beats.
     * @param beats a collection of beats.
     * @return note resolutions used by the given beats.
     */
    public static ObjectSet<NoteResolution> getUsedIntervals(Iterable<Double> beats) {
        ObjectSet<NoteResolution> usedIntervals = new ObjectSet<>();
        for(double beat : beats) {
            usedIntervals.add(valueFromBeat(beat));
        }
        return usedIntervals;
    }
}
