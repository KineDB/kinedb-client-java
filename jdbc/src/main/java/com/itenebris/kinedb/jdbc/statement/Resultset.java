package com.itenebris.kinedb.jdbc.statement;

public interface Resultset {

    public static enum Type {
        FORWARD_ONLY(1000);

        private int value;

        private Type(int jdbcRsType) {
            this.value = jdbcRsType;
        }

        public int getIntValue() {
            return this.value;
        }

        public static Type fromValue(int rsType, Type backupValue) {
            Type[] types = values();
            int len = types.length;

            for(int i = 0; i < len; ++i) {
                Type t = types[i];
                if (t.getIntValue() == rsType) {
                    return t;
                }
            }

            return backupValue;
        }
    }

    public static enum Concurrency {
        READ_ONLY(1007),
        UPDATABLE(1008);

        private int value;

        private Concurrency(int jdbcRsConcur) {
            this.value = jdbcRsConcur;
        }

        public int getIntValue() {
            return this.value;
        }

        public static Concurrency fromValue(int concurMode, Concurrency backupValue) {
            Concurrency[] concurrencies = values();
            int len = concurrencies.length;

            for(int i = 0; i < len; ++i) {
                Concurrency c = concurrencies[i];
                if (c.getIntValue() == concurMode) {
                    return c;
                }
            }

            return backupValue;
        }
    }
}
