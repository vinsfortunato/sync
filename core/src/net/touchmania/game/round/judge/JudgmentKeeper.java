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
import com.badlogic.gdx.utils.LongMap;
import com.google.common.base.Preconditions;
import net.touchmania.game.Game;
import net.touchmania.game.song.Beatmap;
import net.touchmania.game.song.note.NotePanel;

import static net.touchmania.game.util.TimingUtils.toBeatId;

/**
 * Holds note judgments.
 */
public class JudgmentKeeper {
    private IntMap<LongMap<Judgment>> judgments = new IntMap<>();

    private Judgment lastJudgment;

    public JudgmentKeeper() {}

    public JudgmentKeeper(Beatmap beatmap) {
        this(NotePanel.getModePanels(Game.instance().getSettings().getGameMode()), beatmap);
    }

    public JudgmentKeeper(int ... panels) {
        for(int panel : panels) {
            judgments.put(panel, new LongMap<>());
        }
    }

    public JudgmentKeeper(int[] panels, Beatmap beatmap) {
        for(int panel : panels) {
            judgments.put(panel, new LongMap<>(beatmap.countNotes(panel)));
        }
    }

    public JudgmentKeeper(int[] panels, int[] initialSize) {
        Preconditions.checkArgument(panels.length == initialSize.length, "panels array and size array must have the same length.");
        for(int i = 0; i < panels.length; i++) {
            judgments.put(panels[i], new LongMap<>(initialSize[i]));
        }
    }

    public boolean hasJudgment(int panel, double beat) {
        LongMap<Judgment> panelJudgments = judgments.get(panel);
        return panelJudgments != null && panelJudgments.containsKey(toBeatId(beat));
    }


    public Judgment getJudgment(int panel, double beat) {
        LongMap<Judgment> panelJudgments = judgments.get(panel);
        return panelJudgments != null ? panelJudgments.get(toBeatId(beat)) : null;
    }

    public Judgment putJudgment(int panel, double beat, Judgment judgment) {
        //Update last judgment, TODO TEMP
        lastJudgment = judgment;

        LongMap<Judgment> panelJudgments = judgments.get(panel);
        if(panelJudgments == null) {
            panelJudgments = new LongMap<>();
            judgments.put(panel, panelJudgments);
        }
        return panelJudgments.put(toBeatId(beat), judgment);
    }

    public Judgment getLastJudgment() {
        return lastJudgment;
    }
}
