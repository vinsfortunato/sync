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

package net.touchmania.game.ui.play;

import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.match.ControlStateListener;
import net.touchmania.game.match.Match;
import net.touchmania.game.song.NoteColumn;
import net.touchmania.game.song.OldBeatmap;
import net.touchmania.game.song.modifier.BeatModifierApplier;
import net.touchmania.game.song.note.NoteType;
import net.touchmania.game.song.score.JudgeHistory;
import net.touchmania.game.song.score.Judgment;
import net.touchmania.game.song.score.ScoreListener;
import net.touchmania.game.song.score.ScoreKeeper;

import java.util.TreeMap;

/**
 * Contains <code>BeatmapView</code> objects states.
 * BeatmapView draws game objects (receptors and notes).
 * These objects change their state over time. This class provides
 * access to the state of these objects. The state values are
 * calculated considering match modifiers.
 * @author flood2d
 */
public class BeatmapViewState implements ControlStateListener, ScoreListener {
    public Match match;
    public BeatmapView view;

    private ObjectMap<Float, Float> leftHoldRollBeatMap = new ObjectMap<>();
    private ObjectMap<Float, Float> downHoldRollBeatMap = new ObjectMap<>();
    private ObjectMap<Float, Float> upHoldRollBeatMap = new ObjectMap<>();
    private ObjectMap<Float, Float> rightHoldRollBeatMap = new ObjectMap<>();

    private float receptorOutlineAnimationDuration = 0.165f;
    private float leftReceptorOutlineState = -1f;
    private float downReceptorOutlineState = -1f;
    private float upReceptorOutlineState = -1f;
    private float rightReceptorOutlineState = -1f;

    enum ApproachDirection {
        FROM_BOTTOM_TO_TOP,
        FROM_TOP_TO_BOTTOM
    }

    protected BeatmapViewState(BeatmapView view, Match match) {
        this.match = match;
        this.view = view;
    }

    /**
     * Updates the view state based on time. Typically this is
     * called at each frame.
     * @param delta time in seconds since the last frame.
     */
    protected void update(float delta) {
        if(match.getControls().isDownReleased() && downReceptorOutlineState > 0) {
            downReceptorOutlineState -= (delta / receptorOutlineAnimationDuration) * 1.0f;
        }
        if(match.getControls().isLeftReleased() && leftReceptorOutlineState > 0) {
            leftReceptorOutlineState -= (delta / receptorOutlineAnimationDuration) * 1.0f;
        }
        if(match.getControls().isRightReleased() && rightReceptorOutlineState > 0) {
            rightReceptorOutlineState -= (delta / receptorOutlineAnimationDuration) * 1.0f;
        }
        if(match.getControls().isUpReleased() && upReceptorOutlineState > 0) {
            upReceptorOutlineState -= (delta / receptorOutlineAnimationDuration) * 1.0f;
        }
    }

    @Override
    public void onJudgment(NoteColumn noteColumn, NoteType noteType, float beat, Judgment judgment) {}


    @Override
    public void onHoldAttached(NoteColumn noteColumn, float headBeat) {}

    @Override
    public void onHoldDetached(NoteColumn noteColumn, float headBeat) {
        ObjectMap<Float, Float> holdRollBeatMap = getHoldRollBeatMap(noteColumn);
        if(holdRollBeatMap != null) {
            holdRollBeatMap.put(headBeat, match.getCurrentBeat());
        }
    }

    @Override
    public void onControlPressed(NoteColumn noteColumn, float inputTime) {
        switch(noteColumn) {
            case LEFT:
                leftReceptorOutlineState = 1.0f;
                break;
            case DOWN:
                downReceptorOutlineState = 1.0f;
                break;
            case RIGHT:
                rightReceptorOutlineState = 1.0f;
                break;
            case UP:
                upReceptorOutlineState = 1.0f;
                break;
        }
    }

    @Override
    public void onControlReleased(NoteColumn noteColumn, float inputTime) {}

    private ObjectMap<Float, Float> getHoldRollBeatMap(NoteColumn noteColumn) {
        switch(noteColumn) {
            case LEFT:
                return leftHoldRollBeatMap;
            case DOWN:
                return downHoldRollBeatMap;
            case UP:
                return upHoldRollBeatMap;
            case RIGHT:
                return rightHoldRollBeatMap;
        }
        return null;
    }

