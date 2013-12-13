/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redbasin.mongo;


import java.util.HashMap;
import java.util.Map;

/**
 * Mongo logger level fields
 *
 * @author Manoj Joshi
 */

public enum MongoConfig {
    
    CONNECTIONS_PER_HOST(40),
        
    BLOCKABLE_THREADS(10),
    
    CONNECT_TIMEOUT(5000),
    
    MAX_WAIT_TIME(120000),
    
    MAX_AUTO_CONNECT_RETRY_TIME(5000),
    
    W_TIMEOUT(5000);
    
    private final int value;

    private static final Map<Integer, MongoConfig> intToEnum = new HashMap<Integer, MongoConfig>();

    static { // init map from constant name to enum constant
        for (MongoConfig en : values()) {
            intToEnum.put(en.value, en);
        }
    }

    public static boolean contains(int level) {
        for (MongoConfig en : values()) {
            if (en.value == level) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(MongoConfig level) {
        for (MongoConfig en : values()) {
            if (en.value == level.value) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(MongoConfig level) {
        return value == level.value;
    }

    public boolean equals(int level) {
        return value == level;
    }

    MongoConfig(int value) {
        this.value = value;
    }
    
    public boolean higherThan(MongoConfig level) {
        return level.value >= value;
    }

    @Override
    public String toString() {
        return new Integer(value).toString();
    }
    
    public int toInt() {
        return value;
    }

    public static MongoConfig fromInt(int value) {
        return intToEnum.get(value);
    }
}
