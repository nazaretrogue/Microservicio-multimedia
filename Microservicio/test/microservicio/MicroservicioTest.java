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
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


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
        try{
            URL url = new URL("https://images-na.ssl-images-amazon.com/images/I/61S60Xn5BsL._SL1000_.jpg");
            BufferedImage img = ImageIO.read(url.openStream());
            
            File file = new File("descargada.jpg");
            //ImageIO.write(img, "jpg", file);
        
            String mime = URLConnection.guessContentTypeFromName(file.getName());

            assertEquals(mime, "image/jpeg");
        }catch(Exception e){
            System.err.println(e.getLocalizedMessage());
        }
    }
}