    /**
     * Checks if the given note is visible. If it isn't visible
     * the note will not be drawn. For example, tap notes processed by the
     * {@link ScoreKeeper} are invisible
     * if the judgment related to the note is greater than GOOD.
     * @param noteColumn the note column.
     * @param noteType the note type.
     * @param beat the note beat.
     * @return true if the note is visible and must be drawn.
     */
    public boolean isNoteVisible(NoteColumn noteColumn, NoteType noteType, float beat) {
        OldBeatmap beatmap = null;//match.getChart().getBeatmap();
        JudgeHistory judgeHistory = match.getScoreKeeper().getJudgeHistory();
        Judgment judgment;
        if(noteType == NoteType.TAP || noteType == NoteType.HOLD || noteType == NoteType.ROLL) {
            TreeMap<Float, NoteType> notesMap;
            NoteType currNoteType;
            for(NoteColumn column : NoteColumn.values()) {
                notesMap = beatmap.getNotesMap(column);
                currNoteType = notesMap.get(beat);
                if(currNoteType != null) {
                    if(currNoteType == NoteType.TAP || currNoteType == NoteType.HOLD || currNoteType == NoteType.ROLL) {
                        judgment = judgeHistory.getJudgment(beat, column);
                        if(judgment == null || judgment.ordinal() > Judgment.GREAT.ordinal()) {
                            return true;
                        }
                    }
                }
            }

            if(noteType == NoteType.HOLD) {
                return isHoldVisible(noteColumn, beat);
            } else if(noteType == NoteType.ROLL) {
                //TODO
            }
            return false;
        }
        return true;
    }

    public boolean isHoldVisible(NoteColumn noteColumn, float headBeat) {
        OldBeatmap beatmap = null; //match.getChart().getBeatmap();
        JudgeHistory judgeHistory = match.getScoreKeeper().getJudgeHistory();
        Judgment judgment = judgeHistory.getJudgment(headBeat, noteColumn);
        if(judgment == null || judgment == Judgment.MISS) {
            return true;
        }
        float tailBeat = beatmap.getHigherBeat(noteColumn, headBeat);
        Judgment holdJudgment = judgeHistory.getJudgment(tailBeat, noteColumn);
        return holdJudgment == null || holdJudgment == Judgment.NG;
    }

    /**
     * @param noteColumn the note column.
     * @param noteType the note type.
     * @param beat the note beat.
     * @return the note x position relative to view's x position.
     */
    public float getNoteX(NoteColumn noteColumn, NoteType noteType, float beat) {
        return getReceptorX(noteColumn);
    }

    /**
     * @param noteColumn the note column.
     * @param noteType the note type.
     * @param beat the note beat.
     * @return the note y position relative to view's y position.
     */
    public float getNoteY(NoteColumn noteColumn, NoteType noteType, float beat) {
        if(noteType == NoteType.HOLD) {
            if(isHoldHeadFixed(noteColumn, beat)) {
                return getReceptorY(noteColumn);
            }
            ObjectMap<Float, Float> holdRollBeatMap = getHoldRollBeatMap(noteColumn);
            if(holdRollBeatMap != null && holdRollBeatMap.containsKey(beat)) {
                beat = holdRollBeatMap.get(beat);
            }
        }
        BeatModifierApplier modApplier = match.getModifiers().getBeatModifierApplier();
        float modCurrent = modApplier.apply(match.getCurrentBeat());
        float modBeat = modApplier.apply(beat);
        float receptorY = getReceptorY(noteColumn);
        float receptorH = getReceptorHeight(noteColumn);
        float distance = receptorH * (modCurrent - modBeat);
        switch(getApproachDirection()) {
            case FROM_BOTTOM_TO_TOP:
                return receptorY + distance;
            default:
                return receptorY - distance;
        }
    }

    /**
     * @param noteColumn the note column.
     * @param noteType the note type.
     * @param beat the note beat.
     * @return the note scale x.
     */
    public float getNoteScaleX(NoteColumn noteColumn, NoteType noteType, float beat) {
        return 1.0f;
    }

    /**
     * @param noteColumn the note column.
     * @param noteType the note type.
     * @param beat the note beat.
     * @return the note scale y.
     */
    public float getNoteScaleY(NoteColumn noteColumn, NoteType noteType, float beat) {
        return 1.0f;
    }

