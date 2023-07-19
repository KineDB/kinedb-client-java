package com.itenebris.kinedb.jdbc.result;

import com.itenebris.kinedb.jdbc.KineType;
import com.itenebris.kinedb.jdbc.connection.KineConnection;
import com.itenebris.kinedb.jdbc.exceptions.SqlError;
import com.itenebris.kinedb.jdbc.statement.KineStatement;
import com.itenebris.kinedb.jdbc.util.ConvertUtils;
import com.itenebris.kinedb.jdbc.util.DateUtils;
import com.itenebris.kinedb.jdbc.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;

public abstract class AbstractResultSet implements ResultSet{

    Logger log = LoggerFactory.getLogger(this.getClass());
    private int fetchDirection = ResultSet.FETCH_UNKNOWN;
    protected int fetchSize; // Current fetch size (might be 0).
    private final int resultsettype = ResultSet.TYPE_FORWARD_ONLY;
    private final int resultsetconcurrency = ResultSet.CONCUR_READ_ONLY;
    DateTimeFormatter defaultDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter defaultTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    DateTimeFormatter defaultTimestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    KineConnection connection;
    private KineStatement statement;
    protected ResultData data;

    protected Object[] thisRow = null;

    protected int currentRow;

    protected boolean closed;

    @Override
    public void close() throws SQLException {
        checkClosed();
        this.closed = true;
    }

