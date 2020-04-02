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

public abstract class DrawableResource implements Resource<Drawable> {
    public float leftWidth;
    public float rightWidth;
    public float topHeight;
    public float bottomHeight;

    /**
     * Copy constructor
     * @param resource
     */
    public DrawableResource(DrawableResource resource) {
        leftWidth = resource.leftWidth;
        rightWidth = resource.rightWidth;
        topHeight = resource.topHeight;
        bottomHeight = resource.bottomHeight;
    }

    public DrawableResource() {}

    public abstract DrawableResource copy();
}
