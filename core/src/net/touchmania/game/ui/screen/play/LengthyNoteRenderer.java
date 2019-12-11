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

package net.touchmania.game.ui.screen.play;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.song.note.LengthyNote;

/**
 * @author Vincenzo Fortunato
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
    float getTailX(int panel, LengthyNote note, double beat, double time);

    /**
     * Gets note tail y position relative to receptor y position.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the y position relative to the receptor y position.
     */
    float getTailY(int panel, LengthyNote note, double beat, double time);

    /**
     * Checks if the note is active. For example hold/roll notes are active
     * when the user is holding down the control.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return true if the note is active, false otherwise.
     */
    boolean isActive(int panel, LengthyNote note, double beat, double time);

    /**
     * Gets the note body drawable. That is the drawable that is put after
     * the head and repeated (when necessary) to fill the entire note length.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the note body drawable.
     */
    Drawable getNoteBodyDrawable(int panel, LengthyNote note, double beat, double time);

    /**
     * Gets the note connector drawable. That is the drawable that is put between the
     * {@link #getNoteBodyDrawable(int, LengthyNote, double, double)} and the
     * {@link #getNoteTailDrawable(int, LengthyNote, double, double)} to connect them.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the note connector drawable.
     */
    Drawable getNoteConnectorDrawable(int panel, LengthyNote note, double beat, double time);

    /**
     * Gets the note tail drawable. That is the drawable that is put at the end of the note.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the note tail drawable.
     */
    Drawable getNoteTailDrawable(int panel, LengthyNote note, double beat, double time);
}
