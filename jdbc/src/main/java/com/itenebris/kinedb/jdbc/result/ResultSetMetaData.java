package com.itenebris.kinedb.jdbc.result;

import com.itenebris.kinedb.jdbc.connection.KineConnection;
import com.itenebris.kinedb.jdbc.exceptions.SqlError;

import java.sql.SQLException;

public class ResultSetMetaData implements java.sql.ResultSetMetaData {

    private KineConnection connection;
    private Field[] fields;

    public ResultSetMetaData(KineConnection connection, Field[] fields) {
        this.connection = connection;
        this.fields = fields;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return this.fields == null ? 0 : this.fields.length;
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        return false;
    }

    @Override
    public int isNullable(int column) throws SQLException {
        return 0;
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        return false;
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        return 0;
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        return this.getColumnName(column);
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return this.getField(column).getColumnName();
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        return null;
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        try {
            Field f = this.getField(column);
//            switch (f.getKineType()) {
//                case TINYBLOB:
//                case BLOB:
//                case MEDIUMBLOB:
//                case LONGBLOB:
                    return clampedGetLength(f);
//                default:
//                    return f.getKineType().isDecimal() ? clampedGetLength(f) : clampedGetLength(f) / this.session.getServerSession().getCharsetSettings().getMaxBytesPerChar(f.getCollationIndex(), f.getEncoding());
//            }
        } catch (Exception e) {
            throw SqlError.newSQLException("getPrecision error column " + column, e);
        }
    }

    @Override
    public int getScale(int column) throws SQLException {
        return 0;
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return null;
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        return null;
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        try {
            Field f = this.getField(column);
            return f.getKineTypeId();
        } catch (Exception e) {
            throw SqlError.newSQLException("getColumnType error column " + column, e);
        }
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        try {
            Field f = this.getField(column);
            return f.getKineType().getName();
        } catch (Exception e) {
            throw SqlError.newSQLException("getColumnTypeName error column " + column, e);
        }
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        return !this.isReadOnly(column);
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        return this.isWritable(column);
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        try {
            Field f = this.getField(column);
            return f.getKineType().getClassName();
        } catch (Exception e) {
            throw SqlError.newSQLException("getColumnClassName error column " + column, e);
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return iface.cast(this);
        } catch (Exception e) {
            throw SqlError.newSQLException("unwrap error iface " + iface, e);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        try {
            return iface.isInstance(this);
        } catch (Exception e) {
            throw SqlError.newSQLException("isWrapperFor error iface " + iface, e);
        }
    }

    protected Field getField(int columnIndex) throws SQLException {
        if (this.fields == null) {
            return null;
        }
        if (columnIndex >= 1 && columnIndex <= this.fields.length) {
            return this.fields[columnIndex - 1];
        } else {
            throw SqlError.newSQLException("ResultSetMetaData.getField out of bounds columnIndex " + columnIndex);
        }
    }

    private static int clampedGetLength(Field f) {
        long fieldLength = f.getLength();
        if (fieldLength > 2147483647L) {
            fieldLength = 2147483647L;
        }

        return (int)fieldLength;
    }
}
