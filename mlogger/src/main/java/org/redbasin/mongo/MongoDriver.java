/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redbasin.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This is a set of utilities to use Mongo.
 *
 * @author Manoj Joshi
 */
public class MongoDriver {

    private static MongoDriver logInstance = null;
    private DB dbconn = null;
    private MongoOptions mongoOptions = null;
    private Mongo mongo = null;
    private String passwd = null;
    private String user = null;
    private String port = null;
    private String host = null;
    private String db = null;
    private ConcurrentHashMap<String, MongoCollection> collMap = null;  // create a synchronized map

    // some public final constants for MongoOptions
    public final boolean AUTO_CONNECT_RETRY_FLAG = true;

    // BEGIN - Properties set by Spring or some container IoC typically.
    private List<ServerAddress> addrs = null; // mandatory if addrMap, host and port are null
    private Map<String, Integer> addrMap = null; // mandatory if addrs, host and port are null

    private boolean autoConnectRetry = AUTO_CONNECT_RETRY_FLAG;
    private int wTimeout = MongoConfig.W_TIMEOUT.toInt();
    // END - Properties set by Spring or some container IoC typically.
    
    /**
     * Create an instance. This uses the log db in mongo.
     * 
     * @param host mongo host
     * @param port mongo port
     * @return MongoUtil
     * @throws UnknownHostException
     */
    public static synchronized MongoDriver getLogInstance(String host, String port) throws UnknownHostException {
        if (logInstance != null) {
            return logInstance;
        }
        return (logInstance = new MongoDriver(host, port, MongoInit.LOG_MONGO_DB.toString()));
    }
    
    /**
     * Create an instance.
     * 
     * @param host mongo host
     * @param port mongo port
     * @param db mongo db
     * @param user mongo user
     * @param passwd mongo passwd
     * @return MongoUtil
     * @throws UnknownHostException 
     */
    public static synchronized MongoDriver getLogInstance(String host, String port, String db, String user, String passwd) throws UnknownHostException {
        if (logInstance != null) {
            return logInstance;
        }
        return (logInstance = new MongoDriver(host, port, db, user, passwd));
    }
    
    /**
     * Create an instance.
     * 
     * @param host mongo host
     * @param port mongo port
     * @param db mongo db
     * @return MongoUtil
     * @throws UnknownHostException 
     */
    public static synchronized MongoDriver getLogInstance(String host, String port, String db) throws UnknownHostException {
        if (logInstance != null) {
            return logInstance;
        }
        return (logInstance = new MongoDriver(host, port, db));
    }
    
    /**
     * Create an instance.
     * 
     * @param addrMap host port address map
     * @param db mongo db
     * @param user mongo user
     * @param passwd mongo passwd
     * @return MongoUtil
     * @throws UnknownHostException 
     */
    public static synchronized MongoDriver getLogInstance(Map<String, Integer> addrMap, String db, String user, String passwd) throws UnknownHostException {
        if (logInstance != null) {
            return logInstance;
        }
        return (logInstance = new MongoDriver(addrMap, db, user, passwd));
    }
    
    /**
     * Create an instance.
     * 
     * @param addrMap host port address map
     * @param db mongo db
     * @return MongoUtil
     * @throws UnknownHostException 
     */
    public static synchronized MongoDriver getLogInstance(Map<String, Integer> addrMap, String db) throws UnknownHostException {
        if (logInstance != null) {
            return logInstance;
        }
        return (logInstance = new MongoDriver(addrMap, db));
    }
    
    /**
     * Create an instance.
     * 
     * @param addrs list of mongo host addresses
     * @param db mongo db
     * @return MongoUtil
     * @throws UnknownHostException 
     */
    public static synchronized MongoDriver getLogInstance(List<ServerAddress> addrs, String db) throws UnknownHostException {
        if (logInstance != null) {
            return logInstance;
        }
        return (logInstance = new MongoDriver(addrs, db));
    }
    
