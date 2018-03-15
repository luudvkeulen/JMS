package messaging;

import gui.LoanBrokerFrame;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSListener {

    final static Logger LOGGER = Logger.getLogger(JMSListener.class.getName());

    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;
    private LoanMessageListener loanMessageListener;

    public JMSListener(LoanBrokerFrame gui) {
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
            consumer = session.createConsumer(destination);

            connection.start();

            loanMessageListener = new LoanMessageListener(gui);
            consumer.setMessageListener(loanMessageListener);
        } catch (NamingException | JMSException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
