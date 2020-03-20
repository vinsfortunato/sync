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

package net.touchmania.game.android;

import android.database.sqlite.SQLiteDatabase;
import net.touchmania.game.database.Cursor;
import net.touchmania.game.database.Database;

public class AndroidDatabase implements Database {
    private SQLiteDatabase database;

    public AndroidDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public void beginTransaction() {
        database.beginTransaction();
    }

    @Override
    public void endTransaction() {
        database.endTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    @Override
    public boolean inTransaction() {
        return database.inTransaction();
    }

    @Override
    public void executeSQL(String sql) {
        database.execSQL(sql);
    }

    @Override
    public Cursor query(String sql) {
        return new AndroidCursor(database.rawQuery(sql, new String[0]));
    }

    @Override
    public int getVersion() {
        return database.getVersion();
    }

    @Override
    public void setVersion(int version) {
        database.setVersion(version);
    }

    @Override
    public boolean isOpen() {
        return database.isOpen();
    }

    @Override
    public boolean isReadOnly() {
        return database.isReadOnly();
    }

    @Override
    public void close() {
        database.close();
    }
}
