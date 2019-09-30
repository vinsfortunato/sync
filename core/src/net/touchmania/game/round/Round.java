package net.touchmania.game.round;

import net.touchmania.game.song.Chart;
import net.touchmania.game.song.score.ScoreKeeper;

public class Round {
    private Chart chart;
    private ScoreKeeper scoreKeeper;

    public Round(Chart chart) {
        this.chart = chart;
        this.scoreKeeper = new ScoreKeeper();
    }

    public ScoreKeeper getScoreKeeper() {
        return scoreKeeper;
    }

    public Chart getChart() {
        return chart;
    }

    public double getCurrentBeat() {
        return 0.0D;
    }
}
