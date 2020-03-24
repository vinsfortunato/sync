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

package net.touchmania.game.database;

import java.io.Closeable;

/**
 * This interface has been copied from android.database.sqlite.SQLiteOpenHelper
 * class of the <a href="https://www.android.com/">Android</a> framework.
 * Unnecessary features have been removed.
 * <p>
 * A helper interface to manage database creation and version management.
 * </p><p>
 * You create a subclass implementing {@link #onCreate}, {@link #onUpgrade} and
 * optionally {@link #onOpen}, and this class takes care of opening the database
 * if it exists, creating it if it does not, and upgrading it as necessary.
 * </p><p class="note">
 * <strong>Note:</strong> this class assumes
 * monotonically increasing version numbers for upgrades.</p>
 */
public abstract class DatabaseHelper implements Closeable {

    /**
     * Return the name of the database being opened.
     */
    public abstract String getDatabaseName();

    /**
     * Create and/or open a database.  This will be the same object returned by
     * {@link #getWritableDatabase} unless some problem, such as a full disk,
     * requires the database to be opened read-only.  In that case, a read-only
     * database object will be returned.  If the problem is fixed, a future call
     * to {@link #getWritableDatabase} may succeed, in which case the read-only
     * database object will be closed and the read/write object will be returned
     * in the future.
     *
     * @throws SQLException if the database cannot be opened
     * @return a database object valid until {@link #getWritableDatabase}
     *     or {@link #close} is called.
     */
    public abstract Database getReadableDatabase();

    /**
     * Create and/or open a database that will be used for reading and writing.
     * The first time this is called, the database will be opened and
     * {@link #onCreate}, {@link #onUpgrade} and/or {@link #onOpen} will be
     * called.
     *
     * <p>Once opened successfully, the database is cached, so you can
     * call this method every time you need to write to the database.
     * (Make sure to call {@link #close} when you no longer need the database.)
     * Errors such as bad permissions or a full disk may cause this method
     * to fail, but future attempts may succeed if the problem is fixed.</p>
     *
     * @throws SQLException if the database cannot be opened for writing
     * @return a read/write database object valid until {@link #close} is called
     */
    public abstract Database getWritableDatabase();

    /**
     * Called when the database has been opened.  The implementation
     * should check {@link Database#isReadOnly} before updating the
     * database.
     * <p>
     * This method is called after the database connection has been configured
     * and after the database schema has been created, upgraded or downgraded as necessary.
     * </p>
     *
     * @param db The database.
     */
    public void onOpen(Database db) {}

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    public void onCreate(Database db) {
        db.executeSQL("CREATE TABLE IF NOT EXISTS songs (" +
                            "pack TEXT NOT NULL," +
                            "directory TEXT NOT NULL," +
                            "hash TEXT NOT NULL," +
                            "PRIMARY KEY (pack, directory));");
    }

    /**
     * Called when the database needs to be downgraded. This is strictly similar to
     * {@link #onUpgrade} method, but is called whenever current version is newer than requested one.
     *
     * <p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    public void onDowngrade(Database db, int oldVersion, int newVersion) {}

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    public void onUpgrade(Database db, int oldVersion, int newVersion) {}
}
