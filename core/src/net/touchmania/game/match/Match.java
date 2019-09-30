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

package net.touchmania.game.match;

import com.badlogic.gdx.audio.Music;
import net.touchmania.game.Game;
import net.touchmania.game.player.Player;
import net.touchmania.game.song.Chart;
import net.touchmania.game.song.InvalidTimingDataException;
import net.touchmania.game.song.OldTimingGraph;
import net.touchmania.game.song.Song;
import net.touchmania.game.song.score.ScoreKeeper;

/**
 * @author flood2d
 */
public class Match {
    private Game game;
    private Song song;
    private Chart chart;
    private Life life;
    private ScoreKeeper scoreKeeper;
    private InputRecord inputRecord;
    private Player player;
    private Music music;
    private OldTimingGraph timing;
    private MatchModifiers modifiers = new MatchModifiers();
    private ControlState controls;
    private double currentTime;
    private double currentBeat;

    public Match(Game game) {
        this.game = game;
    }

    public void prepare() throws InvalidTimingDataException {
        timing = new OldTimingGraph((float) song.offset, null, null);
        //music = game.getBackend().getComplexAudio().newMusic(song.directory.child(song.musicPath));
        inputRecord = new InputRecord();
        controls = new ControlState(this);
        controls.addListener(inputRecord);
        scoreKeeper = new ScoreKeeper(this);
    }

    public void start() {
        scoreKeeper.init();
        controls.addListener(scoreKeeper);
        music.play();
    }

    /**
     * Updates match state. Usually called at each frame.
     */
    public void update() {
        currentTime = music.getPosition();
        currentBeat = timing.getBeatAt((float) currentTime);
        scoreKeeper.update();
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public Chart getChart() {
        return chart;
    }

    public OldTimingGraph getTiming() {
        return timing;
    }

    public float getCurrentTime() {
        return (float) currentTime;
    }

    public MatchModifiers getModifiers() {
        return modifiers;
    }

    public ScoreKeeper getScoreKeeper() {
        return scoreKeeper;
    }
    /**
     * Gives information on game controls used to play.
     * @return the controls state object.
     */
    public ControlState getControls() {
        return controls;
    }

    public float getCurrentBeat() {
        return (float) currentBeat;
    }
}
