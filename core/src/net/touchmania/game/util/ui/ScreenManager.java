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

package net.touchmania.game.util.ui;

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
    public static int LOAD_EXECUTOR_ID;
    private Stage stage;
    private Theme theme;
    private Screen currentScreen;

    public ScreenManager() {
        Game.instance().getDisposer().manage(this);
        initLoadExecutor();
        initStage();
    }

    private void initLoadExecutor() {
        ExecutorManager executors = Game.instance().getExecutors();
        LOAD_EXECUTOR_ID = executors.generateId();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
        executors.putExecutor(LOAD_EXECUTOR_ID, service);
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
            //Prepare the task async then show it when preparation is done
            getLoadExecutor().submit(new PrepareTask(screen));
        }
    }

    private void showPrepared(final Screen screen) {
        if(currentScreen != null) {
            //Show the next screen after hiding the current one
            currentScreen.hide(new DoneListener() {
                @Override
                public void onDone() {
                    if(getScreenCachePolicy() == ScreenCachePolicy.DISPOSE_ON_HIDE) {
                        getLoadExecutor().submit(new DisposeTask(currentScreen));
                    }
                    stage.clear();
                    currentScreen = screen;
                    currentScreen.show(stage);
                }
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

    private ListeningExecutorService getLoadExecutor() {
        return (ListeningExecutorService) Game.instance().getExecutors().getExecutor(LOAD_EXECUTOR_ID);
    }

    public Theme getTheme() {
        return theme;
    }

    private class PrepareTask implements Runnable {
        private Screen screen;

        PrepareTask(Screen screen) {
            this.screen = screen;
        }

        @Override
        public void run() {
            screen.prepare();
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    showPrepared(screen);
                }
            });
        }
    }

    private class DisposeTask implements Runnable {
        private Screen screen;

        DisposeTask(Screen screen) {
            this.screen = screen;
        }

        @Override
        public void run() {
            screen.dispose();
        }
    }
}
