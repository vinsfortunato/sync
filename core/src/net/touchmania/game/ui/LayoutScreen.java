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

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.base.Preconditions;
import net.touchmania.game.Game;
import net.touchmania.game.util.concurrent.DoneListener;

public class LayoutScreen implements Screen {
    private String layoutId;
    private Layout layout;

    public LayoutScreen(String layoutId) {
        Preconditions.checkArgument(layoutId != null && !layoutId.isEmpty(), "Null or empty layout id!");
        this.layoutId = layoutId;
    }

    @Override
    public void prepare() {
        layout = getTheme().getLayout(layoutId);
        Preconditions.checkNotNull(layout, String.format("Layout with id '%s' not found!", layoutId));
    }

    @Override
    public void show(Stage stage) {

    }

    @Override
    public void hide(DoneListener listener) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isPrepared() {
        return false;
    }

    @Override
    public Theme getTheme() {
        return Game.instance().getThemes().getActiveTheme();
    }
}
