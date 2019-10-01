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

import net.touchmania.game.round.Judgment;

public class LengthyNote implements Note {
    /**
     * The note length in beats.
     */
    public double length;

    /**
     * The note head resolution calculated from the note head beat.
     */
    public final NoteResolution resolution;

    /**
     * The judgement generated for the note head. Can be null if the head
     * has not been judged yet.
     */
    public Judgment headJudgment;

    /**
     * The judgment generated for the note tail. Can be null if the tail
     * has not been judged yet.
     */
    public Judgment tailJudgment;

    /**
     * The time when the {@link #headJudgment} has been generated, relative to the start
     * of the music track.
     */
    public double headJudgmentTime;

    /**
     * The time when the {@link #tailJudgment} has been generated, relative to the start
     * of the music track.
     */
    public double tailJudgmentTime;

    /**
     *
     * @param resolution the note head resolution.
     * @param length the note length measured in beats.
     */
    public LengthyNote(NoteResolution resolution, double length) {
        this.resolution = resolution;
        this.length = length;
    }
}
