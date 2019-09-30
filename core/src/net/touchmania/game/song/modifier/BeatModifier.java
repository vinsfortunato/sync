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

/**
 * @author flood2d
 */
public abstract class BeatModifier {
    /**
     * Modifies the given beat. It is used by speed modifiers and
     * acceleration modifiers to change the way notes are displayed.
     * @param beat the beat to modify.
     * @return the result of the modification.
     */
    public abstract float apply(float beat);

    /**
     * Revert the modification applied by {@link #apply(float)}.
     * @param beat the modified beat to revert.
     * @return the unmodified beat.
     */
    public abstract float revert(float beat);
}
