package com.itenebris.kinedb.jdbc.result;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.itenebris.kinedb.jdbc.KineType;
import com.itenebris.kinedb.jdbc.connection.KineConnection;
import com.itenebris.kinedb.jdbc.exceptions.SqlError;
import com.itenebris.kinedb.jdbc.statement.KineStatement;
import com.itenebris.kinedb.jdbc.util.ConvertUtils;
import com.itenebris.kinedb.jdbc.util.DateUtils;
import com.itenebris.kinedb.jdbc.util.StringUtils;
import com.sun.rowset.internal.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticResultSet extends AbstractResultSet {
	Logger log = LoggerFactory.getLogger(this.getClass());

	public StaticResultSet(ResultData data, KineConnection connection) {
		this.connection = connection;
		this.data = data;
		this.currentRow = -1;
	}

	@Override
	public boolean next() throws SQLException {
		checkClosed();
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

	public ResultData getData() {
		return data;
	}

	public void setData(ResultData data) {
		this.data = data;
	}

	public boolean hasResult() {
		return this.data != null && !this.data.isEmpty();
	}


	public void afterLast() {
		if (this.data.size() > 0) {
			this.currentRow = this.data.size();
		}

	}

	public void beforeFirst() {
		if (this.data.size() > 0) {
			this.currentRow = -1;
		}

	}

	public void beforeLast() {
		if (this.data.size() > 0) {
			this.currentRow = this.data.size() - 2;
		}

	}

	public int getPosition() {
		return this.currentRow;
	}

	public boolean hasNext() {
		boolean hasMore = this.currentRow + 1 < this.data.size();
		return hasMore;
	}

	public boolean isAfterLast() {
		return this.currentRow >= this.data.size() && this.data.size() != 0;
	}

	public boolean isBeforeFirst() {
		return this.currentRow == -1 && this.data.size() != 0;
	}

	public boolean isDynamic() {
		return false;
	}

	public boolean isEmpty() {
		return this.data.size() == 0;
	}

	public boolean isFirst() {
		return this.currentRow == 0;
	}

	public boolean isLast() {
		if (this.data.size() == 0) {
			return false;
		} else {
			return this.currentRow == this.data.size() - 1;
		}
	}

	public void moveRowRelative(int rowsToMove) {
		if (this.data.size() > 0) {
			this.currentRow += rowsToMove;
			if (this.currentRow < -1) {
				this.beforeFirst();
			} else if (this.currentRow > this.data.size()) {
				this.afterLast();
			}
		}

	}

//	public void remove() {
//		this.data.rowValues.remove(this.getPosition());
//	}

	public void setCurrentRow(int newIndex) {
		this.currentRow = newIndex;
	}

	public int size() {
		return this.data.size();
	}

	public boolean wasEmpty() {
		return this.data != null && this.data.size() == 0;
	}

}
