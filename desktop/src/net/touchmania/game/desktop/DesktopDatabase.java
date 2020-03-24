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

import com.google.common.base.Preconditions;
import net.touchmania.game.database.Cursor;
import net.touchmania.game.database.Database;
import net.touchmania.game.database.SQLException;

import java.sql.Connection;
import java.sql.Statement;

public class DesktopDatabase implements Database {
    private DesktopDatabaseHelper helper;
    private Connection conn;
    private boolean inTransaction = false;
    private boolean transactionSuccessful = false;

    public DesktopDatabase(DesktopDatabaseHelper helper, Connection conn) {
        this.helper = helper;
        this.conn = conn;
    }

    @Override
    public void beginTransaction() {
        //TODO This should be reviewed. Nesting transaction wouldn't work as expected.
        try {
            conn.setAutoCommit(false);
            inTransaction = true;
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not begin transaction", e);
        }
    }

    @Override
    public void endTransaction() {
        Preconditions.checkState(inTransaction, "Not in transaction");
        try {
            if(transactionSuccessful) {
                conn.commit();
            } else {
                conn.rollback();
            }
            inTransaction = false;
            transactionSuccessful = false;
            conn.setAutoCommit(true);
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not end transaction", e);
        }
    }

    @Override
    public void setTransactionSuccessful() {
        Preconditions.checkState(inTransaction, "Not in transaction");
        Preconditions.checkState(!transactionSuccessful, "Transaction already set successful");
        transactionSuccessful = true;
    }

    @Override
    public boolean inTransaction() {
        return inTransaction;
    }

    @Override
    public void executeSQL(String sql) {
        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);
            statement.close();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not execute SQL update", e);
        }
    }

    @Override
    public Cursor query(String sql) {
        try {
            //TODO sqlite supports only TYPE_FORWARD_ONLY mode
            Statement statement = conn.createStatement();
            return new DesktopCursor(statement.executeQuery(sql));
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not execute SQL query", e);
        }
    }

    @Override
    public int getVersion() {
        Cursor cursor = query("PRAGMA user_version");
        if(cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1; //Unknown
    }

    @Override
    public void setVersion(int version) {
        executeSQL("PRAGMA user_version = " + version);
    }

    @Override
    public boolean isOpen() {
        try {
            return !conn.isClosed();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not check if database is open", e);
        }
    }

    @Override
    public boolean isReadOnly() {
        try {
            return conn.isReadOnly();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not check if database is read only", e);
        }
    }

    @Override
    public void close() {
        helper.close(conn);
    }
}
