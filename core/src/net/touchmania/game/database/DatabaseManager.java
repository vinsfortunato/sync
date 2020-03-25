/*
 * Copyright 2020 Vincenzo Fortunato
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

package net.touchmania.game.database;

import net.touchmania.game.Game;
import org.jooq.DSLContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.jooq.impl.DSL.using;

public class DatabaseManager {
    /**
     * Creates a DSL executor with a configured connection that can be used
     * directly to create and execute statements.
     * @return a DSL executor with a configured connection.
     * @throws RuntimeException if the database cannot be opened
     */
    public DSLContext openDatabase() {
        try {
            Connection connection = DriverManager.getConnection(Game.instance().getBackend().getDatabaseUrl());
            return using(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot open the database", e);
        }
    }
}
