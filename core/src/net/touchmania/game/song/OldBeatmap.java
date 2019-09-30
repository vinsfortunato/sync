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

package net.touchmania.game.song;

import com.badlogic.gdx.utils.ObjectSet;
import net.touchmania.game.song.note.NoteType;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author flood2d
 */
@Deprecated
public class OldBeatmap {
    public TreeMap<Float, NoteType> leftNotesMap = new TreeMap<>();
    public TreeMap<Float, NoteType> downNotesMap = new TreeMap<>();
    public TreeMap<Float, NoteType> upNotesMap = new TreeMap<>();
    public TreeMap<Float, NoteType> rightNotesMap = new TreeMap<>();

    public TreeMap<Float, NoteType> getNotesMap(NoteColumn noteColumn) {
        switch (noteColumn) {
            case LEFT:
                return leftNotesMap;
            case DOWN:
                return downNotesMap;
            case UP:
                return upNotesMap;
            default:
                return rightNotesMap;
        }
    }

    /**
     * Gets the left note type at the given beat.
     * @param beat the beat.
     * @return the note type or null if there is no note at
     * the given beat.
     */
    public NoteType getLeftNote(float beat) {
        return leftNotesMap.get(beat);
    }

    /**
     * Gets the down note type at the given beat.
     * @param beat the beat.
     * @return the note type or null if there is no note at
     * the given beat.
     */
    public NoteType getDownNote(float beat) {
        return downNotesMap.get(beat);
    }

    /**
     * Gets the up note type at the given beat.
     * @param beat the beat.
     * @return the note type or null if there is no note at
     * the given beat.
     */
    public NoteType getUpNote(float beat) {
        return upNotesMap.get(beat);
    }

    /**
     * Gets the right note type at the given beat.
     * @param beat the beat.
     * @return the note type or null if there is no note at
     * the given beat.
     */
    public NoteType getRightNote(float beat) {
        return rightNotesMap.get(beat);
    }

    public Float getLowerBeat(NoteColumn noteColumn, float beat) {
        return getNotesMap(noteColumn).lowerKey(beat);
    }

    public Float getHigherBeat(NoteColumn noteColumn, float beat) {
        return getNotesMap(noteColumn).higherKey(beat);
    }

    public NoteType getLowerNoteType(NoteColumn noteColumn, float beat) {
        Map.Entry<Float, NoteType> entry = getNotesMap(noteColumn).lowerEntry(beat);
        if(entry != null) {
            return entry.getValue();
        }
        return null;
    }

    public NoteType getHigherNoteType(NoteColumn noteColumn, float beat) {
        Map.Entry<Float, NoteType> entry = getNotesMap(noteColumn).higherEntry(beat);
        if(entry != null) {
            return entry.getValue();
        }
        return null;
    }

    public boolean isJump(float beat) {
        return getChordSize(beat) == 2;
    }

    public boolean isHand(float beat) {
        return getChordSize(beat) > 2;
    }

    public int getMinesCount(float beat) {
        return getNotesCount(beat, NoteType.MINE);
    }

    public int getRollsCount(float beat) {
        return getNotesCount(beat, NoteType.ROLL);
    }

    public int getHoldsCount(float beat) {
        return getNotesCount(beat, NoteType.HOLD);
    }

    public int getFakesCount(float beat) {
        return getNotesCount(beat, NoteType.FAKE);
    }

    public int getLiftsCount(float beat) {
        return getNotesCount(beat, NoteType.LIFT);
    }

    public int getNotesCount(float beat, NoteType note) {
        int count = 0;
        if(getLeftNote(beat) == note) count++;
        if(getDownNote(beat) == note) count++;
        if(getUpNote(beat) == note) count++;
        if(getRightNote(beat) == note) count++;
        return count;
    }

    public int getChordSize(float beat) {
        int count = 0;
        if(isChordCohesive(getLeftNote(beat))) count++;
        if(isChordCohesive(getDownNote(beat))) count++;
        if(isChordCohesive(getUpNote(beat))) count++;
        if(isChordCohesive(getRightNote(beat))) count++;
        return count;
    }

    private boolean isChordCohesive(NoteType note) {
        return note == NoteType.TAP
                || note == NoteType.HOLD
                || note == NoteType.ROLL;
    }

    public ObjectSet<Float> getBeats() {
        ObjectSet<Float> beats = new ObjectSet<>();
        for(Float beat : leftNotesMap.keySet()) beats.add(beat);
        for(Float beat : downNotesMap.keySet()) beats.add(beat);
        for(Float beat : upNotesMap.keySet()) beats.add(beat);
        for(Float beat : rightNotesMap.keySet()) beats.add(beat);
        return beats;
    }
}
