/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redbasin.log;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import org.redbasin.mongo.MongoCollection;
import org.redbasin.mongo.MongoDriver;
import java.net.UnknownHostException;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redbasin.mongo.MongoInit;

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
    
    private static MLogger mlogger = null;
    private static String host = MongoInit.DEFAULT_HOST.toString();
    private static String port = MongoInit.DEFAULT_PORT.toString(); 
    
    public static void reset() {
        mlogger = null;    
    }
    
    private MLogger(String h, String p) {
        host = h;
        port = p;
    }
    
    public static MLogger getInstance() {
       return getInstance(host, port);    
    }
    
    public void setHost(String h) {
        host = h;
    }
    
    public void setPort(String p) {
        port = p;
    }
    
    public static MLogger getInstance(String host, String port) {
        if (mlogger != null) {
            return mlogger;
        }
        mlogger = new MLogger(host, port);
        return mlogger;
    }

    private MongoCollection getCollection(String coll) throws UnknownHostException {
        MongoDriver mongoUtil = MongoDriver.getLogInstance(host, port);
        return mongoUtil.getCollection(coll);
    }
    
    /**
     * Set the logging level.
     * 
     * @param level 
     */
    public synchronized void setLevel(MLevel level) {
        _level = level;
    }
    
    /**
     * Simple message.
     * 
     * @param message
     * @param e
     * @return WriteResult
     * @throws UnknownHostException 
     */
    public WriteResult debug(String message, Throwable e) throws UnknownHostException {
        logger.debug(message, e);
        if (_level.higherThan(MLevel.DEBUG)) {
           return log(MLevel.DEBUG, MLoggerFields.DEFAULT_COLL.toString(), message, e);
        }
        return null;
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
    public WriteResult debug(String coll, String message, Throwable e) throws UnknownHostException {
        logger.debug(message, e);
        if (_level.higherThan(MLevel.DEBUG)) {
           return log(MLevel.DEBUG, coll, message, e);
        }
        return null;
    }
    
    /**
     * 
     * @param message
     * @return WriteResult
     * @throws UnknownHostException 
     */
    public WriteResult debug(String message) throws UnknownHostException {
        logger.debug(message);
        if (_level.higherThan(MLevel.DEBUG)) {
           return log(MLevel.DEBUG, MLoggerFields.DEFAULT_COLL.toString(), message, null);
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
    public WriteResult debug(String coll, String message) throws UnknownHostException {
        logger.debug(message);
        if (_level.higherThan(MLevel.DEBUG)) {
           return log(MLevel.DEBUG, coll, message, null);
        }
        return null;
    }
    
    /**
     * 
     * @param message
     * @param e
     * @return WriteResult
     * @throws UnknownHostException 
     */
    public WriteResult warn(String message, Throwable e) throws UnknownHostException {
        logger.warn(message, e);
        if (_level.higherThan(MLevel.WARN)) {
           return log(MLevel.WARN, MLoggerFields.DEFAULT_COLL.toString(), message, e);
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
    public WriteResult warn(String coll, String message, Throwable e) throws UnknownHostException {
        logger.warn(message, e);
        if (_level.higherThan(MLevel.WARN)) {
           return log(MLevel.WARN, coll, message, e);
        }
        return null;
    }
    
    /**
     * 
     * @param message
     * @return WriteResult
     * @throws UnknownHostException 
     */
    public WriteResult warn(String message) throws UnknownHostException {
        logger.warn(message);
        if (_level.higherThan(MLevel.WARN)) {
           return log(MLevel.WARN, MLoggerFields.DEFAULT_COLL.toString(), message, null);
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
    public WriteResult warn(String coll, String message) throws UnknownHostException {
        logger.warn(message);
        if (_level.higherThan(MLevel.WARN)) {
           return log(MLevel.WARN, coll, message, null);
        }
        return null;
    }
    
    /**
     * 
     * @param message
     * @param e
     * @return WriteResult
     * @throws UnknownHostException 
     */
    public WriteResult info(String message, Throwable e) throws UnknownHostException {
        logger.info(message, e);
        if (_level.higherThan(MLevel.INFO)) {
           return log(MLevel.INFO, MLoggerFields.DEFAULT_COLL.toString(), message, e);
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
    public WriteResult info(String coll, String message, Throwable e) throws UnknownHostException {
        logger.info(message, e);
        if (_level.higherThan(MLevel.INFO)) {
           return log(MLevel.INFO, coll, message, e);
        }
        return null;
    }
    
    /**
     * 
     * @param message
     * @return WriteResult
     * @throws UnknownHostException 
     */
    public WriteResult info(String message) throws UnknownHostException {
        logger.info(message);
        if (_level.higherThan(MLevel.INFO)) {
           return log(MLevel.INFO, MLoggerFields.DEFAULT_COLL.toString(), message, null);
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
    public WriteResult info(String coll, String message) throws UnknownHostException {
        logger.info(message);
        if (_level.higherThan(MLevel.INFO)) {
           return log(MLevel.INFO, coll, message, null);
        }
        return null;
    }
    
    /**
     * 
     * @param message
     * @param e
     * @return WriteResult
     * @throws UnknownHostException 
     */
    public WriteResult error(String message, Throwable e) throws UnknownHostException {
        logger.error(message, e);
        return log(MLevel.ERROR, MLoggerFields.DEFAULT_COLL.toString(), message, e);
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
    public WriteResult error(String coll, String message, Throwable e) throws UnknownHostException {
        logger.error(message, e);
        return log(MLevel.ERROR, coll, message, e);
    }
    
    /**
     * 
     * @param message
     * @return WriteResult
     * @throws UnknownHostException 
     */
    public WriteResult error(String message) throws UnknownHostException {
        logger.error(message);
        return log(MLevel.ERROR, MLoggerFields.DEFAULT_COLL.toString(), message, null);
    }
    
    /**
     * Log a ERROR level message.
     * 
     * @param coll collection suffix
     * @param message
     * @return
     * @throws UnknownHostException 
     */
    public WriteResult error(String coll, String message) throws UnknownHostException {
        logger.error(message);
        return log(MLevel.ERROR, coll, message, null);
    }
    
    private WriteResult log(MLevel level, String coll, String message, Throwable e) throws UnknownHostException {
        
        StackTraceElement[] elements = e.getStackTrace();
        if (e.getCause() != null) {
            StackTraceElement[] causeElements = e.getCause().getStackTrace();
            return log(level, coll, message, elements, causeElements, e);
        } else {
            return log(level, coll, message, elements, null, e);
        }
    }
    
    private WriteResult log(MLevel level, String coll, String message) throws UnknownHostException {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        return log(level, coll, message, elements, null, null);
    }
    
    private WriteResult log(MLevel level, String coll, String message, StackTraceElement[] elements, StackTraceElement[] causeElements, Throwable e) throws UnknownHostException {
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
}