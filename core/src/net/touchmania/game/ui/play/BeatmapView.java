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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import net.touchmania.game.song.NoteColumn;
import net.touchmania.game.song.OldBeatmap;
import net.touchmania.game.song.note.NoteType;

import java.util.Map;
import java.util.TreeMap;

/**
 * Draws notes and receptors.
 * @author flood2d
 */
public class BeatmapView extends Widget {
    private GameScreen screen;
    private BeatmapViewTextures textures;
    private BeatmapViewState state;

    public BeatmapView(GameScreen screen) {
        this.screen = screen;
        this.textures = new BeatmapViewTextures(this, screen, screen.getMatch());
        this.textures.load();
        this.state = new BeatmapViewState(this, screen.getMatch());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        state.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawReceptor(batch, NoteColumn.LEFT);
        drawReceptor(batch, NoteColumn.DOWN);
        drawReceptor(batch, NoteColumn.UP);
        drawReceptor(batch, NoteColumn.RIGHT);
        drawNotes(batch, NoteColumn.LEFT);
        drawNotes(batch, NoteColumn.DOWN);
        drawNotes(batch, NoteColumn.UP);
        drawNotes(batch, NoteColumn.RIGHT);
    }

    private void drawReceptor(Batch batch, NoteColumn noteColumn) {
        float x = state.getReceptorX(noteColumn);
        float y = state.getReceptorY(noteColumn);
        float w = state.getReceptorWidth(noteColumn);
        float h = state.getReceptorHeight(noteColumn);
        float scaleX = state.getReceptorOutlineScaleX(noteColumn);
        float scaleY = state.getReceptorOutlineScaleY(noteColumn);
        float opacity = state.getReceptorOutlineOpacity(noteColumn);
        float rotation = state.getReceptorRotation(noteColumn);
        Color previousColor = batch.getColor();
        Color nextColor = new Color();
        nextColor.set(previousColor);

        if(opacity > 0) { //Draw receptor outline
            nextColor.a = opacity;
            batch.setColor(nextColor);
            batch.draw(textures.getReceptorOutlineTexture(), x, y, w / 2, h / 2, w, h, scaleX, scaleY, rotation);
        }

        opacity = state.getReceptorOpacity(noteColumn);
        if(opacity > 0) { //draw receptor
            nextColor.a = opacity;
            scaleX = state.getReceptorScaleX(noteColumn);
            scaleY = state.getReceptorScaleY(noteColumn);
            batch.setColor(nextColor);
            batch.draw(textures.getReceptorTexture(), x, y, w / 2, h / 2, w, h, scaleX, scaleY, rotation);
        }

        batch.setColor(previousColor);
    }

    private void drawNotes(Batch batch, NoteColumn noteColumn) {
        OldBeatmap beatmap = null; //getScreen().getMatch().getChart().getBeatmap();
        float minimumBeat = state.getMinimumBeat(noteColumn);
        float maximumBeat = state.getMaximumBeat(noteColumn);
        TreeMap<Float, NoteType> notesMap = beatmap.getNotesMap(noteColumn);
        NoteType noteType;
        float beat, x, y, originX, originY, rotation, opacity;
        Color previousColor;
        Color nextColor;
        TextureRegion textureRegion;

        Map.Entry<Float, NoteType> entry = notesMap.ceilingEntry(minimumBeat);
        if(entry.getValue() == NoteType.HOLD_ROLL_TAIL) {
            entry = notesMap.lowerEntry(entry.getKey());
        }
        while(entry != null && entry.getKey() <= maximumBeat) {
            noteType = entry.getValue();
            beat = entry.getKey();
            x = state.getNoteX(noteColumn, noteType, beat);
            y = state.getNoteY(noteColumn, noteType, beat);
            originX = state.getReceptorWidth(noteColumn) / 2;
            originY = state.getReceptorHeight(noteColumn) / 2;
            rotation = state.getNoteRotation(noteColumn, noteType, beat);
            opacity = state.getNoteOpacity(noteColumn, noteType, beat);

            if(noteType == NoteType.HOLD) {
                entry = notesMap.higherEntry(entry.getKey());
                if(entry.getValue() == NoteType.HOLD_ROLL_TAIL && state.isHoldVisible(noteColumn, beat)) {
                    drawHold(batch, noteColumn, y, beat, entry.getKey());
                }

            } else if(noteType == NoteType.ROLL) {
                entry = notesMap.higherEntry(entry.getKey());
                if(entry.getValue() == NoteType.HOLD_ROLL_TAIL  /**TODO && state.isRollVisible(noteColumn, noteType, beat)**/) {
                    drawHold(batch, noteColumn, y, beat, entry.getKey());
                }
            }

            previousColor = batch.getColor();
            nextColor = new Color();
            nextColor.set(previousColor);
            nextColor.a = opacity;
            batch.setColor(nextColor);

            switch(noteType) {
                case TAP:
                case MINE:
                case HOLD:
                case ROLL: {
                    if(state.isNoteVisible(noteColumn, noteType, beat)) {
                        textureRegion = textures.getNoteTexture(noteType, beat);
                        batch.draw(textureRegion, x, y, originX, originY,
                                textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
                                1.0f, 1.0f, rotation);
                    }
                    break;
                }
            }

            batch.setColor(previousColor);
            entry = notesMap.higherEntry(entry.getKey());
        }
    }

