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

package net.touchmania.game.song;

import java.util.Random;

/**
 * This can be used to override the BPM shown in the song selection screen.
 * <p>
 *  Usable implementations:
 *  <ul>
 *      <li> {@link StaticDisplayBPM} </li>
 *      <li> {@link RangeDisplayBPM}  </li>
 *      <li> {@link RandomDisplayBPM} </li>
 *  </ul>
 * </p>
 *
 * @author flood2d
 */
public interface DisplayBPM {
    /**
     * Must be called at each render tick.
     *
     * @return the bpm to show into the song selection screen.
     * Can change during time. How the returned value changes is
     * defined by the implementation.
     */
    int getBpmToShow();

    /**
     * This can be used to show a static BPM.
     *
     * @author flood2d
     */
    class StaticDisplayBPM implements DisplayBPM {

        private final int value;
        /**
         * @param value bpm value.
         */
        public StaticDisplayBPM(int value) {
            this.value = value;
        }

        @Override
        public int getBpmToShow() {
            return value;
        }
    }

    /**
     * This can be used to show a BPM that randomly changes.
     *
     * @author flood2d
     */
    class RandomDisplayBPM extends TransitionDisplayBPM {

        /** Min value returned by {@link #getBpmToShow()} **/
        private static final int MIN = 0;

        /** Max value returned by {@link #getBpmToShow()} **/
        private static final int MAX = 999;

        private Random random = new Random();
        private int nextBPM;

        public RandomDisplayBPM() {
            setCurrentBPM(generateRandomBPM());
            nextBPM = generateRandomBPM();
        }

        @Override
        protected int getNextBPM() {
            return nextBPM;
        }

        @Override
        protected void onTransitionEnd() {
            nextBPM = generateRandomBPM();
        }

        private int generateRandomBPM() {
            return MIN + random.nextInt(MAX - MIN + 1);
        }
    }

    /**
     * This can be used to show a BPM that changes between a defined range.
     *
     * @author flood2d
     */
    class RangeDisplayBPM extends TransitionDisplayBPM {

        private final int min;
        private final int max;
        private boolean nextIsMax = true;

        /**
         * @param min range left bound (minimum BPM value).
         * @param max range right bound (maximum BPM value).
         */
        public RangeDisplayBPM(int min, int max) {
            this.min = min;
            this.max = max;
            setCurrentBPM(min);
        }

        @Override
        protected int getNextBPM() {
            return nextIsMax ? max : min;
        }

        @Override
        protected void onTransitionEnd() {
            nextIsMax = !nextIsMax;
        }
    }

    /**
     * @author flood2d
     */
    abstract class TransitionDisplayBPM implements DisplayBPM {
        /** Transition velocity, measured in bpm unit/second **/
        private static final int VELOCITY = 400;

        /** Cooldown between transitions in milliseconds **/
        private static final long COOLDOWN = 1500;

        private long previousUpdateTime = Long.MIN_VALUE;
        private boolean cooldown = false;
        private int currentBPM = 0;

        @Override
        public int getBpmToShow() {
            if(previousUpdateTime == Long.MIN_VALUE) {
                previousUpdateTime = System.currentTimeMillis();
            }

            if(!cooldown) {
                int increment = (int) ((System.currentTimeMillis() - previousUpdateTime) * VELOCITY / 1000.0F);

                if(currentBPM <= getNextBPM()) {
                    currentBPM += increment;

                    if(currentBPM >= getNextBPM()) {
                        currentBPM = getNextBPM();
                        cooldown = true;
                        onTransitionEnd();
                    }
                } else {
                    currentBPM -= increment;

                    if(currentBPM <= getNextBPM()) {
                        currentBPM = getNextBPM();
                        cooldown = true;
                        onTransitionEnd();
                    }
                }

                previousUpdateTime = System.currentTimeMillis();
            } else if(System.currentTimeMillis() - previousUpdateTime >= COOLDOWN) {
                cooldown = false;
                previousUpdateTime = System.currentTimeMillis();
            }

            return currentBPM;
        }

        /**
         * @param currentBPM the bpm to set as current.
         */
        protected void setCurrentBPM(int currentBPM) {
            this.currentBPM = currentBPM;
        }
        /**
         * @return the bpm value to reach.
         */
        protected abstract int getNextBPM();

        /**
         * Called when current bpm value reaches the value of {@link #getNextBPM()}.
         */
        protected abstract void onTransitionEnd();
    }
}
