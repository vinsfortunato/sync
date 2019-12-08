/*
 * Copyright 2019 Vincenzo Fortunato
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

import com.badlogic.gdx.audio.Music;

/**
 * Wraps a {@link Music} and provides a more accurate position by
 * syncing {@link System#nanoTime()} to {@link Music#getPosition()}.
 * <p> An offset value in seconds can be provided to offset the music track.</p>
 */
public class MusicPosition {
    private Music music;
    private double offset;

    /* Bind music position to system nano time */
    private double lastMusicPos;
    private long lastNanoTime;

    /**
     * Construct a position provider.
     * @param music the music.
     * @param offset the music offset value in seconds.
     */
    public MusicPosition(Music music, double offset) {
        this.music = music;
        this.offset = offset;
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
     * @return the music position offset by the specified value.
     */
    private double getMusicPosition() {
        return music.getPosition() + offset;
    }

    /**
     * Must be called to sync nano time to music position.
     */
    public void update() {
        double position = getPosition();
        double musicPos = getMusicPosition();

        //Check if difference is greater than 20ms
        if(Math.abs(musicPos - position) > 0.020) {
            //Sync nano time with music position
            lastMusicPos = getMusicPosition();
            lastNanoTime = System.nanoTime();
        }
    }
}
