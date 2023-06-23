package com.itenebris.kinedb.jdbc.util;

import com.itenebris.kinedb.jdbc.KineType;
import com.itenebris.kinedb.jdbc.exceptions.SqlError;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.itenebris.kinedb.jdbc.KineType.*;

public class KineTypeUtils {

    public static Object kineTypeBytesToJavaTypes(KineType kineType, byte[] value) throws SQLException {
        switch (kineType) {
            case BIGINT:
            case TIMESTAMP:
            case TIME:
                return bytesToLong(value);
            case INTEGER:
            case DATE:
                return bytesToInteger(value);
            case SMALLINT:
                return bytesToShort(value);
            case TINYINT:
                return bytesToByte(value);
            case BOOLEAN:
                return bytesToBoolean(value);
            case VARCHAR:
            case CHAR:
            case JSON:
                return new String(value);
            case DECIMAL:
                String str = new String(value);
                return new BigDecimal(str);
            case DOUBLE:
                return bytesToDouble(value);
            case EMPTY:
                return null;
            case ARRAY:
                return bytesArrayToBasicTypeArray(value);
            default:
                throw SqlError.newSQLException(" kineType: "+ kineType +" unKnown");
        }
    }

    /**
     * use {@link java.nio.ByteBuffer} byte[] to long
     * @param input
     * @return
     */
    public static Long bytesToLong(byte[] input) {
        ByteBuffer buffer = ByteBuffer.wrap(input, 0,8);
//        if(littleEndian){
//            buffer.order(ByteOrder.LITTLE_ENDIAN);
//        }
        return buffer.getLong();
    }

    public static Integer bytesToInteger(byte[] input) {
        ByteBuffer buffer = ByteBuffer.wrap(input);
        return buffer.getInt();
    }

    public static Short bytesToShort(byte[] input) {
        ByteBuffer buffer = ByteBuffer.wrap(input);
        return buffer.getShort();
    }

    public static Byte bytesToByte(byte[] input) {
        return input[0];
    }

    public static Boolean bytesToBoolean(byte[] input) {
        ByteBuffer buffer = ByteBuffer.wrap(input);
        return buffer.getInt() == 0 ? false : true;
    }

    public static Double bytesToDouble(byte[] input) {
        ByteBuffer buffer = ByteBuffer.wrap(input);
        return buffer.getDouble();
    }

    public static Object[] bytesArrayToBasicTypeArray(byte[] value) throws SQLException {

        // decode header
        //elementCount := BytesToInt32(data[0:4])
        KineType elementType = kineTypeFromCode(value[4]);
        List<Object> values = new ArrayList<>();

        //var arrayValueBuilder strings.Builder
        // decode elements
        int startIdx = 5;
        int end = value.length;

        while (startIdx+4 <= end) {
            byte[] sizeBytes = new byte[4];
            System.arraycopy(value, startIdx, sizeBytes,0,4);
            int elementSize = bytesToInteger(sizeBytes);

            byte[] elementBytes = new byte[elementSize];
            System.arraycopy(value, startIdx+4 , elementBytes,0, elementSize);

            Object basicValue = kineTypeBytesToJavaTypes(elementType, elementBytes);
            values.add(basicValue);
            // forward
            startIdx = startIdx + 4 + elementSize;
        }

        return values.toArray();
    }

    private static KineType kineTypeFromCode(byte code) throws SQLException {
        switch (code) {
            case 0:
                return EMPTY;
            case 1:
                return UNKNOWN;
            case 2:
                return BIGINT;
            case 3:
                return INTEGER;
            case 4:
                return SMALLINT;
            case 5:
                return TINYINT;
            case 6:
                return BOOLEAN;
            case 7:
                return DATE;
            case 8:;
                return DECIMAL;
            case 9:
                return REAL;
            case 10:
                return DOUBLE;
//            case 11:
//                return HYPER_LOG_LOG
//            case 12:
//                return QDIGEST
//            case 13:
//                return TDIGEST
//            case 14:
//                return P4_HYPER_LOG_LOG
//            case 15:
//                return INTERVAL_DAY_TO_SECOND
//            case 16:
//                return INTERVAL_YEAR_TO_MONTH
            case 17:
                return TIMESTAMP;
//            case 18:
//                return TIMESTAMP_MICROSECONDS
//            case 19:
//                return TIMESTAMP_WITH_TIME_ZONE
            case 20:
                return TIME;
//            case 21:
//                return TIME_WITH_TIME_ZONE
            case 22:
                return VARBINARY;
            case 23:
                return VARCHAR;
            case 24:
                return CHAR;
//            case 25:
//                return ROW
            case 26:
                return ARRAY;
//            case 27:
//                return MAP
            case 28:
                return JSON;
//            case 29:
//                return IPADDRESS
//            case 30:
//                return IPPREFIX
            case 31:
                return GEOMETRY;
//            case 32:
//                return BING_TILE
//            case 33:
//                return BIGINT_ENUM
//            case 34:
//                return VARCHAR_ENUM
//            case 35:
//                return DISTINCT_TYPE
//            case 36:
//                return UUID
            default:
                throw SqlError.newSQLException(" KineTypeFromCode code : "+ code +" unKnown");
        }
    }
}
