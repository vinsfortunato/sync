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
import net.touchmania.game.song.note.*;

import static net.touchmania.game.round.judge.JudgmentClass.*;

public class Judge implements PanelState.PanelStateListener {
    private Round round;
    private JudgmentKeeper judgments;
    private JudgeCriteria criteria;
    private IntMap<Double> evaluatedBeats;
    private double evaluatedTime;
    private int[] panels;

    private TapNoteJudge tapNoteJudge;
    private HoldNoteJudge holdNoteJudge;
    private RollNoteJudge rollNoteJudge;
    private LiftNoteJudge liftNoteJudge;
    private MineNoteJudge mineNoteJudge;

    public Judge(Round round, JudgeCriteria criteria) {
        this.round = round;
        this.criteria = criteria;
        this.panels = NotePanel.getModePanels(Game.instance().getSettings().getGameMode());
        this.judgments = new JudgmentKeeper(panels);

        //Init evaluated beats and time
        this.evaluatedBeats = new IntMap<>();
        this.evaluatedTime = Double.MIN_VALUE;
        GameMode mode = Game.instance().getSettings().getGameMode();
        for(int panel : NotePanel.getModePanels(mode)) {
            this.evaluatedBeats.put(panel, 0.0D);
        }

        //Init note judges
        tapNoteJudge = new TapNoteJudge();
        holdNoteJudge = new HoldNoteJudge();
        rollNoteJudge = new RollNoteJudge();
        liftNoteJudge = new LiftNoteJudge();
        mineNoteJudge = new MineNoteJudge();
    }

    /**
     * Updates all note before the given time.
     * @param time the time in seconds relative to the start of the music track.
     */
    public void update(double time) {
        if(time > evaluatedTime) {
            Timing timing = getTiming();
            Beatmap beatmap = getBeatmap();
            double beat = timing.getBeatAt(time);

            for(int panel : panels) {
                //Update all notes from last evaluated beat to current beat
                Note note = beatmap.higherNote(panel, getEvaluatedBeat(panel));
                while (note != null && note.getBeat() < beat) {
                    NoteJudge judge = getNoteJudge(note);
                    judge.update(panel, time, beat, note);
                    note = beatmap.higherNote(panel, note.getBeat());
                }
            }
            evaluatedTime = time;
        }
    }

    @Override
    public void onPanelStateChange(int panel, double time, boolean pressed) {
        //Update all notes before event time
        update(time);

        Timing timing = getTiming();
        Beatmap beatmap = getBeatmap();
        double eventBeat = timing.getBeatAt(time);
        double evalBeat = getEvaluatedBeat(panel);

        //Find next note to judge
        Note note;
        if(eventBeat < evalBeat) {
            //Judge note after eval beat
            note = beatmap.higherNote(panel, evalBeat, Note::canBeJudged);
        } else {
            Note floorNote = beatmap.floorNote(panel, eventBeat, Note::canBeJudged);
            if(floorNote != null && floorNote.getBeat() > evalBeat) {
                //Judge floor note
                note = floorNote;
            } else {
                //Judge higher note
                note = beatmap.higherNote(panel, eventBeat, Note::canBeJudged);
            }
        }

        if(note != null) {
            NoteJudge judge = getNoteJudge(note);
            judge.onPanelStateChange(panel, time, pressed, note);
        }
    }

