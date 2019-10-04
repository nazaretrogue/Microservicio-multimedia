/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package microservicio;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;
import java.net.URLConnection;

/**
 * Unit test for simple App.
 */
public class MicroservicioTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MicroservicioTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(MicroservicioTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
        /*File file = new File("archivo.jpeg");
        String mime = URLConnection.guessContentTypeFromName(file.getName());

        assertEquals(mime, "image/jpeg");*/
    }
}
