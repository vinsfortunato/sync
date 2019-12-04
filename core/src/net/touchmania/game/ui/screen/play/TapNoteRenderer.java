package net.touchmania.game.ui.screen.play;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.Game;
import net.touchmania.game.resource.ResourceProvider;
import net.touchmania.game.resource.lazy.Resource;
import net.touchmania.game.round.judge.JudgmentKeeper;
import net.touchmania.game.round.judge.TapJudgement;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.TapNote;

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
        JudgmentKeeper judgments = getRound().getJudge().getJudgmentKeeper();
        TapJudgement judgment = (TapJudgement) judgments.getJudgment(panel, note);

        if(judgment != null) {
            switch (judgment.getJudgmentClass()) {
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
