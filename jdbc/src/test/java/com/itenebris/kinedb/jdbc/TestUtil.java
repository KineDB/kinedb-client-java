package com.itenebris.kinedb.jdbc;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Assert;

import java.sql.*;
import java.util.Properties;

public class TestUtil {

    static String hostPort = "127.0.0.1:10301";

    static String host = "127.0.0.1";
    static int port = 10301;

    static String database = "mysql100";

    private static boolean initialized = false;

//    public static void initDriver() {
//        synchronized (TestUtil.class) {
//            if (initialized) {
//                return;
//            }
//
//            Properties p = loadPropertyFiles("build.properties");
//            p.putAll(System.getProperties());
//            System.getProperties().putAll(p);
//
//            initialized = true;
//        }
//    }

    public static Connection openDB() throws SQLException {
        return openDB(new Properties());
    }

    public static Connection openDBWithDatabase() throws SQLException {
        return openDB(new Properties());
    }

    public static Connection openDB(Properties props) throws SQLException {

        return DriverManager.getConnection(getURL(host, port), props);
    }

    public static Connection openDBWithDatabase(Properties props) throws SQLException {

        return DriverManager.getConnection(getURLWithDatabase(), props);
    }


    /*
     * Helper - closes an open connection.
     */
    public static void closeDB(@Nullable Connection con) throws SQLException {
        if (con != null) {
            con.close();
        }
    }

    public static String getURL(String host, int port) {
        return "jdbc:kine://"
                + host + ":" + port;
    }

    public static String getURLWithDatabase() {
        return "jdbc:kine://"
                + host + ":" + port + "/" + database + "?engine=presto&fetchSize=1000";
    }

    public static void createTable(Connection con, String table, String columns) throws SQLException {
        Statement st = con.createStatement();
        try {
            // Drop the table
            //dropTable(con, table);

            // Now create the table
            String sql = "CREATE TABLE " + table + " (" + columns + ")";

            st.executeUpdate(sql);
        } finally {
            closeQuietly(st);
        }
    }

    public static void dropTable(Connection con, String table) throws SQLException {
        dropObject(con, "TABLE", table);
    }

    private static void dropObject(Connection con, String type, String name) throws SQLException {
        Statement stmt = con.createStatement();
        try {
            // In a transaction so do not ignore errors for missing object
            stmt.executeUpdate("DROP " + type + " " + name );
        } finally {
            closeQuietly(stmt);
        }
    }

    /**
     * Close a Statement and ignore any errors during closing.
     */
    public static void closeQuietly(@Nullable Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignore) {
            }
        }
    }

    /*
     * Helper - generates INSERT SQL - very simple
     */
    public static String insertSQL(String table, String values) {
        return insertSQL(table, null, values);
    }

    public static String insertSQL(String table, String columns, String values) {
        String s = "INSERT INTO " + table;

        if (columns != null) {
            s = s + " (" + columns + ")";
        }

        return s + " VALUES (" + values + ")";
    }

//    public static String updateSQL(String table, String columns, String values) {
//        String s = "UPDATE " + table;
//
//        s = s + " (" + columns + ")";
//
//
//        return s + " VALUES (" + values + ")";
//    }

    /*
     * Helper - generates SELECT SQL - very simple
     */
    public static String selectSQL(String table, String columns) {
        return selectSQL(table, columns, null, null);
    }

    public static String selectSQL(String table, String columns, String where) {
        return selectSQL(table, columns, where, null);
    }

    public static String selectSQL(String table, String columns, String where, String other) {
        String s = "SELECT " + columns + " FROM " + table;

        if (where != null) {
            s = s + " WHERE " + where;
        }
        if (other != null) {
            s = s + " " + other;
        }

        return s;
    }

    /**
     * Execute a SQL query with a given connection and return whether any rows were
     * returned. No column data is fetched.
     */
    public static boolean executeQuery(Connection conn, String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        boolean hasNext = rs.next();
        rs.close();
        stmt.close();
        return hasNext;
    }

    /**
     * Execute a SQL query with a given connection, fetch the first row, and return its
     * string value.
     */
    public static String queryForString(Connection conn, String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        Assert.assertTrue("Query should have returned exactly one row but none was found: " + sql, rs.next());
        String value = rs.getString(1);
        Assert.assertFalse("Query should have returned exactly one row but more than one found: " + sql, rs.next());
        rs.close();
        stmt.close();
        return value;
    }

}
