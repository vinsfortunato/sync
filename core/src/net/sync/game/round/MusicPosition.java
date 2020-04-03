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

package net.sync.game.round;

import com.badlogic.gdx.audio.Music;

/**
 * Wraps a {@link Music} and provides a more accurate position by
 * syncing {@link System#nanoTime()} to {@link Music#getPosition()}.
 */
public class MusicPosition {
    private Music music;

    /* Bind music position to system nano time */
    private double lastMusicPos;
    private long lastNanoTime;

    /**
     * Construct a position provider.
     * @param music the music.
     */
    public MusicPosition(Music music) {
        this.music = music;
    }

    /**
     * Get accurate current music position in seconds.
     * @return the accurate music position in seconds.
     */
    public double getPosition() {
        return getPositionAt(System.nanoTime());
    }

    /**
     * Get the music position at the given nano time in seconds.
     * @param nanoTime the system nano time.
     * @return the music position at the given nano time in seconds.
     */
    public double getPositionAt(long nanoTime) {
        long nanoDelta = nanoTime - lastNanoTime;
        if(nanoDelta < 0) {
            return lastMusicPos;
        } else {
            return lastMusicPos + (double) (nanoDelta / 1_000_000) / 1000D;
        }
    }

    /**
     * Get the wrapped music being synced.
     * @return the wrapped music.
     */
    public Music getMusic() {
        return music;
    }

    /**
     * Must be called to sync nano time to music position.
     */
    public void update() {
        double position = getPosition();
        double musicPos = music.getPosition();

        //Check if difference is greater than 20ms
        if(Math.abs(musicPos - position) > 0.020) {
            //Sync nano time with music position
            lastMusicPos = music.getPosition();
            lastNanoTime = System.nanoTime();
        }
    }
}