    private void drawHold(Batch batch, NoteColumn noteColumn, float headY, float headBeat, float tailBeat) {
        boolean active = state.isHoldActive(noteColumn, headBeat);
        float tailY = state.getNoteY(noteColumn, NoteType.HOLD_ROLL_TAIL, tailBeat);
        float tailX = state.getNoteX(noteColumn, NoteType.HOLD_ROLL_TAIL, tailBeat);
        float distance = Math.abs(headY - tailY);
        float y = tailY;
        TextureRegion holdTailTexture = textures.getHoldTailTexture(active);
        TextureRegion holdConnectorTexture = textures.getHoldConnectorTexture(active);
        TextureRegion holdBodyTexture = textures.getHoldBodyTexture(active);
        if(state.getApproachDirection() == BeatmapViewState.ApproachDirection.FROM_BOTTOM_TO_TOP) {
            if(tailY + holdTailTexture.getRegionHeight() > 0) {
                batch.draw(holdTailTexture, tailX, y);
            }
            y += holdTailTexture.getRegionHeight();
            if(distance <= holdConnectorTexture.getRegionHeight()) {
                if(y + distance > 0) {
                    TextureRegion region = holdConnectorTexture;
                    int previousRegionHeight = region.getRegionHeight();
                    region.setRegionHeight((int) distance);
                    batch.draw(region, tailX, y);
                    region.setRegionHeight(previousRegionHeight);
                }
            } else {
                if(y + holdConnectorTexture.getRegionHeight() > 0) {
                    batch.draw(holdConnectorTexture, tailX, y);
                }
                y += holdConnectorTexture.getRegionHeight();
                distance -= holdConnectorTexture.getRegionHeight();
                while(distance > holdBodyTexture.getRegionHeight()) {
                    if(y + holdBodyTexture.getRegionHeight() > 0) {
                        batch.draw(holdBodyTexture, tailX, y);
                    }
                    y += holdBodyTexture.getRegionHeight();
                    distance -= holdBodyTexture.getRegionHeight();
                }
                if(y + distance > 0) {
                    int previousRegionHeight = holdBodyTexture.getRegionHeight();
                    holdBodyTexture.setRegionHeight((int) distance);
                    batch.draw(holdBodyTexture, tailX, y);
                    holdBodyTexture.setRegionHeight(previousRegionHeight);
                }
            }
        } else {
            y += holdTailTexture.getRegionHeight();
            if(y < getHeight()){
                TextureRegion region = holdTailTexture;
                float originX = region.getRegionWidth() / 2;
                float originY = region.getRegionHeight() / 2;
                batch.draw(region, tailX, y, originX, originY,
                        region.getRegionWidth(), region.getRegionHeight(), 1.0f, 1.0f, 180);
            }
            if(distance <= holdConnectorTexture.getRegionHeight()) {
                y -= distance;
                if(y < getHeight()) {
                    int previousRegionHeight = holdConnectorTexture.getRegionHeight();
                    holdConnectorTexture.setRegionHeight((int) distance);
                    float originX = holdConnectorTexture.getRegionWidth() / 2;
                    float originY = holdConnectorTexture.getRegionHeight() / 2;
                    batch.draw(holdConnectorTexture, tailX, y, originX, originY,
                            holdConnectorTexture.getRegionWidth(),
                            holdConnectorTexture.getRegionHeight(), 1.0f, 1.0f, 180);
                    holdConnectorTexture.setRegionHeight(previousRegionHeight);
                }
            } else {
                float originX = holdConnectorTexture.getRegionWidth() / 2;
                float originY = holdConnectorTexture.getRegionHeight() / 2;
                y -= holdConnectorTexture.getRegionHeight();
                if(y < getHeight()) {
                    batch.draw(holdConnectorTexture, tailX, y, originX, originY,
                            holdConnectorTexture.getRegionWidth(),
                            holdConnectorTexture.getRegionHeight(), 1.0f, 1.0f, 180);
                }
                distance -= holdConnectorTexture.getRegionHeight();
                originX = holdBodyTexture.getRegionWidth() / 2;
                originY = holdBodyTexture.getRegionHeight() / 2;
                while(distance > holdBodyTexture.getRegionHeight()) {
                    y -= holdBodyTexture.getRegionHeight();
                    if(y < getHeight()) {
                        batch.draw(holdBodyTexture, tailX, y, originX, originY,
                                holdBodyTexture.getRegionWidth(),
                                holdBodyTexture.getRegionHeight(), 1.0f, 1.0f, 180);
                    }
                    distance -= holdBodyTexture.getRegionHeight();
                }
                y -= distance;
                if(y < getHeight()) {
                    int previousRegionHeight = holdBodyTexture.getRegionHeight();
                    holdBodyTexture.setRegionHeight((int) distance);
                    originX = holdBodyTexture.getRegionWidth() / 2;
                    originY = holdBodyTexture.getRegionHeight() / 2;
                    batch.draw(holdBodyTexture, tailX, y, originX, originY,
                            holdBodyTexture.getRegionWidth(),
                            holdBodyTexture.getRegionHeight(), 1.0f, 1.0f, 180);
                    holdBodyTexture.setRegionHeight(previousRegionHeight);
                }
            }
        }
    }

    private void drawRoll(Batch batch, NoteColumn noteColumn, float headY, float headBeat, float tailBeat) {

    }

    public GameScreen getScreen() {
        return screen;
    }

    public BeatmapViewTextures getTextures() {
        return textures;
    }

    public BeatmapViewState getState() {
        return state;
    }
}
