/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServicioMensajeria;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
 
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author nazaret
 */
public class Sender {
    public static void establecerSender() throws JMSException{
        // Abrimos la conexión con el servidor y la iniciamos
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(URL);
        Connection conexion = connectionFactory.createConnection();
        conexion.start();
         
        // Creamos una sesión para recibir y enviar mensajes a través del servidor
        Session sesion = conexion.createSession(false, Session.AUTO_ACKNOWLEDGE);  
         
        // Cola con el nombre "cola_imagen"
        Destination destino = sesion.createQueue(cola); 
         
        // Utilizamos un productor para enviar el mensaje a la cola
        MessageProducer productor = sesion.createProducer(destino);
         
        // Creamos el mensaje; en nuestro caso, necesitamos enviar una imagen 
        TextMessage mensaje = sesion.createTextMessage("Hello !!! Welcome to the world of ActiveMQ.");
         
        // Enviamos el mensaje
        productor.send(mensaje);
         
        System.out.println("JCG printing@@ '" + mensaje.getText() + "'");
        
        // Cerramos la conexión
        conexion.close();
    }
    
    // Url del servidor; al ser default, es en localhost
    private static String URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    
    // Nombre de la cola
    private static String cola = "cola_imagen";
}
