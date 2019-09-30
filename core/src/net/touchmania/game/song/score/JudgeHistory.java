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

import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.song.NoteColumn;
import net.touchmania.game.song.OldBeatmap;

/**
 * @author flood2d
 */
@Deprecated
public class JudgeHistory {
    private ObjectMap<Float, Judgment> leftJudgments;
    private ObjectMap<Float, Judgment> downJudgments;
    private ObjectMap<Float, Judgment> upJudgments;
    private ObjectMap<Float, Judgment> rightJudgments;

    /**
     * Initializes history maps to appropriate size to avoid rehash.
     * @param beatmap the beatmap.
     */
    public void init(OldBeatmap beatmap) {
        leftJudgments = new ObjectMap<>(
                beatmap.leftNotesMap.size() + (int) (beatmap.leftNotesMap.size() * 0.25f));
        downJudgments = new ObjectMap<>(
                beatmap.downNotesMap.size() + (int) (beatmap.downNotesMap.size() * 0.25f));
        upJudgments = new ObjectMap<>(
                beatmap.upNotesMap.size() + (int) (beatmap.upNotesMap.size() * 0.25f));
        rightJudgments = new ObjectMap<>(
                beatmap.rightNotesMap.size() + (int) (beatmap.rightNotesMap.size() * 0.25f));
    }

    public void putLeftJudgment(float beat, Judgment judgment) {
        putJudgment(beat, NoteColumn.LEFT, judgment);
    }

    public void putDownJudgment(float beat, Judgment judgment) {
        putJudgment(beat, NoteColumn.DOWN, judgment);
    }

    public void putUpJudgment(float beat, Judgment judgment) {
        putJudgment(beat, NoteColumn.UP, judgment);
    }

    public void putRightJudgment(float beat, Judgment judgment) {
        putJudgment(beat, NoteColumn.RIGHT, judgment);
    }

    public void putJudgment(float beat, NoteColumn noteColumn, Judgment judgment) {
        switch(noteColumn) {
            case LEFT:
                leftJudgments.put(beat, judgment);
                break;
            case DOWN:
                downJudgments.put(beat, judgment);
                break;
            case UP:
                upJudgments.put(beat, judgment);
                break;
            case RIGHT:
                rightJudgments.put(beat, judgment);
                break;
        }
    }

    public Judgment getLeftJudgment(float beat) {
        return getJudgment(beat, NoteColumn.LEFT);
    }

    public Judgment getDownJudgment(float beat) {
        return getJudgment(beat, NoteColumn.DOWN);
    }

    public Judgment getUpJudgment(float beat) {
        return getJudgment(beat, NoteColumn.DOWN);
    }

    public Judgment getRightJudgment(float beat) {
        return getJudgment(beat, NoteColumn.DOWN);
    }

    public Judgment getJudgment(float beat, NoteColumn noteColumn) {
        switch(noteColumn) {
            case LEFT:
                return leftJudgments.get(beat);
            case DOWN:
                return downJudgments.get(beat);
            case UP:
                return upJudgments.get(beat);
            case RIGHT:
                return rightJudgments.get(beat);
        }
        return null;
    }

    public boolean hasJudgment(float beat, NoteColumn noteColumn) {
        return getJudgment(beat, noteColumn) != null;
    }
}
