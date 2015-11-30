package prueba.jms;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class SubscriptorAsincrono {
	
	final static Logger logger = LogManager.getLogger(SubscriptorAsincrono.class);

	
	  	@Resource(mappedName = "java:/ConnectionFactory")
	    private ConnectionFactory connectionFactory;
	    @Resource(mappedName = "java:/jms/TopicPrueba")
	    private Topic topic;

	    public void recibeMensajeAsincronoCola() throws JMSException {
	        Connection connection = null;
	        Session session = null;

	        MessageConsumer consumer = null;
	        JMSListener listener = null;
	        boolean esTransaccional = false;

	        try {
	            logger.info("Comienzo asincrono");
	            connection = connectionFactory.createConnection();
	            // Creamos una sesion sin transaccionalidad y con envio de acuse automatico
	            session = connection.createSession(esTransaccional, Session.AUTO_ACKNOWLEDGE);
	            // Creamos el consumidor a partir de una cola
	            consumer = session.createConsumer(topic);
	            // Creamos el listener, y lo vinculamos al consumidor -> asincrono
	            listener = new JMSListener();
	            consumer.setMessageListener(listener);
	            // Llamamos a start() para empezar a consumir
	            connection.start();
				
	            // Sacamos el mensaje por consola
	            logger.info("Fin asincrono");
	        } finally {
	            /* Se evita cerrar los recursos para que los subscriptores se mantengan 
	        	 conectados y se pueda probar que funcionan correctamente al publicar algo*/
	            //consumer.close();
	            //session.close();
	            //connection.close();
	        }
	    }
}
