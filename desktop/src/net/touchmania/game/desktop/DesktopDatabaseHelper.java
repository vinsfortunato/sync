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

package net.touchmania.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import net.touchmania.game.database.Database;
import net.touchmania.game.database.DatabaseHelper;
import net.touchmania.game.database.SQLException;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Set;

public class DesktopDatabaseHelper extends DatabaseHelper {
    private static final String DATABASE_NAME = "touchmania_db";
    private static final int DATABASE_VERSION = 1;
    private FileHandle databaseFile;
    private Set<Connection> openConnections = new HashSet<>();

    public DesktopDatabaseHelper() {
        databaseFile = Gdx.files.external(DATABASE_NAME + ".sqlite"); //TODO

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not initialize database helper");
        }
    }

    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Override
    public Database getReadableDatabase() {
        return openDatabase(true);
    }

    @Override
    public Database getWritableDatabase() {
        return openDatabase(false);
    }

    private DesktopDatabase openDatabase(boolean readOnly) {
        boolean exist = getDatabaseFile().exists();

        try {
            Connection conn = DriverManager.getConnection(getConnectionUrl());
            DesktopDatabase db = new DesktopDatabase(this, conn);

            //Create or check for updates
            if (!exist) {
                //Create database schema
                onCreate(db);
            } else {
                //Check database version and upgrade/downgrade if necessary
                updateDatabase(db);
            }

            if(readOnly) {
                //Reopen connection in read only mode
                conn.close();
                SQLiteConfig config = new SQLiteConfig();
                config.setReadOnly(true);
                conn = DriverManager.getConnection(getConnectionUrl(), config.toProperties());
                db = new DesktopDatabase(this, conn);
            }

            openConnections.add(conn); //Track open connection
            onOpen(db);
            return db;
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not open database", e);
        }
    }

    private void updateDatabase(DesktopDatabase db) {
        int version = db.getVersion();
        if(version == DATABASE_VERSION) return;

        //Should upgrade/downgrade
        db.beginTransaction();
        try {
            if(version < DATABASE_VERSION) {
                onUpgrade(db, version, DATABASE_VERSION);
            } else {
                onDowngrade(db, version, DATABASE_VERSION);
            }
            db.setTransactionSuccessful();
        } catch(Throwable e) {
            throw new SQLException("Could not update database", e);
        } finally {
            db.endTransaction();
        }
    }

    private String getConnectionUrl() {
        return "jdbc:sqlite:" + getDatabaseFile().path();
    }

    private FileHandle getDatabaseFile() {
        return databaseFile;
    }

    protected void close(Connection conn) {
        try {
            openConnections.remove(conn); //Remove from open connections
            conn.close();
        } catch(java.sql.SQLException e) {
            Gdx.app.error("Database", "Could not close a database connection", e);
        }
    }

    @Override
    public void close() {
        for(Connection conn : openConnections) {
            close(conn);
        }
    }
}
