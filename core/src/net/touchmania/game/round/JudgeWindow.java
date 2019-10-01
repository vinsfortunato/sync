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

package net.touchmania.game.round;

/**
 * @author flood2d
 */
public class JudgeWindow {
    private float marvelousWindow = 0.0225f; //22.5ms
    private float perfectWindow = 0.045f;    //45ms
    private float greatWindow = 0.090f;      //90ms
    private float goodWindow = 0.135f;       //135ms
    private float booWindow = 0.180f;        //180ms
    private float holdRecover = 0.250f;      //250ms

    /**
     * Calculates judgment of a tap note. Time is relative to
     * the note time. If time is outside all the timing window
     * returns {@link Judgment#MISS}.
     * @param time relative to the note time.
     * @return the judgment.
     */
    public Judgment getJudgment(float time) {
        if(-marvelousWindow <= time && time <= marvelousWindow) {
            return Judgment.MARVELOUS;
        } else if(-perfectWindow <= time && time <= perfectWindow) {
            return Judgment.PERFECT;
        } else if(-greatWindow <= time && time <= greatWindow) {
            return Judgment.GREAT;
        } else if(-goodWindow <= time && time <= goodWindow) {
            return Judgment.GOOD;
        } else if(-booWindow <= time && time <= booWindow) {
            return Judgment.BOO;
        }
        return Judgment.MISS;
    }

    public boolean isMissed(float time) {
        return getJudgment(time) == Judgment.MISS;
    }

    public float getMarvelousWindow() {
        return marvelousWindow;
    }

    public float getPerfectWindow() {
        return perfectWindow;
    }

    public float getGreatWindow() {
        return greatWindow;
    }

    public float getGoodWindow() {
        return goodWindow;
    }

    public float getBooWindow() {
        return booWindow;
    }

    public float getHoldRecover() {
        return holdRecover;
    }

    /**
     * Checks if an hold can be recovered.
     * @param timeDelay time delay in seconds between current time and
     *                  last input time.
     * @return true if the hold can be recovered, false otherwise.
     */
    public boolean canRecoverHold(float timeDelay) {
        return timeDelay < holdRecover;
    }
}
