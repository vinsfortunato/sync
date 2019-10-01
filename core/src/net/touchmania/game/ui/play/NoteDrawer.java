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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.touchmania.game.round.Judgment;
import net.touchmania.game.round.Round;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.NotePanel;

public interface NoteDrawer<N extends Note> {
    /**
     * Draw a note to the batch. The batch is translated to the receptor position.
     * @param note the note to draw.
     * @param panel the note panel.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     */
    void draw(Batch batch, N note, NotePanel panel, double beat, double time);

    /**
     * Gets note x position relative to receptor x position.
     * @param note the note.
     * @param panel the note panel.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the x position relative to the receptor x position.
     */
    float getNoteX(N note, NotePanel panel, double beat, double time);

    /**
     * Gets note y position relative to receptor y position.
     * @param note the note.
     * @param panel the note panel.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the y position relative to the receptor y position.
     */
    float getNoteY(N note, NotePanel panel, double beat, double time);

    /**
     * Gets note scale x. Scale is applied to the note texture.
     * @param note the note.
     * @param panel the note panel.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the note scale x.
     */
    float getNoteScaleX(N note, NotePanel panel, double beat, double time);

    /**
     * Gets note scale y. Scale is applied to the note texture.
     * @param note the note.
     * @param panel the note panel.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the note scale y.
     */
    float getNoteScaleY(N note, NotePanel panel, double beat, double time);

    /**
     * Gets note rotation in degrees with the origin in the center of the note texture.
     * @param note the note.
     * @param panel the note panel.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the note rotation in degrees.
     */
    float getNoteRotation(N note, NotePanel panel, double beat, double time);

    /**
     * Gets note opacity, a value from 0.0f to 1.0f (inclusive).
     * @param note the note.
     * @param panel the note panel.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the note opacity. 0.0f if the note is invisible, 1.0f if
     *         the note is opaque.
     */
    float getNoteOpacity(N note, NotePanel panel, double beat, double time);

    /**
     * Checks if the given note is visible. If it isn't visible
     * the note will not be drawn. For example, tap notes with a judgment
     * greater than {@link Judgment#GOOD} are invisible.
     * @param note the note.
     * @param panel the note panel.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return true if the note is visible and must be drawn.
     */
    boolean isNoteVisible(N note, NotePanel panel, double beat, double time);

    /**
     * Checks if the given note is inside the view.
     * Only notes inside the view will be drawn.
     * @param note the note.
     * @param panel the note panel.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @param viewX the view x position relative to the receptor x.
     * @param viewY the view y position relative to the receptor y.
     * @param viewWidth the view width.
     * @param viewHeight the view height.
     * @return true if the given note is inside the view, false otherwise.
     */
    boolean isNoteInsideView(N note, NotePanel panel, double beat, double time,
                             float viewX, float viewY, float viewWidth, float viewHeight);

    /**
     * Gets note texture region.
     * @param note the note.
     * @param panel the note panel.
     * @param beat the note beat.
     * @param time current time relative to the start of the music track.
     * @return the note texture region.
     */
    TextureRegion getNoteTextureRegion(N note, NotePanel panel, double beat, double time);

    /**
     * Gets the round currently being played.
     * @return the round related to the drawer.
     */
    Round getRound();
}
