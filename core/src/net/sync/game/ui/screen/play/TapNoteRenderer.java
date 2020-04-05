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
import com.badlogic.gdx.utils.Array;
import net.sync.game.Game;
import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.round.judge.JudgeCriteria;
import net.sync.game.round.judge.TapJudgment;
import net.sync.game.song.Beatmap;
import net.sync.game.song.note.ChordNote;
import net.sync.game.song.note.JudgeableNote;
import net.sync.game.song.note.Note;
import net.sync.game.song.note.TapNote;

public class TapNoteRenderer extends BaseNoteRenderer {
    /* Resources */
    private Resource<Drawable> note4Drawable;
    private Resource<Drawable> note8Drawable;
    private Resource<Drawable> note12Drawable;
    private Resource<Drawable> note16Drawable;
    private Resource<Drawable> note24Drawable;
    private Resource<Drawable> note32Drawable;
    private Resource<Drawable> note48Drawable;
    private Resource<Drawable> note64Drawable;
    private Resource<Drawable> note192Drawable;

    public TapNoteRenderer(BeatmapView view) {
        super(view);
        loadResources();
    }

    private void loadResources() {
        ResourceProvider resources = Game.instance().getResources();
        (note4Drawable   = resources.getDrawable("play_dance_note_tap_4"  )).load();
        (note8Drawable   = resources.getDrawable("play_dance_note_tap_8"  )).load();
        (note12Drawable  = resources.getDrawable("play_dance_note_tap_12" )).load();
        (note16Drawable  = resources.getDrawable("play_dance_note_tap_16" )).load();
        (note24Drawable  = resources.getDrawable("play_dance_note_tap_24" )).load();
        (note32Drawable  = resources.getDrawable("play_dance_note_tap_32" )).load();
        (note48Drawable  = resources.getDrawable("play_dance_note_tap_48" )).load();
        (note64Drawable  = resources.getDrawable("play_dance_note_tap_64" )).load();
        (note192Drawable = resources.getDrawable("play_dance_note_tap_192")).load();
    }

    @Override
    public boolean isNoteVisible(int panel, Note note, double beat, double time) {
        TapNote tapNote = (TapNote) note;
        Beatmap beatmap = getRound().getChart().beatmap;
        JudgeCriteria criteria = getRound().getJudge().getCriteria();
        TapJudgment worstJudgment = null;

        if(criteria.isChordCohesionEnabled() && beatmap.isChord(note.getBeat())) {
            Array<Note> chordNotes = beatmap.getNotes(note.getBeat(), n -> n instanceof JudgeableNote && n instanceof ChordNote);
            for(Note chordNote : chordNotes) {
                JudgeableNote judgeableNote = (JudgeableNote) chordNote;
                TapJudgment tapJudgment = (TapJudgment) judgeableNote.getJudgment();
                if(tapJudgment == null) {
                    //A note inside the chord has not been judged yet
                    return true;
                }
                if(worstJudgment == null || tapJudgment.getJudgmentClass().compareTo(worstJudgment.getJudgmentClass()) > 0) {
                    worstJudgment = tapJudgment;
                }
            }
        } else {
            worstJudgment = tapNote.getJudgment();
        }

        if(worstJudgment != null) {
            switch (worstJudgment.getJudgmentClass()) {
                case MARVELOUS:
                case PERFECT:
                case GREAT:
                    return false;
            }
        }
        return true;
    }

    @Override
    public Drawable getNoteDrawable(int panel, Note note, double beat, double time) {
        TapNote tapNote = (TapNote) note;
        switch (tapNote.getResolution()) {
            case NOTE_4TH:   return note4Drawable.get();
            case NOTE_8TH:   return note8Drawable.get();
            case NOTE_12TH:  return note12Drawable.get();
            case NOTE_16TH:  return note16Drawable.get();
            case NOTE_24TH:  return note24Drawable.get();
            case NOTE_32ND:  return note32Drawable.get();
            case NOTE_48TH:  return note48Drawable.get();
            case NOTE_64TH:  return note64Drawable.get();
            case NOTE_192ND: return note192Drawable.get();
        }
        return null;
    }
}
