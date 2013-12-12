/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redbasin.log;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.redbasin.mongod.MongoCollection;
import com.redbasin.mongod.MongoUtil;
import java.net.UnknownHostException;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the Mongo logger. All collections are created in the "log" db.
 * Indexes are automatically created. Collection names are prefixed by the
 * level name.
 *
 * @author Manoj Joshi
 */
public class MLogger {    
    protected static Log logger = LogFactory.getLog(new Object().getClass());
    private static MLevel _level = MLevel.INFO;

    private static MongoCollection getCollection(String coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getLogInstance();
        return mongoUtil.getCollection(coll);
    }
    
    /**
     * Set the logging level.
     * 
     * @param level 
     */
    public static synchronized void setLevel(MLevel level) {
        _level = level;
    }
    
    /**
     * Log a DEBUG level message with exception.
     * 
     * @param coll collection suffix
     * @param message
     * @param e
     * @return
     * @throws UnknownHostException 
     */
    public static WriteResult debug(String coll, String message, Throwable e) throws UnknownHostException {
        logger.debug(message, e);
        if (_level.higherThan(MLevel.DEBUG)) {
           return log(MLevel.DEBUG, coll, message, e);
        }
        return null;
    }
    
    /**
     * Log a DEBUG level message.
     * 
     * @param coll collection suffix
     * @param message
     * @return
     * @throws UnknownHostException 
     */
    public static WriteResult debug(String coll, String message) throws UnknownHostException {
        logger.debug(message);
        if (_level.higherThan(MLevel.DEBUG)) {
           return log(MLevel.DEBUG, coll, message, null);
        }
        return null;
    }
    
    /**
     * Log a WARN level message with exception.
     * 
     * @param coll collection suffix
     * @param message
     * @param e
     * @return
     * @throws UnknownHostException 
     */
    public static WriteResult warn(String coll, String message, Throwable e) throws UnknownHostException {
        logger.warn(message, e);
        if (_level.higherThan(MLevel.WARN)) {
           return log(MLevel.WARN, coll, message, e);
        }
        return null;
    }
    
    /**
     * Log a WARN level message.
     * 
     * @param coll collection suffix
     * @param message
     * @return
     * @throws UnknownHostException 
     */
    public static WriteResult warn(String coll, String message) throws UnknownHostException {
        logger.warn(message);
        if (_level.higherThan(MLevel.WARN)) {
           return log(MLevel.WARN, coll, message, null);
        }
        return null;
    }
    
    /**
     * Log a INFO level message with exception.
     * 
     * @param coll collection suffix
     * @param message
     * @param e
     * @return
     * @throws UnknownHostException 
     */
    public static WriteResult info(String coll, String message, Throwable e) throws UnknownHostException {
        logger.info(message, e);
        if (_level.higherThan(MLevel.INFO)) {
           return log(MLevel.INFO, coll, message, e);
        }
        return null;
    }
    
    /**
     * Log a INFO level message.
     * 
     * @param coll collection suffix
     * @param message
     * @return
     * @throws UnknownHostException 
     */
    public static WriteResult info(String coll, String message) throws UnknownHostException {
        logger.info(message);
        if (_level.higherThan(MLevel.INFO)) {
           return log(MLevel.INFO, coll, message, null);
        }
        return null;
    }
    
    /**
     * Log a ERROR level message with exception.
     * 
     * @param coll collection suffix
     * @param message
     * @param e
     * @return
     * @throws UnknownHostException 
     */
    public static WriteResult error(String coll, String message, Throwable e) throws UnknownHostException {
        logger.error(message, e);
        return log(MLevel.ERROR, coll, message, e);
    }
    
    /**
     * Log a ERROR level message.
     * 
     * @param coll collection suffix
     * @param message
     * @return
     * @throws UnknownHostException 
     */
    public static WriteResult error(String coll, String message) throws UnknownHostException {
        logger.error(message);
        return log(MLevel.ERROR, coll, message, null);
    }
    
    private static WriteResult log(MLevel level, String coll, String message, Throwable e) throws UnknownHostException {
        
        StackTraceElement[] elements = e.getStackTrace();
        if (e.getCause() != null) {
            StackTraceElement[] causeElements = e.getCause().getStackTrace();
            return log(level, coll, message, elements, causeElements, e);
        } else {
            return log(level, coll, message, elements, null, e);
        }
    }
    
