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

import net.sync.game.round.judge.TailJudgment;

public interface JudgeableLengthyNote extends LengthyNote, JudgeableNote {
    /**
     * Gets the tail judgment. Null will be returned if the
     * tail has not been judged yet.
     * @return the tail judgment or null if the tail has not been judged yet.
     */
    TailJudgment getTailJudgment();

    /**
     * Sets the tail judgment.
     * @param judgment the tail judgment to set.
     */
    void setTailJudgment(TailJudgment judgment);

    /**
     * Checks if the note tail has been judged.
     * @return true if the note tail has been judged.
     */
    default boolean hasTailJudgment() {
        return getTailJudgment() != null;
    }
}
