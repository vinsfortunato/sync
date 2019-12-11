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

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import net.touchmania.game.song.note.ChordNote;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.util.Criteria;

import java.util.Map;
import java.util.TreeMap;

//TODO implement comparator with tolerance
//TODO Define tolerance in game settings?
public class Beatmap {
    /*
     * The map key is the note panel and the value is a TreeMap containing notes.
     * TreeMap key is the note beat and value is the note.
     */
    private IntMap<TreeMap<Double, Note>> panels = new IntMap<>();

    /**
     * Gets the note at the given beat on the given panel.
     * @param panel the note panel
     * @param beat the beat
     * @return the note at the given beat, or null if there is no note at the given beat.
     */
    public Note getNote(int panel, double beat) {
        TreeMap<Double, Note> notes = panels.get(panel);
        return notes != null ? notes.get(beat) : null;
    }

    /**
     * Gets all notes at the given beat that are valid for the given criteria.
     * @param beat the beat
     * @param criteria a criteria that checks if the note is valid, or null to ignore criteria
     * @return all notes at the given beat that are valid for the given criteria.
     */
    public Array<Note> getNotes(double beat, Criteria<Note> criteria) {
        Array<Note> notes = new Array<>();
        IntMap.Keys keys = panels.keys();
        while(keys.hasNext) {
            int panel = keys.next();
            Note note = getNote(panel, beat);
            if(note != null && (criteria == null || criteria.isValid(note))) {
                notes.add(note);
            }
        }
        return notes;
    }

    /**
     * Gets all notes at the given beat.
     * @param beat the beat
     * @return all notes at the given beat.
     */
    public Array<Note> getNotes(double beat) {
        return getNotes(beat, null);
    }

    /**
     * Returns the note associated with the greatest beat less than or equal
     * to the given beat, or null if there is no such note.
     * @param panel the note panel
     * @param beat the beat
     * @param criteria a criteria that checks if the note is valid, or null to ignore criteria.
     * @return a note with the greatest beat less than or equal to the given beat, or null if there is no such note
     */
    public Note floorNote(int panel, double beat, Criteria<Note> criteria) {
        Note note;
        TreeMap<Double, Note> notes = panels.get(panel);
        if(notes != null) {
            Map.Entry<Double, Note> entry = notes.floorEntry(beat);
            while(entry != null) {
                note = entry.getValue();
                if(criteria == null || criteria.isValid(note)) {
                    return note;
                }
                entry = notes.lowerEntry(entry.getKey());
            }
        }
        return null;
    }

    /**
     * Returns the note associated with the greatest beat less than or equal
     * to the given beat, or null if there is no such note.
     * @param panel the note panel
     * @param beat the beat
     * @return a note with the greatest beat less than or equal to the given beat, or null if there is no such note
     */
    public Note floorNote(int panel, double beat) {
        return floorNote(panel, beat, null);
    }

    /**
     * Returns the note associated with the least beat greater than or equal
     * to the given beat, or null if there is no such note.
     * @param panel the note panel
     * @param beat the beat
     * @param criteria a criteria that checks if the note is valid, or null to ignore criteria.
     * @return a note with the least beat greater than or equal to beat, or null if there is no such note.
     */
    public Note ceilingNote(int panel, double beat, Criteria<Note> criteria) {
        Note note;
        TreeMap<Double, Note> notes = panels.get(panel);
        if(notes != null) {
            Map.Entry<Double, Note> entry = notes.ceilingEntry(beat);
            while(entry != null) {
                note = entry.getValue();
                if(criteria == null || criteria.isValid(note)) {
                    return note;
                }
                entry = notes.higherEntry(entry.getKey());
            }
        }
        return null;
    }

    /**
     * Returns the note associated with the least beat greater than or equal
     * to the given beat, or null if there is no such note.
     * @param panel the note panel
     * @param beat the beat
     * @return a note with the least beat greater than or equal to beat, or null if there is no such note.
     */
    public Note ceilingNote(int panel, double beat) {
        return ceilingNote(panel, beat, null);
    }

