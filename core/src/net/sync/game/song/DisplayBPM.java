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

package net.sync.game.song;

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
 * @author Vincenzo Fortunato
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
     * @author Vincenzo Fortunato
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
     * @author Vincenzo Fortunato
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
     * @author Vincenzo Fortunato
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
     * @author Vincenzo Fortunato
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
