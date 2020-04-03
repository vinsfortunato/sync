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
import net.sync.game.Game;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.song.note.MineNote;
import net.sync.game.song.note.Note;

/**
 * @author Vincenzo Fortunato
 */
public class MineNoteRenderer extends BaseNoteRenderer {

    private Resource<Drawable> mineDrawable;

    public MineNoteRenderer(BeatmapView view) {
        super(view);

        mineDrawable = Game.instance().getResources().getDrawable("play_dance_note_mine");
        mineDrawable.load();
    }

    @Override
    public float getNoteRotation(int panel, Note note, double beat, double time) {
        float degrees = -60f * (float) beat;
        degrees %= 360f;
        return degrees;
    }

    @Override
    public boolean isNoteVisible(int panel, Note note, double beat, double time) {
        MineNote mineNote = (MineNote) note;
        return mineNote.getJudgment() == null || !mineNote.getJudgment().hasExploded();
    }

    @Override
    public Drawable getNoteDrawable(int panel, Note note, double beat, double time) {
        return mineDrawable.get();
    }
}
