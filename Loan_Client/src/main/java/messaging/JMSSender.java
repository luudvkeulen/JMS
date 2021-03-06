package messaging;

import com.google.gson.Gson;
import gui.LoanClientFrame;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import javax.naming.NamingException;
import model.LoanRequest;

public class JMSSender {

    final static Logger LOGGER = Logger.getLogger(JMSSender.class.getName());
    Connection connection;
    Session session;

    Destination destination;
    MessageProducer producer;
    HashMap<String, LoanRequest> correlations;

    public JMSSender(HashMap<String, LoanRequest> correlations) {
        this.correlations = correlations;
        try {
            Properties properties = new Properties();
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            properties.put(("queue.clientToBroker"), "clientToBroker");

            Context jndiContext = new InitialContext(properties);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");;

            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destination = (Destination) jndiContext.lookup("clientToBroker");
            producer = session.createProducer(destination);

        } catch (NamingException | JMSException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void send(LoanRequest loanRequest) {
        try {
            Message message = session.createTextMessage(new Gson().toJson(loanRequest));

            producer.send(message);
            correlations.put(message.getJMSMessageID(), loanRequest);

            session.close();
            connection.close();
        } catch (JMSException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