    @Override
    public boolean wasNull() throws SQLException {
        return this.data == null || this.data.rowValues == null;
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        try {
            log.debug("get string columnIndex: " + columnIndex);
            checkColumnBounds(columnIndex);
            Object[] row = data.rowValues[currentRow];
            if (row != null && row.length >= columnIndex) {
                Object v = row[columnIndex - 1];
                return v == null ? null : v.toString();
            }
            return null;
        } catch (Exception e) {
            log.error("getString error ", e);
            throw new SQLException("getString error with columnIndex: " + columnIndex, e);
        }
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        try {
            Boolean res = this.getObject(columnIndex, Boolean.TYPE);
            return res == null ? false : res;
        } catch (Exception e) {
            throw SqlError.newSQLException("getBoolean error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        try {
            Byte res = this.getObject(columnIndex, Byte.TYPE);
            return res == null ? 0 : res;
        } catch (Exception e) {
            throw SqlError.newSQLException("getByte error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        try {
            Short res = this.getObject(columnIndex, Short.TYPE);
            return res == null ? 0 : res;
        } catch (Exception e) {
            throw SqlError.newSQLException("getShort error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        try {
            Object res = this.getObject(columnIndex, Integer.TYPE);
            if (res == null) {
                return 0;
            }
            return ConvertUtils.getInteger(res);
        } catch (Exception e) {
            throw SqlError.newSQLException("getInt error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        try {
            Object res = this.getObject(columnIndex, Long.TYPE);
            if (res == null) {
                return 0;
            }
            return ConvertUtils.getLong(res);
        } catch (Exception e) {
            throw SqlError.newSQLException("getLong error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        try {
            Float res = this.getObject(columnIndex, Float.TYPE);
            return res == null ? 0 : res;
        } catch (Exception e) {
            throw SqlError.newSQLException("getFloat error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        try {
            Double res = this.getObject(columnIndex, Double.TYPE);
            return res == null ? 0 : res;
        } catch (Exception e) {
            throw SqlError.newSQLException("getDouble error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        try {
            String res = this.getString(columnIndex);
            if (res != null) {
                return new BigDecimal(res);
            }
            return null;
        } catch (Exception e) {
            throw SqlError.newSQLException("getBigDecimal error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        try {
            Object res = this.getObject(columnIndex);
            return res == null ? null : res.toString().getBytes();
        } catch (Exception e) {
            throw SqlError.newSQLException("getBytes error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        try {
            Integer res = this.getInt(columnIndex);
            if (res == null || res == 0) {
                return null;
            }
            long nowTimeLong=new Long(res).longValue()*1000;

            Date date = new Date(nowTimeLong);
            return date;
        } catch (Exception e) {
            throw SqlError.newSQLException("getDate error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        try {
            Long res = this.getLong(columnIndex);
            if (res == null || res == 0) {
                return null;
            }
            Time time = new Time(res);
            return time;
        } catch (Exception e) {
            throw SqlError.newSQLException("getTime error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        try {
            Long res = this.getLong(columnIndex);
            if (res == null || res == 0) {
                return null;
            }
            Timestamp timestamp = new Timestamp(res);
            return timestamp;
        } catch (Exception e) {
            throw SqlError.newSQLException("getTimestamp error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public String getString(String columnName) throws SQLException {
        log.debug("get string columnName: " + columnName);
        try {
            return this.getString(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getString with colum name :" + columnName, e);
        }
    }

    @Override
    public boolean getBoolean(String columnName) throws SQLException {
        try {
            return this.getBoolean(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getBoolean with colum name :" + columnName, e);
        }
    }

    @Override
    public byte getByte(String columnName) throws SQLException {
        try {
            return this.getByte(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getByte with colum name :" + columnName, e);
        }
    }

    @Override
    public short getShort(String columnName) throws SQLException {
        try {
            return this.getShort(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getShort with colum name :" + columnName, e);
        }
    }

    @Override
    public int getInt(String columnName) throws SQLException {
        try {
            return this.getInt(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getInt with colum name :" + columnName, e);
        }
    }

    @Override
    public long getLong(String columnName) throws SQLException {
        try {
            return this.getLong(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getLong with colum name :" + columnName, e);
        }
    }

    @Override
    public float getFloat(String columnName) throws SQLException {
        try {
            return this.getFloat(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getFloat with colum name :" + columnName, e);
        }
    }

    @Override
    public double getDouble(String columnName) throws SQLException {
        try {
            return this.getDouble(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getDouble with colum name :" + columnName, e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        try {
            return this.getBigDecimal(this.findColumn(columnName), scale);
        } catch (Exception e) {
            throw new SQLException("BigDecimal with colum name :" + columnName, e);
        }
    }

    @Override
    public byte[] getBytes(String columnName) throws SQLException {
        try {
            return this.getBytes(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getBytes with colum name :" + columnName, e);
        }
    }

    @Override
    public Date getDate(String columnName) throws SQLException {
        try {
            return this.getDate(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getDate with colum name :" + columnName, e);
        }
    }

    @Override
    public Time getTime(String columnName) throws SQLException {
        try {
            return this.getTime(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getTime with colum name :" + columnName, e);
        }
    }

    @Override
    public Timestamp getTimestamp(String columnName) throws SQLException {
        try {
            return this.getTimestamp(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getTimestamp with colum name :" + columnName, e);
        }
    }

    @Override
    public InputStream getAsciiStream(String columnName) throws SQLException {
        return null;
    }

    @Override
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        return null;
    }

    @Override
    public InputStream getBinaryStream(String columnName) throws SQLException {
        return null;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public String getCursorName() throws SQLException {
        return null;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return new ResultSetMetaData(connection, this.data.fields);
    }

    public Object getObject(int columnIndex) throws SQLException {
        try {
            this.checkColumnBounds(columnIndex);
            Object column = this.thisRow[columnIndex - 1];
            if (column == null) {
                return null;
            } else {
                KineType columnType = this.data.kineTypes[columnIndex - 1];
                switch (columnType) {
                    case BIT:
//						if (!field.isBinary() && !field.isBlob()) {
//							return field.isSingleBit() ? this.getBoolean(columnIndex) : this.getBytes(columnIndex);
//						} else {
                        byte[] data = this.getBytes(columnIndex);
//							if (!(Boolean)this.connection.getPropertySet().getBooleanProperty(PropertyKey.autoDeserialize).getValue()) {
//								return data;
//							} else {
                        Object obj = data;
                        if (data != null && data.length >= 2) {
                            if (data[0] != -84 || data[1] != -19) {
                                return this.getString(columnIndex);
                            }

                            try {
                                ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
                                ObjectInputStream objIn = new ObjectInputStream(bytesIn);
                                obj = objIn.readObject();
                                objIn.close();
                                bytesIn.close();
                            } catch (ClassNotFoundException e) {
                                throw SqlError.newSQLException("ResultSet.Class_not_found ", e);
                            } catch (IOException var14) {
                                obj = data;
                            }
                        }

                        return obj;
//							}
//						}
                    case BOOLEAN:
                        return this.getBoolean(columnIndex);
                    case TINYINT:
                        return Integer.valueOf(this.getByte(columnIndex));
                    case TINYINT_UNSIGNED:
                    case SMALLINT:
                    case SMALLINT_UNSIGNED:
                    case MEDIUMINT:
                    case MEDIUMINT_UNSIGNED:
                    case INTEGER:
                        return this.getInt(columnIndex);
                    case INT_UNSIGNED:
                    case BIGINT:
                    case BIGINT_UNSIGNED:
                        return this.getLong(columnIndex);
                    case DECIMAL:
                    case DECIMAL_UNSIGNED:
                        String stringVal = this.getString(columnIndex);
                        if (stringVal != null) {
                            if (stringVal.length() == 0) {
                                return new BigDecimal(0);
                            }

                            try {
                                return new BigDecimal(stringVal);
                            } catch (NumberFormatException e) {
                                throw SqlError.newSQLException("ResultSet.Bad_format_for_BigDecimal" + new Object[]{stringVal, columnIndex}, e);
                            }
                        }
                        return null;
                    case FLOAT:
                    case FLOAT_UNSIGNED:
                        return new Float(this.getFloat(columnIndex));
                    case DOUBLE:
                    case DOUBLE_UNSIGNED:
                        return new Double(this.getDouble(columnIndex));
                    case CHAR:
                    case ENUM:
                    case SET:
                    case VARCHAR:
                    case TINYTEXT:
                        return this.getString(columnIndex);
                    case TEXT:
                    case MEDIUMTEXT:
                    case LONGTEXT:
                    case JSON:
                        return this.getStringForClob(columnIndex);
                    case GEOMETRY:
                        return this.getBytes(columnIndex);
                    case BINARY:
                    case VARBINARY:
                    case TINYBLOB:
                    case MEDIUMBLOB:
                    case LONGBLOB:
                    case BLOB:
                        throw SqlError.newSQLException("ResultSet not support columnType now:" + columnType);
                    case YEAR:
                        throw SqlError.newSQLException("ResultSet not support columnType now:" + columnType);
                    case DATE:
                        return this.getDate(columnIndex);
                    case TIME:
                        return this.getTime(columnIndex);
                    case TIMESTAMP:
                        return this.getTimestamp(columnIndex);
                    case DATETIME:
                        return this.getLocalDateTime(columnIndex);
                    case ARRAY:
                        return this.getArray(columnIndex);
                    default:
                        return this.getString(columnIndex);
                }
            }
        } catch (Exception e) {
            throw new SQLException("resultset getObject error with columnIndex: " + columnIndex, e);
        }
    }

    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        try {
            if (type == null) {
                throw SqlError.newSQLException("Type parameter can not be null");
            } else {
                if (type.equals(String.class)) {
                    return (T)this.getString(columnIndex);
                } else if (type.equals(BigDecimal.class)) {
                    return (T)this.getBigDecimal(columnIndex);
                } else if (type.equals(BigInteger.class)) {
                    return (T)this.getBigInteger(columnIndex);
                } else if (!type.equals(Boolean.class) && !type.equals(Boolean.TYPE)) {
                    if (!type.equals(Byte.class) && !type.equals(Byte.TYPE)) {
                        if (!type.equals(Short.class) && !type.equals(Short.TYPE)) {
                            if (!type.equals(Integer.class) && !type.equals(Integer.TYPE)) {
                                if (!type.equals(Long.class) && !type.equals(Long.TYPE)) {
                                    if (!type.equals(Float.class) && !type.equals(Float.TYPE)) {
                                        if (!type.equals(Double.class) && !type.equals(Double.TYPE)) {
                                            if (type.equals(byte[].class)) {
                                                return (T)this.getBytes(columnIndex);
                                            } else if (type.equals(Date.class)) {
                                                return (T)this.getDate(columnIndex);
                                            } else if (type.equals(Time.class)) {
                                                return (T)this.getTime(columnIndex);
                                            } else if (type.equals(Timestamp.class)) {
                                                return (T)this.getTimestamp(columnIndex);
                                            } else if (type.equals(java.util.Date.class)) {
                                                Timestamp ts = this.getTimestamp(columnIndex);
                                                return ts == null ? null : (T)java.util.Date.from(ts.toInstant());
                                            } else if (type.equals(Calendar.class)) {
                                                return (T)this.getUtilCalendar(columnIndex);
                                            } else if (type.equals(Clob.class)) {
                                                return (T)this.getClob(columnIndex);
                                            } else if (type.equals(Blob.class)) {
                                                return (T)this.getBlob(columnIndex);
                                            } else if (type.equals(Array.class)) {
                                                return (T)this.getArray(columnIndex);
                                            } else if (type.equals(Ref.class)) {
                                                return (T)this.getRef(columnIndex);
                                            } else if (type.equals(URL.class)) {
                                                return (T)this.getURL(columnIndex);
                                            } else if (type.equals(Struct.class)) {
                                                throw new SQLFeatureNotSupportedException();
                                            } else if (type.equals(RowId.class)) {
                                                return (T)this.getRowId(columnIndex);
                                            } else if (type.equals(NClob.class)) {
                                                return (T)this.getNClob(columnIndex);
                                            } else if (type.equals(SQLXML.class)) {
                                                return (T)this.getSQLXML(columnIndex);
                                            } else if (type.equals(LocalDate.class)) {
                                                return (T)this.getLocalDate(columnIndex);
                                            } else if (type.equals(LocalDateTime.class)) {
                                                return (T)this.getLocalDateTime(columnIndex);
                                            } else if (type.equals(LocalTime.class)) {
                                                return (T)this.getLocalTime(columnIndex);
                                            } else if (type.equals(OffsetDateTime.class)) {
                                            } else if (type.equals(OffsetTime.class)) {
                                            } else if (type.equals(ZonedDateTime.class)) {
                                            } else if (type.equals(Duration.class)) {
                                            } else {
                                                throw SqlError.newSQLException("Conversion not supported for type " + type.getName());
                                            }
                                        } else {
                                            this.checkColumnBounds(columnIndex);
                                            Object value = this.thisRow[columnIndex - 1];
                                            return value != null ? (T)value : null;
                                        }
                                    } else {
                                        this.checkColumnBounds(columnIndex);
                                        Object value = this.thisRow[columnIndex - 1];
                                        return value != null ? (T)value : null;
                                    }
                                } else {

                                    this.checkColumnBounds(columnIndex);
                                    Object value = this.thisRow[columnIndex - 1];
                                    return value != null ? (T)value : null;
                                }
                            } else {

                                this.checkColumnBounds(columnIndex);
                                Object value = this.thisRow[columnIndex - 1];
                                return value != null ? (T)value : null;
                            }
                        } else {

                            this.checkColumnBounds(columnIndex);
                            Object value = this.thisRow[columnIndex - 1];
                            return value != null ? (T)value : null;
                        }
                    } else {

                        this.checkColumnBounds(columnIndex);
                        Object value = this.thisRow[columnIndex - 1];
                        return value != null ? (T)value : null;
                    }
                } else {

                    this.checkColumnBounds(columnIndex);
                    Object value = this.thisRow[columnIndex - 1];
                    return value != null ? (T)value : null;
                }
            }
        } catch (Exception e) {
            throw SqlError.newSQLException("getObject error classType: " + type, e);
        }
        throw SqlError.newSQLException("getObject error unknown classType: " + type);
    }

    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        try {
            return this.getObject(this.findColumn(columnLabel), type);
        } catch (Exception e) {
            throw SqlError.newSQLException("getObject error columnLabel " + columnLabel + " classType: " + type, e);
        }
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        try {
            return this.getObject(columnIndex);
        } catch (Exception e) {
            throw SqlError.newSQLException("getObject error columnIndex " + columnIndex + " map: " + map, e);
        }
    }

    public Object getObject(String columnName) throws SQLException {
        try {
            return this.getObject(this.findColumn(columnName));
        } catch (Exception e) {
            throw SqlError.newSQLException("getObject error columnName " + columnName, e);
        }
    }

    public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
        try {
            return this.getObject(this.findColumn(colName), map);
        } catch (Exception e) {
            throw SqlError.newSQLException("getObject error colName " + colName, e);
        }
    }

    @Override
    public int findColumn(String columnName) throws SQLException {
        try {
            int index = this.data.findColumn(columnName, 1);
            if (index == -1) {
                throw SqlError.newSQLException("resultSet not found columnName " + columnName);
            }
            return index;
        } catch (Exception e) {
            log.error("findColumn with name {} error ", columnName, e);
            throw SqlError.newSQLException("resultSet findColumn error " + columnName);
        }
    }
    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Reader getCharacterStream(String columnName) throws SQLException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        try {
            String res = this.getString(columnIndex);
            if (res != null) {
                return new BigDecimal(res);
            }
            return null;
        } catch (Exception e) {
            log.error("getBigDecimal error happened ", e);
            throw SqlError.newSQLException("getBigDecimal error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        try {
            return this.getBigDecimal(this.findColumn(columnName));
        } catch (Exception e) {
            throw SqlError.newSQLException("getBigDecimal error columnName "+ columnName, e);
        }
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        if (this.data.size() > 0 && this.currentRow < 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        if (this.data.size() == 0) {
            return false;
        }
        return this.currentRow >= this.data.size();
    }

    @Override
    public boolean isFirst() throws SQLException {
        if (this.data.size() == 0) {
            return false;
        }
        return this.currentRow == 0;
    }

    @Override
    public boolean isLast() throws SQLException {
        if (this.data.size() == 0){
            return false;
        }
        return this.currentRow == this.data.size() - 1;
    }

    @Override
    public void beforeFirst() throws SQLException {
        if (!data.isEmpty()) {
            this.currentRow = -1;
        }
        thisRow = null;
    }

    @Override
    public void afterLast() throws SQLException {
        if (this.data.size() > 0) {
            currentRow = this.data.size();
        }
        thisRow = null;
    }

    @Override
    public boolean first() throws SQLException {
        if (this.data.size() == 0){
            return false;
        }
        this.currentRow = 0;
        return true;
    }

    @Override
    public boolean last() throws SQLException {
        if (this.data.size() == 0){
            return false;
        }
        this.currentRow = this.data.size();
        return true;
    }

    @Override
    public int getRow() throws SQLException {
        if (currentRow < 0 || currentRow >= this.data.size()) {
            return 0;
        }

        return currentRow + 1;
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        try {
            if (!this.hasRows()) {
                throw SqlError.newSQLException("Not a navigable ResultSet.");
            } else {
                boolean b;
                if (data.isEmpty()) {
                    b = false;
                } else if (row == 0) {
                    this.beforeFirst();
                    b = false;
                } else if (row == 1) {
                    b = this.first();
                } else if (row == -1) {
                    b = this.last();
                } else if (row > this.data.size()) {
                    this.afterLast();
                    b = false;
                } else if (row < 0) {
                    int newRowPosition = this.data.size() + row + 1;
                    if (newRowPosition <= 0) {
                        this.beforeFirst();
                        b = false;
                    } else {
                        b = this.absolute(newRowPosition);
                    }
                } else {
                    --row;
                    this.data.setCurrentRow(row);
                    this.thisRow = this.data.get(row);
                    b = true;
                }

                return b;
            }
        } catch (Exception e) {
            throw SqlError.newSQLException("ResultSet.absolute error", e);
        }
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        // have to add 1 since absolute expects a 1-based index
        int index = currentRow + 1 + rows;
        if (index < 0) {
            beforeFirst();
            return false;
        }
        return absolute(index);
    }

    @Override
    public boolean previous() throws SQLException {
        if (currentRow - 1 < 0) {
            currentRow = -1;
            thisRow = null;
            return false;
        } else {
            currentRow--;
        }
        return true;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        checkClosed();
        switch (direction) {
            case ResultSet.FETCH_FORWARD:
                break;
            case ResultSet.FETCH_REVERSE:
            case ResultSet.FETCH_UNKNOWN:
                break;
            default:
                throw new SQLException("Invalid fetch direction constant: " + direction);
        }

        this.fetchDirection = direction;
    }

    @Override
    public int getFetchDirection() throws SQLException {
        checkClosed();
        return fetchDirection;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        checkClosed();
        if (rows < 0) {
            throw new SQLException("Fetch size must be a value greater to or equal to 0.");
        }
        fetchSize = rows;
    }

    @Override
    public int getFetchSize() throws SQLException {
        return fetchSize;
    }

    @Override
    public int getType() throws SQLException {
        checkClosed();
        return resultsettype;
    }

    @Override
    public int getConcurrency() throws SQLException {
        checkClosed();
        return resultsetconcurrency;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        checkClosed();
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        checkClosed();
        return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        checkClosed();
        return false;
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateBoolean(int i, boolean b) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateByte(int i, byte b) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateShort(int i, short i1) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateInt(int i, int i1) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateLong(int i, long l) throws SQLException {

    }

    @Override
    public void updateFloat(int i, float v) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateDouble(int i, double v) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateString(int i, String s) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateBytes(int i, byte[] bytes) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateDate(int i, Date date) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateTime(int i, Time time) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateTimestamp(int i, Timestamp timestamp) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateAsciiStream(int i, InputStream inputStream, int i1) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateBinaryStream(int i, InputStream inputStream, int i1) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateCharacterStream(int i, Reader reader, int i1) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateObject(int i, Object o, int i1) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateObject(int i, Object o) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateNull(String columnName) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateBoolean(String s, boolean b) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateByte(String s, byte b) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateShort(String s, short i) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateInt(String s, int i) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateLong(String s, long l) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateFloat(String s, float v) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateDouble(String s, double v) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateString(String s, String s1) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateBytes(String s, byte[] bytes) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateDate(String s, Date date) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateTime(String s, Time time) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateTimestamp(String s, Timestamp timestamp) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateAsciiStream(String s, InputStream inputStream, int i) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateBinaryStream(String s, InputStream inputStream, int i) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateCharacterStream(String s, Reader reader, int i) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateObject(String s, Object o, int i) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateObject(String s, Object o) throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void insertRow() throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void updateRow() throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void deleteRow() throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void refreshRow() throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw new SQLException("Not Updatable ");
    }

    @Override
    public Statement getStatement() throws SQLException {
        return this.statement;
    }

    public void setStatement(KineStatement statement) {
        this.statement = statement;
    }


    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getBlob not support now");
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getBlob not support now");
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        try {
            Object object = this.getObject(columnIndex);
            //Object[] objects = (Object[]) object;
            //KineType kineType = this.data.kineTypes[columnIndex];
            return (Array)object;
        } catch (Exception e) {
            throw SqlError.newSQLException("getArray error columnIndex " + columnIndex, e);
        }
    }


    @Override
    public Ref getRef(String columnName) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(String columnName) throws SQLException {
        return null;
    }

    @Override
    public Clob getClob(String columnName) throws SQLException {
        return null;
    }

    @Override
    public Array getArray(String columnName) throws SQLException {
        try {
            Object object = this.getObject(this.findColumn(columnName));
            //Object[] objects = (Object[]) object;
            //KineType kineType = this.data.kineTypes[columnIndex];
            return (Array)object;
        } catch (Exception e) {
            throw SqlError.newSQLException("getArray error columnIndex " + columnName, e);
        }
    }

    @Override
    public Date getDate(int columnIndex, Calendar calendar) throws SQLException {
        // todo
        try {
            String res = this.getString(columnIndex);
            if (StringUtils.isNullOrEmpty(res)) {
                return null;
            }
            LocalDate localDate = LocalDate.parse(res, defaultDateFormatter);
            java.util.Date date = DateUtils.asDate(localDate);
            return new Date(date.getTime());
        } catch (Exception e) {
            throw SqlError.newSQLException("getDate error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public Date getDate(String columnName, Calendar calendar) throws SQLException {
        try {
            String res = this.getString(this.findColumn(columnName));
            if (StringUtils.isNullOrEmpty(res)) {
                return null;
            }
            LocalDate localDate = LocalDate.parse(res, defaultDateFormatter);
            java.util.Date date = DateUtils.asDate(localDate);
            return new Date(date.getTime());
        } catch (Exception e) {
            throw SqlError.newSQLException("getDate error columnName "+ columnName, e);
        }
    }

    @Override
    public Time getTime(int columnIndex, Calendar calendar) throws SQLException {
        try {
            return this.getTime(columnIndex);
        } catch (Exception e) {
            throw new SQLException("getTime with colum  :" + columnIndex, e);
        }
    }

    @Override
    public Time getTime(String columnName, Calendar calendar) throws SQLException {
        try {
            return this.getTime(this.findColumn(columnName));
        } catch (Exception e) {
            throw new SQLException("getTime with colum name :" + columnName, e);
        }
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar calendar) throws SQLException {
        try {
            String res = this.getString(columnIndex);
            if (StringUtils.isNullOrEmpty(res)) {
                return null;
            }
            LocalDateTime localDateTime = LocalDateTime.parse(res, defaultTimestampFormatter);
            Timestamp timestamp = Timestamp.valueOf(localDateTime);
            return timestamp;
        } catch (Exception e) {
            throw SqlError.newSQLException("getTimestamp error columnIndex "+ columnIndex, e);
        }
    }

    @Override
    public Timestamp getTimestamp(String columnName, Calendar calendar) throws SQLException {
        try {
            String res = this.getString(this.findColumn(columnName));
            if (StringUtils.isNullOrEmpty(res)) {
                return null;
            }
            LocalDateTime localDateTime = LocalDateTime.parse(res, defaultTimestampFormatter);
            Timestamp timestamp = Timestamp.valueOf(localDateTime);
            return timestamp;
        } catch (Exception e) {
            throw SqlError.newSQLException("getTimestamp error columnName "+ columnName, e);
        }
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public URL getURL(String columnName) throws SQLException {
        return null;
    }

    @Override
    public void updateRef(int i, Ref ref) throws SQLException {

    }

    @Override
    public void updateRef(String s, Ref ref) throws SQLException {

    }

    @Override
    public void updateBlob(int i, Blob blob) throws SQLException {

    }

    @Override
    public void updateBlob(String s, Blob blob) throws SQLException {

    }

    @Override
    public void updateClob(int i, Clob clob) throws SQLException {

    }

    @Override
    public void updateClob(String s, Clob clob) throws SQLException {

    }

    @Override
    public void updateArray(int i, Array array) throws SQLException {

    }

    @Override
    public void updateArray(String s, Array array) throws SQLException {

    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public RowId getRowId(String columnName) throws SQLException {
        return null;
    }

    @Override
    public void updateRowId(int i, RowId rowId) throws SQLException {

    }

    @Override
    public void updateRowId(String s, RowId rowId) throws SQLException {

    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.closed == true;
    }

    @Override
    public void updateNString(int i, String s) throws SQLException {

    }

    @Override
    public void updateNString(String s, String s1) throws SQLException {

    }

    @Override
    public void updateNClob(int i, NClob nClob) throws SQLException {

    }

    @Override
    public void updateNClob(String s, NClob nClob) throws SQLException {

    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public NClob getNClob(String columnName) throws SQLException {
        return null;
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public SQLXML getSQLXML(String columnName) throws SQLException {
        return null;
    }

    @Override
    public void updateSQLXML(int i, SQLXML sqlxml) throws SQLException {

    }

    @Override
    public void updateSQLXML(String s, SQLXML sqlxml) throws SQLException {

    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public String getNString(String columnName) throws SQLException {
        return null;
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Reader getNCharacterStream(String columnName) throws SQLException {
        return null;
    }

    @Override
    public void updateNCharacterStream(int i, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateNCharacterStream(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateAsciiStream(int i, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateBinaryStream(int i, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateCharacterStream(int i, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateAsciiStream(String s, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateBinaryStream(String s, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateCharacterStream(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateBlob(int i, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateBlob(String s, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateClob(int i, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateClob(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateNClob(int i, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateNClob(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateNCharacterStream(int i, Reader reader) throws SQLException {

    }

    @Override
    public void updateNCharacterStream(String s, Reader reader) throws SQLException {

    }

    @Override
    public void updateAsciiStream(int i, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateBinaryStream(int i, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateCharacterStream(int i, Reader reader) throws SQLException {

    }

    @Override
    public void updateAsciiStream(String s, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateBinaryStream(String s, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateCharacterStream(String s, Reader reader) throws SQLException {

    }

    @Override
    public void updateBlob(int i, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateBlob(String s, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateClob(int i, Reader reader) throws SQLException {

    }

    @Override
    public void updateClob(String s, Reader reader) throws SQLException {

    }

    @Override
    public void updateNClob(int i, Reader reader) throws SQLException {

    }

    @Override
    public void updateNClob(String s, Reader reader) throws SQLException {

    }

    @Override
    public <T> T unwrap(Class<T> aClass) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;
    }

    protected final void checkColumnBounds(int columnIndex) throws SQLException {
        if (columnIndex < 1) {
            throw SqlError.newSQLException("ResultSet.Column_Index_out_of_range_low" + columnIndex);
        }
    }
    protected boolean hasRows() {
        return this.data != null && this.data.rowNames != null;

    }

    protected void checkClosed() throws SQLException {
        if (this.connection.isClosed()) {
            throw new SQLException("This ResultSet is closed.");
        }
    }

    public LocalDate getLocalDate(int columnIndex) throws SQLException {
        this.checkColumnBounds(columnIndex);
        return (LocalDate)this.thisRow[columnIndex - 1];
    }

    public LocalDateTime getLocalDateTime(int columnIndex) throws SQLException {
        this.checkColumnBounds(columnIndex);
        return (LocalDateTime)this.thisRow[columnIndex - 1];
    }

    public LocalTime getLocalTime(int columnIndex) throws SQLException {
        this.checkColumnBounds(columnIndex);
        return (LocalTime)this.thisRow[columnIndex - 1];
    }

    public Calendar getUtilCalendar(int columnIndex) throws SQLException {
        this.checkColumnBounds(columnIndex);
        return (Calendar)this.thisRow[columnIndex - 1];
    }

    private String getStringForClob(int columnIndex) throws SQLException {
        String asString = null;
        String forcedEncoding = null;
        ///this.connection.getPropertySet().getStringProperty(PropertyKey.clobCharacterEncoding).getStringValue();
        if (forcedEncoding == null) {
            asString = this.getString(columnIndex);
        } else {
            byte[] asBytes = this.getBytes(columnIndex);
            if (asBytes != null) {
                asString = StringUtils.toString(asBytes, forcedEncoding);
            }
        }

        return asString;
    }

    public BigInteger getBigInteger(int columnIndex) throws SQLException {
        try {
            String stringVal = this.getString(columnIndex);
            if (stringVal == null) {
                return null;
            } else {
                try {
                    return new BigInteger(stringVal);
                } catch (NumberFormatException e) {
                    throw SqlError.newSQLException("ResultSet.Bad_format_for_BigInteger" + new Object[]{columnIndex, stringVal}, e);
                }
            }
        } catch (Exception e) {
            throw SqlError.newSQLException("resultSet getBigInteger error", e);
        }
    }


}
