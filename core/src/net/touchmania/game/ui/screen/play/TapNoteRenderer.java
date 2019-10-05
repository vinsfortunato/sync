package net.touchmania.game.ui.screen.play;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.Game;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.NotePanel;

public class TapNoteRenderer extends BaseNoteRenderer {
    public TapNoteRenderer(BeatmapView view) {
        super(view);
    }

    @Override
    public Drawable getNoteDrawable(NotePanel panel, Note note, double beat, double time) {
        return Game.instance().getResources().getDrawable("play_dance_note_tap");
    }
}
