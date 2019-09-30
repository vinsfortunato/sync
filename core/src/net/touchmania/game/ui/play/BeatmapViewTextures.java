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
import net.touchmania.game.match.Match;
import net.touchmania.game.song.note.NoteResolution;
import net.touchmania.game.song.note.NoteType;

/**
 * @author flood2d
 */
public class BeatmapViewTextures {
    private BeatmapView view;
    private GameScreen screen;
    private Match match;
    private TextureRegion receptorGlowTexture;
    private TextureRegion receptorOutlineTexture;
    private TextureRegion receptorTexture;
    private TextureRegion mineTexture;
    private TextureRegion holdTailInactiveTexture;
    private TextureRegion holdConnectorInactiveTexture;
    private TextureRegion holdBodyInactiveTexture;
    private TextureRegion holdTailActiveTexture;
    private TextureRegion holdConnectorActiveTexture;
    private TextureRegion holdBodyActiveTexture;

    protected BeatmapViewTextures(BeatmapView view, GameScreen screen, Match match) {
        this.view = view;
        this.screen = screen;
        this.match = match;
    }

    protected void load() {
        receptorTexture = screen.getTextureAtlas().findRegion("receptor");
        receptorGlowTexture = screen.getTextureAtlas().findRegion("receptor_glow");
        receptorOutlineTexture = screen.getTextureAtlas().findRegion("receptor_outline");
        if(true){//match.getChart().hasMines()) {
            mineTexture = screen.getTextureAtlas().findRegion("mine");
        }
        if(true){//match.getChart().hasHolds()) {
            holdTailInactiveTexture = screen.getTextureAtlas().findRegion("hold_tail_inactive");
            holdConnectorInactiveTexture = screen.getTextureAtlas().findRegion("hold_connector_inactive");
            holdBodyInactiveTexture = screen.getTextureAtlas().findRegion("hold_body_inactive");
            holdTailActiveTexture = screen.getTextureAtlas().findRegion("hold_tail_active");
            holdConnectorActiveTexture = screen.getTextureAtlas().findRegion("hold_connector_active");
            holdBodyActiveTexture = screen.getTextureAtlas().findRegion("hold_body_active");
        }
    }

    protected TextureRegion getHoldTailTexture(boolean active) {
        if(active) {
            return getHoldTailActiveTexture();
        }
        return getHoldTailInactiveTexture();
    }

    protected TextureRegion getHoldConnectorTexture(boolean active) {
        if(active) {
            return getHoldConnectorActiveTexture();
        }
        return getHoldConnectorInactiveTexture();
    }

    protected TextureRegion getHoldBodyTexture(boolean active) {
        if(active) {
            return getHoldBodyActiveTexture();
        }
        return getHoldBodyInactiveTexture();
    }

    protected TextureRegion getHoldTailInactiveTexture() {
        return holdTailInactiveTexture;
    }

    protected TextureRegion getHoldConnectorInactiveTexture() {
        return holdConnectorInactiveTexture;
    }

    protected TextureRegion getHoldBodyInactiveTexture() {
        return holdBodyInactiveTexture;
    }

    protected TextureRegion getHoldTailActiveTexture() {
        return holdTailActiveTexture;
    }

    protected TextureRegion getHoldConnectorActiveTexture() {
        return holdConnectorActiveTexture;
    }

    protected TextureRegion getHoldBodyActiveTexture() {
        return holdBodyActiveTexture;
    }

    protected TextureRegion getReceptorTexture() {
        return receptorTexture;
    }

    protected TextureRegion getReceptorGlowTexture() {
        return receptorGlowTexture;
    }

    protected TextureRegion getReceptorOutlineTexture() {
        return receptorOutlineTexture;
    }

    protected TextureRegion getMineTexture() {
        return mineTexture;
    }

    protected TextureRegion getNoteTexture(NoteType noteType, float beat) {
        switch(noteType) {
            case HOLD:
            case ROLL:
            case TAP:
                return getArrowTextureRegion(NoteResolution.valueFromBeat(beat));
            case MINE:
                return getMineTexture();
        }
        return null;
    }

    protected TextureRegion getArrowTextureRegion(NoteResolution noteInterval) {
        String regionName;
        switch(noteInterval) {
            case NOTE_4TH:
                regionName = "arrow_4th";
                break;
            case NOTE_8TH:
                regionName = "arrow_8th";
                break;
            case NOTE_12TH:
                regionName = "arrow_12th";
                break;
            case NOTE_16TH:
                regionName = "arrow_16th";
                break;
            case NOTE_24TH:
                regionName = "arrow_24th";
                break;
            case NOTE_32ND:
                regionName = "arrow_32nd";
                break;
            case NOTE_48TH:
                regionName = "arrow_48th";
                break;
            default:
                regionName = "arrow_64th";
                break;
        }

        return screen.getTextureAtlas().findRegion(regionName);
    }
}
