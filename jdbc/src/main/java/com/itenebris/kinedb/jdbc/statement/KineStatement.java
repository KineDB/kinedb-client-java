package com.itenebris.kinedb.jdbc.statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import com.itenebris.kinedb.jdbc.exceptions.SqlError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itenebris.kinedb.jdbc.connection.KineConnection;
import com.itenebris.kinedb.jdbc.result.KineResultSet;
import com.itenebris.kinedb.jdbc.result.ResultData;

public class KineStatement implements Statement {

	Logger log = LoggerFactory.getLogger(this.getClass());
	protected int maxFieldSize;
	public int maxRows;
	protected boolean escapeProcessing = true;
	protected int queryTimeoutInMillis;

	protected KineConnection connection;
	protected KineResultSet resultSet;

	protected int updateCount;

	public KineStatement(KineConnection kineConnection) {
		this.connection = kineConnection;
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		try {
			ResultData data = connection.getExecutor().executeSql(sql);

			KineResultSet kineResultSet = new KineResultSet(data, connection);
			kineResultSet.setStatement(this);
			this.resultSet = kineResultSet;
			return kineResultSet;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		try {
			ResultData data = connection.getExecutor().executeSql(sql);
			this.resultSet = new KineResultSet(data, connection);
			this.resultSet.setStatement(this);
			if (this.resultSet != null) {
				this.updateCount = data.getAffectLine();
				return this.updateCount;
			}
			return 0;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public void close() throws SQLException {
		try {
			connection.close();
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		return this.maxFieldSize;
	}

	@Override
	public void setMaxFieldSize(int maxFieldSize) throws SQLException {
		if (maxFieldSize <= 0) {
			throw SqlError.newSQLException("MaxFieldSize must be greater than 0");
		}
		this.maxFieldSize = maxFieldSize;
	}

	@Override
	public int getMaxRows() throws SQLException {
		return this.maxRows <= 0 ? 0 : this.maxRows;
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		if ( max >= 0L && max <= 50000000L) {
			if (max == 0L) {
				max = -1;
			}
			this.maxRows = max;
		} else {
			throw SqlError.newSQLException(" max rows cannot be > " + 50000000);
		}
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		this.escapeProcessing = enable;
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return this.queryTimeoutInMillis / 1000;
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		if (seconds < 0) {
			throw SqlError.newSQLException(" query timeout must be > " + 0);
		} else {
			this.queryTimeoutInMillis = seconds * 1000;
		}
	}

	@Override
	public void cancel() throws SQLException {

	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {

	}

	@Override
	public void setCursorName(String s) throws SQLException {

	}

	@Override
	public boolean execute(String s) throws SQLException {
		try {
			connection.getExecutor().executeSql(s);
			return true;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return this.resultSet;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return this.updateCount;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		boolean hasMoreResult = false;
		if (this.resultSet != null && !this.resultSet.isClosed()) {
			hasMoreResult = this.resultSet.hasResult();
			if (hasMoreResult) {
				this.resultSet.close();
			}
		}

		return hasMoreResult;
	}

	@Override
	public void setFetchDirection(int i) throws SQLException {

	}

	@Override
	public int getFetchDirection() throws SQLException {
		return 0;
	}

	@Override
	public void setFetchSize(int i) throws SQLException {

	}

	@Override
	public int getFetchSize() throws SQLException {
		return 0;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		return 0;
	}

	@Override
	public void addBatch(String s) throws SQLException {

	}

	@Override
	public void clearBatch() throws SQLException {

	}

	@Override
	public int[] executeBatch() throws SQLException {
		return new int[0];
	}

	protected final void checkConnectionOpen() throws SQLException {
		connection();
	}

	private KineConnection connection() throws SQLException {
		if (this.connection == null) {
			throw new SQLException("Statement is closed");
		}
		if (this.connection.isClosed()) {
			throw new SQLException("Connection is closed");
		}
		return this.connection;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return connection();
	}

	@Override
	public boolean getMoreResults(int i) throws SQLException {
		boolean hasMoreResult = false;
		if (this.resultSet != null && !this.resultSet.isClosed()) {
			hasMoreResult = this.resultSet.hasResult();
			if (hasMoreResult) {
				this.resultSet.close();
			}
		}

		return hasMoreResult;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return null;
	}

	@Override
	public int executeUpdate(String s, int i) throws SQLException {
		return 0;
	}

	@Override
	public int executeUpdate(String s, int[] ints) throws SQLException {
		return 0;
	}

	@Override
	public int executeUpdate(String s, String[] strings) throws SQLException {
		return 0;
	}

	@Override
	public boolean execute(String s, int i) throws SQLException {
		return false;
	}

	@Override
	public boolean execute(String s, int[] ints) throws SQLException {
		return false;
	}

	@Override
	public boolean execute(String s, String[] strings) throws SQLException {
		return false;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return false;
	}

	@Override
	public void setPoolable(boolean b) throws SQLException {

	}

	@Override
	public boolean isPoolable() throws SQLException {
		return false;
	}

	@Override
	public void closeOnCompletion() throws SQLException {

	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> aClass) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> aClass) throws SQLException {
		return false;
	}
}
