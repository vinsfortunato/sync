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

import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.NotePanel;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author flood2d
 */
public class Beatmap {
    private Map<Integer, TreeMap<Double, Note>> panels;

    /**
     * The taps count of the beatmap, or -1 if data
     * is unavailable.
     */
    public int tapsCount = -1;
    /**
     * The jumps count of the beatmap, or -1 if data
     * is unavailable.
     */
    public int jumpsCount = -1;
    /**
     * The holds count of the beatmap, or -1 if data
     * is unavailable.
     */
    public int holdsCount = -1;
    /**
     * The mines count of the beatmap, or -1 if data
     * is unavailable.
     */
    public int minesCount = -1;
    /**
     * The hands count of the beatmap, or -1 if data
     * is unavailable.
     */
    public int handsCount = -1;
    /**
     * The rolls count of the beatmap, or -1 if data
     * is unavailable.
     */
    public int rollsCount = -1;
    /**
     * The fakes count of the beatmap, or -1 if data
     * is unavailable.
     */
    public int fakesCount = -1;
    /**
     * The lifts count of the beatmap, or -1 if data
     * is unavailable.
     */
    public int liftsCount = -1;

    /**
     * Gets a navigable map containing the notes for a specific notePanel.
     * Map key is the beat of the note associated to that key.
     *
     * @param notePanel the note notePanel.
     * @return a map of notes, or null if there's no map associated to the given notePanel.
     */
    public TreeMap<Double, Note> getNotes(NotePanel notePanel) {
        if(panels != null) {
            return panels.get(notePanel.ordinal());
        }
        return null;
    }

    /**
     * Sets a navigable map containing the notes for a specific notePanel.
     * Map key is the beat of the note associated to that key.
     * @param notePanel the note notePanel.
     * @param notesMap a map of notes
     */
    public void setNotesMap(NotePanel notePanel, TreeMap<Double, Note> notesMap) {
        if(panels == null) {
            panels = new HashMap<>();
        }
        panels.put(notePanel.ordinal(), notesMap);
    }

    /**
     * Update the following beatmap statistics:
     * <ul>
     *     <li>Taps count - {@link #tapsCount}</li>
     *     <li>Jumps count - {@link #jumpsCount}</li>
     *     <li>Holds count - {@link #holdsCount}</li>
     *     <li>Mines count - {@link #minesCount}</li>
     *     <li>Hands count - {@link #handsCount}</li>
     *     <li>Rolls count - {@link #rollsCount}</li>
     *     <li>Fakes count - {@link #fakesCount}</li>
     *     <li>Lifts count - {@link #liftsCount}</li>
     * </ul>
     */
    public void updateStats() {
        /** TODO
        ObjectSet<Float> beats = beatmap.getBeats();
        for(Float beat : beats) {
            tapsCount += beatmap.getChordSize(beat);
            jumpsCount += beatmap.isJump(beat) ? 1 : 0;
            handsCount += beatmap.isHand(beat) ? 1 : 0;
            holdsCount += beatmap.getHoldsCount(beat);
            rollsCount += beatmap.getRollsCount(beat);
            minesCount += beatmap.getMinesCount(beat);
            fakesCount += beatmap.getFakesCount(beat);
            liftsCount += beatmap.getLiftsCount(beat);
        }
         **/
    }
}
