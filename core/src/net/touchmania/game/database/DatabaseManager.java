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
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import static net.touchmania.game.database.schema.DefaultSchema.DEFAULT_SCHEMA;
import static org.jooq.impl.DSL.using;

public class DatabaseManager {
    static {
        System.setProperty("org.jooq.no-logo", "true");
    }

    /**
     * Creates and initializes the database manager. Database will be created/updated if necessary
     */
    public DatabaseManager() {
        //Create the database if there are no tables
        //Note: there must be a smarter way to do this, but for now it is ok
        if(getDSL().meta().getTables().isEmpty()) {
            createDatabase();
        }
    }

    /**
     * Creates a DSL executor with a configured connection that can be used
     * directly to create and execute statements.
     * @return a DSL executor with a configured connection.
     * @throws RuntimeException if the database cannot be opened
     */
    public DSLContext getDSL() {
        return using(
                Game.instance().getBackend().getDatabaseDataSource(),
                Game.instance().getBackend().getDatabaseSQLDialect());
    }

    /**
     * Execute the database generation script generated from the schema.
     */
    private void createDatabase() {
        SQLDialect dialect = Game.instance().getBackend().getDatabaseSQLDialect();

        //Generate the creation script from the schema and execute each query
        for(Query query : DSL.using(dialect).ddl(DEFAULT_SCHEMA).queries()) {
            getDSL().execute(query);
        }
    }

    private void updateDatabase() {
        //TODO
    }
}
