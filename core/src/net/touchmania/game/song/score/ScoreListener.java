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

package net.touchmania.game.song.score;

import net.touchmania.game.song.NoteColumn;
import net.touchmania.game.song.note.NoteType;

/**
 * @author flood2d
 */
public interface ScoreListener {

    void onJudgment(NoteColumn noteColumn, NoteType noteType, float beat, Judgment judgment);

    /**
     * Called when an hold head is scored without missing it.
     * @param noteColumn the hold note column.
     * @param headBeat the hold head beat.
     */
    void onHoldAttached(NoteColumn noteColumn, float headBeat);

    /**
     * Called when an hold is lost and can't be recovered.
     * @param noteColumn the hold note column.
     * @param headBeat the hold head beat.
     */
    void onHoldDetached(NoteColumn noteColumn, float headBeat);
}
