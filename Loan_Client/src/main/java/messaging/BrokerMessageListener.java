package messaging;

import com.google.gson.Gson;
import gui.LoanClientFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import model.LoanReply;
import model.LoanRequest;

public class BrokerMessageListener implements MessageListener {
    final static Logger LOGGER = Logger.getLogger(BrokerMessageListener.class.getName());
    
    private final LoanClientFrame gui;
    
    public BrokerMessageListener(LoanClientFrame gui) {
        this.gui = gui;
    }
    
    @Override
    public void onMessage(Message msg) {
        try {
            System.out.println("Got reply");
            TextMessage message = (TextMessage) msg;
            LoanReply reply = new Gson().fromJson(message.getText(), LoanReply.class);
            LoanRequest request = gui.correlations.get(message.getJMSCorrelationID());
            gui.add(request, reply);
        } catch (JMSException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

}
