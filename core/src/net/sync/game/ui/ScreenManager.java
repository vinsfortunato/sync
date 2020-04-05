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

package net.sync.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.common.base.Preconditions;

import static net.sync.game.Game.disposer;
import static net.sync.game.Game.settings;

public class ScreenManager implements Disposable {
    private Stage stage;
    private Screen currentScreen;
    private Screen nextScreen;

    public ScreenManager() {
        disposer().manage(this);

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
        return settings().getScreenCachePolicy();
    }
}
