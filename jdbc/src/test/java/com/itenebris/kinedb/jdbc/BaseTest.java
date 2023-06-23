package com.itenebris.kinedb.jdbc;

import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class BaseTest {

    protected Connection con;

    @Before
    public void setUp() throws Exception {
        Properties props = new Properties();
        con = TestUtil.openDB(props);

    }

    @After
    public void tearDown() throws SQLException {
        TestUtil.closeDB(con);
    }
}
