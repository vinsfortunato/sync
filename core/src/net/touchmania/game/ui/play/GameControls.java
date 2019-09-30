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

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.IntArray;
import net.touchmania.game.match.ControlState;
import net.touchmania.game.song.NoteColumn;

/**
 * @author flood2d
 */
public class GameControls extends Group {
    private GameScreen screen;
    private GameButton leftButton;
    private GameButton downButton;
    private GameButton upButton;
    private GameButton rightButton;

    public GameControls(GameScreen screen) {
        this.screen = screen;
        leftButton = createButton(NoteColumn.LEFT);
        downButton = createButton(NoteColumn.DOWN);
        upButton = createButton(NoteColumn.UP);
        rightButton = createButton(NoteColumn.RIGHT);
        setWidth(1080); //TODO 276
        setHeight(1920); //TODO 186

        int pW = 276 * 3;
        int pH = 186 * 3;

        leftButton.setX(getWidth() / 2 - pW / 2);
        leftButton.setY(100 + pH / 3);
        downButton.setX(getWidth() / 2 - pW / 6);
        downButton.setY(100);
        upButton.setX(getWidth() / 2 - pW / 6);
        upButton.setY(100 + 2 * (pH / 3));
        rightButton.setX(getWidth() / 2 + pW / 6);
        rightButton.setY(100 + pH / 3);
        addActor(leftButton);
        addActor(downButton);
        addActor(upButton);
        addActor(rightButton);
        addListener(new GameControlsInputListener());
        screen.getMatch().getControls().addListener(leftButton);
        screen.getMatch().getControls().addListener(downButton);
        screen.getMatch().getControls().addListener(upButton);
        screen.getMatch().getControls().addListener(rightButton);
    }

    private GameButton createButton(NoteColumn noteColumn) {
        GameButton button = new GameButton(screen, noteColumn);
        button.setWidth(276);
        button.setHeight(186);
        return button;
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    class GameControlsInputListener extends InputListener {
        private IntArray leftPointers = new IntArray();
        private IntArray downPointers = new IntArray();
        private IntArray upPointers = new IntArray();
        private IntArray rightPointers = new IntArray();

        @Override
        public void touchDragged (InputEvent event, float x, float y, int pointer) {
            ControlState controls = screen.getMatch().getControls();
            NoteColumn noteColumn = getNearControl(x, y);
            switch(noteColumn) {
                case LEFT:
                    if(!leftPointers.contains(pointer)) {
                        removePointer(pointer);
                        leftPointers.add(pointer);
                        controls.setPressed(noteColumn, true);
                    }
                    break;
                case DOWN:
                    if(!downPointers.contains(pointer)) {
                        removePointer(pointer);
                        downPointers.add(pointer);
                        controls.setPressed(noteColumn, true);
                    }
                    break;
                case UP:
                    if(!upPointers.contains(pointer)) {
                        removePointer(pointer);
                        upPointers.add(pointer);
                        controls.setPressed(noteColumn, true);
                    }
                    break;
                case RIGHT:
                    if(!rightPointers.contains(pointer)) {
                        removePointer(pointer);
                        rightPointers.add(pointer);
                        controls.setPressed(noteColumn, true);
                    }
                    break;
            }
        }

        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            ControlState controls = screen.getMatch().getControls();
            NoteColumn noteColumn = getNearControl(x, y);
            controls.setPressed(noteColumn, true);
            switch(noteColumn) {
                case LEFT:
                    leftPointers.add(pointer);
                    break;
                case RIGHT:
                    rightPointers.add(pointer);
                    break;
                case UP:
                    upPointers.add(pointer);
                    break;
                case DOWN:
                    downPointers.add(pointer);
                    break;
            }
            return true;
        }

        private NoteColumn getNearControl(float x, float y) {
            NoteColumn noteColumn = NoteColumn.LEFT;
            float distance = getDistance(x, y, leftButton.getCenterX(), leftButton.getCenterY());
            float d;

            d = getDistance(x, y, downButton.getCenterX(), downButton.getCenterY());
            if(d < distance) {
                distance = d;
                noteColumn = NoteColumn.DOWN;
            }

            d = getDistance(x, y, upButton.getCenterX(), upButton.getCenterY());
            if(d < distance) {
                distance = d;
                noteColumn = NoteColumn.UP;
            }

            d = getDistance(x, y, rightButton.getCenterX(), rightButton.getCenterY());
            if(d < distance) {
                noteColumn = NoteColumn.RIGHT;
            }
            return noteColumn;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            removePointer(pointer);
        }

        private void removePointer(int pointer) {
            ControlState controls = screen.getMatch().getControls();
            if(leftPointers.removeValue(pointer) && leftPointers.size == 0) {
                controls.setPressed(NoteColumn.LEFT, false);
            }
            else if(rightPointers.removeValue(pointer) && rightPointers.size == 0) {
                controls.setPressed(NoteColumn.RIGHT, false);
            }
            else if(downPointers.removeValue(pointer) && downPointers.size == 0) {
                controls.setPressed(NoteColumn.DOWN, false);
            }
            else if(upPointers.removeValue(pointer) && upPointers.size == 0) {
                controls.setPressed(NoteColumn.UP, false);
            }
        }
    }
}
