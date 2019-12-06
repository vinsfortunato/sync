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

package net.touchmania.game.round.judge;

import com.badlogic.gdx.utils.IntMap;
import net.touchmania.game.Game;
import net.touchmania.game.GameMode;
import net.touchmania.game.round.PanelState;
import net.touchmania.game.round.Round;
import net.touchmania.game.song.Beatmap;
import net.touchmania.game.song.Timing;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.NotePanel;
import net.touchmania.game.song.note.TapNote;

public class Judge implements PanelState.PanelStateListener {
    private Round round;
    private JudgmentKeeper judgments;
    private JudgeCriteria criteria;
    private IntMap<Double> evaluatedBeats;
    private int[] panels;

    private TapNoteJudge tapNoteJudge;

    public Judge(Round round, JudgeCriteria criteria) {
        this.round = round;
        this.criteria = criteria;
        this.panels = NotePanel.getModePanels(Game.instance().getSettings().getGameMode());
        this.judgments = new JudgmentKeeper(panels);

        //Init evaluated beats to 0.0
        this.evaluatedBeats = new IntMap<>();
        GameMode mode = Game.instance().getSettings().getGameMode();
        for(int panel : NotePanel.getModePanels(mode)) {
            this.evaluatedBeats.put(panel, 0.0D);
        }

        //Init note judges
        tapNoteJudge = new TapNoteJudge();
    }

    /**
     * Updates all note before the given time.
     * @param time the time in seconds relative to the start of the music track.
     */
    public void update(double time) {
        Timing timing = getRound().getTiming();
        Beatmap beatmap = getRound().getChart().beatmap;
        JudgmentKeeper judgments = getJudgmentKeeper();
        double beat = timing.getBeatAt(time);

        for (int panel : panels) {
            //Check all notes from last evaluated beat to current beat
            double b = getEvaluatedBeat(panel);

            Note note = beatmap.higherNote(panel, b);
            while (note != null && b < beat) {
                b = note.getBeat();
                if (!judgments.hasJudgment(panel, b)) {
                    updateNote(panel, note, time, beat);
                }
                note = beatmap.higherNote(panel, b);
            }
        }
    }

    @Override
    public void onPanelStateChange(int panel, double time, boolean pressed) {
        //Update all notes before event time
        update(time);

        Timing timing = getRound().getTiming();
        Beatmap beatmap = getRound().getChart().beatmap;
        JudgmentKeeper judgments = getJudgmentKeeper();
        double eventBeat = timing.getBeatAt(time);
        double evalBeat = getEvaluatedBeat(panel);

        Note note;
        if(eventBeat < evalBeat) {
            //Judge higher note
            note = beatmap.higherNote(panel, eventBeat, Note::canBeJudged);
        } else {
            Note floorNote = beatmap.floorNote(panel, eventBeat, Note::canBeJudged);
            if(floorNote != null && !judgments.hasJudgment(panel, floorNote.getBeat())) {
                //Judge floor note
                note = floorNote;
            } else {
                //Judge higher note
                note = beatmap.higherNote(panel, eventBeat, Note::canBeJudged);
            }
        }

        if(note != null) {
            onPanelStateChange(panel, time, pressed, note);
        }
    }

    private void updateNote(int panel, Note note, double currentTime, double currentBeat) {
        //Dispatch to the appropriate judge
        if(note instanceof TapNote) {
            tapNoteJudge.update(panel, (TapNote) note, currentTime, currentBeat);
        }
    }

    private void onPanelStateChange(int panel, double time, boolean pressed, Note note) {
        //Dispatch to the appropriate judge
        if(note instanceof TapNote) {
            tapNoteJudge.onPanelStateChange(panel, time, pressed, (TapNote) note);
        }
    }

    private void emitJudgment(int panel, Note note, Judgment judgment) {
        judgments.putJudgment(panel, note, judgment);
    }

    private void setEvaluatedBeat(int panel, double beat) {
        evaluatedBeats.put(panel, beat);
    }

