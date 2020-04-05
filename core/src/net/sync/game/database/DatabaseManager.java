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

package net.sync.game.database;

import com.badlogic.gdx.Gdx;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;

import javax.sql.DataSource;

import static net.sync.game.Game.backend;
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
        return backend().getDatabaseDataSource();
    }

    private SQLDialect getSQLDialect() {
        return SQLDialect.SQLITE;
    }
}