    /**
     * Create an instance.
     * 
     * @param addrs List of addresses for mongo.
     * @param db mongo db
     * @param user mongo user
     * @param passwd mongo passwd
     * @return MongoUtil
     * @throws UnknownHostException 
     */
    public static synchronized MongoDriver getLogInstance(List<ServerAddress> addrs, String db, String user, String passwd) throws UnknownHostException {
        if (logInstance != null) {
            return logInstance;
        }
        return (logInstance = new MongoDriver(addrs, db, user, passwd));
    }

    /**
     * Map of <host, port> tuple for replicaSet connection to the mongo server. This property is preferred
     * over using the {@link #setAddrs} as it is mongo independent.
     * If this property is not set, it is assumed that no replica set connectivity is necessary, as long
     * as the addrs property is also not set.
     * Set this (optional) property from Spring IoC or similar container.
     * Instead of using this property, a dedicated constructor can be used from Spring IoC
     * by passing the appropriate constructor argument.
     *
     * @param addrMap set the address map
     */
    public void setAddrMap(Map<String, Integer> addrMap) {
       this.addrMap = addrMap;
    }

    /**
     * {@link com.mongodb.ServerAddress} for replicaSet connection to the mongo server. This property is
     * mongo dependent and requires the IoC to be aware of Mongo objects like ServerAddress.
     * If this property is not set, it is
     * assumed that no replica set connectivity is necessary, as long as the addrMap property is also not set.
     * Set this (optional) property from Spring IoC or similar container.
     * Instead of using this property, a dedicated constructor can be used from Spring IoC
     * by passing the appropriate constructor argument.
     *
     * @param addrs set the list of addresses
     */
    public void setAddrs(List<ServerAddress> addrs) {
       this.addrs = addrs;
    }

    /**
     * Passwd for connection to the mongo server. If this property is not set, it is
     * assumed that no authentication is necessary.
     * Set this (optional) property from Spring IoC or similar container.
     * Instead of using this property, a dedicated constructor can be used from Spring IoC
     * by passing the appropriate constructor argument.
     *
     * @param passwd
     */
    public void setPasswd(String passwd) {
       this.passwd = passwd;
    }

    /**
     * User name for connection to the mongo server. If this property is not set, it is
     * assumed that no authentication is necessary.
     * Set this (optional) property from Spring IoC or similar container.
     * Instead of using this property, a dedicated constructor can be used from Spring IoC
     * by passing the appropriate constructor argument.
     *
     * @param user set the user for auth
     */
    public void setUser(String user) {
       this.user = user;
    }

    /**
     * Port number of the mongo server.
     * Set this (optional) property from Spring IoC or similar container.
     * Instead of using this property, a dedicated constructor can be used from Spring IoC
     * by passing the appropriate constructor argument.
     *
     * @param port set the port for authentication
     */
    public void setPort(String port) {
       this.port = port;
    }

    /**
     * Host name of the mongo server.
     * Set this (optional) property from Spring IoC or similar container.
     * Instead of using this property, a dedicated constructor can be used from Spring IoC
     * by passing the appropriate constructor argument.
     *
     * @param host
     */
    public void setHost(String host) {
       this.host = host;
    }

    /**
     * Set this (optional) property from Spring IoC or similar container.
     * Instead of using this property, a dedicated constructor can be used from Spring IoC
     * by passing the appropriate constructor argument.
     *
     * @param db the name of the database
     */
    public void setDb(String db) {
       this.db = db;
    }

    /**
     * Set this property from Spring IoC or similar container
     * As in {@link com.mongodb.MongoOptions#autoConnectRetry}
     * Default is {@link #AUTO_CONNECT_RETRY_FLAG}
     *
     * @param autoConnectRetry
     */
    public void setAutoConnectRetry(boolean autoConnectRetry) {
        this.autoConnectRetry = autoConnectRetry;
    }

    /**
     * Set this property from Spring IoC or similar container
     * As in {@link com.mongodb.WriteConcern}
     * Default is {@link #W_TIMEOUT}
     *
     * @param wTimeout
     */
    public void setWtimeout(int wTimeout) {
        this.wTimeout = wTimeout;
    }

    /**
     * Create an instance with no arguments. Properties must be set (usually from IoC) if this
     * needs to be used.
     */
    private MongoDriver() { }

