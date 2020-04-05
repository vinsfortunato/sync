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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.IntArray;
import net.sync.game.round.PanelState;
import net.sync.game.round.Round;
import net.sync.game.song.note.NotePanel;

public class ControlsView extends Widget {
    private float controlWidth = 40;
    private float controlHeight = 40;

    private Round round;

    public ControlsView(Round round) {
        super();
        this.round = round;
        addListener(Gdx.app.getType() == Application.ApplicationType.Desktop ? new DesktopControlListener() : new MobileControlListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            return;
        }
        batch.end();

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        Rectangle[] rects = getRects();
        Vector2 center = new Vector2();

        for(int i = 0; i < rects.length; i++) {
            Rectangle rect = rects[i];
            renderer.set(ShapeRenderer.ShapeType.Line);
            renderer.setColor(Color.RED);
            renderer.rect(rect.x, rect.y, rect.width, rect.height);

            int panel = -1;
            switch(i) {
                case 0:
                    panel = NotePanel.LEFT;
                    break;
                case 1:
                    panel = NotePanel.RIGHT;
                    break;
                case 2:
                    panel = NotePanel.UP;
                    break;
                case 3:
                    panel = NotePanel.DOWN;
                    break;
            }
            if(getControls().isPressedAt(panel, round.getMusicPosition().getPosition())) {
                rect.getCenter(center);
                renderer.setColor(new Color(1, 0, 0, 0.3f));
                renderer.set(ShapeRenderer.ShapeType.Filled);
                renderer.rect(rect.x, rect.y, rect.width, rect.height);
            }
        }
        renderer.end();

        batch.begin();
    }

    private Rectangle[] getRects() {
        return new Rectangle[] { getLeftRect(), getRightRect(), getUpRect(), getDownRect() };
    }

    private Rectangle getLeftRect() {
        float controlHeight = this.controlHeight * 2.0f;
        float baseX = getWidth() / 2 - controlWidth * 1.5f;
        float baseY = 350;
        return new Rectangle(baseX, baseY + this.controlHeight / 2, controlWidth, controlHeight);
    }

    private Rectangle getRightRect() {
        float controlHeight = this.controlHeight * 2.0f;
        float baseX = getWidth() / 2 - controlWidth * 1.5f;
        float baseY = 350;
        return new Rectangle(baseX + controlWidth * 2, baseY + this.controlHeight / 2, controlWidth, controlHeight);
    }

    private Rectangle getUpRect() {
        float controlWidth = this.controlWidth * 2;
        float baseX = getWidth() / 2 - this.controlWidth * 1.5f;
        float baseY = 350;
        return new Rectangle(baseX + this.controlWidth / 2, baseY + controlHeight * 2.5f, controlWidth, controlHeight);
    }

    private Rectangle getDownRect() {
        float controlWidth = this.controlWidth * 2;
        float baseX = getWidth() / 2 - this.controlWidth * 1.5f;
        float baseY = 350;
        return new Rectangle(baseX + this.controlWidth / 2, baseY - controlHeight * 0.5f, controlWidth, controlHeight);
    }

    private float getDistance(float x, float y, Rectangle rect) {
        float rx = Math.max(Math.abs(rect.x + rect.width / 2.0f - x) - rect.width / 2.0f, 0.0f);
        float ry = Math.max(Math.abs(rect.y + rect.height / 2.0f - y) - rect.height / 2.0f, 0.0f);
        return (float) Math.sqrt(Math.pow(rx, 2.0f) + Math.pow(ry, 2.0f));
    }

    private PanelState getControls() {
        return round.getPanelState();
    }

    class MobileControlListener extends InputListener {
        private IntArray leftPointers = new IntArray();
        private IntArray downPointers = new IntArray();
        private IntArray upPointers = new IntArray();
        private IntArray rightPointers = new IntArray();

