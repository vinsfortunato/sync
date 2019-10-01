package net.touchmania.game.round;

import net.touchmania.game.song.Chart;

public class Round {
    private Chart chart;
    private Judge judge;

    public Round(Chart chart) {
        this.chart = chart;
        this.judge = new Judge();
    }

    public Judge getJudge() {
        return judge;
    }

    public Chart getChart() {
        return chart;
    }

    public double getCurrentBeat() {
        return 0.0D;
    }
}