    /**
     * Create a mongo authenticated connection. This constructor can be initialized by IoC.
     * Parameters are independent of mongo objects.
     *
     * @param host the host name of the Mongo server
     * @param port the port of the Mongo server
     * @param db the database name in Mongo
     * @param user the user to authenticate
     * @param passwd the password to authenticate
     * @throws MongoException, UnknownHostException
     */
    private MongoDriver(String host, String port, String db, String user, String passwd) throws UnknownHostException {
        initConnection(host, port, db, user, passwd);
    }

    /**
     * Create a mongo connection. This constructor is initialized by IoC. Parameters are independent of
     * mongo objects.
     *
     * @param host host name of the Mongo server
     * @param port port number of the Mongo server
     * @param db database name in Mongo
     * @throws MongoException, UnknownHostException
     */
    private MongoDriver(String host, String port, String db) throws UnknownHostException {
        initConnection(host, port, db);
    }

    /**
     * Used as an intermediate way to convert addrMap to addrs.
     *
     * @param addrMap an map which ignores the order of the entries of server addresses
     * @param db the database name in Mongo
     * @param user the user name for auth
     * @param passwd the password for auth
     */
    private void initConnection(Map<String, Integer> addrMap, String db, String user, String passwd) throws UnknownHostException {
        List<ServerAddress> addrsLocal = new ArrayList<ServerAddress>();
        Iterator<String> iter = addrMap.keySet().iterator();
        while (iter.hasNext()) {
            String hostLocal = iter.next();
            addrsLocal.add(new ServerAddress(hostLocal, addrMap.get(hostLocal)));
        }
        initConnection(addrsLocal, db, user, passwd);
    }

    /**
     * Used as an intermediate way to convert addrMap to addrs.
     *
     * @param addrMap an map of addresses agnostic of the order
     * @param db the database name in Mongo
     */
    private void initConnection(Map<String, Integer> addrMap, String db) throws UnknownHostException {
        List<ServerAddress> addrsLocal = new ArrayList<ServerAddress>();
        Iterator<String> iter = addrMap.keySet().iterator();
        while (iter.hasNext()) {
            String hostLocal = iter.next();
            addrsLocal.add(new ServerAddress(hostLocal, addrMap.get(hostLocal)));
        }
        initConnection(addrsLocal, db);
    }


    /**
     * Use this to create a connection for replica sets and it is independent of Mongo objects.
     * Provide a Map, where key is host, and value is port.
     * TODO: Perhaps we should use an ordered map, in case the order of the host,port in the replica set matters.
     *
     * @param addrMap a Map of server addresses agnostic of order
     * @param db the database name in Mongo
     * @param user the user name to auth
     * @param passwd the password to auth
     * @throws UnknownHostException
     */
    private MongoDriver(Map<String, Integer> addrMap, String db, String user, String passwd) throws UnknownHostException {
        if (addrMap == null) {
           throw new IllegalArgumentException("addrMap, user and passwd must be defined.");
        }
        initConnection(addrMap, db, user, passwd);
    }

    /**
     * Use this to create a connection for replica sets and it is independent of Mongo objects.
     * Provide a Map, where key is host, and value is port.
     * TODO: Perhaps we should use an ordered map, in case the order of the host,port in the replica set matters.
     *
     * @param addrMap a map of server addresses agnostic of order
     * @param db the database name in Mongo
     * @throws UnknownHostException thrown by Mongo if the host name is unknown
     */
    private MongoDriver(Map<String, Integer> addrMap, String db) throws UnknownHostException {
        if (addrMap == null) {
           throw new IllegalArgumentException("addrMap, user and passwd must be defined.");
        }
        initConnection(addrMap, db);
    }

    /**
     * Use this for connecting to replica sets. The ServerAddress is made up of
     * (host, port), and can be defined as a bean through IoC.
     *
     * @param addrs an ordered list of server addresses {@link com.mongodb.ServerAddress}
     * @param db the database name in Mongo
     * @throws MongoException thrown by Mongo if something goes wrong
     */
    private MongoDriver(List<ServerAddress> addrs, String db) {
        if ( (addrs == null) || (addrs.isEmpty())) {
           throw new IllegalArgumentException("ServerAddress and db not specified.");
        }
        initConnection(addrs, db);
    }

