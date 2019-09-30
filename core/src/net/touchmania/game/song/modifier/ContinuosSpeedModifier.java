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

package net.touchmania.game.song.modifier;

import net.touchmania.game.match.Match;

/**
 * @author flood2d
 */
public class ContinuosSpeedModifier extends SpeedModifier {
    private Match match;
    private float constantBpm = 200;

    public ContinuosSpeedModifier(Match match) {
        this.match = match;
    }

    public void setConstantBpm(float constantBpm) {
        this.constantBpm = constantBpm;
    }

    public float getConstantBpm() {
        return constantBpm;
    }

    @Override
    public float apply(float beat) {
        float time = match.getCurrentTime();
        if(match.getTiming().isStop(time)) {
            float stopStart = match.getTiming().getSegmentStartTime(time);
            float stopDuration = match.getTiming().getSegmentDuration(time);
            return (constantBpm / 60.0f) * (stopStart + stopDuration - time) + beat;
        } else {
            float bpm = match.getTiming().getBpmAt(time);
            return (constantBpm / bpm) * beat;
        }
    }

    @Override
    public float revert(float beat) {
        float time = match.getCurrentTime();
        if(match.getTiming().isStop(time)) {
            float stopStart = match.getTiming().getSegmentStartTime(time);
            float stopDuration = match.getTiming().getSegmentDuration(time);
            return beat - (constantBpm / 60.0f) * (stopStart + stopDuration - time);
        } else {
            float bpm = match.getTiming().getBpmAt(time);
            return beat / (constantBpm / bpm);
        }
    }
}
