package edu.fema.jmessenger.jMessenger;



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

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import edu.fema.jmessenger.jMessenger.visual.ChatActive;

public class Chat implements MessageListener {

    private TopicSession pubSession;
    private TopicPublisher publisher;
    private TopicConnection connection;
    private String username;
    private ChatActive chat;

    public Chat(String fabricaDeTopic, String nomeDoTopico, String nomeDeUsuario, String ip, ChatActive chat) throws Exception {
        
        InitialContext contexto = new InitialContext();
        this.chat = chat;
      

        TopicConnectionFactory fabricaDeConexaoDeTopic = new ActiveMQConnectionFactory("admin", "admin",
                "tcp://" + ip + ":61616");
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
            this.chat.atualizarTexto(textMessage.getText());
        } catch (JMSException jmse) {
            jmse.printStackTrace();
        }
    }

    public void escreverMensagem(String usuario, String texto) throws JMSException {
       
       TextMessage message = pubSession.createTextMessage();
       message.setText(usuario + " diz: " + texto);
        System.out.println(message.getText());
       publisher.publish(message);
    }

    public void close() throws JMSException {
        connection.close();
    }
}
