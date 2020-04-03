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

package net.sync.game.ui.screen.play;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.sync.game.song.note.LengthyNote;

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
