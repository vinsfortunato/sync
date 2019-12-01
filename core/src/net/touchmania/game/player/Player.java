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

package net.touchmania.game.player;

public interface Player {
    /**
     * @return player nickname.
     */
    String getName();

    /**
     * @return player total score.
     */
    long getTotalScore();

    /**
     * Gets player accuracy. 1.0 is the maximum accuracy, 0.0 is
     * the minimum accuracy.
     * @return player accuracy, a value between 1.0 and 0.0.
     */
    float getAccuracy();

    /**
     * @return play count.
     */
    int getPlayCount();

    /**
     * @return play time in seconds.
     */
    long getPlayTime();

    /**
     * @return the maximum combo achieved by the player.
     */
    int getMaximumCombo();

    /**
     * @return player level.
     */
    int getLevel();

    /**
     * @return an array containing the id of the unlocked achievements.
     */
    int[] getUnlockedAchievements();
}
