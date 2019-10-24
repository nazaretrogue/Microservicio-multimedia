/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package microservicio;

import filter.TermalOp;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.jms.BytesMessage;
import javax.jms.JMSException;

/**
 *
 * @author nazaret
 */
public class Microservicio {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }
    
    public static BufferedImage procesamiento(BytesMessage mensaje) throws IOException, JMSException{
        // Creamos un array de bytes para introducir los bytes de la imagen del mensaje recibido
        byte[] bytes = new byte[(int) mensaje.getBodyLength()];
        mensaje.readBytes(bytes);
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        
        // Creamos un iterador para leer la imagen
        Iterator<?> lector_it = ImageIO.getImageReadersByFormatName("jpg");
 
        // Vamos leyendo la imagen con el iterador
        ImageReader lector = (ImageReader)lector_it.next();
        
        // Creamos un flujo de entrada para poder transformar la imagen en 
        // el tipo BufferedImage que es el que trabaja con el tratamiento de éstas
        // y en particular nuestro filtro
        Object fuente = is;
        ImageInputStream iis = ImageIO.createImageInputStream(fuente);
        
        // Establecemos el flujo desde el que va a leer y el orden de lectura de los metadatos
        // de la imagen, en este caso al ser true, los lee de forma ascendente
        lector.setInput(iis, true);
        
        // Establecemos los parámetros por defecto para la lectura de la imagen
        ImageReadParam param = lector.getDefaultReadParam();
 
        // Leemos la imagen
        Image imagen = lector.read(0, param);
 
        // Generamos la imagen con la información del espacio de color, tamaño, raster...
        // para después procesarla
        BufferedImage img_source = new BufferedImage(imagen.getWidth(null), imagen.getHeight(null), BufferedImage.TYPE_INT_RGB);
 
        // Grabamos la información leída en la imagen recién creada
        Graphics2D g2d = img_source.createGraphics();
        g2d.drawImage(imagen, null, null);
        
        // Establecemos el nombre de archivo para guardar la imagen en el sistema
        String nombre_img = "archivo_"+(++num_archivo)+".jpg";
 
        // Guardamos la imagen en un archivo
        File archivo_imagen = new File(nombre_img);
        ImageIO.write(img_source, "jpg", archivo_imagen);
        
        // Creamos la imagen destino con el espacio de color necesario
        ColorSpace cs = new sm.image.color.GreyColorSpace();
        ColorConvertOp cop = new ColorConvertOp(cs, null);

        BufferedImage dest = cop.filter(img_source, null);
        
        // Aplicamos el filtro
        TermalOp teop = new TermalOp(dest);
        teop.filter(img_source, img_source);
        
        return img_source;
    }
    
    // Atributo estático para llevar la cuenta del número de archivos que
    // tenemos de forma que si hay varias peticiones, al crear el archivo
    // no se cree con el mismo nombre y no se pisen
    static int num_archivo = 0;
}
