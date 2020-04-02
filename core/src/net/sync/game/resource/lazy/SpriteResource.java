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

package net.sync.game.resource.lazy;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

//TODO
public class SpriteResource extends net.sync.game.resource.lazy.DrawableResource {
    /**
     * Copy constructor
     *
     * @param resource
     */
    public SpriteResource(net.sync.game.resource.lazy.DrawableResource resource) {
        super(resource);
    }

    @Override
    public DrawableResource copy() {
        return null;
    }

    @Override
    public Drawable get() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void load() {

    }
}
