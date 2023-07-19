package com.itenebris.kinedb.jdbc;

import org.junit.Test;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StreamingQueryTests extends BaseTest{

    @Test
    public void testStreamingQuery1() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("select * from tpch1g.customer limit 10000");
        pstmt.setFetchSize(1000);
        pstmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            long key = rs.getLong("c_custkey");
            System.out.println("testStreamingQuery1 key is = "+ key);
        }
        rs.close();
        pstmt.close();
    }

    @Test
    public void testStreamingQuery2() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("select * from tpch1g.lineitem limit 10000");
        pstmt.setFetchSize(1000);
        pstmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        ResultSet rs = pstmt.executeQuery();
        int total = 0;
        while(rs.next()) {
            BigDecimal key = rs.getBigDecimal("0");
            System.out.println("testStreamingQuery2 key is = "+ key);
            total++;
        }
        System.out.println("testStreamingQuery2 total rows is = "+ total);
        rs.close();
        pstmt.close();
    }

    @Test
    public void testStreamingQuery() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("select * from mongo_test.user1");
        pstmt.setFetchSize(1000);
        pstmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        ResultSet rs = pstmt.executeQuery();
        int total = 0;
        while(rs.next()) {
            total++;
            System.out.println("testStreamingQuery data = "+ total);
        }
        rs.close();
        pstmt.close();
    }

    @Test
    public void testNotStreamingQuery() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("select * from tpch1g.customer limit 100");
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            long key = rs.getLong("c_custkey");
            System.out.println("testStreamingQuery1 key is = "+ key);
        }
        rs.close();
        pstmt.close();
    }

    @Test
    public void testStreamingQueryNoResults() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("select * from tpch1g.customer where c_custkey > 999999999");
        pstmt.setFetchSize(1000);
        pstmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        ResultSet rs = pstmt.executeQuery();
        int total = 0;
        while(rs.next()) {
            System.out.println("testStreamingQuery1 key is = ");
            total++;
        }
        rs.close();
        pstmt.close();
    }
}
