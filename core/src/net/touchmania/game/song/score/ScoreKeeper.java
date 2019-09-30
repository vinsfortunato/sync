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

import com.badlogic.gdx.utils.ObjectSet;
import net.touchmania.game.match.ControlState;
import net.touchmania.game.match.ControlStateListener;
import net.touchmania.game.match.Match;
import net.touchmania.game.song.NoteColumn;
import net.touchmania.game.song.OldBeatmap;
import net.touchmania.game.song.OldTimingGraph;
import net.touchmania.game.song.note.NoteType;

import java.util.TreeMap;

/**
 * @author flood2d
 */
public class ScoreKeeper implements ControlStateListener, ScoreListener {
    private final Match match;
    private OldTimingGraph timing;
    private OldBeatmap beatmap;
    private ControlState controls;
    private final JudgeWindow judgeWindow = new JudgeWindow();
    private final JudgeHistory judgeHistory = new JudgeHistory();
    private ObjectSet<ScoreListener> listeners = new ObjectSet<>();

    /** Used to avoid reiteration of the entire beatmap **/
    private float leftProcessedBeat = -1f;
    private float downProcessedBeat = -1f;
    private float upProcessedBeat = -1f;
    private float rightProcessedBeat = -1f;

    public ScoreKeeper(Match match) {
        this.match = match;
    }

    public void init() {
        //judgeHistory.init(match.getChart().getBeatmap());
        timing = match.getTiming();
       // beatmap = match.getChart().getBeatmap();
        controls = match.getControls();
    }

    /**
     * Adds a score listener.
     * @param listener the listener to add.
     * @return true if the listener has been added, false if the listener
     *         to add is null or has been already added.
     */
    public boolean addListener(ScoreListener listener) {
        if(listener != null) {
            return listeners.add(listener);
        }
        return false;
    }

    /**
     * Removes a score listener.
     * @param listener the listener to remove.
     * @return true if the listener has been removed, false if the
     *         listener to remove is null or isn't contained.
     */
    public boolean removeListener(ScoreListener listener) {
        if(listener != null) {
            return listeners.remove(listener);
        }
        return false;
    }

    public ObjectSet<ScoreListener> getListeners() {
        return listeners;
    }

    @Override
    public void onControlPressed(NoteColumn noteColumn, float inputTime) {
        float inputBeat = timing.getBeatAt(inputTime);
        float processedBeat = getProcessedBeat(noteColumn);
        TreeMap<Float, NoteType> notesMap = beatmap.getNotesMap(noteColumn);
        Float noteBeat;
        float noteTime;
        NoteType noteType;
        Judgment judgment;

        noteBeat = notesMap.floorKey(inputBeat);
        while (noteBeat != null && noteBeat > processedBeat) {
            noteTime = timing.getTimeAt(noteBeat);
            judgment = judgeWindow.getJudgment(inputTime - noteTime);
            if(judgment == Judgment.MISS) {
                break;
            }
            if (!judgeHistory.hasJudgment(noteBeat, noteColumn)) {
                noteType = notesMap.get(noteBeat);
                if (noteType == NoteType.TAP || noteType == NoteType.HOLD || noteType == NoteType.ROLL) {
                    judgeHistory.putJudgment(noteBeat, noteColumn, judgment);
                    if(noteType == NoteType.HOLD) {
                        onHoldAttached(noteColumn, noteBeat);
                    } else if(noteType == NoteType.ROLL) {
                        //TODO
                    }
                    if(canScore(noteBeat)) {
                        onJudgment(noteColumn, noteType, noteBeat, getChordJudgment(noteColumn, noteBeat));
                    }
                    return; //Press input processed
                }
            }
            noteBeat = notesMap.lowerKey(noteBeat);
        }

        noteBeat = notesMap.ceilingKey(inputBeat);
        while(noteBeat != null) {
            noteTime = timing.getTimeAt(noteBeat);
            judgment = judgeWindow.getJudgment(inputTime - noteTime);
            if(judgment == Judgment.MISS) {
                break;
            }
            if (!judgeHistory.hasJudgment(noteBeat, noteColumn)) {
                noteType = notesMap.get(noteBeat);
                if (noteType == NoteType.TAP || noteType == NoteType.HOLD || noteType == NoteType.ROLL) {
                    judgeHistory.putJudgment(noteBeat, noteColumn, judgment);
                    if(noteType == NoteType.HOLD) {
                        onHoldAttached(noteColumn, noteBeat);
                    } else if(noteType == NoteType.ROLL) {
                        //TODO
                    }
                    if(canScore(noteBeat)) {
                        onJudgment(noteColumn, noteType, noteBeat, getChordJudgment(noteColumn, noteBeat));
                    }
                    return; //Press input processed
                }
            }
            noteBeat = notesMap.higherKey(noteBeat);
        }
    }

