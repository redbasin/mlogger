/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redbasin.log;


import java.util.HashMap;
import java.util.Map;

/**
 * Mongo logger fields
 *
 * @author Manoj Joshi
 */

public enum MLoggerFields {

    DEFAULT_COLL("log"),

    /**
     * the import date
     */
    DATE("date"),

    MESSAGE("message"),

    EXCEPTION_MESSAGE("exceptionMessage"),

    LEVEL("level"),

    FILE_NAME("fileName"),

    CLASS_NAME("className"),

    METHOD_NAME("methodName"),

    LINE_NUMBER("lineNumber"),

    STACK_TRACE("stackTrace"),

    EXCEPTION_FILE_NAME("exception.fileName"),

    EXCEPTION_CLASS_NAME("exception.className"),

    EXCEPTION_METHOD_NAME("exception.methodName"),

    EXCEPTION_LINE_NUMBER("exception.lineNumber"),

    EXCEPTION_EXCEPTION("exception.exception"),

    CAUSE_FILE_NAME("cause.fileName"),

    CAUSE_CLASS_NAME("cause.className"),

    CAUSE_METHOD_NAME("cause.methodName"),

    CAUSE_LINE_NUMBER("cause.lineNumber"),

    CAUSE_MESSAGE("cause.message"),

    CAUSE_EXCEPTION("cause.exception"),

    EXCEPTION("exception"),

    CAUSE("cause");

    private final String value;

    private static final Map<String, MLoggerFields> stringToEnum = new HashMap<String, MLoggerFields>();

    static { // init map from constant name to enum constant
        for (MLoggerFields en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    public static boolean contains(String key) {
        for (MLoggerFields en : values()) {
            if (en.toString().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(MLoggerFields key) {
        for (MLoggerFields en : values()) {
            if (en.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(MLoggerFields bioType) {
        return value.equals(bioType.toString());
    }

    public boolean equals(String bioType) {
        return value.equals(bioType);
    }

    MLoggerFields(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static MLoggerFields fromString(String value) {
        return stringToEnum.get(value);
    }
}
