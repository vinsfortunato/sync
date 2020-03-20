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

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import net.touchmania.game.database.Database;
import net.touchmania.game.database.DatabaseHelper;

public class AndroidDatabaseHelper extends DatabaseHelper {
    private static final String DATABASE_NAME = "touchmania_db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteOpenHelper sqliteOpenHelper;

    public AndroidDatabaseHelper(Context context) {
        this.sqliteOpenHelper = new AndroidSQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public String getDatabaseName() {
        return sqliteOpenHelper.getDatabaseName();
    }

    @Override
    public Database getReadableDatabase() {
        return new AndroidDatabase(sqliteOpenHelper.getReadableDatabase());
    }

    @Override
    public Database getWritableDatabase() {
        return new AndroidDatabase(sqliteOpenHelper.getWritableDatabase());
    }

    @Override
    public void close() {
        sqliteOpenHelper.close();
    }

    private class AndroidSQLiteOpenHelper extends SQLiteOpenHelper {

        public AndroidSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public AndroidSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            AndroidDatabaseHelper.this.onCreate(new AndroidDatabase(db));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            AndroidDatabaseHelper.this.onUpgrade(new AndroidDatabase(db), oldVersion, newVersion);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            AndroidDatabaseHelper.this.onDowngrade(new AndroidDatabase(db), oldVersion, newVersion);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            AndroidDatabaseHelper.this.onOpen(new AndroidDatabase(db));
        }
    }
 }