    private Judgment getChordJudgment(NoteColumn noteColumn, float beat) {
        int chordSize = beatmap.getChordSize(beat);
        if(chordSize > 1) {
            Judgment judgment = null;
            Judgment j;
            for(NoteColumn column : NoteColumn.values()) {
                j = judgeHistory.getJudgment(beat, column);
                if(judgment == null) {
                    judgment = j;
                } else if(j != null && j.ordinal() > judgment.ordinal()){
                    judgment = j;
                }
            }
            return judgment;
        }
        return judgeHistory.getJudgment(beat, noteColumn);
    }

    @Override
    public void onControlReleased(NoteColumn noteColumn, float time) {}

    private boolean canScore(float noteBeat) {
        int chordSize = beatmap.getChordSize(noteBeat);
        if(chordSize == 1) {
            return true;
        }
        int scored = 0;
        Judgment judgment;
        for(NoteColumn noteColumn : NoteColumn.values()) {
            judgment = judgeHistory.getJudgment(noteBeat, noteColumn);
            if(judgment == Judgment.MISS) {
                return false;
            } else if(judgment != null){
                scored++;
            }
        }
        return scored >= chordSize;
    }

    /**
     * Must be called at each frame.
     */
    public void update() {
        float currentTime = match.getCurrentTime();
        float currentBeat = match.getCurrentBeat();
        TreeMap<Float, NoteType> notesMap;
        Float noteBeat;
        float noteTime;
        NoteType noteType;
        float processedBeat, nextProcessedBeat;

        for(NoteColumn noteColumn : NoteColumn.values()) {
            notesMap = beatmap.getNotesMap(noteColumn);
            noteBeat = notesMap.floorKey(currentBeat);
            processedBeat = getProcessedBeat(noteColumn);
            nextProcessedBeat = processedBeat;

            while(noteBeat != null && noteBeat > processedBeat) {
                noteType = notesMap.get(noteBeat);
                if(noteType == NoteType.TAP) {
                    if(!judgeHistory.hasJudgment(noteBeat, noteColumn)) {
                        noteTime = timing.getTimeAt(noteBeat);
                        if (judgeWindow.isMissed(currentTime - noteTime)) {
                            judgeHistory.putJudgment(noteBeat, noteColumn, Judgment.MISS);
                            if(beatmap.getChordSize(noteBeat) == 1) {
                                onJudgment(noteColumn, noteType, noteBeat, Judgment.MISS);
                            } else {
                                //TODO notify that this is a chord.
                                onJudgment(noteColumn, noteType, noteBeat, Judgment.MISS);
                            }
                        }
                    }
                } else if(noteType == NoteType.HOLD) {
                    Judgment headJudgment = judgeHistory.getJudgment(noteBeat, noteColumn);
                    if(headJudgment == null) { //Still no judgment for head.
                        noteTime = timing.getTimeAt(noteBeat);
                        if(judgeWindow.isMissed(currentTime - noteTime)) {
                            judgeHistory.putJudgment(noteBeat, noteColumn, Judgment.MISS);
                            judgeHistory.putJudgment(notesMap.higherKey(noteBeat), noteColumn, Judgment.NG);
                            onJudgment(noteColumn, noteType, noteBeat, Judgment.MISS);
                            //TODO notifies only head miss (Notify NG ?)
                        }
                    } else {
                        float tailBeat = notesMap.higherKey(noteBeat);
                        if(!judgeHistory.hasJudgment(tailBeat, noteColumn)) {
                            if(headJudgment != Judgment.MISS && !controls.isPressed(noteColumn)) {
                                if (!judgeWindow.canRecoverHold(currentTime - controls.getLastInputTime(noteColumn))) {
                                    judgeHistory.putJudgment(tailBeat, noteColumn, Judgment.NG);
                                    onJudgment(noteColumn, NoteType.HOLD_ROLL_TAIL, tailBeat, Judgment.NG);
                                    onHoldDetached(noteColumn, noteBeat);
                                }
                            }
                        }
                    }
                } else if(noteType == NoteType.ROLL) {
                    //TODO
                } else if(noteType == NoteType.HOLD_ROLL_TAIL) {
                    if(!judgeHistory.hasJudgment(noteBeat, noteColumn)) {
                        NoteType headType = notesMap.lowerEntry(noteBeat).getValue();
                        if(headType == NoteType.HOLD) {
                            Judgment headJudgment = judgeHistory.getJudgment(notesMap.lowerKey(noteBeat), noteColumn);
                            if(headJudgment != null) {
                                if(headJudgment != Judgment.MISS) {
                                    if(controls.isPressed(noteColumn) || judgeWindow.canRecoverHold(currentTime - controls.getLastInputTime(noteColumn))) {
                                        judgeHistory.putJudgment(noteBeat, noteColumn, Judgment.OK);
                                        onJudgment(noteColumn, noteType, noteBeat, Judgment.OK);
                                    } else {
                                        judgeHistory.putJudgment(noteBeat, noteColumn, Judgment.NG);
                                        onJudgment(noteColumn, noteType, noteBeat, Judgment.OK);
                                        onHoldDetached(noteColumn, notesMap.lowerKey(noteBeat));
                                    }
                                }
                            }
                        } else if(headType == NoteType.ROLL) {
                            //TODO
                        }
                    }
                } else if(noteType == NoteType.MINE) {
                    //TODO
                } else if(noteType == NoteType.LIFT) {
                    //TODO
                } else if(noteType == NoteType.FAKE) {
                    //No action on this note type.
                } else if(noteType == NoteType.AUTO_KEYSOUND) {
                    //No action on this note type.
                }

                if(judgeHistory.hasJudgment(noteBeat, noteColumn)) {
                    if(nextProcessedBeat < noteBeat) {
                        nextProcessedBeat = noteBeat;
                    }
                } else {
                    nextProcessedBeat = processedBeat;
                }

                noteBeat = notesMap.lowerKey(noteBeat);
            }

            setProcessedBeat(noteColumn, nextProcessedBeat);
        }
    }

