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

package net.touchmania.game.ui.screen.play;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import net.touchmania.game.Game;
import net.touchmania.game.round.Round;
import net.touchmania.game.song.Chart;
import net.touchmania.game.song.ChartType;
import net.touchmania.game.song.Song;
import net.touchmania.game.song.SongLoader;
import net.touchmania.game.song.sim.SimParser;
import net.touchmania.game.ui.Screen;

/**
 * @author flood2d
 */
public class GameScreen implements Screen {
    private static GameScreen instance;

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

    private GameScreen() {
        Game.instance().getDisposer().manage(this);
    }

    @Override
    public void prepare(Runnable doneCallback) {
        preparing = true;
        prepareDoneCallback = doneCallback;
        resGroup = Game.instance().getResources().startGroup();

        test();
    }

    //TODO
    private void test() {
        FileHandle fh;
        String testSong = "E:/Program Files/StepMania 5.1/Songs/ITG Rodeo Tournament 8/012 - Grayed Out -Antifront-";
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {

            fh = Gdx.files.absolute("E:/Program Files/StepMania 5.1/Songs/Helblinde/Grief & Malice - [Zaia]");
        } else {
            fh = Gdx.files.external("E:/Program Files/StepMania 5.1/Songs/Helblinde/Grief & Malice - [Zaia]");
        }

        SongLoader sl = new SongLoader(fh);

        try {
            Song song = sl.call();
            Chart chart = null;
            for(Chart c : song.charts) {
                if(c.type == ChartType.DANCE_SINGLE && c.difficultyMeter == 15) {
                    chart = c;
                }
            }
            SimParser parser = song.simFormat.newParser();
            parser.init(Files.toString(song.simFile.file(), Charsets.UTF_8));
            chart.beatmap = parser.parseBeatmap(chart);
            chart.beatmap.tempClear();
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
        stack.setDebug(true); //TODO

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
            if(!Game.instance().getResources().isGroupLoading(resGroup)) {
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

        Game.instance().getResources().endGroup(resGroup);
        prepared = false;
    }

    @Override
    public boolean isPrepared() {
        return prepared;
    }

    public static GameScreen instance() {
        return instance == null ? instance = new GameScreen() : instance;
    }
}
