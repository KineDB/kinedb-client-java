package com.itenebris.kinedb.jdbc.statement;

import java.sql.SQLException;

public interface JdbcStatement {

    void enableStreamingResults() throws SQLException;

    void disableStreamingResults() throws SQLException;
}
