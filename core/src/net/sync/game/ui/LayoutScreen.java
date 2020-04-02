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

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.base.Preconditions;
import net.sync.game.Game;
import net.sync.game.resource.Layout;
import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.lazy.Resource;

public class LayoutScreen implements Screen {
    private String layoutId;
    private Resource<Layout> layout;

    public LayoutScreen(String layoutId) {
        Preconditions.checkArgument(layoutId != null && !layoutId.isEmpty(), "Null or empty layout id!");
        this.layoutId = layoutId;
    }

    @Override
    public void prepare(Runnable doneCallback) {
        ResourceProvider resources = Game.instance().getResources();
        layout = resources.getLayout(layoutId);
        Preconditions.checkNotNull(layout, String.format("Layout with id '%s' not found!", layoutId));
        doneCallback.run();
    }

    @Override
    public void show(Stage stage) {
        //TODO
    }

    @Override
    public void hide(Runnable doneCallback) {
        //TODO
    }

    @Override
    public void update() {}

    @Override
    public void dispose() {
        //TODO
    }

    @Override
    public boolean isPrepared() {
        return false;
    }
}
