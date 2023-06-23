package com.itenebris.kinedb.jdbc.statement;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.itenebris.kinedb.jdbc.util.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itenebris.kinedb.jdbc.connection.KineConnection;
import com.itenebris.kinedb.jdbc.result.KineResultSet;
import com.itenebris.kinedb.jdbc.result.ResultData;

public class KinePrepareStatement extends KineStatement implements PreparedStatement {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final Map<Integer, String> parameters = new HashMap<>();
    private int parametersCount;
    private final String statementName;
    private final String originalSql;
    private boolean isClosed;

    //private KineResultSet resultSet;

    public KinePrepareStatement(KineConnection kineConnection, String statementName, String sql) {
        super(kineConnection);

        this.statementName = statementName;
        this.originalSql = sql;
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (c == '?') {
                this.parametersCount++;
            }
        }
        log.debug("statement:{} sql:{}", statementName, sql);

        String.format("PREPARE %s FROM %s", statementName, sql);
    }

    @Override
    public void clearParameters() throws SQLException {
        checkConnectionOpen();
        parameters.clear();
    }

    @Override
    public void close() throws SQLException {
        if (isClosed) {
            return;
        }
        String.format("DROP PREPARE %s", statementName);
        super.close();
        isClosed = true;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        if (resultSet == null) {
            return null;
        }
        return resultSet.getMetaData();
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return null;
    }

    // private void formatParametersTo(StringBuilder builder) throws SQLException {
    // List<String> values = new ArrayList<>();
    // for (int index = 0; index < parameters.size(); index++) {
    // if (!parameters.containsKey(index)) {
    // throw new SQLException("No value specified for parameter " + (index + 1));
    // }
    // values.add(parameters.get(index));
    // }
    // Joiner.on(", ").appendTo(builder, values);
    // }

    // private String getExecuteSql() throws SQLException {
    // StringBuilder sql = new StringBuilder();
    // sql.append("EXECUTE ").append(statementName);
    // if (!parameters.isEmpty()) {
    // sql.append(" USING ");
    // formatParametersTo(sql);
    // }
    // return sql.toString();
    // }

    private String prepareSql() throws SQLException {
        String sql = originalSql;
        int i = 0;
        while (sql.contains("?")) {
            i++;
            if (!parameters.containsKey(i)) {
                throw new SQLException("No value specified for parameter " + i);
            }

            int index = sql.indexOf("?");
            String before = sql.substring(0, index);
            String after = sql.substring(index + 1);
            sql = before + parameters.get(i) + after;
        }
        return sql;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        String sql = prepareSql();
        try {
            ResultData data = connection.getExecutor().executeSql(sql);
            this.resultSet = new KineResultSet(data, connection);
            return this.resultSet;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        String sql = prepareSql();
        try {
            ResultData data = connection.getExecutor().executeSql(sql);
            this.resultSet = new KineResultSet(data, connection);
            if (this.resultSet != null) {
                this.updateCount = data.size();
                return data.size();
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return 0;
    }

    @Override
    public long executeLargeUpdate() throws SQLException {
        String sql = prepareSql();
        try {
            ResultData data = connection.getExecutor().executeSql(sql);
            this.resultSet = new KineResultSet(data, connection);
            if (this.resultSet != null) {
                this.updateCount = data.size();
                return data.size();
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return 0;
    }

    @Override
    public boolean execute() throws SQLException {
        String sql = prepareSql();
        try {
            ResultData data = connection.getExecutor().executeSql(sql);
            this.resultSet = new KineResultSet(data, connection);
            return this.resultSet != null ;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        this.parameters.put(parameterIndex, null);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        parameters.put(parameterIndex, String.valueOf(x));
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        if (x == null) {
            parameters.put(parameterIndex, "NULL");
        } else {
            parameters.put(parameterIndex, "'" + x + "'");
        }
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getSqlStringOrEmpty(x));
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getSqlStringOrEmpty(x));
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getSqlStringOrEmpty(x));
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType)
            throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void addBatch() throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {

    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {

    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {

    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {

    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        this.parameters.put(parameterIndex, ConvertUtils.getStringEmpty(x));
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {

    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {

    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {

    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {

    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {

    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("setBinaryStream");
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("setCharacterStream");
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        throw new SQLFeatureNotSupportedException("setNCharacterStream");
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("setClob");
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("setBlob");
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("setNClob");
    }

}
