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

package net.touchmania.game.util;

/**
 * Represent a logic criteria. Used to check if a given object
 * follows the criteria. It is equivalent to a logic predicate.
 * @param <T> the type of the value the criteria expects.
 */
public interface Criteria<T> {
    /**
     * Check the given value against the criteria.
     * @param value the value to check.
     * @return true if the given value follows the criteria, false otherwise.
     */
    boolean check(T value);
}