        @Override
        public void touchDragged (InputEvent event, float x, float y, int pointer) {
            int noteColumn = getNearControl(x, y);
            double eventTimeSeconds = round.getMusicPosition().getPositionAt(Gdx.input.getCurrentEventTime());

            switch(noteColumn) {
                case NotePanel.LEFT:
                    if(!leftPointers.contains(pointer)) {
                        removePointer(pointer);
                        leftPointers.add(pointer);
                        getControls().setPressed(noteColumn, eventTimeSeconds);
                    }
                    break;
                case NotePanel.DOWN:
                    if(!downPointers.contains(pointer)) {
                        removePointer(pointer);
                        downPointers.add(pointer);
                        getControls().setPressed(noteColumn, eventTimeSeconds);
                    }
                    break;
                case NotePanel.UP:
                    if(!upPointers.contains(pointer)) {
                        removePointer(pointer);
                        upPointers.add(pointer);
                        getControls().setPressed(noteColumn, eventTimeSeconds);
                    }
                    break;
                case NotePanel.RIGHT:
                    if(!rightPointers.contains(pointer)) {
                        removePointer(pointer);
                        rightPointers.add(pointer);
                        getControls().setPressed(noteColumn, eventTimeSeconds);
                    }
                    break;
            }
        }

        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            int noteColumn = getNearControl(x, y);

            double eventTimeSeconds = round.getMusicPosition().getPositionAt(Gdx.input.getCurrentEventTime());

            getControls().setPressed(noteColumn, eventTimeSeconds);
            switch(noteColumn) {
                case NotePanel.LEFT:
                    leftPointers.add(pointer);
                    break;
                case  NotePanel.RIGHT:
                    rightPointers.add(pointer);
                    break;
                case  NotePanel.UP:
                    upPointers.add(pointer);
                    break;
                case NotePanel.DOWN:
                    downPointers.add(pointer);
                    break;
            }
            return true;
        }

        private int getNearControl(float x, float y) {
            int noteColumn = NotePanel.LEFT;
            Rectangle rect;

            rect = getLeftRect();
            float distance = getDistance(x, y, rect);
            float d;

            rect = getDownRect();
            d = getDistance(x, y, rect);
            if(d < distance) {
                distance = d;
                noteColumn = NotePanel.DOWN;
            }

            rect = getUpRect();
            d = getDistance(x, y, rect);
            if(d < distance) {
                distance = d;
                noteColumn = NotePanel.UP;
            }

            rect = getRightRect();
            d = getDistance(x, y, rect);
            if(d < distance) {
                noteColumn = NotePanel.RIGHT;
            }
            return noteColumn;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            removePointer(pointer);
        }

        private void removePointer(int pointer) {
            PanelState panelState = getControls();

            double eventTimeSeconds = round.getMusicPosition().getPositionAt(Gdx.input.getCurrentEventTime());

            if(leftPointers.removeValue(pointer) && leftPointers.size == 0) {
                panelState.setReleased(NotePanel.LEFT, eventTimeSeconds);
            }
            else if(rightPointers.removeValue(pointer) && rightPointers.size == 0) {
                panelState.setReleased(NotePanel.RIGHT, eventTimeSeconds);
            }
            else if(downPointers.removeValue(pointer) && downPointers.size == 0)  {
                panelState.setReleased(NotePanel.DOWN, eventTimeSeconds);
            }
            else if(upPointers.removeValue(pointer) && upPointers.size == 0) {
                panelState.setReleased(NotePanel.UP, eventTimeSeconds);
            }
        }
    }

    class DesktopControlListener extends InputListener {
        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            double eventTimeSeconds = round.getMusicPosition().getPosition();
            if(keycode == Input.Keys.Y) {
                getControls().setPressed(NotePanel.UP, eventTimeSeconds);
            }
            if(keycode == Input.Keys.G) {
                getControls().setPressed(NotePanel.LEFT, eventTimeSeconds);
            }
            if(keycode == Input.Keys.H) {
                getControls().setPressed(NotePanel.DOWN, eventTimeSeconds);
            }
            if(keycode == Input.Keys.J) {
                getControls().setPressed(NotePanel.RIGHT, eventTimeSeconds);
            }
            return true;
        }

        @Override
        public boolean keyUp(InputEvent event, int keycode) {
            double eventTimeSeconds = round.getMusicPosition().getPosition();
            if(keycode == Input.Keys.Y) {
                getControls().setReleased(NotePanel.UP, eventTimeSeconds);
            }
            if(keycode == Input.Keys.G) {
                getControls().setReleased(NotePanel.LEFT, eventTimeSeconds);
            }
            if(keycode == Input.Keys.H) {
                getControls().setReleased(NotePanel.DOWN, eventTimeSeconds);
            }
            if(keycode == Input.Keys.J) {
                getControls().setReleased(NotePanel.RIGHT, eventTimeSeconds);
            }
            return true;
        }
    }
}
