package com.itenebris.kinedb.jdbc.exceptions;

import java.sql.SQLException;

public class SqlError {

    public static SQLException newSQLException(String message) {
        SQLException sqlEx = new SQLException(message);
        return sqlEx;
    }

    public static SQLException newSQLException(String message,  Throwable cause) {
        SQLException sqlEx = newSQLException(message);
        if (sqlEx.getCause() == null && cause != null) {
                sqlEx.initCause(cause);
        }
    return sqlEx;
    }
}
