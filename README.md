mlogger
=======

Mongo Logger

This allows us to log to mongo. By default the mongo db will be "log", but this can be changed. The collection names can also be chosen as suffix. The prefix is the level. This way different log levels go to separate collections. We do have support for mongo multiple shards and also clusters. You could use IoC to inject the params for the mongo connectors if you like. But the simple three lines will work too. Mongo indexes are automatically created, so you can search through these collections really fast.

MLogger log = MLogger.getInstance();

log.debug("test message", e);

The mongo collection DEBUGlog will be created with an entry that looks like:

> db.DEBUGlog.find().pretty()
{
	"_id" : ObjectId("52aa7ead30040a83eed00608"),
	"date" : ISODate("2013-12-13T03:27:41.348Z"),
	"exception" : {
		"fileName" : "MLoggerTest.java",
		"methodName" : "testApp",
		"lineNumber" : 64,
		"className" : "org.redbasin.mlogger.MLoggerTest",
		"message" : "simple test message",
		"exceptionMessage" : "Test error",
		"exception" : "java.lang.IllegalArgumentException"
	},
	"cause" : {
		"fileName" : "MLoggerTest.java",
		"methodName" : "testApp",
		"lineNumber" : 64,
		"className" : "org.redbasin.mlogger.MLoggerTest",
		"message" : "some nasty number error",
		"exception" : "java.lang.NumberFormatException"
	}
}


The logger will also pickup any additional log4j.properties or similar file and print it to any additional appenders. You can always disable these appenders, if you wish to log only to Mongo. So once you use this logger you do not need to use the log4j or commons-logging logger. This logger takes care of both logging.
