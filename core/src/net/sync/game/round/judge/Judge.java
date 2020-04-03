/*
 * Copyright (c) 2020 Vincenzo Fortunato.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.sync.game.round.judge;

import com.badlogic.gdx.utils.IntMap;
import net.sync.game.Game;
import net.sync.game.GameMode;
import net.sync.game.round.PanelState;
import net.sync.game.round.Round;
import net.sync.game.song.Beatmap;
import net.sync.game.song.Timing;
import net.sync.game.song.note.RollNote;

import static net.sync.game.round.judge.JudgmentClass.*;

/**
 * <p>Generates judgments for a round following the given set of {@link net.sync.game.round.judge.JudgeCriteria criterias}.
 * Can generate judgments in real time or all in a single moment making it usable both during
 * gameplay and replay mode.</p>
 * <p>There are two important methods that should be called:
 *  <li> {@link #update(double)} should be called periodically to update unjudged notes. It must
 *  be called during gameplay to update the note judgments in real time. During replay calling this
 *  method is not needed because all panel state history is available and can be processed in batch.
 *  <li> {@link #onPanelStateChange(int, double, boolean)} should be called when the panel state changes.
 *  Before handling the panel state change, this method will call {@link #update(double)} passing the
 *  time when the panel state changed. Panel state changes should be sent ordered by their time to
 *  this method. </li>
 * </p>
 */
public class Judge implements net.sync.game.round.PanelState.PanelStateListener {
    private net.sync.game.round.Round round;
    private net.sync.game.round.judge.JudgeCriteria criteria;

    /* Map a beat to a given panel. All notes on a panel with a beat less
     * or equal to this one will be considered judged and skipped. */
    private IntMap<Double> evaluatedBeats;
    /* Represents the processed time. Unjudged notes will be updated only
     * if the time passed to update(...) is greater than this value. */
    private double evaluatedTime;
    private int[] panels;

    /* Note specific judges */
    private TapNoteJudge tapNoteJudge;
    private HoldNoteJudge holdNoteJudge;
    private RollNoteJudge rollNoteJudge;
    private LiftNoteJudge liftNoteJudge;
    private MineNoteJudge mineNoteJudge;

    //TODO temp location
    private net.sync.game.round.judge.Judgment lastJudgment;
    public net.sync.game.round.judge.Judgment getLastJudgment() {
        return lastJudgment;
    }

