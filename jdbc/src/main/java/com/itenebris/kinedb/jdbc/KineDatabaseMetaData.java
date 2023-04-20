package com.itenebris.kinedb.jdbc;

import com.itenebris.kinedb.jdbc.connection.KineConnection;
import com.itenebris.kinedb.jdbc.executor.Kine;
import com.itenebris.kinedb.jdbc.executor.KineData;
import com.itenebris.kinedb.jdbc.result.Field;
import com.itenebris.kinedb.jdbc.result.KineResultSet;
import com.itenebris.kinedb.jdbc.result.ResultData;
import com.itenebris.kinedb.jdbc.statement.KinePrepareStatement;
import com.itenebris.kinedb.jdbc.util.StringUtils;
import com.sun.rowset.internal.Row;

import java.sql.*;
import java.util.*;

public class KineDatabaseMetaData implements DatabaseMetaData {

    protected static int maxBufferSize = 65535;
    private static final String[] KINE_KEYWORDS = new String[]{"ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE", "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONDITION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CUBE", "CUME_DIST", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELAYED", "DELETE", "DENSE_RANK", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH", "ELSE", "ELSEIF", "EMPTY", "ENCLOSED", "ESCAPED", "EXCEPT", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH", "FIRST_VALUE", "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "FUNCTION", "GENERATED", "GET", "GRANT", "GROUP", "GROUPING", "GROUPS", "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT", "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IO_AFTER_GTIDS", "IO_BEFORE_GTIDS", "IS", "ITERATE", "JOIN", "JSON_TABLE", "KEY", "KEYS", "KILL", "LAG", "LAST_VALUE", "LEAD", "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT", "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "LONG", "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MASTER_BIND", "MASTER_SSL_VERIFY_SERVER_CERT", "MATCH", "MAXVALUE", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL", "NOT", "NO_WRITE_TO_BINLOG", "NTH_VALUE", "NTILE", "NULL", "NUMERIC", "OF", "ON", "OPTIMIZE", "OPTIMIZER_COSTS", "OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE", "OVER", "PARTITION", "PERCENT_RANK", "PERSIST", "PERSIST_ONLY", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE", "RANK", "READ", "READS", "READ_WRITE", "REAL", "RECURSIVE", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESIGNAL", "RESTRICT", "RETURN", "REVOKE", "RIGHT", "RLIKE", "ROW", "ROWS", "ROW_NUMBER", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SIGNAL", "SMALLINT", "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL", "STARTING", "STORED", "STRAIGHT_JOIN", "SYSTEM", "TABLE", "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING", "VIRTUAL", "WHEN", "WHERE", "WHILE", "WINDOW", "WITH", "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL"};
    static final List<String> SQL2003_KEYWORDS = Arrays.asList("ABS", "ALL", "ALTER", "AND", "ANY", "ARE", "ARRAY", "AS", "ASENSITIVE", "ASYMMETRIC", "AT", "ATOMIC", "AUTHORIZATION", "AVG", "BEGIN", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOOLEAN", "BOTH", "BY", "CALL", "CALLED", "CARDINALITY", "CASCADED", "CASE", "CAST", "CEIL", "CEILING", "CHAR", "CHARACTER", "CHARACTER_LENGTH", "CHAR_LENGTH", "CHECK", "CLOB", "CLOSE", "COALESCE", "COLLATE", "COLLECT", "COLUMN", "COMMIT", "CONDITION", "CONNECT", "CONSTRAINT", "CONVERT", "CORR", "CORRESPONDING", "COUNT", "COVAR_POP", "COVAR_SAMP", "CREATE", "CROSS", "CUBE", "CUME_DIST", "CURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP", "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_USER", "CURSOR", "CYCLE", "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELETE", "DENSE_RANK", "DEREF", "DESCRIBE", "DETERMINISTIC", "DISCONNECT", "DISTINCT", "DOUBLE", "DROP", "DYNAMIC", "EACH", "ELEMENT", "ELSE", "END", "END-EXEC", "ESCAPE", "EVERY", "EXCEPT", "EXEC", "EXECUTE", "EXISTS", "EXP", "EXTERNAL", "EXTRACT", "FALSE", "FETCH", "FILTER", "FLOAT", "FLOOR", "FOR", "FOREIGN", "FREE", "FROM", "FULL", "FUNCTION", "FUSION", "GET", "GLOBAL", "GRANT", "GROUP", "GROUPING", "HAVING", "HOLD", "HOUR", "IDENTITY", "IN", "INDICATOR", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT", "INTEGER", "INTERSECT", "INTERSECTION", "INTERVAL", "INTO", "IS", "JOIN", "LANGUAGE", "LARGE", "LATERAL", "LEADING", "LEFT", "LIKE", "LN", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOWER", "MATCH", "MAX", "MEMBER", "MERGE", "METHOD", "MIN", "MINUTE", "MOD", "MODIFIES", "MODULE", "MONTH", "MULTISET", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NEW", "NO", "NONE", "NORMALIZE", "NOT", "NULL", "NULLIF", "NUMERIC", "OCTET_LENGTH", "OF", "OLD", "ON", "ONLY", "OPEN", "OR", "ORDER", "OUT", "OUTER", "OVER", "OVERLAPS", "OVERLAY", "PARAMETER", "PARTITION", "PERCENTILE_CONT", "PERCENTILE_DISC", "PERCENT_RANK", "POSITION", "POWER", "PRECISION", "PREPARE", "PRIMARY", "PROCEDURE", "RANGE", "RANK", "READS", "REAL", "RECURSIVE", "REF", "REFERENCES", "REFERENCING", "REGR_AVGX", "REGR_AVGY", "REGR_COUNT", "REGR_INTERCEPT", "REGR_R2", "REGR_SLOPE", "REGR_SXX", "REGR_SXY", "REGR_SYY", "RELEASE", "RESULT", "RETURN", "RETURNS", "REVOKE", "RIGHT", "ROLLBACK", "ROLLUP", "ROW", "ROWS", "ROW_NUMBER", "SAVEPOINT", "SCOPE", "SCROLL", "SEARCH", "SECOND", "SELECT", "SENSITIVE", "SESSION_USER", "SET", "SIMILAR", "SMALLINT", "SOME", "SPECIFIC", "SPECIFICTYPE", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQRT", "START", "STATIC", "STDDEV_POP", "STDDEV_SAMP", "SUBMULTISET", "SUBSTRING", "SUM", "SYMMETRIC", "SYSTEM", "SYSTEM_USER", "TABLE", "TABLESAMPLE", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSLATE", "TRANSLATION", "TREAT", "TRIGGER", "TRIM", "TRUE", "UESCAPE", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UPDATE", "UPPER", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING", "VAR_POP", "VAR_SAMP", "WHEN", "WHENEVER", "WHERE", "WIDTH_BUCKET", "WINDOW", "WITH", "WITHIN", "WITHOUT", "YEAR");
    private static volatile String kineKeywords = null;
    protected String database = null;


    private KineConnection connection;


    public KineDatabaseMetaData(KineConnection connection, String databaseToSet) {
        this.connection = connection;
        this.database = databaseToSet;
    }

    @Override
    public boolean allProceduresAreCallable() throws SQLException {
        return false;
    }

    @Override
    public boolean allTablesAreSelectable() throws SQLException {
        return false;
    }

    @Override
    public String getURL() throws SQLException {
        return this.connection.getUrl();
    }

    @Override
    public String getUserName() throws SQLException {
        return "";
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this.connection.isReadOnly();
    }

    @Override
    public boolean nullsAreSortedHigh() throws SQLException {
        return false;
    }

    @Override
    public boolean nullsAreSortedLow() throws SQLException {
        return false;
    }

    @Override
    public boolean nullsAreSortedAtStart() throws SQLException {
        return false;
    }

    @Override
    public boolean nullsAreSortedAtEnd() throws SQLException {
        return false;
    }

    @Override
    public String getDatabaseProductName() throws SQLException {
        return "kinedb 1.0.0";
    }

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        return "kinedb 1.0.0";
    }

