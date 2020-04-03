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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.sync.game.round.Round;
import net.sync.game.round.judge.JudgmentClass;
import net.sync.game.song.note.Note;

public interface NoteRenderer {
    /**
     * Draw a note to the batch. Batch is translated to the view position.
     * Is called only if {@link #isNoteInsideView(int, Note, double, double, float, float, float, float)} returns true.
     * @param batch the batch.
     * @param panel the note panel.
     * @param note the note to draw.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @param receptorX the receptor x position inside the view.
     * @param receptorY the receptor y position inside the view.
     */
    void draw(Batch batch, int panel, Note note, double beat, double time, float receptorX, float receptorY);

    /**
     * Gets note x position relative to receptor x position.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the x position relative to the receptor x position.
     */
    float getNoteX(int panel, Note note, double beat, double time);

    /**
     * Gets note y position relative to receptor y position.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the y position relative to the receptor y position.
     */
    float getNoteY(int panel, Note note, double beat, double time);

    /**
     * Gets unscaled note width.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the note unscaled width.
     */
    float getNoteWidth(int panel, Note note, double beat, double time);

    /**
     * Gets unscaled note height.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the note unscaled width.
     */
    float getNoteHeight(int panel, Note note, double beat, double time);

    /**
     * Gets note scale x.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time current time relative to the start of the music track.
     * @return the note scale x.
     */
    float getNoteScaleX(int panel, Note note, double beat, double time);

    /**
     * Gets note scale y.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the note scale y.
     */
    float getNoteScaleY(int panel, Note note, double beat, double time);

    /**
     * Gets note rotation in degrees with the origin in the center of the note texture.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the note rotation in degrees.
     */
    float getNoteRotation(int panel, Note note, double beat, double time);

    /**
     * Gets note opacity, a value from 0.0f to 1.0f (inclusive).
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the note opacity. 0.0f if the note is invisible, 1.0f if the note is fully opaque.
     */
    float getNoteOpacity(int panel, Note note, double beat, double time);

    /**
     * Checks if the given note is visible. If it isn't visible
     * the note will not be drawn. For example, tap notes with a judgment
     * greater than {@link JudgmentClass#GOOD} are invisible.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return true if the note is visible and must be drawn.
     */
    boolean isNoteVisible(int panel, Note note, double beat, double time);

    /**
     * Checks if the given note is inside the view.
     * Only notes inside the view will be drawn. It should ignore
     * note properties like opacity and visibility.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @param receptorX the receptor x position inside the view.
     * @param receptorY the receptor y position inside the view.
     * @param viewWidth the view width.
     * @param viewHeight the view height.
     * @return true if the given note is inside the view, false otherwise.
     */
    boolean isNoteInsideView(int panel, Note note, double beat, double time,
                             float receptorX, float receptorY, float viewWidth, float viewHeight);

    /**
     * Gets note drawable.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time current time relative to the start of the music track.
     * @return the note drawable.
     */
    Drawable getNoteDrawable(int panel, Note note, double beat, double time);

    /**
     * Gets the round currently being played.
     * @return the current round.
     */
    Round getRound();
}
