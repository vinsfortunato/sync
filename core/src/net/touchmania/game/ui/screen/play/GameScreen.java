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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import net.touchmania.game.Game;
import net.touchmania.game.resource.ResourceProvider;
import net.touchmania.game.round.Round;
import net.touchmania.game.song.*;
import net.touchmania.game.song.note.NotePanel;
import net.touchmania.game.song.note.TapNote;
import net.touchmania.game.song.sim.SimParser;
import net.touchmania.game.util.concurrent.DoneListener;
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
        FileHandle fh = Gdx.files.external("touchmania/Songs/ITG Rodeo Tournament 8/010 - Holic");

        SongLoader sl = new SongLoader(fh);

        try {
            Song song = sl.call();
            Chart chart = null;
            for(Chart c : song.charts) {
                if(c.type == ChartType.DANCE_SINGLE) {
                    chart = c;
                }
            }

            round = new Round(chart);
            SimParser parser = song.simFormat.newParser();
            parser.init(Files.toString(song.simFile.file(), Charsets.UTF_8));
            chart.beatmap = parser.parseBeatmap(chart);

            beatmapView = new BeatmapView(round);
            music = Gdx.audio.newMusic(Gdx.files.external(song.directory.path() + "/" + song.musicPath));
            round.setMusic(music);
        } catch (Exception e) {
            System.err.println("Cannot read song!");
            e.printStackTrace();
        }
    }

    @Override
    public void show(Stage stage) {
        stage.getActors().clear();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true); //TODO

        table.add(beatmapView)
                .padLeft(0)
                .padTop(0)
                .width(1080)
                .height(1920);

        table.left().top();

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
