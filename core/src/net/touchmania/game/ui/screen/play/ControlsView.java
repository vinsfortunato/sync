/*
 * Copyright 2019 Vincenzo Fortunato
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
import net.touchmania.game.round.PanelState;
import net.touchmania.game.round.Round;
import net.touchmania.game.song.note.NotePanel;

public class ControlsView extends Widget {
    private float controlWidth = 240;
    private float controlHeight = 240;

    private Round round;

    public ControlsView(Round round) {
        super();
        this.round = round;
        addListener(new GameControlsInputListener());
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
        float baseX = getWidth() / 2 - controlWidth * 1.5f;
        float baseY = 100;
        return new Rectangle(baseX, baseY + controlHeight, controlWidth, controlHeight);
    }

    private Rectangle getRightRect() {
        float baseX = getWidth() / 2 - controlWidth * 1.5f;
        float baseY = 100;
        return new Rectangle(baseX + controlWidth * 2, baseY + controlHeight, controlWidth, controlHeight);
    }

    private Rectangle getUpRect() {
        float baseX = getWidth() / 2 - controlWidth * 1.5f;
        float baseY = 100;
        return new Rectangle(baseX + controlWidth, baseY + controlHeight * 2, controlWidth, controlHeight);
    }

    private Rectangle getDownRect() {
        float baseX = getWidth() / 2 - controlWidth * 1.5f;
        float baseY = 100;
        return new Rectangle(baseX + controlWidth, baseY, controlWidth, controlHeight);
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private PanelState getControls() {
        return round.getPanelState();
    }

    class GameControlsInputListener extends InputListener {
        private IntArray leftPointers = new IntArray();
        private IntArray downPointers = new IntArray();
        private IntArray upPointers = new IntArray();
        private IntArray rightPointers = new IntArray();

        private long syncNanoTime = -1;
        private long syncEventTime = -1;

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if(syncNanoTime == -1) {
                float delta = Gdx.graphics.getRawDeltaTime();
                syncNanoTime = System.nanoTime() - (long) ((delta / 2.0f) * 1000L) * 1_000_000;
                syncEventTime = Gdx.input.getCurrentEventTime();
            }
            long eventTime = syncNanoTime + (Gdx.input.getCurrentEventTime() - syncEventTime);

            double eventTimeSeconds = round.getMusicPosition().getPositionAt(eventTime);
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
            if(syncNanoTime == -1) {
                float delta = Gdx.graphics.getRawDeltaTime();
                syncNanoTime = System.nanoTime() - (long) ((delta / 2.0f) * 1000L) * 1_000_000;
                syncEventTime = Gdx.input.getCurrentEventTime();
            }
            long eventTime = syncNanoTime + (Gdx.input.getCurrentEventTime() - syncEventTime);

            double eventTimeSeconds = round.getMusicPosition().getPositionAt(eventTime);
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
            Vector2 center = new Vector2();

            getLeftRect().getCenter(center);
            float distance = getDistance(x, y, center.x, center.y);
            float d;

            getDownRect().getCenter(center);
            d = getDistance(x, y, center.x, center.y);
            if(d < distance) {
                distance = d;
                noteColumn = NotePanel.DOWN;
            }

            getUpRect().getCenter(center);
            d = getDistance(x, y, center.x, center.y);
            if(d < distance) {
                distance = d;
                noteColumn = NotePanel.UP;
            }

            getRightRect().getCenter(center);
            d = getDistance(x, y, center.x, center.y);
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
            else if(downPointers.removeValue(pointer) && downPointers.size == 0) {
                panelState.setReleased(NotePanel.DOWN, eventTimeSeconds);
            }
            else if(upPointers.removeValue(pointer) && upPointers.size == 0) {
                panelState.setReleased(NotePanel.UP, eventTimeSeconds);
            }
        }
    }
}
