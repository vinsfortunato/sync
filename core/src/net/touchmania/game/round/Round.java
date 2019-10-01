package net.touchmania.game.round;

import net.touchmania.game.song.Chart;
import net.touchmania.game.song.InvalidTimingDataException;
import net.touchmania.game.song.Timing;

public class Round {
    private Chart chart;
    private Timing timing;
    private Score score;
    private Life life;

    public Round(Chart chart) {
        this.chart = chart;
        this.timing = new Timing(chart.song.timingData);
    }

    public Chart getChart() {
        return chart;
    }

    public double getCurrentTime() {
        return 0.0D;
    }

    public Timing getTiming() {
        return timing;
    }
}
