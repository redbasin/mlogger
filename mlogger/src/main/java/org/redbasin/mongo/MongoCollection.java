/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redbasin.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

/**
 * A simple wrapper around the minimum methods in mongo.
 *
 * @author Manoj Joshi
 */
public class MongoCollection {

    private DBCollection coll = null;
    private int wTimeout = 5000;

    /**
     * Create a convenience wrapper around {@link com.mongodb.DBCollection}.
     *
     * @param coll the mongo collection
     * @param wTimeout the write timeout to avoid blocking
     */
    public MongoCollection(DBCollection coll, int wTimeout) {
        this.coll = coll;
        this.wTimeout = wTimeout;
    }

    /**
     * The write timeout on mongo.
     *
     * @return int get the current write timeout
     */
    public int getWtimeout() {
        return wTimeout;
    }

    /**
     * Return the instance of DBCollection.
     *
     * @return DBCollection get the current {@link com.mongodb.DBCollection}
     */
    public DBCollection getCollection() {
        return coll;
    }

    /**
     * Return DB
     *
     * @return DB return instance of {@link com.mongodb.DB}
     */
    public DB getDB() {
        return coll.getDB();
    }

    /**
     * Return the name of the collection.
     *
     * @return String the name of the collection
     */
    public String getName() {
        return coll.getName();
    }
   
    /**
     * Insert an object to the mongo collection.
     *
     * @param dbObject
     * @return CommandResult
     */
    public WriteResult insert(DBObject dbObject) {
        return coll.insert(dbObject);
    }

    public void ensureIndex(String key,
                            String name,
                            boolean unique)
            throws MongoException {
        coll.ensureIndex(new BasicDBObject(key, 1), null, unique);
    }
    
    public void ensureIndex(Enum key,
                            Enum name,
                            boolean unique)
            throws MongoException {
        ensureIndex(key.toString(), name.toString(), unique);
    }
}