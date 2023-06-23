package com.itenebris.kinedb.jdbc.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 基本数据类型之间的相互转换,对象为空返回空对象,或者0
 * 包括String,byte,short,int,long,float,double,BigDecimal等等转换操作
 * 长度：byte 1字节,char 1字节,short 2字节,int 4字节,long 8字节,float 4字节,double 8字节.
 * 注意：1,java系统里是高位字节在前. 2,基本类型转成对象类型尽量少用 new, 这里不再提供方法.
 * Integer 最大数 2147483647, Long 最大数 922337203 6854775808
 *
 */
public class ConvertUtils {
    private static final int scalev = 4;
    private static SimpleDateFormat longFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat shortfromat = new SimpleDateFormat("yyyy-MM-dd");

    /** 根据未知类型,把字符转换成相应类型对象值,返回Object */
    public static Object getObject(String attrVal, Class<?> clazz) {
        String str = trimEmpty(attrVal);
        if(str == null  || clazz == null) return null;
        try {
            if(String.class.isAssignableFrom(clazz)) {
                return str;
            } else if(Integer.class.isAssignableFrom(clazz)) {
                return getInt(str);
            } else if(Long.class.isAssignableFrom(clazz)) {
                return getLong(str);
            } else if(Double.class.isAssignableFrom(clazz)) {
                return getDouble(str);
            } else if(BigDecimal.class.isAssignableFrom(clazz)) {
                return getBigDecimal(str);
            } else if(Float.class.isAssignableFrom(clazz)) {
                return getFloat(str);
            } else if(Short.class.isAssignableFrom(clazz)) {
                return getShort(str);
            } else if(Byte.class.isAssignableFrom(clazz)) {
                return getByte(str);
            } else if(int.class.isAssignableFrom(clazz)) {
                return getint(str);
            } else if(long.class.isAssignableFrom(clazz)) {
                return getlong(str);
            } else if(double.class.isAssignableFrom(clazz)) {
                return getDoubleValue(str);
            } else if(float.class.isAssignableFrom(clazz)) {
                return getfloat(str);
            } else if(short.class.isAssignableFrom(clazz)) {
                return getshort(str);
            } else if(Date.class.isAssignableFrom(clazz)) {
                return getDate(str);
            }
        } catch(Exception e) {
            System.out.println("类型转换错误:");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /** 根据已知T类型,把字符转换成相应T类型对象值,返回T */
    public static <T> T getObject(String attrVal, Class<T> clazz, T defval) {
        String str = trimEmpty(attrVal);
        if(str == null  || clazz == null) return null;
        try {
            if(String.class.isAssignableFrom(clazz)) {
                return clazz.cast(str);
            } else if(Integer.class.isAssignableFrom(clazz)) {
                return clazz.cast(getInt(str));
            } else if(Long.class.isAssignableFrom(clazz)) {
                return clazz.cast(getLong(str));
            } else if(Double.class.isAssignableFrom(clazz)) {
                return clazz.cast(getDouble(str));
            } else if(BigDecimal.class.isAssignableFrom(clazz)) {
                return clazz.cast(getBigDecimal(str));
            } else if(Float.class.isAssignableFrom(clazz)) {
                return clazz.cast(getFloat(str));
            } else if(Short.class.isAssignableFrom(clazz)) {
                return clazz.cast(getShort(str));
            } else if(Byte.class.isAssignableFrom(clazz)) {
                return clazz.cast(getByte(str));
            } else if(int.class.isAssignableFrom(clazz)) {
                return clazz.cast(getint(str));
            } else if(long.class.isAssignableFrom(clazz)) {
                return clazz.cast(getlong(str));
            } else if(double.class.isAssignableFrom(clazz)) {
                return clazz.cast(getDoubleValue(str));
            } else if(float.class.isAssignableFrom(clazz)) {
                return clazz.cast(getfloat(str));
            } else if(short.class.isAssignableFrom(clazz)) {
                return clazz.cast(getshort(str));
            } else if(Date.class.isAssignableFrom(clazz)) {
                return clazz.cast(getDate(str));
            }
        } catch(Exception e) {
            System.out.println("类型转换错误:");
            e.printStackTrace();
            return defval;
        }
        return defval;
    }


    /**
     * 初始日期格式化对象,当日期格式化时发生某种异常时，格式化对象会变得失效,
     * 因此，如果日期格式产生异常，再初始化一个新对象。
     */
    private static void initDateFormat() {
        longFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    public static String getString(Double o){
        return o ==null? null : Double.toString(o.doubleValue());
    }
    public static String getString(Float o){
        return o ==null? null : Double.toString(o.floatValue());
    }
    public static String getString(Integer o){
        return o ==null? null : Integer.toString(o.intValue());
    }
    public static String getString(Long o){
        return o ==null? null : Long.toString(o.longValue());
    }
    public static String getString(Short o){
        return o ==null? null : Short.toString(o.shortValue());
    }
    public static String getString(BigDecimal o){
        return o ==null? null : o.toString();
    }
    public static String getString(String o){
        return trimEmpty(o);
    }
    public static String trim(String o){
        return o ==null? null : o.trim();
    }
    public static String trimEmpty(String o){
        if(o==null) return null;
        String str = o.trim();
        if(str==null ||str.equals("") ||str.equals("null") ||str.equals("NULL")) return null;
        return str;
    }
    public static String getString(Double o, int scale){
        return o ==null? null : BigDecimal.valueOf(o.doubleValue())
                .setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String getString(Float o, int scale){
        return o ==null? null : BigDecimal.valueOf(o.doubleValue())
                .setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String getString(BigDecimal o, int scale){
        return o ==null? null : o.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String getString(Object data){
        if(data == null) return null;
        if(data instanceof String){
            return trimEmpty((String)data);
        }
        if(data instanceof Integer){
            return Integer.toString(((Integer)data).intValue());
        }
        if(data instanceof Long){
            return Long.toString(((Long)data).longValue());
        }
        if(data instanceof Double){
            return BigDecimal.valueOf(((Double)data).doubleValue()).setScale(scalev, BigDecimal.ROUND_HALF_UP).toString();
        }
        if(data instanceof Float){
            return BigDecimal.valueOf(((Float)data).doubleValue()).setScale(scalev, BigDecimal.ROUND_HALF_UP).toString();
        }
        if(data instanceof BigDecimal){
            BigDecimal bb =(BigDecimal)data;
            return bb.scale()==0? bb.toString() : bb.setScale(scalev, BigDecimal.ROUND_HALF_UP).toString();
        }
        if(data instanceof Short){
            return Short.toString(((Short)data).shortValue());
        }
        if(data instanceof Byte){
            return Byte.toString(((Byte)data).byteValue());
        }//日期对象不处理
        String s = String.valueOf(data).trim();
        s = trimEmpty(s);
        data = null;
        return s;
    }

    public static String getStringEmpty(Double o){
        return o ==null? "" : o.toString();
    }
    public static String getStringEmpty(Float o){
        return o ==null? "" : o.toString();
    }
    public static String getStringEmpty(Integer o){
        return o ==null? "" : o.toString();
    }
    public static String getStringEmpty(Long o){
        return o ==null? "" : o.toString();
    }
    public static String getStringEmpty(Short o){
        return o ==null? "" : o.toString();
    }
    public static String getStringEmpty(BigDecimal o){
        return o ==null? "" : o.toString();
    }
    public static String getStringEmpty(String o){
        return o ==null? "" : o;
    }
    public static String getStringEmpty(Double o, int scale){
        return o ==null? "" : BigDecimal.valueOf(o.doubleValue())
                .setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String getStringEmpty(Float o, int scale){
        return o ==null? "" : BigDecimal.valueOf(o.doubleValue())
                .setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String getStringEmpty(BigDecimal o, int scale){
        return o ==null? "" : o.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String getStringEmpty(Object data){
        if(data == null) return "";
        return String.valueOf(data).trim();
    }

    public static String getSqlStringOrEmpty(Object data){
        if(data == null) return null;
        return "'" + String.valueOf(data).trim() + "'";
    }

    public static int getint(Double o){
        return o ==null? 0: o.intValue();
    }
    public static int getint(Float o){
        return o ==null? 0: o.intValue();
    }
    public static int getint(Integer o){
        return o ==null? 0: o.intValue();
    }
    public static int getint(Long o){
        return o ==null? 0: o.intValue();
    }
    public static int getint(Short o){
        return o ==null? 0: o.intValue();
    }
    public static int getint(BigDecimal o){
        return o ==null? 0: o.intValue();
    }
    public static int getint(String o){
        o = trimEmpty(o);
        if(o == null) return 0;
        int x=0;
        try {
            x = Integer.parseInt(o);
        } catch (java.lang.Exception e) {
            x=0;
            System.out.println("字符转换成 Integer 数据错误: "+o);
        }
        return x;
    }

    public static Integer getInt(Double o){
        return o ==null? null: Integer.valueOf(o.intValue());
    }
    public static Integer getInt(Float o){
        return o ==null? null: Integer.valueOf(o.intValue());
    }
    public static Integer getInt(Integer o){
        return o ==null? null: Integer.valueOf(o.intValue());
    }
    public static Integer getInt(Long o){
        return o ==null? null: Integer.valueOf(o.intValue());
    }
    public static Integer getInt(Short o){
        return o ==null? null: Integer.valueOf(o.intValue());
    }
    public static Integer getInt(BigDecimal o){
        return o ==null? null: Integer.valueOf(o.intValue());
    }
    public static Integer getInt(String o){
        o = trimEmpty(o);
        if(o == null) return null;
        Integer x=null;
        try {
            x = Integer.valueOf(o);
        } catch (java.lang.Exception e) {
            x=null;
            System.out.println("字符转换成 Integer 数据错误: "+o);
        }
        return x;
    }

    public static Integer getInteger(Object data){
        if(data==null){ return null; }
        if(data instanceof Integer){
            return (Integer)data;
        }
        if(data instanceof Long){
            return Integer.valueOf(((Long)data).intValue());
        }
        if(data instanceof Short){
            return Integer.valueOf(((Short)data).intValue());
        }
        if(data instanceof Double){
            return Integer.valueOf(((Double)data).intValue());
        }
        if(data instanceof Float){
            return Integer.valueOf(((Float)data).intValue());
        }
        if(data instanceof BigDecimal){
            return Integer.valueOf(((BigDecimal)data).intValue());
        }
        if(data instanceof Byte){
            return Integer.valueOf(((Byte)data).intValue());
        }
        String s = String.valueOf(data).trim();
        if(s.equals("")){ s = null; return null; }
        Integer v = null;
        try {
            v = new Integer(s);
        } catch (Exception e) {
            data = null;  v = null;
            System.out.println("对象 "+s+" 转换成 Integer 数据错误.");
        } finally {
            data = null;
            s = null;
        }
        return v;
    }

    public static double getDoubleValue(Double o){
        return o ==null? 0: o.doubleValue();
    }

    public static double getDoubleValue(Float o){
        return o ==null? 0: o.doubleValue();
    }

    public static double getDoubleValue(Integer o){
        return o ==null? 0: o.doubleValue();
    }
    public static double getDoubleValue(Long o){
        return o ==null? 0: o.doubleValue();
    }
    public static double getDoubleValue(Short o){
        return o ==null? 0: o.doubleValue();
    }
    public static double getDoubleValue(BigDecimal o){
        return o ==null? 0: o.doubleValue();
    }
    public static double getDoubleValue(String o){
        o = trimEmpty(o);
        if(o == null) return 0d;
        double x=0;
        try {
            x = Double.parseDouble(o);
        } catch (java.lang.Exception e) {
            x=0;
            System.out.println("字符转换成 Double 数据错误: "+o);
        }
        return x;
    }

    public static Double getDouble(Double o){
        return o ==null? null: Double.valueOf(o.doubleValue());
    }
    public static Double getDouble(Float o){
        return o ==null? null: Double.valueOf(o.doubleValue());
    }
    public static Double getDouble(Integer o){
        return o ==null? null: Double.valueOf(o.doubleValue());
    }
    public static Double getDouble(Long o){
        return o ==null? null: Double.valueOf(o.doubleValue());
    }
    public static Double getDouble(Short o){
        return o ==null? null: Double.valueOf(o.doubleValue());
    }
    public static Double getDouble(BigDecimal o){
        return o ==null? null: Double.valueOf(o.doubleValue());
    }
    public static Double getDouble(String o){
        o = trimEmpty(o);
        if(o == null) return null;
        Double x=null;
        try {
            x = Double.valueOf(o);
        } catch (java.lang.Exception e) {
            x=null;
            System.out.println("字符转换成 Double 数据错误: "+o);
        }
        return x;
    }

    public static Double getDouble(Object data){
        if(data==null){ return null; }
        if(data instanceof Double){
            return (Double)data;
        }
        if(data instanceof Float){
            return new Double(((Float)data).doubleValue());
        }
        if(data instanceof BigDecimal){
            return new Double(((BigDecimal)data).doubleValue());
        }
        if(data instanceof Long){
            return new Double(((Long)data).doubleValue());
        }
        if(data instanceof Integer){
            return new Double(((Integer)data).doubleValue());
        }
        if(data instanceof Short){
            return new Double(((Short)data).doubleValue());
        }
        if(data instanceof Byte){
            return new Double(((Byte)data).doubleValue());
        }
        String s = String.valueOf(data).trim();
        if(s.equals("")){ s = null; return null; }
        Double v = null;
        try {
            v = new Double(s);
        } catch (Exception e) {
            data = null;  v = null;
            System.out.println("对象 "+s+" 转换成 Double 数据错误.");
        } finally {
            data = null;
            s = null;
        }
        return v;
    }


    public static float getfloat(Double o){
        return o ==null? 0: o.floatValue();
    }
    public static float getfloat(Float o){
        return o ==null? 0: o.floatValue();
    }
    public static float getfloat(Integer o){
        return o ==null? 0: o.floatValue();
    }
    public static float getfloat(Long o){
        return o ==null? 0: o.floatValue();
    }
    public static float getfloat(Short o){
        return o ==null? 0: o.floatValue();
    }
    public static float getfloat(BigDecimal o){
        return o ==null? 0: o.floatValue();
    }
    public static float getfloat(String o){
        o = trimEmpty(o);
        if(o == null) return 0F;
        float x=0;
        try {
            x = Float.parseFloat(o);
        } catch (java.lang.Exception e) {
            x=0;
            System.out.println("字符转换成 Float 数据错误: "+o);
        }
        return x;
    }

    public static Float getFloat(Double o){
        return o ==null? null: Float.valueOf(o.floatValue());
    }
    public static Float getFloat(Float o){
        return o ==null? null: Float.valueOf(o.floatValue());
    }
    public static Float getFloat(Integer o){
        return o ==null? null: Float.valueOf(o.floatValue());
    }
    public static Float getFloat(Long o){
        return o ==null? null: Float.valueOf(o.floatValue());
    }
    public static Float getFloat(Short o){
        return o ==null? null: Float.valueOf(o.floatValue());
    }
    public static Float getFloat(BigDecimal o){
        return o ==null? null: Float.valueOf(o.floatValue());
    }
    public static Float getFloat(String o){
        o = trimEmpty(o);
        if(o == null) return null;
        Float x=null;
        try {
            x = Float.valueOf(o);
        } catch (java.lang.Exception e) {
            x=null;
            System.out.println("字符转换成 Float 数据错误: "+o);
        }
        return x;
    }

    public static Float getFloat(Object data){
        if(data==null){ return null; }
        if(data instanceof Float){
            return (Float)data;
        }
        if(data instanceof Double){
            return new Float(((Double)data).floatValue());
        }
        if(data instanceof BigDecimal){
            return new Float(((BigDecimal)data).floatValue());
        }
        if(data instanceof Long){
            return new Float(((Long)data).floatValue());
        }
        if(data instanceof Integer){
            return new Float(((Integer)data).floatValue());
        }
        if(data instanceof Short){
            return new Float(((Short)data).floatValue());
        }
        if(data instanceof Byte){
            return new Float(((Byte)data).floatValue());
        }
        String s = String.valueOf(data).trim();
        if(s.equals("")){ s = null; return null; }
        Float v = null;
        try {
            v = new Float(s);
        } catch (Exception e) {
            data = null;  v = null;
            System.out.println("对象 "+s+" 转换成 Float 数据错误.");
        } finally {
            data = null;
            s = null;
        }
        return v;
    }


    public static long getlong(Double o){
        return o ==null? 0: o.longValue();
    }
    public static long getlong(Float o){
        return o ==null? 0: o.longValue();
    }
    public static long getlong(Integer o){
        return o ==null? 0: o.longValue();
    }
    public static long getlong(Long o){
        return o ==null? 0: o.longValue();
    }
    public static long getlong(Short o){
        return o ==null? 0: o.longValue();
    }
    public static long getlong(BigDecimal o){
        return o ==null? 0: o.longValue();
    }
    public static long getlong(Date date) {
        if(date==null) return 0;
        long l = date.getTime();
        return l;
    }
    public static long getlong(String o){
        o = trimEmpty(o);
        if(o == null) return 0L;
        long x=0;
        try {
            x = Long.parseLong(o);
        } catch (java.lang.Exception e) {
            x=0;
            System.out.println("字符转换成 Long 数据错误: "+o);
        }
        return x;
    }

    public static Long getLong(Double o){
        return o ==null? null: Long.valueOf(o.longValue());
    }
    public static Long getLong(Float o){
        return o ==null? null: Long.valueOf(o.longValue());
    }
    public static Long getLong(Integer o){
        return o ==null? null: Long.valueOf(o.longValue());
    }
    public static Long getLong(Long o){
        return o ==null? null: Long.valueOf(o.longValue());
    }
    public static Long getLong(Short o){
        return o ==null? null: Long.valueOf(o.longValue());
    }
    public static Long getLong(BigDecimal o){
        return o ==null? null: Long.valueOf(o.longValue());
    }

    public static Long getLong(Date date) {
        if(date==null) return null;
        long l = date.getTime();
        return l;
    }

    public static Long getLong(String o){
        o = trimEmpty(o);
        if(o == null) return null;
        Long x=null;
        try {
            x = Long.valueOf(o);
        } catch (java.lang.Exception e) {
            x=null;
            System.out.println("字符转换成 Long 数据错误: "+o);
        }
        return x;
    }

    public static Long getLong(Object data){
        if(data==null){ return null; }
        if(data instanceof Long){
            return (Long)data;
        }
        if(data instanceof Integer){
            return Long.valueOf(((Integer)data).longValue());
        }
        if(data instanceof Short){
            return Long.valueOf(((Short)data).longValue());
        }
        if(data instanceof Double){
            return Long.valueOf(((Double)data).longValue());
        }
        if(data instanceof Float){
            return Long.valueOf(((Float)data).longValue());
        }
        if(data instanceof BigDecimal){
            return Long.valueOf(((BigDecimal)data).longValue());
        }
        if(data instanceof Byte){
            return Long.valueOf(((Byte)data).longValue());
        }
        String s = String.valueOf(data).trim();
        if(s.equals("")){ s = null; return null; }
        Long v = null;
        try {
            v = new Long(s);
        } catch (Exception e) {
            data = null;  v = null;
            System.out.println("对象 "+s+" 转换成 Long 数据错误.");
        } finally {
            data = null;
            s = null;
        }
        return v;
    }

    public static short getshort(Double o){
        return o ==null? 0: o.shortValue();
    }
    public static short getshort(Float o){
        return o ==null? 0: o.shortValue();
    }
    public static short getshort(Integer o){
        return o ==null? 0: o.shortValue();
    }
    public static short getshort(Long o){
        return o ==null? 0: o.shortValue();
    }
    public static short getshort(Short o){
        return o ==null? 0: o.shortValue();
    }
    public static short getshort(BigDecimal o){
        return o ==null? 0: o.shortValue();
    }
    public static short getshort(String o){
        o = trimEmpty(o);
        if(o == null) return 0;
        short x=0;
        try {
            x = Short.parseShort(o);
        } catch (java.lang.Exception e) {
            x=0;
            System.out.println("字符转换成 Short 数据错误: "+o);
        }
        return x;
    }

    public static Short getShort(Double o){
        return o ==null? null: Short.valueOf(o.shortValue());
    }
    public static Short getShort(Float o){
        return o ==null? null: Short.valueOf(o.shortValue());
    }
    public static Short getShort(Integer o){
        return o ==null? null: Short.valueOf(o.shortValue());
    }
    public static Short getShort(Long o){
        return o ==null? null: Short.valueOf(o.shortValue());
    }
    public static Short getShort(Short o){
        return o ==null? null: Short.valueOf(o.shortValue());
    }
    public static Short getShort(BigDecimal o){
        return o ==null? null: Short.valueOf(o.shortValue());
    }
    public static Short getShort(String o){
        o = trimEmpty(o);
        if(o == null) return null;
        Short x=null;
        try {
            x = Short.valueOf(o);
        } catch (java.lang.Exception e) {
            x=null;
            System.out.println("字符转换成 Short 数据错误: "+o);
        }
        return x;
    }

    public static Short getShort(Object data){
        if(data==null){ return null; }
        if(data instanceof Short){
            return (Short)data;
        }
        if(data instanceof Long){
            return Short.valueOf(((Long)data).shortValue());
        }
        if(data instanceof Integer){
            return Short.valueOf(((Short)data).shortValue());
        }
        if(data instanceof Double){
            return Short.valueOf(((Double)data).shortValue());
        }
        if(data instanceof Float){
            return Short.valueOf(((Float)data).shortValue());
        }
        if(data instanceof BigDecimal){
            return Short.valueOf(((BigDecimal)data).shortValue());
        }
        if(data instanceof Byte){
            return Short.valueOf(((Byte)data).shortValue());
        }
        String s = String.valueOf(data).trim();
        if(s.equals("")){ s = null; return null; }
        Short v = null;
        try {
            v = new Short(s);
        } catch (Exception e) {
            data = null;  v = null;
            System.out.println("对象 "+s+" 转换成 Integer 数据错误.");
        } finally {
            data = null;
            s = null;
        }
        return v;
    }


    public static Byte getByte(Object data){
        if(data==null){ return null; }
        if(data instanceof Byte){
            return (Byte)data;
        }
        if(data instanceof Short){
            return Byte.valueOf(((Short)data).byteValue());
        }
        if(data instanceof Long){
            return Byte.valueOf(((Long)data).byteValue());
        }
        if(data instanceof Integer){
            return Byte.valueOf(((Short)data).byteValue());
        }
        if(data instanceof Double){
            return Byte.valueOf(((Double)data).byteValue());
        }
        if(data instanceof Float){
            return Byte.valueOf(((Float)data).byteValue());
        }
        if(data instanceof BigDecimal){
            return Byte.valueOf(((BigDecimal)data).byteValue());
        }
        String s = String.valueOf(data).trim();
        if(s.equals("")){ s = null; return null; }
        Byte v = null;
        try {
            v = new Byte(s);
        } catch (Exception e) {
            data = null;  v = null;
            System.out.println("对象 "+s+" 转换成 Integer 数据错误.");
        } finally {
            data = null;
            s = null;
        }
        return v;
    }


    public static BigDecimal getBigDecimal(Double o){
        return o ==null? null: BigDecimal.valueOf(o.doubleValue());
    }
    public static BigDecimal getBigDecimal(Float o){
        return o ==null? null: BigDecimal.valueOf(o.doubleValue());
    }
    public static BigDecimal getBigDecimal(Integer o){
        return o ==null? null: BigDecimal.valueOf(o.longValue());
    }
    public static BigDecimal getBigDecimal(Long o){
        return o ==null? null: BigDecimal.valueOf(o.longValue());
    }
    public static BigDecimal getBigDecimal(Short o){
        return o ==null? null: BigDecimal.valueOf(o.longValue());
    }
    public static BigDecimal getBigDecimal(String o){
        o = trimEmpty(o);
        if(o == null) return null;
        BigDecimal v=null;
        try {
            v = new BigDecimal(o);
        } catch (java.lang.Exception e) {
            v=null;
            System.out.println("字符转换成 BigDecimal 数据错误:"+o);
        }
        return v;
    }
    public static BigDecimal getBigDecimal(Double o, int scale){
        return o ==null? null : BigDecimal.valueOf(o.doubleValue())
                .setScale(scale, BigDecimal.ROUND_HALF_UP);
    }
    public static BigDecimal getBigDecimal(Float o, int scale){
        return o ==null? null : BigDecimal.valueOf(o.doubleValue())
                .setScale(scale, BigDecimal.ROUND_HALF_UP);
    }
    public static BigDecimal getBigDecimal(BigDecimal o, int scale){
        return o ==null? null : o.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getBigDecimal(Object data){
        return getBigDecimal(data, scalev);
    }

    public static BigDecimal getBigDecimal(Object data,int scale){
        if(data==null){ return null; }
        if(data instanceof BigDecimal){
            return ((BigDecimal)data).setScale(scale, BigDecimal.ROUND_HALF_UP);
        }
        if(data instanceof Double){
            BigDecimal v = BigDecimal.valueOf(((Double)data).doubleValue())
                    .setScale(scale, BigDecimal.ROUND_HALF_UP);
            return v;
        }
        if(data instanceof Float){
            BigDecimal v = BigDecimal.valueOf(((Float)data).doubleValue())
                    .setScale(scale, BigDecimal.ROUND_HALF_UP);
            return v;
        }
        if(data instanceof Long){
            return BigDecimal.valueOf(((Long)data).longValue());
        }
        if(data instanceof Integer){
            return BigDecimal.valueOf(((Integer)data).longValue());
        }
        String s = String.valueOf(data).trim();
        if(s.equals("")){ s = null; return null; }
        BigDecimal v = null;
        try {
            v = new BigDecimal(s).setScale(scale, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            data = null;  v = null;
            System.out.println("对象 "+s+" 转换成 BigDecimal 数据错误.");
        } finally {
            data = null; s = null;
        }
        return v;
    }

    public static Date getDate(String o){
        o = trimEmpty(o);
        if(o == null) return null;
        Date v=null;
        try {
            if (o.length() == 10) o = o + " 00:00:00";
            v = longFromat.parse(o);
        } catch (java.lang.Exception e) {
            v=null;
            System.out.println("字符转换成 Date 数据错误: "+o);
        }
        return v;
    }


    public static Date getDate(Object data){
        if(data==null){ return null; }
        if(data instanceof Long){
            long ll = ((Long)data).longValue();
            return ll==0? null: new Date(ll);
        }
        if(data instanceof Integer){
            long ll = ((Integer)data).longValue();
            return ll==0? null: new Date(ll);
        }
        if(data instanceof BigDecimal){
            long ll = ((BigDecimal)data).longValue();
            return ll==0? null: new Date(ll);
        }
        if(data instanceof Date){
            return (Date)data;
        }
        if(data instanceof java.sql.Date){
            return (Date)data;
        }
        if(data instanceof java.sql.Timestamp){
            return (Date)data;
        }
        String s = String.valueOf(data).trim();
        if(s.equals("")){ s = null; return null; }
        Date v = null;
        try {
            if (s.length() == 10) s = s + " 00:00:00";
            v = longFromat.parse(s);
        } catch (Exception e) {
            initDateFormat();
            data = null;  v = null;
            System.out.println("对象 "+s+" 转换成 Date 数据错误.");
        } finally {
            data = null;
            s = null;
        }
        return v;
    }

    public static Date getDate(Object data, SimpleDateFormat fromat){
        if(data==null){ return null; }
        if(data instanceof Long){
            long ll = ((Long)data).longValue();
            return ll==0? null: new Date(ll);
        }
        if(data instanceof Integer){
            long ll = ((Integer)data).longValue();
            return ll==0? null: new Date(ll);
        }
        if(data instanceof BigDecimal){
            long ll = ((BigDecimal)data).longValue();
            return ll==0? null: new Date(ll);
        }
        if(data instanceof Date){
            return (Date)data;
        }
        if(data instanceof java.sql.Date){
            return (Date)data;
        }
        if(data instanceof java.sql.Timestamp){
            return (Date)data;
        }
        String s = String.valueOf(data).trim();
        if(s.equals("")){ s = null; return null; }
        Date v = null;
        try {
            if (s.length() == 10) s = s + " 00:00:00";
            v = fromat.parse(s);
        } catch (Exception e) {
            data = null;  v = null;
            System.out.println("对象 "+s+" 转换成 Date 数据错误.");
        } finally {
            data = null;
            s = null;
        }
        return v;
    }

}
 
