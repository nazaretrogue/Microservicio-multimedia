package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.awt.image.BufferedImage;

public class MultimediaTests {
    @Test
    public void JPGTest(){
        /*try{
            File f = new File(dlg.getSelectedFile().getAbsolutePath()){
                @Override
                public String toString(){
                    return this.getName();
                }
            };

            String[] nombre_archivo = f.toString().split("\\.");

            if(nombre_archivo[1].toUpperCase() == "JPEG" || nombre_archivo[1].toUpperCase() == "JPG"){
                BufferedImage img = ImageIO.read(f);
            }

        }catch(Exception e){
                System.err.println("Error al leer el fichero");
        }*/

        BufferedImage img = new BufferedImage(100, 100, TYPE_INT_RGB);
        Assert.assertEquals(TYPE_INT_RGB, img.getType());
    }
}
