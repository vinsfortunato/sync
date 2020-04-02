/*
 * Copyright 2020 Vincenzo Fortunato
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

package net.sync.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.common.base.Preconditions;
import net.sync.game.Game;

public class ScreenManager implements Disposable {
    private Stage stage;
    private Screen currentScreen;
    private Screen nextScreen;

    public ScreenManager() {
        net.sync.game.Game.instance().getDisposer().manage(this);

        //Init stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    public void show(Screen screen) {
        Preconditions.checkNotNull(screen, "Screen cannot be null!");
        if(nextScreen != null) {
            //TODO Is still loading? Block or dispose
        }
        nextScreen = screen;

        if(nextScreen.isPrepared()) {
            //Show the already prepared screen immediately
            showNextScreen();
        } else {
            //Prepare the screen and show it when ready
            screen.prepare(this::showNextScreen);
        }
    }

    private void showNextScreen() {
        if(currentScreen != null) {
            //Show the next screen after hiding the current one
            currentScreen.hide(() -> {
                if(getScreenCachePolicy() == ScreenCachePolicy.DISPOSE_ON_HIDE)
                    currentScreen.dispose();
                stage.clear();
                currentScreen = nextScreen;
                nextScreen = null;
                currentScreen.show(stage);
            });
        } else {
            //Show the screen immediately
            stage.clear();
            currentScreen = nextScreen;
            nextScreen = null;
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
        //Update the active screens
        if(currentScreen != null) {
            currentScreen.update();
        }
        if(nextScreen != null) {
            nextScreen.update();
        }

        //Update stage
        stage.act(Gdx.graphics.getDeltaTime());

        //Render stage
        stage.draw();
    }

    private ScreenCachePolicy getScreenCachePolicy() {
        return Game.instance().getSettings().getScreenCachePolicy();
    }
}
