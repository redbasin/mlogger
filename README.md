mlogger
=======

Mongo Logger

This allows us to log to mongo. Like different collections for different levels and all. Yeah really. No nerve-wrecking appenders. We do have support for mongo multiple shards and also clusters. You could use IoC to inject the params for the mongo connectors if you like. But the simple three lines will work too.

MLogger log = MLogger.getInstance("localhost", "27017");
log.setLevel(MLevel.DEBUG);
log.debug("feature", "test message", e);

The mongo collection DEBUGfeature will be created with an entry that looks like:

{
	"_id" : ObjectId("52aa6e9d300405803b9f7703"),
	"date" : ISODate("2013-12-13T02:19:09.242Z"),
	"exception" : {
		"fileName" : "MLogger.java",
		"methodName" : "main",
		"lineNumber" : 253,
		"className" : "org.redbasin.log.MLogger",
		"message" : "test message",
		"exceptionMessage" : "Test error",
		"exception" : "java.lang.IllegalArgumentException"
	},
	"cause" : {
		"fileName" : "MLogger.java",
		"methodName" : "main",
		"lineNumber" : 253,
		"className" : "org.redbasin.log.MLogger",
		"message" : "some nasty number error",
		"exception" : "java.lang.NumberFormatException"
	}
}

The logger will also pickup any additional log4j.properties or similar file and print it to any additional appenders. You can always disable these appenders, if you wish to log only to Mongo. So once you use this logger you do not need to use the log4j or commons-logging logger. This logger takes care of both logging.

