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

import net.touchmania.game.database.Cursor;
import net.touchmania.game.database.SQLException;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;

public class DesktopCursor implements Cursor {
    private ResultSet set;

    public DesktopCursor(ResultSet set) {
        this.set = set;
    }

    @Override
    public int getCount() {
        try {
            int row = set.getRow();
            set.last();
            int count = set.getRow();
            set.absolute(row);
            return count;
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get cursor row count", e);
        }
    }

    @Override
    public int getPosition() {
        try {
            return set.getRow() - 1;
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get cursor position", e);
        }
    }

    @Override
    public boolean move(int offset) {
        try {
            return set.relative(offset);
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not move the cursor by the given offset", e);
        }
    }

    @Override
    public boolean moveToPosition(int position) {
        try {
            return set.absolute(position + 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not move the cursor to the given position", e);
        }
    }

    @Override
    public boolean moveToFirst() {
        try {
            return set.first();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not move the cursor to the first position", e);
        }
    }

    @Override
    public boolean moveToLast() {
        try {
            return set.last();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not move the cursor to the last position", e);
        }
    }

    @Override
    public boolean moveToNext() {
        try {
            return set.next();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not move the cursor to the next position", e);
        }
    }

    @Override
    public boolean moveToPrevious() {
        try {
            return set.previous();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not move the cursor to the previous position", e);
        }
    }

    @Override
    public boolean isFirst() {
        try {
            return set.isFirst();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not check if the cursor is on first position", e);
        }
    }

    @Override
    public boolean isLast() {
        try {
            return set.isLast();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not check if the cursor is on last position", e);
        }
    }

    @Override
    public boolean isBeforeFirst() {
        try {
            return set.isBeforeFirst();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not check if the cursor is before first position", e);
        }
    }

    @Override
    public boolean isAfterLast() {
        try {
            return set.isAfterLast();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not check if the cursor is after last position", e);
        }
    }

    @Override
    public int getColumnIndex(String columnName) {
        try {
            return set.findColumn(columnName) - 1;
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the column index", e);
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        try {
            return set.getMetaData().getColumnName(columnIndex + 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the column name", e);
        }
    }

    @Override
    public String[] getColumnNames() {
        try {
            ResultSetMetaData metaData = set.getMetaData();
            String[] names = new String[metaData.getColumnCount()];
            for(int i = 0; i < names.length; i++) {
                names[i] = metaData.getColumnName(i + 1);
            }
            return names;
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the column names", e);
        }
    }

    @Override
    public int getColumnCount() {
        try {
            return set.getMetaData().getColumnCount();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the column count", e);
        }
    }

    @Override
    public byte[] getBlob(int columnIndex) {
        try {
            Blob blob = set.getBlob(columnIndex + 1);
            return blob == null ? new byte[0] : blob.getBytes(0, (int) blob.length());
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the blob value", e);
        }
    }

    @Override
    public String getString(int columnIndex) {
        try {
            return set.getString(columnIndex + 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the string value", e);
        }
    }

    @Override
    public short getShort(int columnIndex) {
        try {
            return set.getShort(columnIndex + 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the short value", e);
        }
    }

    @Override
    public int getInt(int columnIndex) {
        try {
            return set.getInt(columnIndex + 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the integer value", e);
        }
    }

    @Override
    public long getLong(int columnIndex) {
        try {
            return set.getLong(columnIndex + 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the long value", e);
        }
    }

    @Override
    public float getFloat(int columnIndex) {
        try {
            return set.getFloat(columnIndex + 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the float value", e);
        }
    }

    @Override
    public double getDouble(int columnIndex) {
        try {
            return set.getDouble(columnIndex + 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the double value", e);
        }
    }

    @Override
    public boolean isNull(int columnIndex) {
        try {
            set.getObject(columnIndex + 1);
            return set.wasNull();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not check if value is null", e);
        }
    }

    @Override
    public void close() {
        try {
            set.close();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not close the cursor", e);
        }
    }

    @Override
    public boolean isClosed() {
        try {
            return set.isClosed();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not check if the cursor is closed", e);
        }
    }

    @Override
    public int getType(int columnIndex) {
        //TODO probably wrong
        try {
            int type = set.getMetaData().getColumnType(columnIndex + 1);
            switch (type){
                case Types.BINARY:
                case Types.BLOB:
                    return FIELD_TYPE_BLOB;
                case Types.VARCHAR:
                case Types.DATE:
                case Types.CLOB:
                case Types.CHAR:
                    return FIELD_TYPE_STRING;
                case Types.DECIMAL:
                case Types.DOUBLE:
                case Types.NUMERIC:
                case Types.REAL:
                case Types.FLOAT:
                    return FIELD_TYPE_FLOAT;
                case Types.INTEGER:
                case Types.BIGINT:
                case Types.SMALLINT:
                case Types.TINYINT:
                case Types.BOOLEAN:
                    return FIELD_TYPE_INTEGER;
                default:
                    return FIELD_TYPE_NULL;
            }
        } catch (java.sql.SQLException e) {
            throw new SQLException("Could not get the value type", e);
        }
    }
}
