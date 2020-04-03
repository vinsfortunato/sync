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

package net.sync.game.ui.widgets;

public class ScrollPane extends com.badlogic.gdx.scenes.scene2d.ui.ScrollPane {
    private boolean overscrollX = true;
    private boolean overscrollY = true;
    private boolean hScrollOnBottom = true;
    private boolean vScrollOnRight = true;
    private float fadeAlphaSeconds = 1f;
    private float fadeDelaySeconds = 1f;
    private float overscrollDistance = 50f;
    private float overscrollSpeedMin = 30f;
    private float overscrollSpeedMax = 200f;

    public ScrollPane() {
        super(null);
    }

    @Override
    public void setOverscroll(boolean overscrollX, boolean overscrollY) {
        super.setOverscroll(overscrollX, overscrollY);
        this.overscrollX = overscrollX;
        this.overscrollY = overscrollY;
    }

    @Override
    public void setScrollBarPositions(boolean bottom, boolean right) {
        super.setScrollBarPositions(bottom, right);
        this.hScrollOnBottom = bottom;
        this.vScrollOnRight = right;
    }

    @Override
    public void setupFadeScrollBars(float fadeAlphaSeconds, float fadeDelaySeconds) {
        super.setupFadeScrollBars(fadeAlphaSeconds, fadeDelaySeconds);
        this.fadeAlphaSeconds = fadeAlphaSeconds;
        this.fadeDelaySeconds = fadeDelaySeconds;
    }

    @Override
    public void setupOverscroll(float distance, float speedMin, float speedMax) {
        super.setupOverscroll(distance, speedMin, speedMax);
        this.overscrollDistance = distance;
        this.overscrollSpeedMin = speedMin;
        this.overscrollSpeedMax = speedMax;
    }

    public boolean isOverscrollX() {
        return overscrollX;
    }

    public boolean isOverscrollY() {
        return overscrollY;
    }

    public boolean isVerticalScrollBarOnRight() {
        return vScrollOnRight;
    }

    public boolean isHorizontalScrollBarOnBottom() {
        return hScrollOnBottom;
    }

    public float getFadeAlphaSeconds() {
        return fadeAlphaSeconds;
    }

    public float getFadeDelaySeconds() {
        return fadeDelaySeconds;
    }

    public float getOverScrollDistance() {
        return overscrollDistance;
    }

    public float getOverScrollSpeedMin() {
        return overscrollSpeedMin;
    }

    public float getOverScrollSpeedMax() {
        return overscrollSpeedMax;
    }
}
