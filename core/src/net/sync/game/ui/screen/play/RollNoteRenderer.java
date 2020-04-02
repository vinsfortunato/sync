/*
 * Copyright 2020 Vincenzo Fortunato
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

package net.sync.game.ui.screen.play;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.sync.game.Game;
import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.song.note.LengthyNote;
import net.sync.game.song.note.Note;
import net.sync.game.song.note.RollNote;

/**
 * @author Vincenzo Fortunato
 */
public class RollNoteRenderer extends BaseLengthyNoteRenderer {
    /* Resources */
    private net.sync.game.resource.lazy.Resource<Drawable> head4Drawable;
    private net.sync.game.resource.lazy.Resource<Drawable> head8Drawable;
    private net.sync.game.resource.lazy.Resource<Drawable> head12Drawable;
    private net.sync.game.resource.lazy.Resource<Drawable> head16Drawable;
    private net.sync.game.resource.lazy.Resource<Drawable> head24Drawable;
    private net.sync.game.resource.lazy.Resource<Drawable> head32Drawable;
    private net.sync.game.resource.lazy.Resource<Drawable> head48Drawable;
    private net.sync.game.resource.lazy.Resource<Drawable> head64Drawable;
    private net.sync.game.resource.lazy.Resource<Drawable> head192Drawable;
    private net.sync.game.resource.lazy.Resource<Drawable> bodyInactiveDrawable;
    private net.sync.game.resource.lazy.Resource<Drawable> bodyActiveDrawable;
    private net.sync.game.resource.lazy.Resource<Drawable> tailInactiveDrawable;
    private Resource<Drawable> tailActiveDrawable;

    public RollNoteRenderer(BeatmapView view) {
        super(view);
        ResourceProvider resources = Game.instance().getResources();
        (head4Drawable        = resources.getDrawable("play_dance_note_roll_head_4"        )).load();
        (head8Drawable        = resources.getDrawable("play_dance_note_roll_head_8"        )).load();
        (head12Drawable       = resources.getDrawable("play_dance_note_roll_head_12"       )).load();
        (head16Drawable       = resources.getDrawable("play_dance_note_roll_head_16"       )).load();
        (head32Drawable       = resources.getDrawable("play_dance_note_roll_head_32"       )).load();
        (head24Drawable       = resources.getDrawable("play_dance_note_roll_head_24"       )).load();
        (head48Drawable       = resources.getDrawable("play_dance_note_roll_head_48"       )).load();
        (head64Drawable       = resources.getDrawable("play_dance_note_roll_head_64"       )).load();
        (head192Drawable      = resources.getDrawable("play_dance_note_roll_head_192"      )).load();
        (bodyInactiveDrawable = resources.getDrawable("play_dance_note_roll_body_inactive" )).load();
        (bodyActiveDrawable   = resources.getDrawable("play_dance_note_roll_body_active"   )).load();
        (tailInactiveDrawable = resources.getDrawable("play_dance_note_roll_tail_inactive" )).load();
        (tailActiveDrawable   = resources.getDrawable("play_dance_note_roll_tail_active"   )).load();
    }

    @Override
    public Drawable getNoteDrawable(int panel, Note note, double beat, double time) {
        RollNote rollNote = (RollNote) note;
        switch (rollNote.getResolution()) {
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
}