    @Override
    public String getDriverName() throws SQLException {
        return "jdbc for kinedb";
    }

    @Override
    public String getDriverVersion() throws SQLException {
        return "1.0.0";
    }

    @Override
    public int getDriverMajorVersion() {
        return KineDriver.DRIVER_VERSION_MAJOR;
    }

    @Override
    public int getDriverMinorVersion() {
        return KineDriver.DRIVER_VERSION_MINOR;
    }

    @Override
    public boolean usesLocalFiles() throws SQLException {
        return false;
    }

    @Override
    public boolean usesLocalFilePerTable() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        return true;
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        return true;
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public String getIdentifierQuoteString() throws SQLException {
        return "`";
    }

    @Override
    public String getSQLKeywords() throws SQLException {
        try {
            if (kineKeywords != null) {
                return kineKeywords;
            } else {
                Class var1 = DatabaseMetaData.class;
                synchronized(DatabaseMetaData.class) {
                    if (kineKeywords != null) {
                        return kineKeywords;
                    } else {
                        Set<String> mysqlKeywordSet = new TreeSet();
                        StringBuilder mysqlKeywordsBuffer = new StringBuilder();
                        Collections.addAll(mysqlKeywordSet, KINE_KEYWORDS);
                        mysqlKeywordSet.removeAll(SQL2003_KEYWORDS);
                        Iterator var4 = mysqlKeywordSet.iterator();

                        while(var4.hasNext()) {
                            String keyword = (String)var4.next();
                            mysqlKeywordsBuffer.append(",").append(keyword);
                        }

                        kineKeywords = mysqlKeywordsBuffer.substring(1);
                        return kineKeywords;
                    }
                }
            }
        } catch (Exception e) {
            throw new SQLException("getSQLKeywords error", e);
        }
    }

