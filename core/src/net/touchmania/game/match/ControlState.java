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

package net.touchmania.game.match;

import com.badlogic.gdx.utils.ObjectSet;
import net.touchmania.game.song.NoteColumn;

/**
 * @author flood2d
 */
public class ControlState {
    private Match match;
    private boolean leftPressed = false;
    private boolean downPressed = false;
    private boolean upPressed = false;
    private boolean rightPressed = false;
    private float leftLastInputTime = -1.0f;
    private float downLastInputTime = -1.0f;
    private float upLastInputTime = -1.0f;
    private float rightLastInputTime = -1.0f;
    private ObjectSet<ControlStateListener> listeners = new ObjectSet<>();

    public ControlState(Match match) {
        this.match = match;
    }

    /**
     * Adds a control state listener.
     * @param listener the listener to add.
     * @return true if the listener has been added, false if the listener
     *         to add is null or has been already added.
     */
    public boolean addListener(ControlStateListener listener) {
        if(listener != null) {
            return listeners.add(listener);
        }
        return false;
    }

    /**
     * Removes a control state listener.
     * @param listener the listener to remove.
     * @return true if the listener has been removed, false if the
     *         listener to remove is null or isn't contained.
     */
    public boolean removeListener(ControlStateListener listener) {
        if(listener != null) {
            return listeners.remove(listener);
        }
        return false;
    }

    public ObjectSet<ControlStateListener> getListeners() {
        return listeners;
    }

    /**
     * Sets the control state then notify listeners.
     * @param noteColumn the note column associated to the control.
     * @param pressed the state to set, true for pressed, false for released.
     */
    public void setPressed(NoteColumn noteColumn, boolean pressed) {
        boolean hasChanged = false;
        float inputTime = match.getCurrentTime();

        switch(noteColumn) {
            case LEFT:
                if(leftPressed != pressed) {
                    leftPressed = pressed;
                    leftLastInputTime = inputTime;
                    hasChanged = true;
                }
                break;
            case DOWN:
                if(downPressed != pressed) {
                    downPressed = pressed;
                    downLastInputTime = inputTime;
                    hasChanged = true;
                }
                break;
            case UP:
                if(upPressed != pressed) {
                    upPressed = pressed;
                    upLastInputTime = inputTime;
                    hasChanged = true;
                }
                break;
            case RIGHT:
                if(rightPressed != pressed) {
                    rightPressed = pressed;
                    rightLastInputTime = inputTime;
                    hasChanged = true;
                }
                break;
        }

        if(hasChanged) {
            for(ControlStateListener listener : listeners) {
                if(pressed) {
                    listener.onControlPressed(noteColumn, inputTime);
                } else {
                    listener.onControlReleased(noteColumn, inputTime);
                }
            }
        }
    }

    public boolean isLeftPressed() {
        return isPressed(NoteColumn.LEFT);
    }

    public boolean isDownPressed() {
        return isPressed(NoteColumn.DOWN);
    }

    public boolean isUpPressed() {
        return isPressed(NoteColumn.UP);
    }

    public boolean isRightPressed() {
        return isPressed(NoteColumn.RIGHT);
    }

    /**
     * Checks if the control associated to the given note column is
     * currently pressed.
     * @param noteColumn the note column associated to the control.
     * @return true if it's pressed, false otherwise.
     */
    public boolean isPressed(NoteColumn noteColumn) {
        switch(noteColumn) {
            case LEFT:
                return leftPressed;
            case DOWN:
                return downPressed;
            case UP:
                return upPressed;
            case RIGHT:
                return rightPressed;
        }
        return false;
    }

    public boolean isLeftReleased() {
        return !isLeftPressed();
    }

    public boolean isDownReleased() {
        return !isDownPressed();
    }

    public boolean isUpReleased() {
        return !isUpPressed();
    }

    public boolean isRightReleased() {
        return !isRightPressed();
    }

    /**
     * Checks if the control associated to the given note column is
     * currently released.
     * @param noteColumn the note column associated to the control.
     * @return true if it's released, false otherwise.
     */
    public boolean isReleased(NoteColumn noteColumn) {
        return !isPressed(noteColumn);
    }

    public float getLeftLastInputTime() {
        return getLastInputTime(NoteColumn.LEFT);
    }

    public float getDownLastInputTime() {
        return getLastInputTime(NoteColumn.DOWN);
    }

    public float getUpLastInputTime() {
        return getLastInputTime(NoteColumn.UP);
    }

    public float getRightLastInputTime() {
        return getLastInputTime(NoteColumn.RIGHT);
    }

    public float getLastInputTime(NoteColumn noteColumn) {
        switch(noteColumn) {
            case LEFT:
                return leftLastInputTime;
            case DOWN:
                return downLastInputTime;
            case UP:
                return upLastInputTime;
            case RIGHT:
                return rightLastInputTime;
        }
        return -1.0f;
    }
}
