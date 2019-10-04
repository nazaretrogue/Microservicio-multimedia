package com.mycompany.app;

import java.awt.image.BufferedImage;
import java.awt.color.ColorSpace;
import java.awt.image.ColorConvertOp;
import nrg.TermalOp;
/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        System.out.println( "Hello World!" );
    }

    public BufferedImage funcion(BufferedImage img_source){
        if(img_source != null){
            try{
                ColorSpace cs = new sm.image.color.GreyColorSpace();
                ColorConvertOp cop = new ColorConvertOp(cs, null);

                BufferedImage dest = cop.filter(img_source, null);

                TermalOp teop = new TermalOp(dest);
                teop.filter(img_source, img_source);
            }catch(IllegalArgumentException e){
                System.err.println(e.getLocalizedMessage());
            }
        }
        return img_source;
    }
}
