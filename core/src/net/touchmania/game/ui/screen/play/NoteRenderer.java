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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.round.Judgment;
import net.touchmania.game.round.Round;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.NotePanel;

public interface NoteRenderer {
    /**
     * Draw a note to the batch. Batch is translated to the view position.
     * @param batch the batch.
     * @param panel the note panel.
     * @param note the note to draw.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @param receptorX the receptor x position inside the view.
     * @param receptorY the receptor y position inside the view.
     */
    void draw(Batch batch, NotePanel panel, Note note, double beat, double time, float receptorX, float receptorY);

    /**
     * Gets note x position relative to receptor x position.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the x position relative to the receptor x position.
     */
    float getNoteX(NotePanel panel, Note note, double beat, double time);

    /**
     * Gets note y position relative to receptor y position.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the y position relative to the receptor y position.
     */
    float getNoteY(NotePanel panel, Note note, double beat, double time);

    /**
     * Gets note scale x. Scale is applied to the note texture.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time current time relative to the start of the music track.
     * @return the note scale x.
     */
    float getNoteScaleX(NotePanel panel, Note note, double beat, double time);

    /**
     * Gets note scale y. Scale is applied to the note texture.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the note scale y.
     */
    float getNoteScaleY(NotePanel panel, Note note, double beat, double time);

    /**
     * Gets note rotation in degrees with the origin in the center of the note texture.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the note rotation in degrees.
     */
    float getNoteRotation(NotePanel panel, Note note, double beat, double time);

    /**
     * Gets note opacity, a value from 0.0f to 1.0f (inclusive).
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the note opacity. 0.0f if the note is invisible, 1.0f if the note is fully opaque.
     */
    float getNoteOpacity(NotePanel panel, Note note, double beat, double time);

    /**
     * Checks if the given note is visible. If it isn't visible
     * the note will not be drawn. For example, tap notes with a judgment
     * greater than {@link Judgment#GOOD} are invisible.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return true if the note is visible and must be drawn.
     */
    boolean isNoteVisible(NotePanel panel, Note note, double beat, double time);

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
    boolean isNoteInsideView(NotePanel panel, Note note, double beat, double time,
                             float receptorX, float receptorY, float viewWidth, float viewHeight);

    /**
     * Gets note drawable.
     * @param panel the note panel.
     * @param note the note.
     * @param beat the current beat.
     * @param time current time relative to the start of the music track.
     * @return the note drawable.
     */
    Drawable getNoteDrawable(NotePanel panel, Note note, double beat, double time);

    /**
     * Gets the round currently being played.
     * @return the current round.
     */
    Round getRound();
}
