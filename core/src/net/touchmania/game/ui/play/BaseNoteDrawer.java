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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.touchmania.game.round.Round;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.NotePanel;

public class BaseNoteDrawer<N extends Note> implements NoteDrawer<N> {
    private final BeatmapView view;

    public BaseNoteDrawer(BeatmapView view) {
        this.view = view;
    }

    @Override
    public void draw(Batch batch, N note, NotePanel panel, double beat, double time) {

    }

    @Override
    public float getNoteX(N note, NotePanel panel, double beat, double time) {
        return 0;
    }

    @Override
    public float getNoteY(N note, NotePanel panel, double beat, double time) {
        return 0;
    }

    @Override
    public float getNoteScaleX(N note, NotePanel panel, double beat, double time) {
        return 0;
    }

    @Override
    public float getNoteScaleY(N note, NotePanel panel, double beat, double time) {
        return 0;
    }

    @Override
    public float getNoteRotation(N note, NotePanel panel, double beat, double time) {
        float rotation = 0.0f; //for panels: down, right_down, center.
        switch(panel) { //rotate texture according to the panel
            case LEFT:
            case LEFT_DOWN:
                rotation = -90f;
                break;
            case UP:
            case LEFT_UP:
                rotation = 180f;
                break;
            case RIGHT:
            case RIGHT_UP:
                rotation = 90f;
                break;
        }
        return rotation;
    }

    @Override
    public float getNoteOpacity(N note, NotePanel panel, double beat, double time) {
        return 0;
    }

    @Override
    public boolean isNoteVisible(N note, NotePanel panel, double beat, double time) {
        return false;
    }

    @Override
    public TextureRegion getNoteTextureRegion(N note, NotePanel panel, double beat, double time) {
        return null;
    }

    @Override
    public Round getRound() {
        return view.getRound();
    }
}
