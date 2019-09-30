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
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import net.touchmania.game.match.ControlStateListener;
import net.touchmania.game.song.NoteColumn;

/**
 * @author flood2d
 */
public class GameButton extends Actor implements ControlStateListener {
    private GameScreen screen;
    private TextureRegion icon;
    private NinePatch background;
    private NinePatch glow;
    private AlphaAction glowAnimation;
    private NoteColumn noteColumn;

    public GameButton(GameScreen screen, NoteColumn noteColumn) {
        this.screen = screen;
        this.noteColumn = noteColumn;
        loadAssets();
        glow.getColor().a = 0.0f;
        glowAnimation = new AlphaAction();
        glowAnimation.setColor(glow.getColor());
        glowAnimation.setAlpha(0.0f);
        glowAnimation.setDuration(0.15f);
    }

    private void loadAssets() {
        icon = screen.getTextureAtlas().findRegion("control_icon");
        background = new NinePatch(
                screen.getTextureAtlas().findRegion("control"), 42, 42, 42, 42);
        glow = new NinePatch(
                screen.getTextureAtlas().findRegion("control_glow"), 42, 42, 42, 42);
    }

    public float getCenterX() {
        return getX() + getWidth() / 2;
    }

    public float getCenterY() {
        return getY() + getHeight() / 2;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        background.draw(batch, getX(), getY(), getWidth(), getHeight());
        glow.draw(batch, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void onControlPressed(NoteColumn noteColumn, float inputTime) {
        if(this.noteColumn == noteColumn) {
            glow.getColor().a = 1.0f;
            removeAction(glowAnimation);
        }
    }

    @Override
    public void onControlReleased(NoteColumn noteColumn, float inputTime) {
        if(this.noteColumn == noteColumn) {
            glowAnimation.restart();
            addAction(glowAnimation);
        }
    }
}
