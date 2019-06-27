package edu.fema.jmessenger.jMessenger;


import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import edu.fema.jmessenger.jMessenger.locate.Starter;

public class App 
{
    public static void main( String[] args )
    {
    	
    	
    	Starter.thread(new HelloWorldProducer(), false);
    	Starter.thread(new HelloWorldConsumer(), false);
    	
    }
    
    public static class HelloWorldProducer implements Runnable {
    	
    	public void run() {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
			
			try {
				Connection connection = connectionFactory.createConnection();
				connection.start();
				
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				
				Destination destination = session.createQueue("TEST.FOO");
				MessageProducer producer = session.createProducer(destination);
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

				String text = "Eae mano";
				TextMessage message = session.createTextMessage(text);
				producer.send(message);
				session.close();
				connection.close();				
				
			} catch (JMSException e) {
				e.printStackTrace();
			}
			
		}
		
	}
    
    public static class HelloWorldConsumer implements Runnable, ExceptionListener {

		public void run() {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
			
			try {
				Connection connection = connectionFactory.createConnection();
				connection.start();
				
				connection.setExceptionListener(this);
				
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				
				Destination destination = session.createQueue("TEST.FOO");
				
				MessageConsumer consumer = session.createConsumer(destination);
				
				Message message = consumer.receive(1000);
				
				if(message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					System.out.println("Recebido: " + text);
				} else {
					System.out.println("Recebido: " + message);
				}
				consumer.close();
				session.close();
				connection.close();				
			} catch (JMSException e) {
				e.printStackTrace();
			}
			
		}

		public void onException(JMSException exception) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
}
