package org.redbasin.mlogger;

import java.net.UnknownHostException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.redbasin.log.MLevel;
import org.redbasin.log.MLogger;

/**
 * Unit test for simple App.
 */
public class MLoggerTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MLoggerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MLoggerTest.class );
    }

    /**
     * A document in mongo that looks like below should be created:
     * 
     * <pre>
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

     * </pre>
     */
    public void testApp() throws UnknownHostException
    {
        //assertTrue( true );
        try {
            throw new IllegalArgumentException("Test error", new NumberFormatException("some nasty number error"));
        } catch (Exception e) {
            MLogger log = MLogger.getInstance("localhost", "27017");
            log.setLevel(MLevel.DEBUG);
            log.debug("feature", "test message", e);
        }
    }
}