    private double getEvaluatedBeat(int panel) {
        return evaluatedBeats.get(panel);
    }

    /**
     * Get the round.
     * @return the round.
     */
    public Round getRound() {
        return round;
    }

    /**
     * Get the judgment keeper that holds all generated judgments.
     * @return the judgment keeper.
     */
    public JudgmentKeeper getJudgmentKeeper() {
        return judgments;
    }

    /**
     * Get the judge criteria used to evaluate notes and generate judgements.
     * @return the judge criteria.
     */
    public JudgeCriteria getCriteria() {
        return criteria;
    }

    private interface NoteJudge<N extends Note> {
        /**
         * Update unjudged note status.
         * @param panel the panel.
         * @param note the unjudged note.
         * @param time the time in seconds relative to the start of the music track.
         * @param beat the beat.
         */
        void update(int panel, N note, double time, double beat);

        /**
         * Called when a panel changes state and update unjudged note status.
         * @param panel the panel.
         * @param time the time in seconds relative to the start of the music track.
         * @param note the unjudged note.
         */
        void onPanelStateChange(int panel, double time, boolean pressed, N note);
    }

    private class TapNoteJudge implements NoteJudge<TapNote> {
        @Override
        public void update(int panel, TapNote note, double time, double beat) {
            //TODO check if is chord
            Beatmap beatmap = getRound().getChart().beatmap;
            Timing timing = getRound().getTiming();
            double noteTime = timing.getTimeAt(note.getBeat());
            double timingError = noteTime - time;
            double worstWindow = criteria.getWorstTapWindow();
            Note nextNote = beatmap.higherNote(panel, note.getBeat(), Note::canBeJudged);
            Judgment judgment = null;

            if(nextNote != null && nextNote.getBeat() < beat) {
                //Next note has passed receptor
                double nextNoteTime = timing.getTimeAt(nextNote.getBeat());
                judgment = new TapJudgement(nextNoteTime, Math.max(noteTime - nextNoteTime, -worstWindow), JudgmentClass.MISS);
            } else if(timingError < -worstWindow) {
                //Note outside worst window
                judgment = new TapJudgement(noteTime + worstWindow, -worstWindow, JudgmentClass.MISS);
            }

            if(judgment != null) {
                emitJudgment(panel, note, judgment);
                //Update evaluated beat
                setEvaluatedBeat(panel, note.getBeat());
            }
        }

        @Override
        public void onPanelStateChange(int panel, double time, boolean pressed, TapNote note) {
            if(pressed) {
                Timing timing = getRound().getTiming();
                double noteTime = timing.getTimeAt(note.getBeat());
                double timingError = noteTime - time;
                JudgmentClass judgmentClass = getJudgmentClass(timingError);
                if(judgmentClass != JudgmentClass.MISS) {
                    //Note judged. Emit judgment
                    Judgment judgment = new TapJudgement(time, timingError, judgmentClass);
                    emitJudgment(panel, note, judgment);
                    //Update evaluated beat
                    setEvaluatedBeat(panel, note.getBeat());
                }
            }
        }

        private JudgmentClass getJudgmentClass(double timingError) {
            if(Double.compare(Math.abs(timingError), criteria.getWorstTapWindow()) <= 0) {
                if(Double.compare(Math.abs(timingError), criteria.getMarvelousWindow()) <= 0)
                    return JudgmentClass.MARVELOUS;
                if(Double.compare(Math.abs(timingError), criteria.getPerfectWindow()) <= 0)
                    return JudgmentClass.PERFECT;
                if(Double.compare(Math.abs(timingError), criteria.getGreatWindow()) <= 0)
                    return JudgmentClass.GREAT;
                if(Double.compare(Math.abs(timingError), criteria.getGoodWindow()) <= 0)
                    return JudgmentClass.GOOD;
                if(Double.compare(Math.abs(timingError), criteria.getBooWindow()) <= 0)
                    return JudgmentClass.BOO;
            }
            return JudgmentClass.MISS;
        }
    }
}
