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

public abstract class LengthyNote extends Note {
    private double length;
    private NoteResolution resolution;

    public LengthyNote(double beat) {
        this(beat, 0);
    }

    /**
     *
     * @param beat the note head beat.
     * @param length the note length measured in beats.
     */
    public LengthyNote(double beat, double length) {
        super(beat);
        this.resolution = NoteResolution.valueFromBeat(beat);
        this.setLength(length);
    }

    /**
     * Gets the note length in beats.
     * @return note length in beats.
     */
    public double getLength() {
        return length;
    }

    /**
     * Sets the note length in beats.
     * @param length the note length in beats.
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Gets the note head resolution calculated from the note head beat.
     * @return the note head resolution.
     */
    public NoteResolution getResolution() {
        return resolution;
    }
}
