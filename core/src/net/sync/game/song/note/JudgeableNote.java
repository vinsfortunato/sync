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

import net.sync.game.round.judge.Judgment;

/**
 * A note that can be judged.
 */
public interface JudgeableNote extends Note {
    /**
     * Get the note judgment. Null will be returned if the note has not
     * been judged yet.
     * @return the note judgment, or null if the note has not been judged.
     */
    Judgment getJudgment();

    /**
     * Set the note judgment.
     * @param judgment the note judgment.
     */
    void setJudgment(Judgment judgment);

    /**
     * Check if the note has been judged.
     * @return true if the note has been judged.
     */
    default boolean hasJudgment() {
        return getJudgment() != null;
    }
}
