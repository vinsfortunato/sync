package net.touchmania.game.ui.screen.play;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.song.note.Note;

public class FakeNoteRenderer extends BaseNoteRenderer {
    public FakeNoteRenderer(BeatmapView view) {
        super(view);
    }

    @Override
    public boolean isNoteVisible(int panel, Note note, double beat, double time) {
        return true;
    }

    @Override
    public Drawable getNoteDrawable(int panel, Note note, double beat, double time) {
        return null;
    }
}
