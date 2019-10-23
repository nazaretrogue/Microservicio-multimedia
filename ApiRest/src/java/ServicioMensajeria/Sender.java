/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServicioMensajeria;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
 
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author nazaret
 */
public class Sender {
    public Sender(byte[] bytes) throws JMSException{
        // Abrimos la conexión con el servidor y la iniciamos
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(URL);
        Connection conexion = connectionFactory.createConnection();
        conexion.start();
         
        // Creamos una sesión para recibir y enviar mensajes a través del servidor; 
        // si no se hace en local (primer parámetro false). el segundo parámetro indica como 
        // se responderán los ACK de los mensajes recibidos
        Session sesion = conexion.createSession(false, Session.AUTO_ACKNOWLEDGE);  
         
        // Cola con el nombre "cola_imagen"
        Destination destino = sesion.createQueue(cola); 
         
        // Utilizamos un productor para enviar el mensaje a la cola
        MessageProducer productor = sesion.createProducer(destino);
         
        // Creamos el mensaje; en nuestro caso, necesitamos enviar una imagen por lo que el mensaje es de tipo Byte
        BytesMessage mensaje = sesion.createBytesMessage();
        mensaje.writeBytes(bytes);
         
        // Enviamos el mensaje
        productor.send(mensaje);
         
        //System.out.println("JCG printing@@ '" + mensaje.getText() + "'");
        
        // Cerramos la conexión
        conexion.close();
    }
    
    // Url del servidor; al ser default, es en localhost
    private final String URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    
    // Nombre de la cola
    private final String cola = "cola_imagen";
}
