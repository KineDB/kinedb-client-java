package com.itenebris.kinedb.jdbc.statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Iterator;

import com.itenebris.kinedb.jdbc.exceptions.SqlError;
import com.itenebris.kinedb.jdbc.executor.ExecutorParams;
import com.itenebris.kinedb.jdbc.executor.Kine;
import com.itenebris.kinedb.jdbc.result.StreamingResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itenebris.kinedb.jdbc.connection.KineConnection;
import com.itenebris.kinedb.jdbc.result.StaticResultSet;
import com.itenebris.kinedb.jdbc.result.ResultData;

public class KineStatement implements Statement, JdbcStatement {

	Logger log = LoggerFactory.getLogger(this.getClass());
	protected int maxFieldSize;
	public int maxRows;
	protected boolean escapeProcessing = true;
	protected int queryTimeoutInMillis;

	protected KineConnection connection;
	protected StaticResultSet resultSet;

	protected int updateCount;

	// now only support forward only
	private Resultset.Type resultSetType;
	// use streaming query need this value > 0
	public int fetchSize;
	public String currentDatabase;

	public String engine;
	private boolean enableStreamingResults;

	public KineStatement(KineConnection kineConnection) {
		this.connection = kineConnection;
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		try {
			ExecutorParams executorParams = new ExecutorParams(this.fetchSize, this.currentDatabase, this.engine);
			if (checkEnableStreamingResults()) {
				Iterator<Kine.Results> resultsIterator = connection.getExecutor().streamExecuteSql(sql, executorParams);
				StreamingResultSet resultSet = new StreamingResultSet(resultsIterator);
				return resultSet;
			}
			long start = System.nanoTime();
			ResultData data = connection.getExecutor().executeSql(sql, executorParams);
			StaticResultSet staticResultSet = new StaticResultSet(data, connection);
			staticResultSet.setStatement(this);
			this.resultSet = staticResultSet;
			long end = System.nanoTime();
			long consume = (end - start) / 1000000;
			log.info("KineStatement.executeQuery consume [{}]", consume);
			return staticResultSet;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		try {
			ExecutorParams executorParams = new ExecutorParams(this.fetchSize, this.currentDatabase, this.engine);
			ResultData data = connection.getExecutor().executeSql(sql, executorParams);
			this.resultSet = new StaticResultSet(data, connection);
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
			//long start = System.nanoTime();
			ExecutorParams executorParams = new ExecutorParams(this.fetchSize, this.currentDatabase, this.engine);
			connection.getExecutor().executeSql(s, executorParams);
//			long end = System.nanoTime();
//			long consume = (end - start) / 1000000;
//			log.info("KineStatement.execute consume [{}]", consume);
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
		Resultset.Type type = Resultset.Type.fromValue(i, Resultset.Type.FORWARD_ONLY);
		this.resultSetType = type;
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return this.resultSetType.getIntValue();
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		if ((rows >= 0 || rows == Integer.MIN_VALUE) && (this.maxRows <= 0 || rows <= this.getMaxRows())) {
			this.fetchSize = rows;
		} else {
			throw new SQLException("fetchSize must be less than " + this.maxRows);
		}
	}

	@Override
	public int getFetchSize() throws SQLException {
		return this.fetchSize;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		return Resultset.Type.FORWARD_ONLY.getIntValue();
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

	public boolean checkEnableStreamingResults() {
		if (this.enableStreamingResults) {
			return true;
		}
		if (this.fetchSize > 0 && this.resultSetType == Resultset.Type.FORWARD_ONLY) {
			return true;
		}
		return false;
	}

	@Override
	public void enableStreamingResults() throws SQLException {
		this.setFetchSize(Integer.MIN_VALUE);
		// we not consider resultSet type now
		this.setResultType(Resultset.Type.FORWARD_ONLY);
		this.enableStreamingResults = true;
	}

	@Override
	public void disableStreamingResults() throws SQLException {
		this.enableStreamingResults = false;
	}

	public int getResultFetchSize() {
		return this.fetchSize;
	}

	public void setResultFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public Resultset.Type getResultType() {
		return this.resultSetType;
	}

	public void setResultType(Resultset.Type resultSetType) {
		this.resultSetType = resultSetType;
	}

	public String getCurrentDatabase() {
		return this.currentDatabase;
	}

	public void setCurrentDatabase(String currentDatabase) {
		this.currentDatabase = currentDatabase;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}


}
