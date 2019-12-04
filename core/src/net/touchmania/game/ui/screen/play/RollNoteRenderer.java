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
import net.touchmania.game.song.note.LengthyNote;
import net.touchmania.game.song.note.Note;

/**
 * @author flood2d
 */
public class RollNoteRenderer extends BaseLengthyNoteRenderer {
    /* Resources */

    public RollNoteRenderer(BeatmapView view) {
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

    @Override
    public Drawable getNoteBodyDrawable(int panel, LengthyNote note, double beat, double time) {
        return null;
    }

    @Override
    public Drawable getNoteConnectorDrawable(int panel, LengthyNote note, double beat, double time) {
        return null;
    }

    @Override
    public Drawable getNoteTailDrawable(int panel, LengthyNote note, double beat, double time) {
        return null;
    }
}
