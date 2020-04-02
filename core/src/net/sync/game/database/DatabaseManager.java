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

package net.sync.game.database;

import com.badlogic.gdx.Gdx;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import net.sync.game.Game;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;

import javax.sql.DataSource;

import static org.jooq.impl.DSL.using;

public class DatabaseManager {
    private static int DATABASE_VERSION = 1;

    static {
        System.setProperty("org.jooq.no-logo", "true");
    }

    /**
     * Creates and initializes the database manager. Database will be created/updated if necessary
     */
    public DatabaseManager() {
        int version = getDatabaseVersion();
        if(version == 0) {
            createDatabase();
        } else if(version != DATABASE_VERSION) {
            convertDatabase();
        }
    }

    /**
     * Creates a DSL executor with a configured connection that can be used
     * directly to create and execute statements.
     * @return a DSL executor with a configured connection.
     * @throws RuntimeException if the database cannot be opened
     */
    public DSLContext getDSL() {
        return using(getDataSource(), getSQLDialect());
    }

    /**
     * Gets the database version, a value greater or equal to 0.
     * 0 is returned if the database schema must be created.
     * @return the database version.
     */
    private int getDatabaseVersion() {
        Result<Record> result = getDSL().fetch("PRAGMA user_version");
        return result.isEmpty() ? 0 : result.get(0).get(0, Integer.class);
    }

    private void createDatabase() {
        //Get the database generation script
        String script = Gdx.files.internal("template.sql").readString(Charsets.UTF_8.name());

        //Sanitize for execution:
        //Remove comments
        script = script.replaceAll("--.*", "");

        //Execute the database generation script
        for(String sql : Splitter.on(";").trimResults().omitEmptyStrings().split(script)) {
            getDSL().execute(sql);
        }

        //Set database version
        getDSL().execute("PRAGMA user_version = " + DATABASE_VERSION);
    }

    private void convertDatabase() {
        //Convert legacy version of the database
        //TODO
    }

    private DataSource getDataSource() {
        return Game.instance().getBackend().getDatabaseDataSource();
    }

    private SQLDialect getSQLDialect() {
        return SQLDialect.SQLITE;
    }
}