    /**
     * @param noteColumn the note column.
     * @param noteType the note type.
     * @param beat the note beat.
     * @return the note opacity.
     */
    public float getNoteOpacity(NoteColumn noteColumn, NoteType noteType, float beat) {
        return 1.0f;
    }

    /**
     * @param noteColumn the note column.
     * @param noteType the note type.
     * @param beat the note beat.
     * @return the note rotation with the origin in the center of the note texture.
     */
    public float getNoteRotation(NoteColumn noteColumn, NoteType noteType, float beat) {
        if(noteType == NoteType.MINE) {
            float degrees = -60f * match.getCurrentBeat();
            degrees %= 360;
            return degrees;
        } else {
            switch(noteColumn) {
                case LEFT:
                    return -90;
                case DOWN:
                    return 0;
                case UP:
                    return 180;
                case RIGHT:
                    return 90;
                default:
                    return 0;
            }
        }
    }

    /**
     * Checks if the receptor is visible. If it isn't visible it must
     * not be drawn.
     * @param noteColumn the receptor note column.
     * @return true if it is visible, false otherwise.
     */
    public boolean isReceptorVisible(NoteColumn noteColumn) {
        return true;
    }

    /**
     * @param noteColumn the receptor note column.
     * @return the receptor x position relative to the view x position.
     */
    public float getReceptorX(NoteColumn noteColumn) {
        float width = view.getWidth();
        float receptorWidth = getReceptorWidth(noteColumn);
        return width / 2 - receptorWidth * 2 + receptorWidth * noteColumn.ordinal();
    }

    /**
     * @param noteColumn the receptor note column,
     * @return the receptor y position relative to the view y position.
     */
    public float getReceptorY(NoteColumn noteColumn) {
        if(getApproachDirection() == ApproachDirection.FROM_BOTTOM_TO_TOP) {
            return view.getHeight() - getReceptorHeight(noteColumn) * 3 / 2;
        } else {
            return getReceptorHeight(noteColumn) / 2;
        }
    }

    /**
     * @param noteColumn the receptor note column.
     * @return the receptor width.
     */
    public float getReceptorWidth(NoteColumn noteColumn) {
        return view.getTextures().getReceptorTexture().getRegionWidth();
    }

    /**
     * @param noteColumn the receptor note column.
     * @return the receptor height.
     */
    public float getReceptorHeight(NoteColumn noteColumn) {
        return view.getTextures().getReceptorTexture().getRegionHeight();
    }

    /**
     * @param noteColumn the receptor note column.
     * @return the receptor scale x.
     */
    public float getReceptorScaleX(NoteColumn noteColumn) {
        switch (noteColumn) {
            case LEFT:
                return 1.0f - leftReceptorOutlineState * 0.2f;
            case DOWN:
                return 1.0f - downReceptorOutlineState * 0.2f;
            case UP:
                return 1.0f - upReceptorOutlineState * 0.2f;
            case RIGHT:
                return 1.0f - rightReceptorOutlineState * 0.2f;
        }
        return 1.0f;
    }

    /**
     * @param noteColumn the receptor note column.
     * @return the receptor scale y.
     */
    public float getReceptorScaleY(NoteColumn noteColumn) {
        switch (noteColumn) {
            case LEFT:
                return 1.0f - leftReceptorOutlineState * 0.2f;
            case DOWN:
                return 1.0f - downReceptorOutlineState * 0.2f;
            case UP:
                return 1.0f - upReceptorOutlineState * 0.2f;
            case RIGHT:
                return 1.0f - rightReceptorOutlineState * 0.2f;
        }
        return 1.0f;
    }

    /**
     * @param noteColumn the receptor note column.
     * @return the receptor opacity.
     */
    public float getReceptorOpacity(NoteColumn noteColumn) {
        return 1.0f;
    }

    /**
     * @param noteColumn the receptor note column.
     * @return the receptor rotation in degrees with the origin in the
     *         center of the receptor texture.
     */
    public float getReceptorRotation(NoteColumn noteColumn) {
        switch(noteColumn) {
            case LEFT:
                return -90;
            case DOWN:
                return 0;
            case UP:
                return 180;
            case RIGHT:
                return 90;
            default:
                return 0;
        }
    }