    /**
     * Use this for auth connecting to replica sets. The ServerAddress is made up of
     * (host, port), and can be defined as a bean through IoC.
     *
     * @param addrs a list of {@link com.mongodb.ServerAddress}
     * @param db the name of the database in Mongo
     * @param user user name for auth
     * @param passwd password for auth
     * @throws MongoException
     */
    private MongoDriver(List<ServerAddress> addrs, String db, String user, String passwd) {
        if ( (addrs == null)) {
           throw new IllegalArgumentException("ServerAddress and db not specified.");
        }
        initConnection(addrs, db, user, passwd);
    }

    /**
     * Reset this object. Once destroyed, this instance cannot be re-used. The user must
     * create a new instance.
     */
    public void reset() {
       logInstance = null;
       dbconn = null;
       if (mongo != null) {
           mongo.close();
       }
       mongoOptions = null;
       mongo = null;
       collMap = null;
    }

    /**
     * Destroy this connection when no longer in use. This method is usually automatically called
     * when garbage collecting.
     */
    @Override
    public void finalize() throws Throwable {
       super.finalize();
       reset();
    }

    /**
     * MongoOptions has global variables in it's member class. The only way to initialize them
     * is by initializing these variables directly. There are no modifier methods. I find this kind
     * of creepy, but have no choice for now.
     *
     * @return MongoOptions an object {@link com.mongodb.MongoOptions}that contains options to connect to Mongo
     */
    private MongoOptions getMongoOptions() {
        mongoOptions = new MongoOptions();
        mongoOptions.autoConnectRetry = autoConnectRetry;
        mongoOptions.connectionsPerHost = MongoConfig.CONNECTIONS_PER_HOST.toInt();
        mongoOptions.threadsAllowedToBlockForConnectionMultiplier = MongoConfig.BLOCKABLE_THREADS.toInt();
        mongoOptions.maxAutoConnectRetryTime = MongoConfig.MAX_AUTO_CONNECT_RETRY_TIME.toInt();
        mongoOptions.maxWaitTime = MongoConfig.MAX_WAIT_TIME.toInt();
        mongoOptions.connectTimeout = MongoConfig.CONNECT_TIMEOUT.toInt();
        return mongoOptions;
    }

    /**
     * This is the "init-method" typically invoked from an IoC container like Spring.
     * A MongoConnection constructor with no arguments (and instead with properties) set can be
     * defined in the IoC, as long as an init-method is specified to be this method which is "init".
     * The db name must be specified irrespective of any configuration initialization.
     * The order of precedence of definition here is
     *
     * 1. addrMap
     * 2. addrs
     * 3. host, port
     *
     * If the addrMap is specified, then that is considered first. In which case the user and passwd
     * are optional.
     * <p>
     * If the addrs is specified, then that is considered next. In which case the user and passwd
     * are optional.
     * <p>
     * If both the addrMap and addrs are not specified, the host and port must be specified.
     * If the host and port are specified, the user and passwd are again optional.
     *
     * @throws UnknownHostException thrown by Mongo if the host name is unknown
     */
    public void init() throws UnknownHostException {
        if (db == null) {
           throw new IllegalArgumentException("The db name is not defined.");
        }
        if (addrMap != null) {
           if ((user != null) && (passwd != null)) {
              initConnection(addrMap, db, user, passwd);
           } else {
              initConnection(addrMap, db);
           }
        } else {
           if (addrs != null) {
              if ((user != null) && (passwd != null)) {
                 initConnection(addrMap, db, user, passwd);
              } else {
                 initConnection(addrMap, db);
              }
           } else {
              if ((host == null) || (port == null)) {
                 throw new IllegalArgumentException("The host and port are found to be null.");
              } else {
                 if ((user != null) && (passwd != null)) {
                    initConnection(host, port, db, user, passwd);
                 } else {
                    initConnection(host, port, db);
                 }
              }
           }
        }
    }

