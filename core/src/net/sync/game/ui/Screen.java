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
import com.badlogic.gdx.utils.Disposable;

public interface Screen extends Disposable {
    /**
     * Prepares the screen.
     * <p>This method is executed on rendering thread so it must not block
     * and must be fast.</p>
     * <p> This method should prepare the screen by loading required resources.
     * It should not block thus resources that require some time to load must
     * be loaded asynchronously.</p>
     * <p> The provided callback should be called when preparation completes.
     * It should be called on the rendering thread. </p>
     * @param doneCallback done callback. Must be called on the rendering thread when
     * preparation completes. (e.g. all resources have been loaded, and required
     * tasks have been terminated).
     */
    void prepare(Runnable doneCallback);

    /**
     * Shows the screen.
     * <p>This method is executed on rendering thread so it must not block
     * and must be fast.</p>
     * <p> Populate the stage and starts showing animations. </p>
     * @param stage a clean stage.
     */
    void show(Stage stage);

    /**
     * Hides the screen.
     * <p> This method is executed on rendering thread so it must not block
     * and must be fast.</p>
     * <p> Hiding animations should be started here. </p>
     * <p> The provided callback should be called when hiding completes. It should
     * be called on the rendering thread.</p>
     * @param doneCallback the done callback. Must be called on the rendering thread
     * when hiding completes. (e.g. when all hiding animations completes).
     */
    void hide(Runnable doneCallback);

    /**
     * This is called at each render tick to update the screen status
     * only if it the screen is shown or preparing to be shown.
     */
    void update();

    /**
     * Returns true if the screen is prepared and required resources are ready.
     * @return true if the screen is prepared, false otherwise.
     */
    boolean isPrepared();
}
