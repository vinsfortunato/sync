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
    private PanelState panelState;
    private long nanoStartTime = 0;

    public Round(Chart chart) {
        this.chart = chart;
        this.timing = new Timing(chart.song.timingData);
        this.panelState = new PanelState();
        this.judge = new Judge(this, new JudgeCriteria());
        this.panelState.addListener(this.judge);
    }

    public Chart getChart() {
        return chart;
    }

    public double getCurrentTime() {
        if(nanoStartTime == 0) {
            return music.getPosition();
        } else {
            long timeMillis = (System.nanoTime() - nanoStartTime) / 1_000_000;
            return ((double) timeMillis) / 1000D;
        }
    }

    public long getNanoStartTime() {
         return nanoStartTime;
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


    public PanelState getPanelState() {
        return panelState;
    }

    public void update() {
        judge.update(getCurrentTime());
        if(nanoStartTime == 0 && music.getPosition() > 0) {
            long musicPosMillis = (long) (music.getPosition() * 1000D);
            nanoStartTime = System.nanoTime() - musicPosMillis * 1_000_000;
            //System.out.println();
            //System.out.println("START AT " + musicPosMillis);
        } else if(nanoStartTime != 0) {
            long musicPosMillis = ((long) (music.getPosition() * 1000D));
            long calcPosNanos = System.nanoTime() - nanoStartTime;
            //System.out.println(musicPosMillis + " " + calcPosNanos);
        }
    }
}
