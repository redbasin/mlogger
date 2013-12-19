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

public enum MongoInit {

    DEFAULT_HOST("localhost"),

    DEFAULT_PORT("27017"),

    LOG_MONGO_DB("log");

    private final String value;

    private static final Map<String, MongoInit> stringToEnum = new HashMap<String, MongoInit>();

    static { // init map from constant name to enum constant
        for (MongoInit en : values()) {
            stringToEnum.put(en.value, en);
        }
    }

    public static boolean contains(String level) {
        for (MongoInit en : values()) {
            if (en.value.equals(level)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(MongoInit level) {
        for (MongoInit en : values()) {
            if (en.value.equals(level.value)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(MongoInit level) {
        return value.equals(level.value);
    }

    public boolean equals(String level) {
        return value.equals(level);
    }

    MongoInit(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static MongoInit fromString(String value) {
        return stringToEnum.get(value);
    }
}
