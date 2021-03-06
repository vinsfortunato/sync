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
import net.sync.game.round.PanelState;
import net.sync.game.round.judge.JudgmentClass;
import net.sync.game.round.judge.TailJudgment;
import net.sync.game.round.judge.TapJudgment;
import net.sync.game.round.modifier.SpeedModifier;
import net.sync.game.song.Timing;
import net.sync.game.song.note.JudgeableLengthyNote;
import net.sync.game.song.note.JudgeableNote;
import net.sync.game.song.note.LengthyNote;
import net.sync.game.song.note.Note;

/**
 * @author Vincenzo Fortunato
 */
public abstract class BaseLengthyNoteRenderer extends BaseNoteRenderer implements LengthyNoteRenderer {
    public BaseLengthyNoteRenderer(BeatmapView view) {
        super(view);
    }

    @Override
    public void draw(Batch batch, int panel, Note note, double beat, double time, float receptorX, float receptorY) {
        if(!isNoteVisible(panel, note, beat, time)) {
            return;
        }

        Drawable bodyDrawable = getNoteBodyDrawable(panel, (LengthyNote) note, beat, time);
        Drawable tailDrawable = getNoteTailDrawable(panel, (LengthyNote) note, beat, time);

        if(bodyDrawable == null) return; //TODO Shouldnt be null at this point
        if(tailDrawable == null) return;

        int noteX = (int) (receptorX + getNoteX(panel, note, beat, time));
        int noteY = (int) (receptorY + getNoteY(panel, note, beat, time));
        int noteHeight = (int) getNoteHeight(panel, note, beat, time);
        int tailX = (int) (receptorX + getTailX(panel, (LengthyNote) note, beat, time));
        int tailY = (int) (receptorY + getTailY(panel, (LengthyNote) note, beat, time));
        int tailHeight = (int) tailDrawable.getMinHeight();
        int bodyHeight = (int) (Math.abs(tailY - noteY) - tailHeight + noteHeight / 2.0f);
        int opacity = (int) getNoteOpacity(panel, note, beat, time);

        //Set opacity
        Color color = new Color(batch.getColor());
        color.a = opacity;
        batch.setColor(color);

        int currentY = (int) (noteY + noteHeight / 2.0f);
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
    public float getNoteY(int panel, Note note, double beat, double time) {
        if(note instanceof JudgeableNote) {
            JudgeableNote judgeableNote = (JudgeableNote) note;
            //TODO here we assume getJudgment is a TapJudgment
            TapJudgment judgment = (TapJudgment) judgeableNote.getJudgment();
            if(judgment == null || judgment.getJudgmentClass() == JudgmentClass.MISS) {
                //Head not judged or missed
                return super.getNoteY(panel, note, beat, time);
            }
        }

        if(note instanceof JudgeableLengthyNote) {
            JudgeableLengthyNote lengthyNote = (JudgeableLengthyNote) note;
            TailJudgment tailJudgment = lengthyNote.getTailJudgment();
            if(tailJudgment == null){
                //Note head overlaps receptor
                return 0.0f;
            } else {
                Timing timing = getRound().getTiming();
                double genBeat = timing.getBeatAt(tailJudgment.getGenTime());
                float height = getNoteHeight(panel, note, beat, time);
                SpeedModifier speedMod = getRound().getModifiers().getSpeedModifier();
                return (float) -(height * speedMod.getSpeedAt(beat) * (genBeat - beat));
            }
        }

        //Base lengthy note
        return super.getNoteY(panel, note, beat, time);
    }

    @Override
    public float getTailX(int panel, LengthyNote note, double beat, double time) {
        return 0.0f;
    }

    @Override
    public float getTailY(int panel, LengthyNote note, double beat, double time) {
        Drawable drawable = getNoteDrawable(panel, note, beat, time);
        float height = drawable.getMinHeight();
        SpeedModifier speedMod = getRound().getModifiers().getSpeedModifier();
        return (float) -(height * speedMod.getSpeedAt(beat) * (note.getBeat() + note.getLength() - beat));
    }

    @Override
    public boolean isActive(int panel, LengthyNote note, double beat, double time) {
        if(note instanceof JudgeableLengthyNote) {
            PanelState states = getRound().getPanelState();
            JudgeableLengthyNote lengthyNote = (JudgeableLengthyNote) note;
            TapJudgment headJudgment = (TapJudgment) lengthyNote.getJudgment();
            TailJudgment tailJudgment = (TailJudgment) lengthyNote.getTailJudgment();
            return headJudgment != null                                         //Has head judgment
                    && headJudgment.getJudgmentClass() != JudgmentClass.MISS    //Head judgment is not miss
                    && tailJudgment == null                                     //No tail judgment
                    && states.isPressedAt(panel, time);                         //Panel pressed
        }
        return false;
    }

    @Override
    public boolean isNoteVisible(int panel, Note note, double beat, double time) {
        if(note instanceof JudgeableLengthyNote) {
            JudgeableLengthyNote lengthyNote = (JudgeableLengthyNote) note;
            TapJudgment headJudgment = (TapJudgment) lengthyNote.getJudgment();
            TailJudgment tailJudgment = (TailJudgment) lengthyNote.getTailJudgment();
            return headJudgment == null                                         //No head judgment
                    || headJudgment.getJudgmentClass() == JudgmentClass.MISS    //Head missed
                    || tailJudgment == null                                     //No tail judgment
                    || tailJudgment.getJudgmentClass() == JudgmentClass.NG;     //Trail missed
        }
        return true;
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
