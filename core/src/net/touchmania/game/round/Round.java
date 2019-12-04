package net.touchmania.game.round;

import com.badlogic.gdx.audio.Music;
import net.touchmania.game.round.judge.Judge;
import net.touchmania.game.round.judge.JudgeCriteria;
import net.touchmania.game.song.Chart;
import net.touchmania.game.song.Timing;

public class Round {
    private Chart chart;
    private Timing timing;
    private Score score;
    private Life life;
    private Music music;
    private Judge judge;
    private Controls controls;

    public Round(Chart chart) {
        this.chart = chart;
        this.timing = new Timing(chart.song.timingData);
        this.judge = new Judge(this, new JudgeCriteria());
        this.controls = new Controls(this);
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

    public Judge getJudge() {
        return judge;
    }

    public Controls getControls() {
        return controls;
    }

    public void update() {
        judge.update(getCurrentTime());
    }
}
