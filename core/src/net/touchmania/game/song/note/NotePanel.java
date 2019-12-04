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

package net.touchmania.game.song.note;

import net.touchmania.game.GameMode;

public class NotePanel {
    public static final int LEFT_UP = 0;
    public static final int LEFT = 1;
    public static final int LEFT_DOWN = 2;
    public static final int DOWN = 3;
    public static final int RIGHT_DOWN = 4;
    public static final int RIGHT = 5;
    public static final int RIGHT_UP = 6;
    public static final int UP = 7;
    public static final int CENTER = 8;

    public static int[] getModePanels(GameMode mode) {
        switch (mode) {
            case DANCE:
                return new int[] { LEFT, DOWN, UP, RIGHT };
            case PUMP:
                return new int[] { LEFT_DOWN, LEFT_UP, CENTER, RIGHT_UP, RIGHT_DOWN };
        }

        return new int[0];
    }
}
