package edu.fema.jmessenger.jMessenger;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

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
import javax.swing.JOptionPane;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import edu.fema.jmessenger.jMessenger.entidades.Mensagem;
import edu.fema.jmessenger.jMessenger.locate.Starter;

public class App {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.print("Produzir ou Consumir?:");
		int resultado = sc.nextInt();
		if (resultado == 1) {
			String nome = JOptionPane.showInputDialog(null,"Digite seu nome!");
			System.out.println("Digite suas mensagens!");
			while (true) {
				String mensagem = sc.nextLine();
				Starter.thread(new HelloWorldProducer(new Mensagem(nome, mensagem)), false);
			}
		} else {
			new Timer().schedule(new tarefa2(), 0, 2000);
		}

	}

	public static class tarefa2 extends TimerTask {
		public void run() {
			Starter.thread(new HelloWorldConsumer(), false);
		}
	}

	public static class HelloWorldProducer implements Runnable {

		private Mensagem mensagem;

		public HelloWorldProducer(Mensagem mensagem) {
			this.mensagem = mensagem;
		}

		public void run() {
			
			
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_BROKER_URL);

			try {
				Connection connection = connectionFactory.createConnection();
				connection.start();

				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				Destination destination = session.createQueue("TEST.FOO");
				MessageProducer producer = session.createProducer(destination);
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

				TextMessage message = session.createTextMessage(this.mensagem.toString());
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
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_BROKER_URL);

			try {
				Connection connection = connectionFactory.createConnection();
				connection.start();

				connection.setExceptionListener(this);

				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				Destination destination = session.createQueue("TEST.FOO");

				MessageConsumer consumer = session.createConsumer(destination);

				Message message = consumer.receive(1000);

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					if (text != null) {
						System.out.println("Recebido: " + text);
					}
				} else {
					if(message != null) {
					System.out.println("Recebido: " + message);
				}}
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
