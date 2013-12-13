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
