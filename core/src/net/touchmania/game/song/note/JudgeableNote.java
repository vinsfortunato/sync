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

import net.touchmania.game.round.judge.Judgment;

/**
 * A note that can be judged.
 */
public interface JudgeableNote extends Note {
    /**
     * Get the note judgment. Null will be returned if the note has not
     * been judged yet.
     * @return the note judgment, or null if the not has not been judged.
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