    @Override
    public String getNumericFunctions() throws SQLException {
        return "ABS,ACOS,ASIN,ATAN,ATAN2,BIT_COUNT,CEILING,COS,COT,DEGREES,EXP,FLOOR,LOG,LOG10,MAX,MIN,MOD,PI,POW,POWER,RADIANS,RAND,ROUND,SIN,SQRT,TAN,TRUNCATE";
    }

    @Override
    public String getStringFunctions() throws SQLException {
        return "ASCII,BIN,BIT_LENGTH,CHAR,CHARACTER_LENGTH,CHAR_LENGTH,CONCAT,CONCAT_WS,CONV,ELT,EXPORT_SET,FIELD,FIND_IN_SET,HEX,INSERT,INSTR,LCASE,LEFT,LENGTH,LOAD_FILE,LOCATE,LOCATE,LOWER,LPAD,LTRIM,MAKE_SET,MATCH,MID,OCT,OCTET_LENGTH,ORD,POSITION,QUOTE,REPEAT,REPLACE,REVERSE,RIGHT,RPAD,RTRIM,SOUNDEX,SPACE,STRCMP,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING_INDEX,TRIM,UCASE,UPPER";
    }

    @Override
    public String getSystemFunctions() throws SQLException {
        return null;
    }

    @Override
    public String getTimeDateFunctions() throws SQLException {
        return null;
    }

    @Override
    public String getSearchStringEscape() throws SQLException {
        return "\\";
    }

    @Override
    public String getExtraNameCharacters() throws SQLException {
        return "#@";
    }

    @Override
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsColumnAliasing() throws SQLException {
        return true;
    }