    /**
     * Construct a judge from the given round and set of criteria.
     * @param round the round
     * @param criteria the judge criteria, a set of criteria used by the judge system to generate judgments.
     */
    public Judge(net.sync.game.round.Round round, net.sync.game.round.judge.JudgeCriteria criteria) {
        this.round = round;
        this.criteria = criteria;
        this.panels = net.sync.game.song.note.NotePanel.getModePanels(net.sync.game.Game.instance().getSettings().getGameMode());

        //Init evaluated beats and time
        this.evaluatedBeats = new IntMap<>();
        this.evaluatedTime = Double.MIN_VALUE;
        GameMode mode = Game.instance().getSettings().getGameMode();
        for(int panel : net.sync.game.song.note.NotePanel.getModePanels(mode)) {
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
     * Updates all unjudged notes before the given time. Should be called periodically to update unjudged notes.
     * If the given time is {@link Double#MAX_VALUE} or a value greater than the time of the last note time
     * it will update all unjudged notes till the end of the song and generate all remaining judgments.
     * @param time the time in seconds relative to the start of the music track.
     */
    public void update(double time) {
        if(time > evaluatedTime) {
            Timing timing = getTiming();
            Beatmap beatmap = getBeatmap();
            double beat = timing.getBeatAt(time);

            for(int panel : panels) {
                //Update all notes from last evaluated beat to current beat
                double evalBeat = getEvaluatedBeat(panel);
                net.sync.game.song.note.JudgeableNote note = (net.sync.game.song.note.JudgeableNote) beatmap.higherNote(panel, evalBeat, n -> n instanceof net.sync.game.song.note.JudgeableNote);
                while (note != null && note.getBeat() < beat) {
                    NoteJudge judge = getNoteJudge(note);
                    judge.update(panel, time, beat, note);
                    note = (net.sync.game.song.note.JudgeableNote) beatmap.higherNote(panel, note.getBeat(), n -> n instanceof net.sync.game.song.note.JudgeableNote);
                }
            }
            evaluatedTime = time;
        }
    }

    /**
     * Should be called when the panel state changes. Process the panel state change event.
     * It will call {@link #update(double)} by passing the time when the panel state changed, updating
     * all unjudged notes before processing the state change event.
     * @param panel the panel
     * @param time the time in seconds when the change occurs
     * @param pressed the updated state, true if pressed, false if released
     */
    @Override
    public void onPanelStateChange(int panel, double time, boolean pressed) {
        //Update all notes before event time
        update(time);

        Timing timing = getTiming();
        Beatmap beatmap = getBeatmap();
        double eventBeat = timing.getBeatAt(time);
        double evalBeat = getEvaluatedBeat(panel);

        //Find next note to judge
        net.sync.game.song.note.JudgeableNote note;
        if(eventBeat < evalBeat) {
            //Judge note after eval beat
            note = (net.sync.game.song.note.JudgeableNote) beatmap.higherNote(panel, evalBeat, n -> n instanceof net.sync.game.song.note.JudgeableNote);
        } else {
            net.sync.game.song.note.JudgeableNote floorNote = (net.sync.game.song.note.JudgeableNote) beatmap.floorNote(panel, eventBeat, n -> n instanceof net.sync.game.song.note.JudgeableNote);
            if(floorNote != null && floorNote.getBeat() > evalBeat) {
                //Judge floor note
                note = floorNote;
            } else {
                //Judge higher note
                note = (net.sync.game.song.note.JudgeableNote) beatmap.higherNote(panel, eventBeat, n -> n instanceof net.sync.game.song.note.JudgeableNote);
            }
        }

        if(note != null) {
            getNoteJudge(note).onPanelStateChange(panel, time, pressed, note);
        }
    }

    private NoteJudge getNoteJudge(net.sync.game.song.note.Note note) {
        if(note instanceof net.sync.game.song.note.TapNote)
            return tapNoteJudge;
        if(note instanceof net.sync.game.song.note.HoldNote)
            return holdNoteJudge;
        if(note instanceof RollNote)
            return rollNoteJudge;
        if(note instanceof net.sync.game.song.note.LiftNote)
            return liftNoteJudge;
        if(note instanceof net.sync.game.song.note.MineNote)
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

    private net.sync.game.round.PanelState getPanelState() {
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
        public abstract void update(int panel, double time, double beat, net.sync.game.song.note.JudgeableNote note);

        /**
         * Called when a panel changes state and update unjudged note status.
         * @param panel the panel.
         * @param time the time in seconds relative to the start of the music track.
         * @param note the unjudged note.
         */
        public abstract void onPanelStateChange(int panel, double time, boolean pressed, net.sync.game.song.note.JudgeableNote note);

        /**
         * Called when the judge emit a judgment.
         * @param panel the panel.
         * @param note the time in seconds relative to the start of the music track.
         * @param judgment the generated note judgment.
         */
        public void emitJudgment(int panel, net.sync.game.song.note.JudgeableNote note, net.sync.game.round.judge.Judgment judgment) {
            //Store judgment
            note.setJudgment(judgment);
            //Move evaluated beat cursor
            setEvaluatedBeat(panel, note.getBeat());

            lastJudgment = judgment;    //TODO temp
        }
    }

    private class TapNoteJudge extends NoteJudge {
        @Override
        public void update(int panel, double time, double beat, net.sync.game.song.note.JudgeableNote note) {
            Beatmap beatmap = getBeatmap();
            Timing timing = getTiming();
            double noteTime = timing.getTimeAt(note.getBeat());
            double timingError = noteTime - time;
            double worstWindow = criteria.getWorstTapWindow();
            net.sync.game.song.note.Note higherNote = beatmap.higherNote(panel, note.getBeat(), n -> n instanceof net.sync.game.song.note.JudgeableNote);

            if(higherNote != null && higherNote.getBeat() < beat) {
                //Next note surpassed current note
                double higherNoteTime = timing.getTimeAt(higherNote.getBeat());
                timingError = Math.max(noteTime - higherNoteTime, -worstWindow);
                emitJudgment(panel, note, new net.sync.game.round.judge.TapJudgment(noteTime - timingError, timingError, MISS));
            } else if(timingError < -worstWindow) {
                //Note outside worst window
                emitJudgment(panel, note, new net.sync.game.round.judge.TapJudgment(noteTime + worstWindow, -worstWindow, MISS));
            }
        }

        @Override
        public void onPanelStateChange(int panel, double time, boolean pressed, net.sync.game.song.note.JudgeableNote note) {
            if(pressed) {
                Timing timing = getTiming();
                double noteTime = timing.getTimeAt(note.getBeat());
                double timingError = noteTime - time;
                net.sync.game.round.judge.JudgmentClass judgmentClass = getJudgmentClass(timingError);
                if(judgmentClass != MISS) {
                    //Note judged. Emit judgment
                    emitJudgment(panel, note, new net.sync.game.round.judge.TapJudgment(time, timingError, judgmentClass));
                }
            }
        }

        private JudgmentClass getJudgmentClass(double timingError) {
            //TODO using compare?
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
        public void update(int panel, double time, double beat, net.sync.game.song.note.JudgeableNote note) {
            if (!note.hasJudgment()) {
                //Update head
                super.update(panel, time, beat, note);
            } else {
                //Update trail
                updateTrail(panel, time, beat, (net.sync.game.song.note.JudgeableLengthyNote) note);
            }
        }

        @Override
        public void onPanelStateChange(int panel, double time, boolean pressed, net.sync.game.song.note.JudgeableNote note) {
            if(!note.hasJudgment()) {
                //Handle state change on head
                super.onPanelStateChange(panel, time, pressed, note);
            } else {
                //Handle state change on trail
                onPanelStateChangeTrail(panel, time, pressed, (net.sync.game.song.note.JudgeableLengthyNote) note);
            }
        }

        public void updateTrail(int panel, double time, double beat, net.sync.game.song.note.JudgeableLengthyNote note) {}

        public void onPanelStateChangeTrail(int panel, double time, boolean pressed, net.sync.game.song.note.JudgeableLengthyNote note) {}

        @Override
        public void emitJudgment(int panel, net.sync.game.song.note.JudgeableNote note, net.sync.game.round.judge.Judgment judgment) {
            if(judgment instanceof net.sync.game.round.judge.TapJudgment) {
                    //Head judgment
                    note.setJudgment(judgment);

                lastJudgment = judgment;    //TODO temp

                //Check if the head has been missed
                net.sync.game.round.judge.TapJudgment tapJudgment = (TapJudgment) judgment;
                if(tapJudgment.getJudgmentClass() == MISS) {
                    //Head has been missed, set negative tail judgment
                    emitJudgment(panel, note, new net.sync.game.round.judge.TailJudgment(judgment.getGenTime(), NG));
                }

            } else if(judgment instanceof net.sync.game.round.judge.TailJudgment){
                //Tail judgment
                net.sync.game.song.note.JudgeableLengthyNote lengthyNote = (net.sync.game.song.note.JudgeableLengthyNote) note;
                lengthyNote.setTailJudgment((net.sync.game.round.judge.TailJudgment) judgment);

                lastJudgment = judgment;    //TODO temp

                //Move evaluated beat cursor
                setEvaluatedBeat(panel, note.getBeat());
            }
        }
    }

    private class HoldNoteJudge extends LengthyNoteJudge {
        @Override
        public void updateTrail(int panel, double time, double beat, net.sync.game.song.note.JudgeableLengthyNote note) {
            net.sync.game.round.PanelState states = getPanelState();
            Timing timing = getTiming();
            double tailTime = timing.getTimeAt(note.getBeat() + note.getLength());
            boolean insideTrail = time < tailTime;

            if(states.isReleasedAt(panel, time)) {
                double refTime = Math.min(time, tailTime);
                double floorTimeReleased = states.getFloorTimeReleased(panel, time);
                if(!criteria.isHoldRecoverEnabled()) {
                    //Hold recover is disabled
                    emitJudgment(panel, note, new net.sync.game.round.judge.TailJudgment(floorTimeReleased, NG));
                } else if(Math.abs(refTime - floorTimeReleased) > criteria.getHoldRecover()) {
                    //Hold recover is enabled but cannot recover in time
                    emitJudgment(panel, note, new net.sync.game.round.judge.TailJudgment(floorTimeReleased + criteria.getHoldRecover(), NG));
                } else if(!insideTrail) {
                    //Hold note completed
                    emitJudgment(panel, note, new net.sync.game.round.judge.TailJudgment(tailTime, OK));
                }
            } else if(!insideTrail) {
                //Hold note completed
                emitJudgment(panel, note, new net.sync.game.round.judge.TailJudgment(tailTime, OK));
            }
        }
    }

    private class RollNoteJudge extends LengthyNoteJudge {
        @Override
        public void updateTrail(int panel, double time, double beat, net.sync.game.song.note.JudgeableLengthyNote note) {
            net.sync.game.round.PanelState states = getPanelState();
            Timing timing = getTiming();
            double tailTime = timing.getTimeAt(note.getBeat() + note.getLength());
            double lowerTimePressed = states.getLowerTimePressed(panel, time);
            double refTime = Math.min(time, tailTime);

            if(Math.abs(refTime - lowerTimePressed) > criteria.getRollRecover()) {
                //Cannot recover in time
                emitJudgment(panel, note, new net.sync.game.round.judge.TailJudgment(lowerTimePressed + criteria.getRollRecover(), NG));
            } else if(time >= tailTime) {
                //Roll note completed
                emitJudgment(panel, note, new TailJudgment(tailTime, OK));
            }
        }
    }

    private class MineNoteJudge extends NoteJudge {
        @Override
        public void update(int panel, double time, double beat, net.sync.game.song.note.JudgeableNote note) {
            //Check if previous note has been judged
            net.sync.game.song.note.JudgeableNote prevNote = (net.sync.game.song.note.JudgeableNote) getBeatmap().lowerNote(panel, note.getBeat(), n -> n instanceof net.sync.game.song.note.JudgeableNote);
            if(prevNote != null && prevNote.getBeat() < getEvaluatedBeat(panel)) {
                //Prev note not judged yet
                return;
            }

            PanelState states = getPanelState();
            Timing timing = getTiming();
            double mineWindowEnd = timing.getTimeAt(note.getBeat());
            double mineWindowStart = mineWindowEnd - criteria.getMineWindow();

            if(time > mineWindowStart) {
                //Check if the mine has been triggered and generate judgment
                Judgment judgment = null;
                if(!states.isReleasedAt(panel, mineWindowStart)) {
                    //Mine triggered at the beginning of the mine window. Wasn't released when window started
                    judgment = new net.sync.game.round.judge.MineJudgment(mineWindowStart, true);
                } else if(states.getFloorTimeState(panel, mineWindowEnd) > mineWindowStart) {
                    //Mine triggered. A state change occurred inside the window
                    judgment = new net.sync.game.round.judge.MineJudgment( states.getFloorTimePressed(panel, mineWindowEnd), true);
                } else if(time > mineWindowEnd) {
                    //Mine not triggered and we are after the window. Generate positive judgment
                    judgment = new MineJudgment(mineWindowEnd, false);
                }
                if(judgment != null) {
                    emitJudgment(panel, note, judgment);
                }
            }
        }

        @Override
        public void onPanelStateChange(int panel, double time, boolean pressed, net.sync.game.song.note.JudgeableNote note) {}
    }

    private class LiftNoteJudge extends NoteJudge {
        @Override
        public void update(int panel, double time, double beat, net.sync.game.song.note.JudgeableNote note) {
            //TODO
        }

        @Override
        public void onPanelStateChange(int panel, double time, boolean pressed, net.sync.game.song.note.JudgeableNote note) {
            //TODO
        }
    }
}
