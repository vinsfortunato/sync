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
import net.touchmania.game.song.note.MineNote;
import net.touchmania.game.song.note.NotePanel;

/**
 * @author flood2d
 */
public class MineNoteDrawer extends BaseNoteDrawer<MineNote> {
    public MineNoteDrawer(BeatmapView view) {
        super(view);
    }

    @Override
    public float getNoteRotation(MineNote note, NotePanel panel, double beat, double time) {
        float degrees = -60f * (float) getRound().getCurrentBeat();
        degrees %= 360f;
        return degrees;
    }

    @Override
    public TextureRegion getNoteTextureRegion(MineNote note, NotePanel panel, double beat, double time) {
        return null; //TODO return mine texture
    }
}
