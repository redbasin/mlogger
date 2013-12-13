mlogger
=======

Mongo Logger

This allows us to log to mongo. Like different collections for different levels and all. Yeah really. No nerve-wrecking appenders. We do have support for mongo multiple shards and also clusters. You could use IoC to inject the params for the mongo connectors if you like. But the simple three lines will work too.

MLogger log = MLogger.getInstance("localhost", "27017");
log.setLevel(MLevel.DEBUG);
log.debug("feature", "test message", e);

The mongo collection DEBUGfeature will be created with an entry that looks like:

> db.DEBUGfeature.stats()
{
	"ns" : "log.DEBUGfeature",
	"count" : 3,
	"size" : 1488,
	"avgObjSize" : 496,
	"storageSize" : 8192,
	"numExtents" : 1,
	"nindexes" : 10,
	"lastExtentSize" : 8192,
	"paddingFactor" : 1,
	"systemFlags" : 1,
	"userFlags" : 0,
	"totalIndexSize" : 81760,
	"indexSizes" : {
		"_id_" : 8176,
		"exception.lineNumber_1" : 8176,
		"exception.className_1" : 8176,
		"exception.fileName_1" : 8176,
		"exception.methodName_1" : 8176,
		"cause.lineNumber_1" : 8176,
		"cause.className_1" : 8176,
		"cause.fileName_1" : 8176,
		"cause.methodName_1" : 8176,
		"message_1" : 8176
	},
	"ok" : 1
}

