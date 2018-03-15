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
import model.JMSContext;

public class JMSListener {

    final static Logger LOGGER = Logger.getLogger(JMSListener.class.getName());

    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;
    private LoanBrokerFrame gui;

    public JMSListener(LoanBrokerFrame gui) {
        this.gui = gui;
    }

    public void listen(String queue) {
        try {
            Properties properties = new Properties();
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            properties.put(("queue." + queue), queue);

            Context jndiContext = new InitialContext(properties);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");;

            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destination = (Destination) jndiContext.lookup(queue);
            consumer = session.createConsumer(destination);

            connection.start();

            if (queue.equals(JMSContext.CLIENT)) {
                ClientMessageListener clientMessageListener = new ClientMessageListener(gui);
                consumer.setMessageListener(clientMessageListener);
            } else if (queue.equals(JMSContext.BANK)) {
                BankMessageListener bankMessageListener = new BankMessageListener(gui);
                consumer.setMessageListener(bankMessageListener);
            }
        } catch (NamingException | JMSException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
