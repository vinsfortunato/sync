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

import net.touchmania.game.song.score.Judgment;

/**
 * @author flood2d
 */
public class TapNote implements Note {
    /**
     * The note resolution calculated from the note beat.
     */
    public final NoteResolution resolution;

    /**
     * The judgement generated for this note. Can be null if the note has not been
     * judged yet.
     */
    public Judgment judgment;

    /**
     * The time when the {@link #judgment} has been generated, relative to the start
     * of the music track.
     */
    public double judgmentTime;

    public TapNote(NoteResolution resolution) {
        this.resolution = resolution;
    }
}
