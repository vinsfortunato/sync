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

package net.touchmania.game.round.judge;

public class MineJudgment extends Judgment {
    private boolean exploded;

    /**
     * @param genTime the judgment's generation time relative to the start of the music track.
     */
    public MineJudgment(double genTime) {
        super(genTime);
    }

    public boolean hasExploded() {
        return exploded;
    }
}
