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
