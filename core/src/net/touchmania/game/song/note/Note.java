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

import com.google.common.collect.ComparisonChain;

import javax.annotation.Nonnull;

/**
 * Represents a note.
 */
public abstract class Note implements Comparable<Note> {
    private double beat;

    public Note(double beat) {
        this.beat = beat;
    }

    /**
     * Gets the note beat.
     * @return the note beat.
     */
    public double getBeat() {
        return beat;
    }

    /**
     * Check if the given note can be judged. If not it will be
     * ignored during judgment.
     * @return true if the note can be judged.
     */
    public abstract boolean canBeJudged();

    /**
     * Check if the note can be inside a chord.
     * @return true if the note can be inside a chord.
     */
    public abstract boolean canBeChord();

    @Override
    public int compareTo(@Nonnull Note o) {
        return ComparisonChain
                .start()
                .compare(getBeat(), o.getBeat())
                .result();
    }
}
