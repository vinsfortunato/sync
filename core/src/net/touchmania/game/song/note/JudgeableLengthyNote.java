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

package net.touchmania.game.song.note;

import net.touchmania.game.round.judge.TailJudgment;

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
