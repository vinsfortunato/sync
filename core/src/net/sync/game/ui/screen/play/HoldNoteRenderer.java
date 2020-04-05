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
import net.sync.game.resource.lazy.Resource;
import net.sync.game.round.judge.JudgmentClass;
import net.sync.game.round.judge.TailJudgment;
import net.sync.game.round.judge.TapJudgment;
import net.sync.game.song.note.HoldNote;
import net.sync.game.song.note.JudgeableLengthyNote;
import net.sync.game.song.note.LengthyNote;
import net.sync.game.song.note.Note;

import static net.sync.game.Game.resources;

/**
 * @author Vincenzo Fortunato
 */
public class HoldNoteRenderer extends BaseLengthyNoteRenderer {
    private Resource<Drawable> head4Drawable;
    private Resource<Drawable> head8Drawable;
    private Resource<Drawable> head12Drawable;
    private Resource<Drawable> head16Drawable;
    private Resource<Drawable> head24Drawable;
    private Resource<Drawable> head32Drawable;
    private Resource<Drawable> head48Drawable;
    private Resource<Drawable> head64Drawable;
    private Resource<Drawable> head192Drawable;
    private Resource<Drawable> bodyInactiveDrawable;
    private Resource<Drawable> bodyActiveDrawable;
    private Resource<Drawable> tailInactiveDrawable;
    private Resource<Drawable> tailActiveDrawable;

    public HoldNoteRenderer(BeatmapView view) {
        super(view);
        (head4Drawable = resources().getDrawable("play_dance_note_hold_head_4")).load();
        (head8Drawable = resources().getDrawable("play_dance_note_hold_head_8")).load();
        (head12Drawable = resources().getDrawable("play_dance_note_hold_head_12")).load();
        (head16Drawable = resources().getDrawable("play_dance_note_hold_head_16")).load();
        (head32Drawable = resources().getDrawable("play_dance_note_hold_head_32")).load();
        (head24Drawable = resources().getDrawable("play_dance_note_hold_head_24")).load();
        (head48Drawable = resources().getDrawable("play_dance_note_hold_head_48")).load();
        (head64Drawable = resources().getDrawable("play_dance_note_hold_head_64")).load();
        (head192Drawable = resources().getDrawable("play_dance_note_hold_head_192")).load();
        (bodyInactiveDrawable = resources().getDrawable("play_dance_note_hold_body_inactive" )).load();
        (bodyActiveDrawable = resources().getDrawable("play_dance_note_hold_body_active")).load();
        (tailInactiveDrawable = resources().getDrawable("play_dance_note_hold_tail_inactive")).load();
        (tailActiveDrawable = resources().getDrawable("play_dance_note_hold_tail_active")).load();
    }

    @Override
    public Drawable getNoteDrawable(int panel, Note note, double beat, double time) {
        HoldNote holdNote = (HoldNote) note;
        switch (holdNote.getResolution()) {
            case NOTE_4TH:   return head4Drawable.get();
            case NOTE_8TH:   return head8Drawable.get();
            case NOTE_12TH:  return head12Drawable.get();
            case NOTE_16TH:  return head16Drawable.get();
            case NOTE_24TH:  return head24Drawable.get();
            case NOTE_32ND:  return head32Drawable.get();
            case NOTE_48TH:  return head48Drawable.get();
            case NOTE_64TH:  return head64Drawable.get();
            case NOTE_192ND: return head192Drawable.get();
        }
        return null;
    }

    @Override
    public Drawable getNoteBodyDrawable(int panel, LengthyNote note, double beat, double time) {
        return isActive(panel, note, beat, time) ? bodyActiveDrawable.get() : bodyInactiveDrawable.get();
    }

    @Override
    public Drawable getNoteConnectorDrawable(int panel, LengthyNote note, double beat, double time) {
        return null; //TODO
    }

    @Override
    public Drawable getNoteTailDrawable(int panel, LengthyNote note, double beat, double time) {
        return isActive(panel, note, beat, time) ? tailActiveDrawable.get() : tailInactiveDrawable.get();
    }

    @Override
    public boolean isNoteVisible(int panel, Note note, double beat, double time) {
        JudgeableLengthyNote lengthyNote = (JudgeableLengthyNote) note;
        TapJudgment headJudgment = (TapJudgment) lengthyNote.getJudgment();
        TailJudgment tailJudgment = (TailJudgment) lengthyNote.getTailJudgment();
        return headJudgment == null                                         //No head judgment
                || headJudgment.getJudgmentClass() == JudgmentClass.MISS    //Head missed
                || tailJudgment == null                                     //No tail judgment
                || tailJudgment.getJudgmentClass() == JudgmentClass.NG;     //Trail missed
    }

}
