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

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.Game;
import net.touchmania.game.song.note.LengthyNote;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.NotePanel;

/**
 * @author flood2d
 */
public class HoldNoteRenderer extends BaseLengthyNoteRenderer {
    private Drawable noteDrawable = Game.instance().getResources().getDrawable("play_dance_note_hold_head");
    private Drawable bodyDrawable = Game.instance().getResources().getDrawable("play_dance_note_hold_body");
    private Drawable connDrawable = Game.instance().getResources().getDrawable("play_dance_note_hold_conn");
    private Drawable tailDrawable = Game.instance().getResources().getDrawable("play_dance_note_hold_tail");

    public HoldNoteRenderer(BeatmapView view) {
        super(view);
    }

    @Override
    public Drawable getNoteDrawable(NotePanel panel, Note note, double beat, double time) {
        return noteDrawable;
    }

    @Override
    public Drawable getNoteBodyDrawable(NotePanel panel, LengthyNote note, double beat, double time) {
        return bodyDrawable;
    }

    @Override
    public Drawable getNoteConnectorDrawable(NotePanel panel, LengthyNote note, double beat, double time) {
        return connDrawable;
    }

    @Override
    public Drawable getNoteTailDrawable(NotePanel panel, LengthyNote note, double beat, double time) {
        return tailDrawable;
    }
}
