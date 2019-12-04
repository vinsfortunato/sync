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
import net.touchmania.game.song.note.LengthyNote;
import net.touchmania.game.song.note.Note;

/**
 * @author flood2d
 */
public abstract class BaseLengthyNoteRenderer extends BaseNoteRenderer implements LengthyNoteRenderer {
    public BaseLengthyNoteRenderer(BeatmapView view) {
        super(view);
    }

    @Override
    public void draw(Batch batch, int panel, Note note, double beat, double time, float receptorX, float receptorY) {
        Drawable noteDrawable = getNoteDrawable(panel, note, beat, time);
        Drawable bodyDrawable = getNoteBodyDrawable(panel, (LengthyNote) note, beat, time);
        Drawable tailDrawable = getNoteTailDrawable(panel, (LengthyNote) note, beat, time);

        if(bodyDrawable == null) return; //TODO Shouldnt be null at this point
        if(tailDrawable == null) return;
        if(noteDrawable == null) return;

        float noteX = receptorX + getNoteX(panel, note, beat, time);
        float noteY = receptorY + getNoteY(panel, note, beat, time);
        float tailX = receptorX + getTailX(panel, (LengthyNote) note, beat, time);
        float tailY = receptorY + getTailY(panel, (LengthyNote) note, beat, time);
        float tailHeight = tailDrawable.getMinHeight();
        float bodyHeight = Math.abs(tailY - noteY) - tailHeight + noteDrawable.getMinHeight() / 2.0f;

        float currentY = noteY + noteDrawable.getMinHeight() / 2.0f;
        while(bodyHeight > 0) {
            float h = Math.min(bodyHeight, bodyDrawable.getMinHeight());
            currentY -= h;
            bodyDrawable.draw(batch, noteX, currentY, bodyDrawable.getMinWidth(), h);
            bodyHeight -= bodyDrawable.getMinHeight();
        }
        tailDrawable.draw(batch, tailX, tailY, tailDrawable.getMinWidth(), tailDrawable.getMinHeight());

        super.draw(batch, panel, note, beat, time, receptorX, receptorY);
    }

    @Override
    public float getTailX(int panel, LengthyNote note, double beat, double time) {
        return 0.0f;
    }

    @Override
    public float getTailY(int panel, LengthyNote note, double beat, double time) {
        Drawable drawable = getNoteDrawable(panel, note, beat, time);
        float height = drawable.getMinHeight();
        float speedMod = 2.5f;
        return (float) -(height * speedMod * (note.getBeat() + note.getLength() - beat));
    }

    @Override
    public boolean isActive(int panel, LengthyNote note, double beat, double time) {
        return false;
    }

    @Override
    public boolean isNoteInsideView(int panel, Note note, double beat, double time, float receptorX, float receptorY, float viewWidth, float viewHeight) {
        if(!super.isNoteInsideView(panel, note, beat, time, receptorX, receptorY, viewWidth, viewHeight)) {
            //Head not inside view, check trail
            Drawable noteDrawable = getNoteDrawable(panel, note, beat, time);
            Drawable bodyDrawable = getNoteBodyDrawable(panel, (LengthyNote) note, beat, time);
            Drawable tailDrawable = getNoteTailDrawable(panel, (LengthyNote) note, beat, time);

            if(bodyDrawable == null || tailDrawable == null || noteDrawable == null)
                return false; //TODO Shouldnt be null at this point

            float noteY = getNoteY(panel, note, beat, time);
            float tailX = getTailX(panel, (LengthyNote) note, beat, time);
            float tailY = getTailY(panel, (LengthyNote) note, beat, time);

            //Trail bounds
            float x = tailX + receptorX;
            float y = tailY + receptorY;
            float width = Math.max(bodyDrawable.getMinWidth(), tailDrawable.getMinWidth());
            float height = Math.abs(tailY - noteY) + noteDrawable.getMinHeight() / 2.0f;
            return x < viewWidth && x + width > 0 && y < viewHeight && y + height > 0;
        }

        return true;
    }
}
