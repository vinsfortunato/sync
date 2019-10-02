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
import net.touchmania.game.song.note.NoteResolution;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class Beatmap {
    /*
     * The map key is the note panel and the value is a TreeMap containing notes.
     * TreeMap key is the note beat and value is the note.
     */
    private Map<NotePanel, TreeMap<Double, Note>> panels = new HashMap<>();

    /**
     * Returns the note associated with the greatest beat less than or equal
     * to the given beat, or null if there is no such note.
     * @param panel the note panel
     * @param beat the beat
     * @return a note with the greatest beat less than or equal to the given beat, or null if there is no such note
     */
    public Note floorNote(NotePanel panel, double beat) {
        Note note = null;
        TreeMap<Double, Note> notes = panels.get(panel);
        if(notes != null) {
            Map.Entry<Double, Note> entry = notes.floorEntry(beat);
            if(entry != null) {
                note = entry.getValue();
            }
        }
        return note;
    }

    /**
     * Returns the note associated with the least beat greater than or equal
     * to the given beat, or null if there is no such note.
     * @param panel the note panel
     * @param beat the beat
     * @return a note with the least beat greater than or equal to beat, or null if there is no such note.
     */
    public Note ceilingNote(NotePanel panel, double beat) {
        Note note = null;
        TreeMap<Double, Note> notes = panels.get(panel);
        if(notes != null) {
            Map.Entry<Double, Note> entry = notes.ceilingEntry(beat);
            if(entry != null) {
                note = entry.getValue();
            }
        }
        return note;
    }

    /**
     * Returns the note associated with the least beat strictly greater
     * than the given beat, or null if there is no such note.
     * @param panel the note panel
     * @param beat the beat
     * @return a note with the least beat greater than beat, or null if there is no such note
     */
    public Note higherNote(NotePanel panel, double beat) {
        Note note = null;
        TreeMap<Double, Note> notes = panels.get(panel);
        if(notes != null) {
            Map.Entry<Double, Note> entry = notes.higherEntry(beat);
            if(entry != null) {
                note = entry.getValue();
            }
        }
        return note;
    }

    /**
     * Returns the note on the given panel associated with the greatest key strictly
     * less than the given beat, or null if there is no such note.
     * @param panel the note panel
     * @param beat the beat
     * @return a note with the greatest beat less than given beat, or null if there is no such note
     */
    public Note lowerNote(NotePanel panel, double beat) {
        Note note = null;
        TreeMap<Double, Note> notes = panels.get(panel);
        if(notes != null) {
            Map.Entry<Double, Note> entry = notes.lowerEntry(beat);
            if(entry != null) {
                note = entry.getValue();
            }
        }
        return note;
    }

    /**
     * Returns the note associated with the least beat in this map, or null if the map is empty.
     * @param panel the note panel.
     * @return a note with the least beat, or null if this map is empty
     */
    public Note firstNote(NotePanel panel) {
        TreeMap<Double, Note> notes = panels.get(panel);
        return notes != null && !notes.isEmpty() ? notes.firstEntry().getValue() : null;
    }

    /**
     * Returns the note associated with the greatest beat in this map, or null if the map is empty
     * @param panel the note panel.
     * @return a note with the greatest beat, or null if this map is empty
     */
    public Note lastNote(NotePanel panel) {
        TreeMap<Double, Note> notes = panels.get(panel);
        return notes != null && !notes.isEmpty() ? notes.lastEntry().getValue() : null;
    }

    /**
     * Returns the count of notes for the given panel.
     * @param panel the note panel
     * @return the notes count
     */
    public int countNotes(NotePanel panel) {
        Map notes = panels.get(panel);
        return notes != null ? notes.size() : 0;
    }

    /**
     * Checks if there are notes on the given panel.
     * @param panel the note panel
     * @return true if there are notes on the given panel, false otherwise
     */
    public boolean hasNotes(NotePanel panel) {
        return countNotes(panel) > 0;
    }

    /**
     * Put note inside the beatmap.
     * @param panel the note panel
     * @param note the note
     */
    public void putNote(NotePanel panel, Note note) {
        //Get notes and init map if necessary
        TreeMap<Double, Note> notes = panels.get(panel);
        if(notes == null) {
            notes = new TreeMap<>();
            panels.put(panel, notes);
        }

        //Associate the note to its beat
        notes.put(note.getBeat(), note);
    }
}
