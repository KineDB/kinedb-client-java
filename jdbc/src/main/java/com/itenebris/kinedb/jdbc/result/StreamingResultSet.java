package com.itenebris.kinedb.jdbc.result;

import com.itenebris.kinedb.jdbc.executor.Kine;
import com.itenebris.kinedb.jdbc.executor.KineData;
import com.itenebris.kinedb.jdbc.statement.Resultset;
import com.sun.rowset.internal.Row;

import java.sql.SQLException;
import java.util.Iterator;

public class StreamingResultSet extends AbstractResultSet {

    private Iterator<Kine.Results> resultsIterator;
    private boolean isAfterEnd = false;
    private boolean noMoreRows = false;
    private boolean streamerClosed = false;

    private int totalFetchRows = 0;

    public StreamingResultSet(Iterator<Kine.Results> resultsIterator) {
        this.resultsIterator = resultsIterator;
        this.currentRow = 0;
    }

    public void close() {
//        boolean hadMore = false;
//        int howMuchMore = 0;
//            while(this.next()) {
//                hadMore = true;
//                ++howMuchMore;
//                if (howMuchMore % 100 == 0) {
//                    Thread.yield();
//                }
//            }
        // now do nothing
        this.resultsIterator = null;
        log.info("StreamingResultSet closed");
    }

    public boolean hasNext() {
        boolean hasNext = this.resultsIterator.hasNext();
        if (!hasNext && !this.streamerClosed) {
            this.streamerClosed = true;
        }
        return hasNext;
    }

    public boolean isAfterLast() {
        return this.isAfterEnd;
    }

    public boolean isBeforeFirst() {
        return this.currentRow < 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isFirst() {
        return this.currentRow == 0;
    }

    public boolean isLast() {
        return !this.isBeforeFirst() && !this.isAfterLast() && this.noMoreRows;
    }

    public boolean next() throws SQLException {
        try {

            if (!setCurrentRow()) {

                // fetch next batch rows
                if (!this.noMoreRows) {

                    if (resultsIterator.hasNext()) {
                        Kine.Results results = resultsIterator.next();
                        if (results.getCode() != 200) {
                            throw new SQLException(results.getMessage());
                        }
                        KineData result = KineData.convertExecuteSqlResult(results);
                        result.buildIndexMapping();
                        this.data = result;
                        this.currentRow = -1;
                        boolean haveMoreRows = setCurrentRow();
                        if (!haveMoreRows) {
                            this.noMoreRows = true;
                            this.isAfterEnd = true;
                        }
                    } else {
                        this.noMoreRows = true;
                        this.isAfterEnd = true;
                    }
                } else {
                    this.isAfterEnd = true;
                }

                if (this.noMoreRows) {
                    this.streamerClosed = true;
                }

                if (!this.noMoreRows && this.totalFetchRows != Integer.MAX_VALUE) {
                    ++this.totalFetchRows;
                }

                return !this.noMoreRows;
            }
            return true;
        } catch (Exception e) {
            this.noMoreRows = true;
            log.error("Iterator stream result set error", e);
            throw e;
        }
    }

    private boolean setCurrentRow() {
        // No traversal is currently complete
        if (hasRows()) {
            log.debug("row num :" + data.rowValues.length + ", currentRow :" + currentRow + ", go next");
            if (data.rowValues.length > 0 && currentRow < data.rowValues.length - 1) {
                currentRow++;
                this.thisRow = this.data.get(currentRow);
                return true;
            }
        }
        return false;
    }

    public int getPosition() throws SQLException {
        throw new SQLException("Operation not allowed for a result set of type ResultSet.TYPE_FORWARD_ONLY.");
    }

    public void afterLast() throws SQLException {
        throw new SQLException("Operation not allowed for a result set of type ResultSet.TYPE_FORWARD_ONLY.");
    }

    public void beforeFirst() throws SQLException {
        throw new SQLException("Operation not allowed for a result set of type ResultSet.TYPE_FORWARD_ONLY.");
    }

    public void beforeLast() throws SQLException {
        throw new SQLException("Operation not allowed for a result set of type ResultSet.TYPE_FORWARD_ONLY.");
    }

    public void moveRowRelative(int rows) throws SQLException {
        throw new SQLException("Operation not allowed for a result set of type ResultSet.TYPE_FORWARD_ONLY.");
    }

    public void setCurrentRow(int rowNumber) throws SQLException {
        throw new SQLException("Operation not allowed for a result set of type ResultSet.TYPE_FORWARD_ONLY.");
    }

}