    private static WriteResult log(MLevel level, String coll, String message) throws UnknownHostException {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        return log(level, coll, message, elements, null, null);
    }
    
    private static WriteResult log(MLevel level, String coll, String message, StackTraceElement[] elements, StackTraceElement[] causeElements, Throwable e) throws UnknownHostException {
        MongoCollection statusCollection = getCollection(level.name() + coll);
        statusCollection.ensureIndex(MLoggerFields.EXCEPTION_LINE_NUMBER, MLoggerFields.LINE_NUMBER, false);
        statusCollection.ensureIndex(MLoggerFields.EXCEPTION_CLASS_NAME, MLoggerFields.CLASS_NAME, false);
        statusCollection.ensureIndex(MLoggerFields.EXCEPTION_FILE_NAME, MLoggerFields.FILE_NAME, false);
        statusCollection.ensureIndex(MLoggerFields.EXCEPTION_METHOD_NAME, MLoggerFields.METHOD_NAME, false);
        statusCollection.ensureIndex(MLoggerFields.CAUSE_LINE_NUMBER, MLoggerFields.LINE_NUMBER, false);
        statusCollection.ensureIndex(MLoggerFields.CAUSE_CLASS_NAME, MLoggerFields.CLASS_NAME, false);
        statusCollection.ensureIndex(MLoggerFields.CAUSE_FILE_NAME, MLoggerFields.FILE_NAME, false);
        statusCollection.ensureIndex(MLoggerFields.CAUSE_METHOD_NAME, MLoggerFields.METHOD_NAME, false);
        statusCollection.ensureIndex(MLoggerFields.MESSAGE, MLoggerFields.CAUSE_MESSAGE, false);
        
        BasicDBObject exceptionDBObject = new BasicDBObject();
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put(MLoggerFields.DATE.toString(), new Date()); 
        basicDBObject.put(MLoggerFields.EXCEPTION.toString(), exceptionDBObject);
        exceptionDBObject.put(MLoggerFields.FILE_NAME.toString(), elements[0].getFileName());
        exceptionDBObject.put(MLoggerFields.METHOD_NAME.toString(), elements[0].getMethodName());
        exceptionDBObject.put(MLoggerFields.LINE_NUMBER.toString(), elements[0].getLineNumber());
        exceptionDBObject.put(MLoggerFields.CLASS_NAME.toString(), elements[0].getClassName());
        exceptionDBObject.put(MLoggerFields.MESSAGE.toString(), message);
        if (e != null) {
            exceptionDBObject.put(MLoggerFields.EXCEPTION_MESSAGE.toString(), e.getMessage());
        }
        exceptionDBObject.put(MLoggerFields.EXCEPTION.toString(), e.getClass().getCanonicalName());
        if (causeElements[0] != null && e.getCause() != null) {
            BasicDBObject causeDBObject = new BasicDBObject();
            basicDBObject.put(MLoggerFields.CAUSE.toString(), causeDBObject);
            causeDBObject.put(MLoggerFields.FILE_NAME.toString(), causeElements[0].getFileName());
            causeDBObject.put(MLoggerFields.METHOD_NAME.toString(), causeElements[0].getMethodName());
            causeDBObject.put(MLoggerFields.LINE_NUMBER.toString(), causeElements[0].getLineNumber());
            causeDBObject.put(MLoggerFields.CLASS_NAME.toString(), causeElements[0].getClassName());
            causeDBObject.put(MLoggerFields.MESSAGE.toString(), e.getCause().getMessage());
            causeDBObject.put(MLoggerFields.EXCEPTION.toString(), e.getCause().getClass().getCanonicalName());
        }
        return statusCollection.insert(basicDBObject);
    }

    public static void main(String[] args) throws UnknownHostException {
        try {
            throw new IllegalArgumentException("Test error", new NumberFormatException("some nasty number error"));
        } catch (Exception e) {
            MLogger.setLevel(MLevel.DEBUG);
            MLogger.debug("feature", "test message", e);
        }
    }
}
