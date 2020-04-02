/*
 * Copyright 2020 Vincenzo Fortunato
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

package net.sync.game.round;

import com.badlogic.gdx.audio.Music;
import net.sync.game.Game;
import net.sync.game.round.judge.Judge;
import net.sync.game.round.judge.JudgeCriteria;
import net.sync.game.round.modifier.MaxSpeedModifier;
import net.sync.game.round.modifier.Modifiers;
import net.sync.game.round.modifier.MultiplySpeedModifier;
import net.sync.game.song.Chart;
import net.sync.game.song.Song;
import net.sync.game.song.Timing;

public class Round {
    private Song song;
    private Chart chart;
    private Timing timing;
    private Score score;
    private Life life;
    private Music music;
    private net.sync.game.round.MusicPosition musicPosition;
    private Judge judge;
    private net.sync.game.round.PanelState panelState;
    private net.sync.game.round.modifier.Modifiers modifiers;

    public Round(Song song, Chart chart, Music music) {
        this.song = song;
        this.chart = chart;
        this.music = music;
        this.musicPosition = new net.sync.game.round.MusicPosition(music);
        this.timing = new Timing(chart.timingData);
        this.modifiers = new net.sync.game.round.modifier.Modifiers();
        this.panelState = new net.sync.game.round.PanelState();
        this.judge = new Judge( this, new JudgeCriteria());
        this.panelState.addListener(this.judge);

        setDefaultMods();
    }

    private void setDefaultMods() {
        //Init speed modifier
        //Duration can be calculated only on Android. So this is a temp solution
        double duration = Game.instance().getBackend().getDuration(music);
        if(duration > 0.0D) {
            modifiers.setSpeedModifier(new MaxSpeedModifier(timing.getDominantBpm(duration), 500D));
        } else {
            modifiers.setSpeedModifier(new MultiplySpeedModifier(2.5f));
        }
    }

    public Song getSong() {
        return song;
    }

    public Chart getChart() {
        return chart;
    }

    public MusicPosition getMusicPosition() {
        return musicPosition;
    }

    public Timing getTiming() {
        return timing;
    }

    public Judge getJudge() {
        return judge;
    }

    public PanelState getPanelState() {
        return panelState;
    }

    public Modifiers getModifiers() {
        return modifiers;
    }

    public void update() {
        musicPosition.update();
        judge.update(musicPosition.getPosition());
    }
}
