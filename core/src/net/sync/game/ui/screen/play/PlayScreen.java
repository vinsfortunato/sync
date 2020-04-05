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

package net.sync.game.ui.screen.play;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.sync.game.Game;
import net.sync.game.round.Round;
import net.sync.game.song.Chart;
import net.sync.game.song.ChartType;
import net.sync.game.song.Song;
import net.sync.game.song.SongLoader;
import net.sync.game.song.sim.SimParser;
import net.sync.game.ui.Screen;

import static net.sync.game.Game.disposer;
import static net.sync.game.Game.resources;

/**
 * @author Vincenzo Fortunato
 */
public class PlayScreen implements Screen {
    private static PlayScreen instance;

    /* True if the screen is still preparing */
    private boolean preparing = false;
    /* True if the screen is prepared and ready to be shown */
    private boolean prepared = false;
    /* The preparation done callback */
    private Runnable prepareDoneCallback;

    private int resGroup;

    /* Widgets */
    private BeatmapView beatmapView;
    private ControlsView controlsView;
    private JudgmentView judgmentView;

    private Music music;
    private Round round;

    private PlayScreen() {
        disposer().manage(this);
    }

    @Override
    public void prepare(Runnable doneCallback) {
        preparing = true;
        prepareDoneCallback = doneCallback;
        resGroup = resources().startGroup();

        test();
    }

    //TODO
    private void test() {
        FileHandle fh;
        String testSong = "E:/Program Files/StepMania 5.1/Songs/ITG Rodeo Tournament 8/012 - Grayed Out -Antifront-";
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            fh = Gdx.files.absolute(Game.INPUT_PATH == null ? "E:/Program Files/StepMania 5.1/Songs/ITG Rodeo Tournament 8/013 - Come & Get It" : Game.INPUT_PATH);
        } else {
            fh = Gdx.files.external(Game.instance().tempFile);
        }

        SongLoader sl = new SongLoader("Test", fh);

        try {
            sl.run();
            Song song = sl.get();
            Chart chart = null;

            for(Chart c : song.charts) {
                if(c.type == ChartType.DANCE_SINGLE) {
                    chart = c;
                }
            }
            SimParser parser = song.simFile.getFormat().newParser();
            parser.init(song.simFile);
            chart.beatmap = parser.getChartParser(chart.hash).parseBeatmap();
            FileHandle musicFile;
            if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
                musicFile = Gdx.files.absolute(song.directory.path() + "/" + song.musicPath);
            } else {
                musicFile = Gdx.files.external(song.directory.path() + "/" + song.musicPath);
            }
            music = Gdx.audio.newMusic(musicFile);

            round = new Round(song, chart, music);

            //Init actors
            controlsView = new ControlsView(round);
            beatmapView = new BeatmapView(round);
            judgmentView = new JudgmentView(round);
        } catch (Exception e) {
            System.err.println("Cannot read song!");
            e.printStackTrace();
        }
    }

    @Override
    public void show(Stage stage) {
        boolean desktop = Gdx.app.getType() == Application.ApplicationType.Desktop;
        stage.getActors().clear();
        Stack stack = new Stack();
        stack.setFillParent(true);
        stage.addActor(stack);

        Table controlsTable = new Table();
        controlsTable.add(controlsView)
                .padLeft(0)
                .padTop(0)
                .width(desktop ? 1920 : 1080)
                .height(desktop ? 1080 : 1920);
        controlsTable.left().top();

        Table beatmapTable = new Table();
        beatmapTable.setFillParent(true);
        beatmapTable.add(beatmapView)
                .padLeft(0)
                .padTop(0)
                .width(desktop ? 1920 : 1080)
                .height(desktop ? 1080 : 1920);
        beatmapTable.left().top();

        Table judgmentsTable = new Table();
        judgmentsTable.setFillParent(true);
        judgmentsTable.add(judgmentView)
                .padLeft(0)
                .padTop(0)
                .width(desktop ? 1920 : 1080)
                .height(desktop ? 1080 : 1920);
        judgmentsTable.left().top();

        stack.add(beatmapTable);
        stack.add(judgmentView);
        stack.add(controlsTable);

        stage.setKeyboardFocus(controlsView);
        music.play();
    }

    @Override
    public void hide(Runnable doneCallback) {
        doneCallback.run();
    }

    @Override
    public void update() {
        if(preparing) {
            //Check and update preparation status
            if(!resources().isGroupLoading(resGroup)) {
                preparing = false;
                prepared = true;
                prepareDoneCallback.run();
            }
        } else {
            round.update();
        }
    }

    @Override
    public void dispose() {
        if(music != null) {
            music.dispose();
        }

        resources().endGroup(resGroup);
        prepared = false;
    }

    @Override
    public boolean isPrepared() {
        return prepared;
    }

    public static PlayScreen instance() {
        return instance == null ? instance = new PlayScreen() : instance;
    }
}
