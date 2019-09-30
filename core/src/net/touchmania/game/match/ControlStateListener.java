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

import net.touchmania.game.song.NoteColumn;

/**
 * @author flood2d
 */
public interface ControlStateListener {
    /**
     * Called when a game control is pressed.
     * @param noteColumn the note column associated to the control.
     * @param inputTime the input time, relative to the start of the song.
     */
    void onControlPressed(NoteColumn noteColumn, float inputTime);

    /**
     * Called when a game control is released.
     * @param noteColumn the note column associated to the control.
     * @param inputTime the input time, relative to the start of the song.
     */
    void onControlReleased(NoteColumn noteColumn, float inputTime);
}
