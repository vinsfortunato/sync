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

package net.touchmania.game.ui.play;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.touchmania.game.Game;
import net.touchmania.game.util.concurrent.DoneListener;
import net.touchmania.game.util.ui.Screen;
import net.touchmania.game.util.ui.Theme;

/**
 * @author flood2d
 */
public class GameScreen implements Screen {
    private static GameScreen instance;
    private boolean prepared = false;

    /* Widgets */
    private BeatmapView beatmapView;

    private GameScreen() {
        Game.instance().getDisposer().manage(this);
    }

    @Override
    public void prepare() {
        prepared = true;
    }

    @Override
    public void show(Stage stage) {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true); //TODO

        beatmapView = new BeatmapView();

        table.add(beatmapView)
                .padLeft(0)
                .padTop(0)
                .width(1080)
                .height(1920);

        table.left().top();
    }

    @Override
    public void hide(DoneListener listener) {

    }

    @Override
    public void dispose() {
        prepared = false;
    }

    @Override
    public boolean isPrepared() {
        return prepared;
    }

    @Override
    public Theme getTheme() {
        return Game.instance().getThemes().getActiveTheme();
    }

    public static GameScreen instance() {
        return instance == null ? instance = new GameScreen() : instance;
    }
}
