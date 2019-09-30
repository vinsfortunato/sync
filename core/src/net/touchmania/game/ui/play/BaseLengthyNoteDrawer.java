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

package net.touchmania.game.ui.play;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.touchmania.game.song.note.LengthyNote;
import net.touchmania.game.song.note.NotePanel;

/**
 * @author flood2d
 */
public class BaseLengthyNoteDrawer<N extends LengthyNote> extends BaseNoteDrawer<N> implements LengthyNoteDrawer<N> {
    public BaseLengthyNoteDrawer(BeatmapView view) {
        super(view);
    }

    @Override
    public float getTailX(N note, NotePanel panel, double beat, double time) {
        return 0;
    }

    @Override
    public float getTailY(N note, NotePanel panel, double beat, double time) {
        return 0;
    }

    @Override
    public boolean isActive(N note, NotePanel panel, double beat, double time) {
        return false;
    }

    @Override
    public TextureRegion getNoteBodyTexture(N note, NotePanel panel, double beat, double time) {
        return null;
    }

    @Override
    public TextureRegion getNoteConnectorTexture(N note, NotePanel panel, double beat, double time) {
        return null;
    }

    @Override
    public TextureRegion getNoteTailTexture(N note, NotePanel panel, double beat, double time) {
        return null;
    }
}