    /**
     * Use this for replica sets.
     *
     * @param addrs an ordered list of {@link com.mongodb.ServerAddress}
     * @param db the database name in Mongo
     */
    private void initConnection(List<ServerAddress> addrs, String db) {
        if (dbconn != null) {
           return;   // don't need to re-initialize the connection
        }
        reset();
        this.addrs = addrs;
        this.db = db;
        mongoOptions = getMongoOptions();
        mongo = new Mongo(addrs, mongoOptions);
        dbconn = mongo.getDB(db.toString());
        collMap = new ConcurrentHashMap<String, MongoCollection>();
    }

    /**
     * Use this for replica sets.
     *
     * @param addrs an ordered list of {@link com.mongodb.ServerAddress}
     * @param db the database name in Mongo
     */
    private void initConnection(List<ServerAddress> addrs, String db, String user, String passwd) {
        if (dbconn != null) {
           return;   // don't need to re-initialize the connection
        }
        reset();
        this.addrs = addrs;
        this.db = db;
        this.user = user;
        this.passwd = passwd;
        mongoOptions = getMongoOptions();
        mongo = new Mongo(addrs, mongoOptions);
        dbconn = mongo.getDB(db.toString());
        dbconn.authenticate(user.toString(), passwd.toString().toCharArray());
        collMap = new ConcurrentHashMap<String, MongoCollection>();
    }

    /**
     * Call this at init, or later if it dies.
     *
     * @param host
     * @param port
     * @param db
     * @throws UnknownHostException
     */
    private void initConnection(String host, String port, String db) throws UnknownHostException {
        if (dbconn != null) {
           return;
        }
        reset();
        this.host = host;
        this.port = port;
        this.db = db;
        mongoOptions = getMongoOptions();
        mongo = new Mongo(new ServerAddress(host.toString() , new Integer(this.port.toString())), mongoOptions);
        dbconn = mongo.getDB(db.toString());
        collMap = new ConcurrentHashMap<String, MongoCollection>();
    }

    /**
     * Call this at init, or later if it dies.
     *
     * @param host the host name of the mongo server
     * @param port the port number of the mongo server
     * @param db the database name in Mongo
     * @throws UnknownHostException
     */
    private void initConnection(String host, String port, String db, String user, String passwd) throws UnknownHostException {
        if (dbconn != null) {
           return;
        }
        reset();
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.passwd = passwd;
        mongoOptions = getMongoOptions();
        mongo = new Mongo(new ServerAddress(host.toString() , new Integer(this.port.toString())), mongoOptions);
        dbconn = mongo.getDB(db.toString());
        dbconn.authenticate(user.toString(), passwd.toString().toCharArray());
        collMap = new ConcurrentHashMap<String, MongoCollection>();
    }

    /**
     * Return DB usually needed by other daos.
     *
     * @return DB return an instance of {@link com.mongodb.DB}
     */
    public DB getDB() {
        return dbconn;
    }

    /**
     * Create a collection if it does not already exist. If it does exist return the existing
     * collection.
     *
     * @param collName name for the collection
     * @return MongoCollection
     * @throws IllegalArgumentException if collName is not specified
     */
    public MongoCollection createCollection(String collName) {
        if (collName == null || collName.isEmpty()) {
           throw new IllegalArgumentException("Collection name must be specified.");
        }
        if (dbconn.collectionExists(collName)) {
           return getCollection(collName);
        }
        return new MongoCollection(dbconn.createCollection(collName, null), wTimeout);
    }

    /**
     * Return a collection from this connection. The connection must be initialized.
     *
     * @param collName the name of the collection
     * @return MongoCollection an instance of {@link MongoCollection}
     */
    public MongoCollection getCollection(String collName) {
        MongoCollection mongoCollection = collMap.get(collName);
        if (mongoCollection == null) {
           DBCollection coll;
           coll = dbconn.getCollection(collName);
           mongoCollection = new MongoCollection(coll, wTimeout);
           collMap.put(collName, mongoCollection);
        }
        return mongoCollection;
    }

    /**
     * Return an instance of mongo. This is if the user wants to get more insights on the
     * state of mongo directly. This will allow the user to access methods in Mongo directly.
     * Care must be taken not to exercise methods in Mongo that are already implemented in this
     * contrib, as that would mean, we do not gather stats in those cases.
     *
     * @return Mongo an instance of {@link com.mongodb.Mongo}
     */
    public Mongo getMongo() {
        return mongo;
    }
}