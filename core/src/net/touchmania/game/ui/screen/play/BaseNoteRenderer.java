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
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import net.touchmania.game.round.Round;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.NotePanel;

public abstract class BaseNoteRenderer implements NoteRenderer {
    private final BeatmapView view;

    public BaseNoteRenderer(BeatmapView view) {
        this.view = view;
    }

    @Override
    public void draw(Batch batch, NotePanel panel, Note note, double beat, double time, float receptorX, float receptorY) {
        Drawable drawable = getNoteDrawable(panel, note, beat, time);
        if(drawable == null) { //TODO at this point drawable should not be null
            return;
        }

        float width = drawable.getMinWidth();
        float height = drawable.getMinHeight();
        float x = receptorX + getNoteX(panel, note, beat, time);
        float y = receptorY + getNoteY(panel, note, beat, time);
        float originX = width / 2.0f;
        float originY = height / 2.0f;
        float rotation = getNoteRotation(panel, note, beat, time);
        float scaleX = getNoteScaleX(panel, note, beat, time);
        float scaleY = getNoteScaleY(panel, note, beat, time);

        if(drawable instanceof TransformDrawable) {
            TransformDrawable transformDrawable = (TransformDrawable) drawable;
            transformDrawable.draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
        } else {
            drawable.draw(batch, x, y, width * scaleX, height * scaleY);
        }
    }

    @Override
    public float getNoteX(NotePanel panel, Note note, double beat, double time) {
        return 0.0f;
    }

    @Override
    public float getNoteY(NotePanel panel, Note note, double beat, double time) {
        Drawable drawable = getNoteDrawable(panel, note, beat, time);
        float height = drawable.getMinHeight();
        float speedMod = 2.5f;
        return (float) -(height * speedMod * (note.getBeat() - beat));
    }

    @Override
    public float getNoteScaleX(NotePanel panel, Note note, double beat, double time) {
        return 1.0f;
    }

    @Override
    public float getNoteScaleY(NotePanel panel, Note note, double beat, double time) {
        return 1.0f;
    }

    @Override
    public float getNoteRotation(NotePanel panel, Note note, double beat, double time) {
        float rotation = 0.0f; //for panels: down, right_down, center.
        switch(panel) { //rotate texture according to the panel
            case LEFT:
            case LEFT_DOWN:
                rotation = -90f;
                break;
            case UP:
            case LEFT_UP:
                rotation = 180f;
                break;
            case RIGHT:
            case RIGHT_UP:
                rotation = 90f;
                break;
        }
        return rotation;
    }

    @Override
    public float getNoteOpacity(NotePanel panel, Note note, double beat, double time) {
        return 1.0f;
    }

    @Override
    public boolean isNoteVisible(NotePanel panel, Note note, double beat, double time) {
        return true;
    }

    @Override
    public boolean isNoteInsideView(NotePanel panel, Note note, double beat, double time,
                                    float receptorX, float receptorY, float viewWidth, float viewHeight) {
        Drawable drawable = getNoteDrawable(panel, note, beat, time);
        if(drawable == null) { //TODO at this point drawable should not be null
            return false;
        }

        float x = receptorX + getNoteX(panel, note, beat, time);
        float y = receptorY + getNoteY(panel, note, beat, time);
        float width = drawable.getMinWidth();
        float height = drawable.getMinHeight();
        return x < viewWidth && x + width > 0 && y < viewHeight && y + height > 0;
    }

    @Override
    public Round getRound() {
        return view.getRound();
    }
}
