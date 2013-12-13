/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redbasin.log;


import java.util.HashMap;
import java.util.Map;

/**
 * Mongo logger level fields
 *
 * @author Manoj Joshi
 */

public enum MLevel {

    /**
     * the import date
     */
    DEBUG(10),
        
    INFO(20),
    
    WARN(30),
    
    ERROR(40);
    
    private final int value;

    private static final Map<Integer, MLevel> intToEnum = new HashMap<Integer, MLevel>();

    static { // init map from constant name to enum constant
        for (MLevel en : values()) {
            intToEnum.put(en.value, en);
        }
    }

    public static boolean contains(int level) {
        for (MLevel en : values()) {
            if (en.value == level) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(MLevel level) {
        for (MLevel en : values()) {
            if (en.value == level.value) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(MLevel level) {
        return value == level.value;
    }

    public boolean equals(int level) {
        return value == level;
    }

    MLevel(int value) {
        this.value = value;
    }
    
    public boolean higherThan(MLevel level) {
        return level.value >= value;
    }

    @Override
    public String toString() {
        return new Integer(value).toString();
    }

    public static MLevel fromInt(int value) {
        return intToEnum.get(value);
    }
}
