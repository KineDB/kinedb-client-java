package com.itenebris.kinedb.jdbc;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itenebris.kinedb.jdbc.connection.KineConnection;
import com.itenebris.kinedb.jdbc.connection.DriverUri;

public class KineDriver implements Driver, Closeable {

    Logger log = LoggerFactory.getLogger(KineDriver.class);

    public static final String DRIVER_NAME = "KineDB JDBC Driver";
    public static final String DRIVER_VERSION;
    public static final int DRIVER_VERSION_MAJOR;
    public static final int DRIVER_VERSION_MINOR;
    private static final String DRIVER_URL_START = "jdbc:kine:";
    static {
        // 我们通过静态构造函数注册我们的驱动，如果了解golang 的sql 驱动的话，也是一样的，支持golang 基于了 init 函数进行驱动的注册
        try {
            DRIVER_VERSION_MAJOR = 3;
            DRIVER_VERSION_MINOR = 1;
            DRIVER_VERSION = "1.0.0";
            KineDriver driver = new KineDriver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        // http connect demo
    }

    // implements connect manage connect
    @Override
    public Connection connect(String url, Properties properties) throws SQLException {
        log.debug("jdbc url: " + url);
        if (!acceptsURL(url))
            return null;

        DriverUri uri = new DriverUri(url, properties);
        return new KineConnection(uri);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith(DRIVER_URL_START);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return DRIVER_VERSION_MAJOR;
    }

    @Override
    public int getMinorVersion() {
        return DRIVER_VERSION_MINOR;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        // TODO: support java.util.Logging
        throw new SQLFeatureNotSupportedException();
    }
}
