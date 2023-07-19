package com.itenebris.kinedb.jdbc.result;

import com.sun.rowset.internal.Row;

import java.sql.SQLException;
import java.util.Iterator;

public interface Rows extends Iterator<Row> {
    int RESULT_SET_SIZE_UNKNOWN = -1;

    default Row previous() throws SQLException {
        throw new SQLException("Operation not supported exception");
    }

    default Row get(int n) throws SQLException {
        throw new SQLException("Operation not supported exception");
    }

    default int getPosition() throws SQLException {
        throw new SQLException("Operation not supported exception");
    }

    default int size() {
        return -1;
    }
}
