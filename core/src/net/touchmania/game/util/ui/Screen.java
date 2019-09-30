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

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import net.touchmania.game.util.concurrent.DoneListener;

public interface Screen extends Disposable {
    /**
     * Prepares the screen resources and performs required
     * time expensive computations. This method is executed
     * on a separate thread.
     * <p>
     *     {@link #isPrepared()} must return:
     *     <ul>
     *         <li><code>false</code> during method computation </li>
     *         <li><code>true</code> after method computation </li>
     *     </ul>
     * </p>
     */
    void prepare();

    /**
     * Populate the stage and starts showing animations.
     * This method is executed on rendering thread so it must not block
     * and must be fast.
     * @param stage a clean stage.
     */
    void show(Stage stage);

    /**
     * Starts hiding the screen. Hiding animations should be started here.
     * This method is executed on rendering thread so it must not block
     * and must be fast. The given listener must be notified when
     * the screen hiding process is complete.
     *
     * @param listener the listener to notify when hiding process is complete.
     */
    void hide(DoneListener listener);

    /**
     * Disposes the screen resources. This methods is executed
     * on a separate thread.
     * <p>
     *     {@link #isPrepared()} must return false during and
     *     immediately after method computation.
     * </p>
     */
    @Override
    void dispose();

    /**
     * Returns true if {@link #prepare()} terminated its computation and
     * {@link #dispose()} has not been called yet.
     *
     * @return true if the screen is prepared.
     */
    boolean isPrepared();

    /**
     * Gets the screen theme.
     *
     * @return the screen theme
     */
    Theme getTheme();
}
