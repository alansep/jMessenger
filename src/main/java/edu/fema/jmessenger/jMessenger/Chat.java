package edu.fema.jmessenger.jMessenger;

import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

public class Chat implements MessageListener {
	private TopicSession pubSession;
	private TopicPublisher publisher;
	private TopicConnection connection;
	private String username;

	public Chat(String fabricaDeTopic, String nomeDoTopico, String nomeDeUsuario) throws Exception {
		InitialContext contexto = new InitialContext();
		TopicConnectionFactory fabricaDeConexaoDeTopic = (TopicConnectionFactory) contexto.lookup(fabricaDeTopic);
		TopicConnection conexao = fabricaDeConexaoDeTopic.createTopicConnection();

		TopicSession sessaoPublisher = conexao.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		TopicSession sessaoSubscriber = conexao.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

		Topic topicDoChat = (Topic) contexto.lookup(nomeDoTopico);

		TopicPublisher publisher = sessaoPublisher.createPublisher(topicDoChat);
		TopicSubscriber subscriber = sessaoSubscriber.createSubscriber(topicDoChat);

		subscriber.setMessageListener(this);

		this.connection = conexao;
		this.pubSession = sessaoPublisher;
		this.publisher = publisher;
		this.username = nomeDeUsuario;

		conexao.start();
	}

	public void onMessage(Message mensagem) {
		try {
			TextMessage textMessage = (TextMessage) mensagem;
			System.out.println(textMessage.getText());
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
	}

	public void escreverMensagem(String texto) throws JMSException {
		TextMessage message = pubSession.createTextMessage();
		message.setText(username + "diz: " + texto);
		publisher.publish(message);
	}

	public void close() throws JMSException {
		connection.close();
	}

	public static void main(String[] args) {
		Scanner scannerLinhaDeComando = new Scanner(System.in);
		try {

			System.out.print("Digite seu nome: ");
			String nome = scannerLinhaDeComando.nextLine();

			Chat chat = new Chat("TopicCF", "topicChat", nome);
			System.out.println("\n\n\tBem vindo " + nome + "!");

			while (true) {
				String palavra = scannerLinhaDeComando.nextLine();
				if (palavra.equalsIgnoreCase("exit")) {
					chat.close();
					System.exit(0);
				} else {
					chat.escreverMensagem(palavra);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			scannerLinhaDeComando.close();
		}
	}
}
