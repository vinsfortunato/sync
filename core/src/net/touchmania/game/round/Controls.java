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

import com.badlogic.gdx.utils.IntMap;

public class Controls {
    private IntMap<ControlState> controlState = new IntMap<>();

    private Round round;

    public Controls(Round round) {
        this.round = round;
    }

    public void setPressed(int panel, boolean pressed, double time) {
        if(isPressed(panel) != pressed) {
            ControlState state =  pressed ? ControlState.PRESSED : ControlState.RELEASED;
            controlState.put(panel, state);
            ControlStateChangeEvent event = new ControlStateChangeEvent(panel, state, time);
            round.getJudge().handleEvent(event);
        }
    }

    public boolean isPressed(int panel) {
        return controlState.get(panel) != null && controlState.get(panel) == ControlState.PRESSED;
    }
}