    /**
     * Returns the note associated with the least beat strictly greater
     * than the given beat, or null if there is no such note.
     * @param panel the note panel
     * @param beat the beat
     * @param criteria a criteria that checks if the note is valid, or null to ignore criteria.
     * @return a note with the least beat greater than beat, or null if there is no such note
     */
    public Note higherNote(int panel, double beat, Criteria<Note> criteria) {
        Note note;
        TreeMap<Double, Note> notes = panels.get(panel);
        if(notes != null) {
            Map.Entry<Double, Note> entry = notes.higherEntry(beat);
            while(entry != null) {
                note = entry.getValue();
                if(criteria == null || criteria.isValid(note)) {
                    return note;
                }
                entry = notes.higherEntry(entry.getKey());
            }
        }
        return null;
    }

    /**
     * Returns the note associated with the least beat strictly greater
     * than the given beat, or null if there is no such note.
     * @param panel the note panel
     * @param beat the beat
     * @return a note with the least beat greater than beat, or null if there is no such note
     */
    public Note higherNote(int panel, double beat) {
        return higherNote(panel, beat, null);
    }

    /**
     * Returns the note on the given panel associated with the greatest key strictly
     * less than the given beat, or null if there is no such note.
     * @param panel the note panel
     * @param beat the beat
     * @param criteria a criteria that checks if the note is valid, or null to ignore criteria.
     * @return a note with the greatest beat less than given beat, or null if there is no such note
     */
    public Note lowerNote(int panel, double beat, Criteria<Note> criteria) {
        Note note = null;
        TreeMap<Double, Note> notes = panels.get(panel);
        if(notes != null) {
            Map.Entry<Double, Note> entry = notes.lowerEntry(beat);
            while(entry != null) {
                note = entry.getValue();
                if(criteria == null || criteria.isValid(note)) {
                    return note;
                }
                entry = notes.lowerEntry(entry.getKey());
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
    public Note lowerNote(int panel, double beat) {
        return lowerNote(panel, beat, null);
    }

    /**
     * Returns the note associated with the least beat in this map, or null if the map is empty.
     * @param panel the note panel.
     * @return a note with the least beat, or null if this map is empty
     */
    public Note firstNote(int panel) {
        TreeMap<Double, Note> notes = panels.get(panel);
        return notes != null && !notes.isEmpty() ? notes.firstEntry().getValue() : null;
    }

    /**
     * Returns the note associated with the greatest beat in this map, or null if the map is empty
     * @param panel the note panel.
     * @return a note with the greatest beat, or null if this map is empty
     */
    public Note lastNote(int panel) {
        TreeMap<Double, Note> notes = panels.get(panel);
        return notes != null && !notes.isEmpty() ? notes.lastEntry().getValue() : null;
    }

    /**
     * Returns the count of notes for the given panel.
     * @param panel the note panel
     * @return the notes count
     */
    public int countNotes(int panel) {
        Map notes = panels.get(panel);
        return notes != null ? notes.size() : 0;
    }

    /**
     * Checks if there are notes on the given panel.
     * @param panel the note panel
     * @return true if there are notes on the given panel, false otherwise
     */
    public boolean hasNotes(int panel) {
        return countNotes(panel) > 0;
    }

    public boolean isChord(double beat) {
        return getChordSize(beat) > 1;
    }

    public int getChordSize(double beat) {
        int count = 0;
        IntMap.Keys keys = panels.keys();
        while(keys.hasNext) {
            int panel = keys.next();
            Note note = getNote(panel, beat);
            if(note instanceof ChordNote) {
                count++;
            }
        }
        return count;
    }

    /**
     * Put note inside the beatmap.
     * @param panel the note panel
     * @param note the note
     */
    public void putNote(int panel, Note note) {
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