    public float getReceptorOutlineScaleX(NoteColumn noteColumn) {
        switch (noteColumn) {
            case LEFT:
                return 1.0f + (0.2f - leftReceptorOutlineState * 0.2f);
            case DOWN:
                return 1.0f + (0.2f - downReceptorOutlineState * 0.2f);
            case UP:
                return 1.0f + (0.2f - upReceptorOutlineState * 0.2f);
            case RIGHT:
                return 1.0f + (0.2f - rightReceptorOutlineState * 0.2f);
        }
        return 1.0f;
    }

    public float getReceptorOutlineScaleY(NoteColumn noteColumn) {
        switch (noteColumn) {
            case LEFT:
                return 1.0f + (0.2f - leftReceptorOutlineState * 0.2f);
            case DOWN:
                return 1.0f + (0.2f - downReceptorOutlineState * 0.2f);
            case UP:
                return 1.0f + (0.2f - upReceptorOutlineState * 0.2f);
            case RIGHT:
                return 1.0f + (0.2f - rightReceptorOutlineState * 0.2f);
        }
        return 1.0f;
    }

    public float getReceptorOutlineOpacity(NoteColumn noteColumn) {
        switch(noteColumn) {
            case LEFT:
                return leftReceptorOutlineState;
            case DOWN:
                return downReceptorOutlineState;
            case UP:
                return upReceptorOutlineState;
            case RIGHT:
                return rightReceptorOutlineState;
        }
        return 0.0f;
    }

    public boolean isHoldActive(NoteColumn noteColumn, float headBeat) {
        return isHoldHeadFixed(noteColumn, headBeat) && match.getControls().isPressed(noteColumn);
    }

    /**
     * Checks if the hold head is fixed on receptor.
     * @param noteColumn the note column.
     * @param headBeat the hold head beat.
     * @return true if the hold head is fixed on receptor.
     */
    public boolean isHoldHeadFixed(NoteColumn noteColumn, float headBeat) {
        JudgeHistory judgeHistory = match.getScoreKeeper().getJudgeHistory();
        Judgment headJudgment = judgeHistory.getJudgment(headBeat, noteColumn);
        if(headJudgment != null && headJudgment != Judgment.MISS) {
            float tailBeat = 0.0f; //match.getChart().getBeatmap().getHigherBeat(noteColumn, headBeat);
            Judgment tailJudgment = judgeHistory.getJudgment(tailBeat, noteColumn);
            return tailJudgment == null;
        }
        return false;
    }

    /**
     * Gets minimum visible beat. Note associated to a beat that is
     * less than this will not be drawn.
     * @param noteColumn the note column.
     * @return the minimum visible beat.
     */
    public float getMinimumBeat(NoteColumn noteColumn) {
        BeatModifierApplier modApplier = match.getModifiers().getBeatModifierApplier();
        float modCurrent = modApplier.apply(match.getCurrentBeat());
        float beatDistance;
        float receptorY = getReceptorY(noteColumn);
        float receptorH = getReceptorHeight(noteColumn);
        switch(getApproachDirection()) {
            case FROM_BOTTOM_TO_TOP:
                beatDistance = (view.getHeight() - receptorY) / receptorH;
                return modApplier.revert(modCurrent - beatDistance);
            default:
                beatDistance = receptorY / receptorH + 1.0f;
                return modApplier.revert(modCurrent - beatDistance);
        }
    }

    /**
     * Gets maximum visible beat. Note associated to a beat that is
     * greater than this will note be drawn.
     * @param noteColumn the note column.
     * @return the maximum visible beat.
     */
    public float getMaximumBeat(NoteColumn noteColumn) {
        BeatModifierApplier modApplier = match.getModifiers().getBeatModifierApplier();
        float modCurrent = modApplier.apply(match.getCurrentBeat());
        float beatDistance;
        float receptorY = getReceptorY(noteColumn);
        float receptorH = getReceptorHeight(noteColumn);
        switch(getApproachDirection()) {
            case FROM_BOTTOM_TO_TOP:
                beatDistance = -(receptorY + receptorH) / receptorH;
                return modApplier.revert(modCurrent - beatDistance);
            default:
                beatDistance = -(view.getHeight() - receptorY) / receptorH;
                return modApplier.revert(modCurrent - beatDistance);
        }
    }

    /**
     * Gets the direction notes approach receptors.
     * @return the notes approach direction.
     */
    public ApproachDirection getApproachDirection() {
        return ApproachDirection.FROM_BOTTOM_TO_TOP; //TODO
    }
}
