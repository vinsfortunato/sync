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

package net.touchmania.game.ui.play;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.touchmania.game.song.note.LengthyNote;
import net.touchmania.game.song.note.NotePanel;

/**
 * @author flood2d
 */
public interface LengthyNoteRenderer extends NoteRenderer {
    /**
     * Gets note tail x position relative to receptor x position.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the x position relative to the receptor x position.
     */
    float getTailX(NotePanel panel, LengthyNote note, double beat, double time);

    /**
     * Gets note tail y position relative to receptor y position.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the y position relative to the receptor y position.
     */
    float getTailY(NotePanel panel, LengthyNote note, double beat, double time);

    /**
     * Checks if the note is active. For example hold/roll notes are active
     * when the user is holding down the control.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return true if the note is active, false otherwise.
     */
    boolean isActive(NotePanel panel, LengthyNote note, double beat, double time);

    /**
     * Gets the note body texture region. That is the texture that is put after
     * the head and repeated (when necessary) to fill the entire note length.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the note body texture region.
     */
    TextureRegion getNoteBodyTexture(NotePanel panel, LengthyNote note, double beat, double time);

    /**
     * Gets the note connector texture. That is the texture that is put between the
     * {@link #getNoteBodyTexture(NotePanel, LengthyNote, double, double)} and the
     * {@link #getNoteTailTexture(NotePanel, LengthyNote, double, double)} to connect them.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the note connector texture region.
     */
    TextureRegion getNoteConnectorTexture(NotePanel panel, LengthyNote note, double beat, double time);

    /**
     * Gets the note tail texture. That is the texture that is put at the end of the note.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the note tail texture region.
     */
    TextureRegion getNoteTailTexture(NotePanel panel, LengthyNote note, double beat, double time);
}
