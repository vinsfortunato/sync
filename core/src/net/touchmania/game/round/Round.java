package net.touchmania.game.round;

import com.badlogic.gdx.audio.Music;
import net.touchmania.game.song.Chart;
import net.touchmania.game.song.InvalidTimingDataException;
import net.touchmania.game.song.Timing;

public class Round {
    private Chart chart;
    private Timing timing;
    private Score score;
    private Life life;
    private Music music;

    public Round(Chart chart) {
        this.chart = chart;
        this.timing = new Timing(chart.song.timingData);
    }

    public Chart getChart() {
        return chart;
    }

    public double getCurrentTime() {
        return music.getPosition();
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public Timing getTiming() {
        return timing;
    }
}
