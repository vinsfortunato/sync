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

import com.google.common.collect.ComparisonChain;

import javax.annotation.Nonnull;

public interface Note extends Comparable<Note> {
    /**
     * Gets the note beat.
     * @return the note beat.
     */
    double getBeat();

    @Override
    default int compareTo(@Nonnull Note o) {
        return ComparisonChain
                .start()
                .compare(getBeat(), o.getBeat())
                .result();
    }
}
