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

package net.touchmania.game;

import net.touchmania.game.database.DatabaseHelper;
import net.touchmania.game.database.Database;
import net.touchmania.game.database.Cursor;

/**
 * @author flood2d
 */
public interface Backend {
    /**
     * Returns a helper interface to manage game's database creation/opening and version management.
     * <p>
     * Each backend must provide platform specific implementations of the following interfaces:
     * <ul>
     *     <li>{@link DatabaseHelper}</li>
     *     <li>{@link Database}</li>
     *     <li>{@link Cursor}</li>
     * </ul>
     * </p>
     * @return a database helper.
     */
    DatabaseHelper getDatabaseHelper();
}
