package com.itenebris.kinedb.jdbc.connection;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import okhttp3.OkHttpClient;

public class DriverUri {

    Logger log = LoggerFactory.getLogger(DriverUri.class);

    private static final String JDBC_URL_START = "jdbc:";
    private URI uri;
    private String database;
    private String user;
    private boolean useSecureConnection;

    public DriverUri(String url, Properties properties) throws SQLException {
        this.uri = Objects.requireNonNull(parseDriverUrl(url), "uri is null");

        // TODO properties
        this.user = "";
        this.useSecureConnection = false;

        // no database in kine db
        initDatabase();
    }

    private URI parseDriverUrl(String url) throws SQLException {
        URI uri;
        try {
            uri = new URI(url.substring(JDBC_URL_START.length()));
        } catch (URISyntaxException e) {
            throw new SQLException("Invalid JDBC URL: " + url, e);
        }
        if (Strings.isNullOrEmpty(uri.getHost())) {
            throw new SQLException("No host specified: " + url);
        }
        if (uri.getPort() == -1) {
            throw new SQLException("No port number specified: " + url);
        }
        if ((uri.getPort() < 1) || (uri.getPort() > 65535)) {
            throw new SQLException("Invalid port number: " + url);
        }
        log.debug("uri: " + uri);
        return uri;
    }

    private void initDatabase() throws SQLException {
        String path = uri.getPath();
        log.debug("path: " + path);
        if (Strings.isNullOrEmpty(uri.getPath()) || path.equals("/")) {
            return;
        }

        // remove first slash
        if (!path.startsWith("/")) {
            throw new SQLException("Path does not start with a slash: " + uri);
        }
        path = path.substring(1);

        List<String> parts = Splitter.on("/").splitToList(path);
        // remove last item due to a trailing slash
        if (parts.get(parts.size() - 1).isEmpty()) {
            parts = parts.subList(0, parts.size() - 1);
        }

        if (parts.size() > 1) {
            throw new SQLException("Invalid path segments in URL: " + uri);
        }

        if (parts.get(0).isEmpty()) {
            System.out.println("NO Database name set for jdbc url");
            //throw new SQLException("Database name is empty: " + uri);
        }
        database = parts.get(0);
        log.debug("database: " + database);
    }

    private String buildHttpUri() {
        String scheme = useSecureConnection ? "https" : "http";
        return scheme + "://" + this.uri.getHost() + ":" + this.uri.getPort();
    }

    public void setupHtttpClient(OkHttpClient.Builder builder) throws SQLException {
        try {
            // TODO add properties
        } catch (RuntimeException e) {
            throw new SQLException("Error setting up connection", e);
        }
    }

    public URI getJdbcUri() {
        return uri;
    }

    public String getDatabase() {
        return database;
    }

    public String getHttpUri() {
        return buildHttpUri();
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "DriverUri{" +
                "uri=" + uri +
                ", database='" + database + '\'' +
                ", user='" + user + '\'' +
                ", useSecureConnection=" + useSecureConnection +
                '}';
    }
}
