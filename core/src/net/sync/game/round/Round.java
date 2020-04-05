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

package net.sync.game.round;

import com.badlogic.gdx.audio.Music;
import net.sync.game.round.judge.Judge;
import net.sync.game.round.judge.JudgeCriteria;
import net.sync.game.round.modifier.MaxSpeedModifier;
import net.sync.game.round.modifier.Modifiers;
import net.sync.game.round.modifier.MultiplySpeedModifier;
import net.sync.game.song.Chart;
import net.sync.game.song.Song;
import net.sync.game.song.Timing;

import static net.sync.game.Game.backend;

public class Round {
    private Song song;
    private Chart chart;
    private Timing timing;
    private Score score;
    private Life life;
    private Music music;
    private MusicPosition musicPosition;
    private Judge judge;
    private PanelState panelState;
    private Modifiers modifiers;

    public Round(Song song, Chart chart, Music music) {
        this.song = song;
        this.chart = chart;
        this.music = music;
        this.musicPosition = new MusicPosition(music);
        this.timing = new Timing(chart.timingData);
        this.modifiers = new Modifiers();
        this.panelState = new PanelState();
        this.judge = new Judge( this, new JudgeCriteria());
        this.panelState.addListener(this.judge);

        setDefaultMods();
    }

    private void setDefaultMods() {
        //Init speed modifier
        //Duration can be calculated only on Android. So this is a temp solution
        double duration = backend().getDuration(music);
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
