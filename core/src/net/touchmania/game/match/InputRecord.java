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

package net.touchmania.game.match;

import com.badlogic.gdx.utils.Array;
import net.touchmania.game.song.NoteColumn;

/**
 * Contains match control input history. It's used to replay a played match.
 * @author flood2d
 */
public class InputRecord implements ControlStateListener {
    private Array<Input> inputs = new Array<>();

    public void recordPressInput(float time, NoteColumn noteColumn) {
        recordInput(time, noteColumn, true);
    }

    public void recordReleaseInput(float time, NoteColumn noteColumn) {
        recordInput(time, noteColumn, false);
    }

    private void recordInput(float time, NoteColumn noteColumn, boolean isPress) {
        Input input = new Input(time, noteColumn, isPress);
        Input last = inputs.size > 0 ? inputs.peek() : null;
        inputs.add(input);
        if(last != null) {
            if(input.compareTo(last) < 0) {
                inputs.sort();
            }
        }
    }

    @Override
    public void onControlPressed(NoteColumn noteColumn, float inputTime) {
        recordPressInput(inputTime, noteColumn);
    }

    @Override
    public void onControlReleased(NoteColumn noteColumn, float inputTime) {
        recordReleaseInput(inputTime, noteColumn);
    }

    public class Input implements Comparable<Input>{
        private final float time;
        private final NoteColumn noteColumn;
        private final boolean pressInput;

        private Input(float time, NoteColumn noteColumn, boolean pressInput) {
            this.time = time;
            this.noteColumn = noteColumn;
            this.pressInput = pressInput;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null) return false;
            if(obj == this) return true;
            if(obj instanceof Input) {
                Input input = (Input) obj;
                return Float.compare(time, input.time) == 0 &&
                       noteColumn == input.noteColumn &&
                       pressInput == input.pressInput;
            }
            return false;
        }

        public float getTime() {
            return time;
        }

        public NoteColumn getNoteColumn() {
            return noteColumn;
        }

        public boolean isPress() {
            return pressInput;
        }

        public boolean isRelease() {
            return !pressInput;
        }

        @Override
        public int compareTo(Input o) {
            return Float.compare(time, o.time);
        }
    }
}