    @Override
    public boolean nullPlusNonNullIsNull() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsConvert() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        return true;
    }

    @Override
    public boolean supportsTableCorrelationNames() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsOrderByUnrelated() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsGroupBy() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsGroupByUnrelated() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsLikeEscapeClause() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsMultipleResultSets() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMultipleTransactions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsNonNullableColumns() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsCoreSQLGrammar() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsANSI92FullSQL() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOuterJoins() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsFullOuterJoins() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsLimitedOuterJoins() throws SQLException {
        return true;
    }

    @Override
    public String getSchemaTerm() throws SQLException {
        return "schema";
    }

    @Override
    public String getProcedureTerm() throws SQLException {
        return null;
    }

    @Override
    public String getCatalogTerm() throws SQLException {
        return "database";
    }

    @Override
    public boolean isCatalogAtStart() throws SQLException {
        return true;
    }

    @Override
    public String getCatalogSeparator() throws SQLException {
        return ".";
    }

    @Override
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsPositionedDelete() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsPositionedUpdate() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSelectForUpdate() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsStoredProcedures() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInExists() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInIns() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsUnion() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsUnionAll() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        return false;
    }

    @Override
    public int getMaxBinaryLiteralLength() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxCharLiteralLength() throws SQLException {
        return 256;
    }

    @Override
    public int getMaxColumnNameLength() throws SQLException {
        return 256;
    }

    @Override
    public int getMaxColumnsInGroupBy() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxColumnsInIndex() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxColumnsInOrderBy() throws SQLException {
        return 20;
    }

    @Override
    public int getMaxColumnsInSelect() throws SQLException {
        return 256;
    }

    @Override
    public int getMaxColumnsInTable() throws SQLException {
        return 256;
    }

    @Override
    public int getMaxConnections() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxCursorNameLength() throws SQLException {
        return 64;
    }

    @Override
    public int getMaxIndexLength() throws SQLException {
        return 256;
    }

    @Override
    public int getMaxSchemaNameLength() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxProcedureNameLength() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxCatalogNameLength() throws SQLException {
        return 32;
    }

    @Override
    public int getMaxRowSize() throws SQLException {
        return 2147483639;
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        return true;
    }

    @Override
    public int getMaxStatementLength() throws SQLException {
        return maxBufferSize - 4;
    }

    @Override
    public int getMaxStatements() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxTableNameLength() throws SQLException {
        return 64;
    }

    @Override
    public int getMaxTablesInSelect() throws SQLException {
        return 256;
    }

    @Override
    public int getMaxUserNameLength() throws SQLException {
        return 16;
    }

    @Override
    public int getDefaultTransactionIsolation() throws SQLException {
        return 0;
    }

    @Override
    public boolean supportsTransactions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        return false;
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        return false;
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        return false;
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        return false;
    }

    @Override
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
            throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern,
            String columnNamePattern) throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    protected ResultSet getDatabases() throws SQLException {
        return this.getDatabases(null);
    }

    protected ResultSet getDatabases(String dbPattern) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet results = null;
        Statement stmt = null;

        try {
            // not consider the resultSet type
            stmt = this.connection.createStatement();
            StringBuilder queryBuf = new StringBuilder("SHOW DATABASES");
            if (dbPattern != null) { // not support now
                queryBuf.append(" LIKE ?");
            }

            pStmt = new KinePrepareStatement(this.connection, "SHOW DATABASES", queryBuf.toString());
            if (dbPattern != null) {
                pStmt.setString(1, dbPattern);
            }

            results = pStmt.executeQuery();
            return results;
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                }
            }
            if (pStmt != null) {
                try {
                    pStmt.close();
                } catch (Exception e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    protected String getDatabase(String catalog, String schema) {

        return catalog == null ? (this.database == null ? schema : this.database) : catalog;
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
            throws SQLException {

            final Statement stmt = this.connection.createStatement();
            String dbPattern = this.getDatabase(catalog, schemaPattern);
            final String tableNamePat = tableNamePattern;

            try {
                ResultSet results = null;
                StringBuilder sqlBuf = new StringBuilder("SHOW TABLES ");
                sqlBuf.append(dbPattern);

                if (tableNamePat != null) {
                    sqlBuf.append(" LIKE ");
                    sqlBuf.append(StringUtils.quoteIdentifier(tableNamePat, "'", true));
                }

                results = stmt.executeQuery(sqlBuf.toString());
                // now only have one table_name column ,not include table_type

                return results;
            } catch (Exception e) {
                throw new SQLException("getTables error", e);
            }
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        ResultSet databaseResultSet = this.getDatabases();
        ResultData resultData = ((KineResultSet)databaseResultSet).getData();
        resultData.setRowNames(new String[]{"TABLE_SCHEM"});
        return databaseResultSet;
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        ResultSet databaseResultSet = this.getDatabases();
        ResultData resultData = ((KineResultSet)databaseResultSet).getData();
        resultData.setRowNames(new String[]{"TABLE_CAT"});
        return databaseResultSet;
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {

        List<String> rowNames = new ArrayList<>(1);
        List<String> rowTypes = new ArrayList<>(1);

        rowNames.add("TABLE_TYPE");
        rowTypes.add(KineType.VARCHAR.getName());

        List<String[]> rowValues  = new ArrayList<>(1);
        rowValues.add(new String[]{"TABLE"}); // now only support table


        KineData resultData = new KineData();
        resultData.setRowNames(rowNames.toArray((new String[0])));
        resultData.setTypes(rowTypes.toArray((new String[0])));
        resultData.setRowValues(rowValues.toArray(new Object[0][0]));

        return new KineResultSet(resultData, this.connection);
    }

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
            throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
            throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
            throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable)
            throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        final Statement stmt = this.connection.createStatement();
        String dbPattern = this.getDatabase(catalog, schema);
        final String tableNamePat = table;

        try {
            ResultSet results = null;
            StringBuilder sqlBuf = new StringBuilder("SHOW INDEX FROM ");
            sqlBuf.append(dbPattern);
            sqlBuf.append(".");
            sqlBuf.append(table);

            results = stmt.executeQuery(sqlBuf.toString());
            ResultData resultData = ((KineResultSet)results).getData();
            Object[][] rows = resultData.rowValues;
            List<Object[]> primaryRows = new ArrayList<>();
            Object[] columnNames = resultData.rowNames;
            if (rows != null && rows.length > 0) {
                int keyNameIndex = -1;
                for (int i=0; i< columnNames.length; i++) {
                    if (columnNames[i].toString() == "KEY_NAME") {
                        keyNameIndex = i;
                        break;
                    }
                }
                for (Object[] row: rows) {
                    String keyName = row[keyNameIndex].toString();
                    if (keyName == "PRIMARY") {
                        primaryRows.add(row);
                    }
                }
            }
            resultData.setRowValues(primaryRows.toArray(new Object[0][0]));
            return results;
        } catch (Exception e) {
            throw new SQLException("getIndexInfo error", e);
        } finally {
            stmt.close();
        }
    }

    @Override
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable,
            String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    protected byte[] string2bytes(String s) throws SQLException {
        if (s == null) {
            return null;
        } else {
            try {
                return StringUtils.getBytes(s);
            } catch (Exception e) {
                throw new SQLException("string2bytes s :" + s, e);
            }
        }
    }

    private byte[][] getTypeInfo(String kineTypeName) throws SQLException {
        KineType mt = KineType.getByName(kineTypeName);
        byte[][] rowVal = new byte[18][];
        rowVal[0] = this.string2bytes(kineTypeName);
        rowVal[1] = Integer.toString(mt.getJdbcType()).getBytes();
        rowVal[2] = Integer.toString(mt.getPrecision() > 2147483647L ? Integer.MAX_VALUE : mt.getPrecision().intValue()).getBytes();
        switch (mt) {
            case ENUM:
            case SET:
            case CHAR:
            case VARCHAR:
            case TINYTEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
            case JSON:
            case TEXT:
            case TINYBLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case BLOB:
            case BINARY:
            case VARBINARY:
            case DATE:
            case TIME:
            case DATETIME:
            case TIMESTAMP:
            case GEOMETRY:
            case ARRAY:
            case UNKNOWN:
                rowVal[3] = this.string2bytes("'");
                rowVal[4] = this.string2bytes("'");
                break;
            case BIT:
            case TINYINT:
            case TINYINT_UNSIGNED:
            case BOOLEAN:
            case NULL:
            default:
                rowVal[3] = this.string2bytes("");
                rowVal[4] = this.string2bytes("");
        }

        rowVal[5] = this.string2bytes(mt.getCreateParams());
        rowVal[6] = Integer.toString(1).getBytes();
        rowVal[7] = this.string2bytes("true");
        rowVal[8] = Integer.toString(3).getBytes();
        rowVal[9] = this.string2bytes(mt.isAllowed(32) ? "true" : "false");
        rowVal[10] = this.string2bytes("false");
        rowVal[11] = this.string2bytes("false");
        rowVal[12] = this.string2bytes(mt.getName());
        switch (mt) {
            case DECIMAL:
            case DECIMAL_UNSIGNED:
            case DOUBLE:
            case DOUBLE_UNSIGNED:
                rowVal[13] = this.string2bytes("-308");
                rowVal[14] = this.string2bytes("308");
                break;
            case FLOAT:
            case FLOAT_UNSIGNED:
                rowVal[13] = this.string2bytes("-38");
                rowVal[14] = this.string2bytes("38");
                break;
            default:
                rowVal[13] = this.string2bytes("0");
                rowVal[14] = this.string2bytes("0");
        }

        rowVal[15] = this.string2bytes("0");
        rowVal[16] = this.string2bytes("0");
        rowVal[17] = this.string2bytes("10");
        return rowVal;
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
//        try {
//            Field[] fields = new Field[]{new Field("", "TYPE_NAME", this.metadataCollationIndex, this.metadataEncoding, MysqlType.CHAR, 32), new Field("", "DATA_TYPE", this.metadataCollationIndex, this.metadataEncoding, MysqlType.INT, 5), new Field("", "PRECISION", this.metadataCollationIndex, this.metadataEncoding, MysqlType.INT, 10), new Field("", "LITERAL_PREFIX", this.metadataCollationIndex, this.metadataEncoding, MysqlType.CHAR, 4), new Field("", "LITERAL_SUFFIX", this.metadataCollationIndex, this.metadataEncoding, MysqlType.CHAR, 4), new Field("", "CREATE_PARAMS", this.metadataCollationIndex, this.metadataEncoding, MysqlType.CHAR, 32), new Field("", "NULLABLE", this.metadataCollationIndex, this.metadataEncoding, MysqlType.SMALLINT, 5), new Field("", "CASE_SENSITIVE", this.metadataCollationIndex, this.metadataEncoding, MysqlType.BOOLEAN, 3), new Field("", "SEARCHABLE", this.metadataCollationIndex, this.metadataEncoding, MysqlType.SMALLINT, 3), new Field("", "UNSIGNED_ATTRIBUTE", this.metadataCollationIndex, this.metadataEncoding, MysqlType.BOOLEAN, 3), new Field("", "FIXED_PREC_SCALE", this.metadataCollationIndex, this.metadataEncoding, MysqlType.BOOLEAN, 3), new Field("", "AUTO_INCREMENT", this.metadataCollationIndex, this.metadataEncoding, MysqlType.BOOLEAN, 3), new Field("", "LOCAL_TYPE_NAME", this.metadataCollationIndex, this.metadataEncoding, MysqlType.CHAR, 32), new Field("", "MINIMUM_SCALE", this.metadataCollationIndex, this.metadataEncoding, MysqlType.SMALLINT, 5), new Field("", "MAXIMUM_SCALE", this.metadataCollationIndex, this.metadataEncoding, MysqlType.SMALLINT, 5), new Field("", "SQL_DATA_TYPE", this.metadataCollationIndex, this.metadataEncoding, MysqlType.INT, 10), new Field("", "SQL_DATETIME_SUB", this.metadataCollationIndex, this.metadataEncoding, MysqlType.INT, 10), new Field("", "NUM_PREC_RADIX", this.metadataCollationIndex, this.metadataEncoding, MysqlType.INT, 10)};
//            ArrayList<Row> tuples = new ArrayList();
//            tuples.add(new ByteArrayRow(this.getTypeInfo("BIT"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("BOOL"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("TINYINT"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("TINYINT UNSIGNED"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("BIGINT"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("BIGINT UNSIGNED"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("LONG VARBINARY"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("MEDIUMBLOB"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("LONGBLOB"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("BLOB"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("VARBINARY"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("TINYBLOB"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("BINARY"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("LONG VARCHAR"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("MEDIUMTEXT"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("LONGTEXT"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("TEXT"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("CHAR"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("ENUM"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("SET"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("DECIMAL"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("NUMERIC"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("INTEGER"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("INTEGER UNSIGNED"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("INT"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("INT UNSIGNED"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("MEDIUMINT"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("MEDIUMINT UNSIGNED"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("SMALLINT"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("SMALLINT UNSIGNED"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("FLOAT"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("DOUBLE"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("DOUBLE PRECISION"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("REAL"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("VARCHAR"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("TINYTEXT"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("DATE"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("YEAR"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("TIME"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("DATETIME"), this.getExceptionInterceptor()));
//            tuples.add(new ByteArrayRow(this.getTypeInfo("TIMESTAMP"), this.getExceptionInterceptor()));
//            return this.resultSetFactory.createFromResultsetRows(1007, 1004, new ResultsetRowsStatic(tuples, new DefaultColumnDefinition(fields)));
//        } catch (Exception e) {
//            throw SQLExceptionsMapping.translateException(var4, this.getExceptionInterceptor());
//        }
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getIndexInfo(String catalog, String schemaPattern, String table, boolean unique, boolean approximate)
            throws SQLException {
        final Statement stmt = this.connection.createStatement();
        String dbPattern = this.getDatabase(catalog, schemaPattern);
        final String tableNamePat = table;

        try {
            ResultSet results = null;
            StringBuilder sqlBuf = new StringBuilder("SHOW INDEX FROM ");
            sqlBuf.append(dbPattern);
            sqlBuf.append(".");
            sqlBuf.append(table);

            results = stmt.executeQuery(sqlBuf.toString());

            return results;
        } catch (Exception e) {
            throw new SQLException("getIndexInfo error", e);
        }
    }

    @Override
    public boolean supportsResultSetType(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        return false;
    }

    @Override
    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean ownDeletesAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean ownInsertsAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean othersUpdatesAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean othersDeletesAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean othersInsertsAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean updatesAreDetected(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean deletesAreDetected(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean insertsAreDetected(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean supportsBatchUpdates() throws SQLException {
        return false;
    }

    @Override
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
            throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
    }

    @Override
    public boolean supportsSavepoints() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsNamedParameters() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMultipleOpenResults() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsGetGeneratedKeys() throws SQLException {
        return false;
    }

    @Override
    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern,
            String attributeNamePattern) throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    @Override
    public int getDatabaseMajorVersion() throws SQLException {
        return 1;
    }

    @Override
    public int getDatabaseMinorVersion() throws SQLException {
        return 0;
    }

    @Override
    public int getJDBCMajorVersion() throws SQLException {
        return 1;
    }

    @Override
    public int getJDBCMinorVersion() throws SQLException {
        return 0;
    }

    @Override
    public int getSQLStateType() throws SQLException {
        return 0;
    }

    @Override
    public boolean locatorsUpdateCopy() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsStatementPooling() throws SQLException {
        return false;
    }

    @Override
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        return RowIdLifetime.ROWID_UNSUPPORTED;
    }

    @Override
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        return false;
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        return false;
    }

    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern)
            throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern,
            String columnNamePattern) throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern,
            String columnNamePattern) throws SQLException {
        return new KineResultSet(new KineData(), this.connection);
    }

    @Override
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            try {
                return iface.cast(this);
            } catch (ClassCastException e) {
                throw new SQLException("Unable To Unwrap" + iface);
            }
        } catch (Exception e) {
            throw new SQLException("Unable To Unwrap" + iface, e);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

protected static enum TableType {
    LOCAL_TEMPORARY("LOCAL TEMPORARY"),
    SYSTEM_TABLE("SYSTEM TABLE"),
    SYSTEM_VIEW("SYSTEM VIEW"),
    TABLE("TABLE", new String[]{"BASE TABLE"}),
    VIEW("VIEW"),
    UNKNOWN("UNKNOWN");

    private String name;
    private byte[] nameAsBytes;
    private String[] synonyms;

    private TableType(String tableTypeName) {
        this(tableTypeName, (String[])null);
    }

    private TableType(String tableTypeName, String[] tableTypeSynonyms) {
        this.name = tableTypeName;
        this.nameAsBytes = tableTypeName.getBytes();
        this.synonyms = tableTypeSynonyms;
    }

    String getName() {
        return this.name;
    }

    byte[] asBytes() {
        return this.nameAsBytes;
    }

    boolean equalsTo(String tableTypeName) {
        return this.name.equalsIgnoreCase(tableTypeName);
    }

    static TableType getTableTypeEqualTo(String tableTypeName) {
        TableType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            TableType tableType = var1[var3];
            if (tableType.equalsTo(tableTypeName)) {
                return tableType;
            }
        }

        return UNKNOWN;
    }

    boolean compliesWith(String tableTypeName) {
        if (this.equalsTo(tableTypeName)) {
            return true;
        } else {
            if (this.synonyms != null) {
                String[] var2 = this.synonyms;
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    String synonym = var2[var4];
                    if (synonym.equalsIgnoreCase(tableTypeName)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    static TableType getTableTypeCompliantWith(String tableTypeName) {
        TableType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            TableType tableType = var1[var3];
            if (tableType.compliesWith(tableTypeName)) {
                return tableType;
            }
        }

        return UNKNOWN;
    }
}
}
