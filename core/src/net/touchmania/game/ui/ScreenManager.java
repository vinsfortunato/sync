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

package net.touchmania.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.*;
import net.touchmania.game.Game;
import net.touchmania.game.util.concurrent.DoneListener;
import net.touchmania.game.util.concurrent.ExecutorManager;

import java.util.concurrent.Executors;

public class ScreenManager implements Disposable {
    private Stage stage;
    private Screen currentScreen;

    public ScreenManager() {
        Game.instance().getDisposer().manage(this);
        initStage();
    }

    private void initStage() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    public void show(Screen screen) {
        Preconditions.checkNotNull(screen, "Screen cannot be null!");
        if(screen.isPrepared()) {
            //Show the already prepared screen immediately
            showPrepared(screen);
        } else {
            //Prepare the screen and show it when ready
            screen.prepare(() -> Gdx.app.postRunnable(() -> showPrepared(screen)));
        }
    }

    private void showPrepared(final Screen screen) {
        if(currentScreen != null) {
            //Show the next screen after hiding the current one
            currentScreen.hide(() -> {
                screen.dispose();
                stage.clear();
                currentScreen = screen;
                currentScreen.show(stage);
            });
        } else {
            //Show the screen immediately
            stage.clear();
            currentScreen = screen;
            currentScreen.show(stage);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private ScreenCachePolicy getScreenCachePolicy() {
        return Game.instance().getSettings().getScreenCachePolicy();
    }
}
