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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import net.sync.game.round.Round;
import net.sync.game.round.modifier.SpeedModifier;
import net.sync.game.song.note.Note;
import net.sync.game.song.note.NotePanel;

public abstract class BaseNoteRenderer implements NoteRenderer {
    private final BeatmapView view;

    public BaseNoteRenderer(BeatmapView view) {
        this.view = view;
    }

    @Override
    public void draw(Batch batch, int panel, Note note, double beat, double time, float receptorX, float receptorY) {
        if(!isNoteVisible(panel, note, beat, time)) {
            return;
        }

        Drawable drawable = getNoteDrawable(panel, note, beat, time);
        if(drawable == null) { //TODO at this point drawable should not be null
            return;
        }

        float width = getNoteWidth(panel, note, beat, time);
        float height = getNoteHeight(panel, note, beat, time);
        float x = receptorX + getNoteX(panel, note, beat, time);
        float y = receptorY + getNoteY(panel, note, beat, time);
        float originX = width / 2.0f;
        float originY = height / 2.0f;
        float rotation = getNoteRotation(panel, note, beat, time);
        float scaleX = getNoteScaleX(panel, note, beat, time);
        float scaleY = getNoteScaleY(panel, note, beat, time);
        float opacity = getNoteOpacity(panel, note, beat, time);

        //Set opacity
        Color color = new Color(batch.getColor());
        color.a = opacity;
        batch.setColor(color);

        if(drawable instanceof TransformDrawable) {
            TransformDrawable transformDrawable = (TransformDrawable) drawable;
            transformDrawable.draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
        } else {
            drawable.draw(batch, x, y, width * scaleX, height * scaleY);
        }
    }

    @Override
    public float getNoteX(int panel, Note note, double beat, double time) {
        return 0.0f;
    }

    @Override
    public float getNoteY(int panel, Note note, double beat, double time) {
        Drawable drawable = getNoteDrawable(panel, note, beat, time);
        float height = drawable.getMinHeight();
        SpeedModifier speedMod = getRound().getModifiers().getSpeedModifier();
        return (float) -(height * speedMod.getSpeedAt(beat) * (note.getBeat() - beat));
    }

    @Override
    public float getNoteWidth(int panel, Note note, double beat, double time) {
        Drawable drawable = getNoteDrawable(panel, note, beat, time);
        return drawable != null ? drawable.getMinWidth() : 0;
    }

    @Override
    public float getNoteHeight(int panel, Note note, double beat, double time) {
        Drawable drawable = getNoteDrawable(panel, note, beat, time);
        return drawable != null ? drawable.getMinHeight() : 0;
    }

    @Override
    public float getNoteScaleX(int panel, Note note, double beat, double time) {
        return 1.0f;
    }

    @Override
    public float getNoteScaleY(int panel, Note note, double beat, double time) {
        return 1.0f;
    }

    @Override
    public float getNoteRotation(int panel, Note note, double beat, double time) {
        float rotation = 0.0f; //for panels: down, right_down, center.
        switch(panel) { //rotate texture according to the panel
            case NotePanel.LEFT:
            case NotePanel.LEFT_DOWN:
                rotation = -90f;
                break;
            case NotePanel.UP:
            case NotePanel.LEFT_UP:
                rotation = 180f;
                break;
            case NotePanel.RIGHT:
            case NotePanel.RIGHT_UP:
                rotation = 90f;
                break;
        }
        return rotation;
    }

    @Override
    public float getNoteOpacity(int panel, Note note, double beat, double time) {
        return 1.0f;
    }

    @Override
    public boolean isNoteInsideView(int panel, Note note, double beat, double time,
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
