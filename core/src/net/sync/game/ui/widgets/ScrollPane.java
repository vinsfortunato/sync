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
