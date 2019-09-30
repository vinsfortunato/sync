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

package net.touchmania.game.util.ui.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.touchmania.game.Game;
import net.touchmania.game.util.concurrent.DoneListener;
import net.touchmania.game.util.ui.Screen;
import net.touchmania.game.util.ui.Theme;

public class TestScreen implements Screen {
    private static TestScreen instance;
    private boolean prepared = false;

    private TestScreen() {
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
        table.setDebug(true);

        Actor actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.end();

                ShapeRenderer renderer = new ShapeRenderer();
                renderer.setProjectionMatrix(batch.getProjectionMatrix());
                renderer.setTransformMatrix(batch.getTransformMatrix());
                renderer.translate(getX(), getY(), 0);

                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(Color.RED);
                renderer.rect(0, 0, getWidth(), getHeight());
                renderer.end();

                batch.begin();
            }
        };

        table.add(actor)
                .padLeft(10)
                .padTop(10)
                .width(100)
                .height(100);

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
        return null;
    }

    public static TestScreen instance() {
        return instance == null ? instance = new TestScreen() : instance;
    }
}
