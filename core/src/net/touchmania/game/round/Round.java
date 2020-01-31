package net.touchmania.game.round;

import com.badlogic.gdx.audio.Music;
import net.touchmania.game.round.judge.Judge;
import net.touchmania.game.round.judge.JudgeCriteria;
import net.touchmania.game.round.modifier.Modifiers;
import net.touchmania.game.round.modifier.MultiplySpeedModifier;
import net.touchmania.game.song.Chart;
import net.touchmania.game.song.Song;
import net.touchmania.game.song.Timing;

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
        this.timing = new Timing(chart.song.timingData);
        this.modifiers = new Modifiers();
        this.panelState = new PanelState();
        this.judge = new Judge( this, new JudgeCriteria());
        this.panelState.addListener(this.judge);

        setDefaultMods();
    }

    private void setDefaultMods() {
        //Init speed modifier

        //MaxSpeedModifier speedMod = new MaxSpeedModifier(timing.getDominantBpm(music.));
        modifiers.setSpeedModifier(new MultiplySpeedModifier(2.5f));
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
