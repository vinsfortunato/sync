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

package net.sync.game.song.note;

import net.sync.game.GameMode;

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