    private NoteJudge getNoteJudge(Note note) {
        if(note instanceof TapNote)
            return tapNoteJudge;
        if(note instanceof HoldNote)
            return holdNoteJudge;
        if(note instanceof RollNote)
            return rollNoteJudge;
        if(note instanceof LiftNote)
            return liftNoteJudge;
        if(note instanceof MineNote)
            return mineNoteJudge;
        return null;
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

    private Timing getTiming() {
        return getRound().getTiming();
    }

    private Beatmap getBeatmap() {
        return getRound().getChart().beatmap;
    }

    private PanelState getPanelState() {
        return getRound().getPanelState();
    }

    private abstract class NoteJudge {
        /**
         * Update unjudged note status.
         * @param panel the panel.
         * @param time the time in seconds relative to the start of the music track.
         * @param beat the beat.
         * @param note the unjudged note.
         */
        public abstract void update(int panel, double time, double beat, Note note);

        /**
         * Called when a panel changes state and update unjudged note status.
         * @param panel the panel.
         * @param time the time in seconds relative to the start of the music track.
         * @param note the unjudged note.
         */
        public abstract void onPanelStateChange(int panel, double time, boolean pressed, Note note);

        /**
         * Called when the judge emit a judgment.
         * @param panel the panel.
         * @param note the time in seconds relative to the start of the music track.
         * @param judgment the generated note judgment.
         */
        public void emitJudgment(int panel, Note note, Judgment judgment) {
            //Store judgment
            getJudgmentKeeper().putJudgment(panel, note.getBeat(), judgment);
            //Move evaluated beat cursor
            setEvaluatedBeat(panel, note.getBeat());
        }
    }

    private class TapNoteJudge extends NoteJudge {
        @Override
        public void update(int panel, double time, double beat, Note note) {
            Beatmap beatmap = getBeatmap();
            Timing timing = getTiming();
            double noteTime = timing.getTimeAt(note.getBeat());
            double timingError = noteTime - time;
            double worstWindow = criteria.getWorstTapWindow();
            Note nextNote = beatmap.higherNote(panel, note.getBeat(), Note::canBeJudged);
            Judgment judgment = null;

            if(nextNote != null && nextNote.getBeat() < beat) {
                //Next note has passed receptor
                double nextNoteTime = timing.getTimeAt(nextNote.getBeat());
                judgment = new TapJudgment(nextNoteTime, Math.max(noteTime - nextNoteTime, -worstWindow), MISS);
            } else if(timingError < -worstWindow) {
                //Note outside worst window
                judgment = new TapJudgment(noteTime + worstWindow, -worstWindow, MISS);
            }

            if(judgment != null) {
                emitJudgment(panel, note, judgment);
            }
        }

        @Override
        public void onPanelStateChange(int panel, double time, boolean pressed, Note note) {
            if(pressed) {
                Timing timing = getTiming();
                double noteTime = timing.getTimeAt(note.getBeat());
                double timingError = noteTime - time;
                JudgmentClass judgmentClass = getJudgmentClass(timingError);
                if(judgmentClass != MISS) {
                    //Note judged. Emit judgment
                    Judgment judgment = new TapJudgment(time, timingError, judgmentClass);
                    emitJudgment(panel, note, judgment);
                }
            }
        }

        private JudgmentClass getJudgmentClass(double timingError) {
            if(Double.compare(Math.abs(timingError), criteria.getWorstTapWindow()) <= 0) {
                if(Double.compare(Math.abs(timingError), criteria.getMarvelousWindow()) <= 0)
                    return MARVELOUS;
                if(Double.compare(Math.abs(timingError), criteria.getPerfectWindow()) <= 0)
                    return PERFECT;
                if(Double.compare(Math.abs(timingError), criteria.getGreatWindow()) <= 0)
                    return GREAT;
                if(Double.compare(Math.abs(timingError), criteria.getGoodWindow()) <= 0)
                    return GOOD;
                if(Double.compare(Math.abs(timingError), criteria.getBooWindow()) <= 0)
                    return BOO;
            }
            return MISS;
        }
    }

    private abstract class LengthyNoteJudge extends TapNoteJudge {
        @Override
        public void update(int panel, double time, double beat, Note note) {
            if (!isHeadJudged(panel, note)) {
                //Update head
                super.update(panel, time, beat, note);
            } else {
                //Update trail
                updateTrail(panel, time, beat, (LengthyNote) note);
            }
        }

        @Override
        public void onPanelStateChange(int panel, double time, boolean pressed, Note note) {
            if(!isHeadJudged(panel, note)) {
                //Handle state change on head
                super.onPanelStateChange(panel, time, pressed, note);
            } else {
                //Handle state change on trail
                onPanelStateChangeTrail(panel, time, pressed, (LengthyNote) note);
            }
        }

        public void updateTrail(int panel, double time, double beat, LengthyNote note) {}

        public void onPanelStateChangeTrail(int panel, double time, boolean pressed, LengthyNote note) {}

        @Override
        public void emitJudgment(int panel, Note note, Judgment judgment) {
            JudgmentKeeper judgments = getJudgmentKeeper();

            if(judgment instanceof TapJudgment) {
                //Head judgment
                judgments.putJudgment(panel, note.getBeat(), judgment);

                //Check if the head has been missed
                TapJudgment tapJudgment = (TapJudgment) judgment;
                if(tapJudgment.getJudgmentClass() == MISS) {
                    //Head has been missed, set negative tail judgment
                    emitJudgment(panel, note, new TailJudgment(judgment.getGenTime(), NG));
                }
            } else {
                //Tail judgment
                LengthyNote lengthyNote = (LengthyNote) note;
                judgments.putJudgment(panel, note.getBeat() + lengthyNote.getLength(), judgment);

                //Move evaluated beat cursor
                setEvaluatedBeat(panel, note.getBeat());
            }
        }

        protected boolean isHeadJudged(int panel, Note note) {
            return getJudgmentKeeper().hasJudgment(panel, note.getBeat());
        }
    }

    private class HoldNoteJudge extends LengthyNoteJudge {
        @Override
        public void updateTrail(int panel, double time, double beat, LengthyNote note) {
            PanelState states = getPanelState();
            Timing timing = getTiming();
            double tailTime = timing.getTimeAt(note.getBeat() + note.getLength());
            boolean insideTrail = time < tailTime;

            if(states.isReleasedAt(panel, time)) {
                double refTime = Math.min(time, tailTime);
                double floorTimeReleased = states.getFloorTimeReleased(panel, time);
                if(!criteria.isHoldRecoverEnabled()) {
                    //Hold recover is disabled
                    emitJudgment(panel, note, new TailJudgment(floorTimeReleased, NG));
                } else if(Math.abs(refTime - floorTimeReleased) > criteria.getHoldRecover()) {
                    //Hold recover is enabled but cannot recover in time
                    emitJudgment(panel, note, new TailJudgment(floorTimeReleased + criteria.getHoldRecover(), NG));
                } else if(!insideTrail) {
                    //Hold note completed
                    emitJudgment(panel, note, new TailJudgment(tailTime, OK));
                }
            } else if(!insideTrail) {
                //Hold note completed
                emitJudgment(panel, note, new TailJudgment(tailTime, OK));
            }
        }
    }

    private class RollNoteJudge extends LengthyNoteJudge {
        @Override
        public void updateTrail(int panel, double time, double beat, LengthyNote note) {
            PanelState states = getPanelState();
            Timing timing = getTiming();
            double tailTime = timing.getTimeAt(note.getBeat() + note.getLength());
            double lowerTimePressed = states.getLowerTimePressed(panel, time);
            double refTime = Math.min(time, tailTime);

            if(Math.abs(refTime - lowerTimePressed) > criteria.getRollRecover()) {
                //Cannot recover in time
                emitJudgment(panel, note, new TailJudgment(lowerTimePressed + criteria.getRollRecover(), NG));
            } else if(time >= tailTime) {
                //Roll note completed
                emitJudgment(panel, note, new TailJudgment(tailTime, OK));
            }
        }
    }

    private class LiftNoteJudge extends NoteJudge {
        @Override
        public void update(int panel, double time, double beat, Note note) {

        }

        @Override
        public void onPanelStateChange(int panel, double time, boolean pressed, Note note) {

        }
    }

    private class MineNoteJudge extends NoteJudge {
        @Override
        public void update(int panel, double time, double beat, Note note) {

        }

        @Override
        public void onPanelStateChange(int panel, double time, boolean pressed, Note note) {

        }
    }

}
