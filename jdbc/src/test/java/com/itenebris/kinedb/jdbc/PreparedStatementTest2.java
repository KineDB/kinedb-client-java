package com.itenebris.kinedb.jdbc;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreparedStatementTest2 extends BaseTest2{

    Logger log = LoggerFactory.getLogger(this.getClass());
    @Test
    public void testSimpleQuery() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("SELECT id ,name,age from user1");

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        log.debug("testSimpleQuery ResultSet {}", rs);
        pstmt.close();
    }

    @Test
    public void testSimpleQuery2() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("SELECT id ,name,age from user1 where id > ? and age > ?");
        pstmt.setInt(1, 1);
        pstmt.setInt(2, 1);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        log.debug("testSimpleQuery2 ResultSet {}", rs);
        pstmt.close();
    }

    @Test
    public void testInsert() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("INSERT INTO user1 (id ,name, age) values (?, ?, ?)");
        pstmt.setInt(1, 1011);
        pstmt.setString(2, "name1001");
        pstmt.setInt(3, 1021);
        boolean result = pstmt.execute();
        log.debug("testInsert result {}", result);
        pstmt.close();
    }

    @Test
    public void testUpdate() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("UPDATE user1 set name=?, age=? where id = ?");
        pstmt.setString(1, "name1101");
        pstmt.setInt(2, 11);
        pstmt.setInt(3, 1001);
        boolean result = pstmt.execute();
        log.debug("testUpdate result {}", result);
        pstmt.close();
    }

    @Test
    public void testDelete() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("DELETE FROM user1 where id = ?");
        pstmt.setInt(1, 1001);
        boolean result = pstmt.execute();
        log.debug("testDelete result {}", result);
        pstmt.close();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //TestUtil.createTable(con, "mysql100.user1", "id bigint primary key not null, name varchar(20) not null, age integer");
    }

    @Override
    public void tearDown() throws SQLException {
        //TestUtil.dropTable(con, "mysql100.user1");
        super.tearDown();
    }
}
