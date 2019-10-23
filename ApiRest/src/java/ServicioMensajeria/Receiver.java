/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServicioMensajeria;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
 
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import microservicio.Microservicio;

/**
 *
 * @author nazaret
 */
public class Receiver {
    public Receiver() throws JMSException, IOException{
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
 
        // MessageConsumer is used for receiving (consuming) messages
        MessageConsumer consumidor = sesion.createConsumer(destino);
 
        // Here we receive the message.
        Message mensaje = consumidor.receive();
 
        BufferedImage img;
        // We will be using TestMessage in our example. MessageProducer sent us a TextMessage
        // so we must cast to it to get access to its .getText() method.
        if (mensaje instanceof BytesMessage) {
            img = Microservicio.procesamiento((BytesMessage)mensaje);
        }
        
        conexion.close();
    }
    
    // Url del servidor; al ser default, es en localhost
    private final String URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    
    // Nombre de la cola
    private final String cola = "cola_imagen";
}