    private float getProcessedBeat(NoteColumn noteColumn) {
        switch(noteColumn) {
            case LEFT:
                return leftProcessedBeat;
            case UP:
                return upProcessedBeat;
            case DOWN:
                return downProcessedBeat;
            case RIGHT:
                return rightProcessedBeat;
        }
        return 0;
    }

    private void setProcessedBeat(NoteColumn noteColumn, float beat) {
        switch (noteColumn) {
            case LEFT:
                leftProcessedBeat = beat;
                break;
            case UP:
                upProcessedBeat = beat;
                break;
            case DOWN:
                downProcessedBeat = beat;
                break;
            case RIGHT:
                rightProcessedBeat = beat;
                break;
        }
    }

    public JudgeHistory getJudgeHistory() {
        return judgeHistory;
    }

    public JudgeWindow getJudgeWindow() {
        return judgeWindow;
    }

    @Override
    public void onJudgment(NoteColumn noteColumn, NoteType noteType, float beat, Judgment judgment) {
        for(ScoreListener listener : listeners) {
            listener.onJudgment(noteColumn, noteType, beat, judgment);
        }
    }

    @Override
    public void onHoldAttached(NoteColumn noteColumn, float headBeat) {
        for(ScoreListener listener : listeners) {
            listener.onHoldAttached(noteColumn, headBeat);
        }
    }

    @Override
    public void onHoldDetached(NoteColumn noteColumn, float headBeat) {
        for(ScoreListener listener : listeners) {
            listener.onHoldDetached(noteColumn, headBeat);
        }
    }
}