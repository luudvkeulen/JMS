package messaging;

import com.google.gson.Gson;
import gui.LoanBrokerFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import model.LoanRequest;

public class LoanMessageListener implements MessageListener {
    final static Logger LOGGER = Logger.getLogger(LoanMessageListener.class.getName());
    
    private final LoanBrokerFrame gui;
    
    public LoanMessageListener(LoanBrokerFrame gui) {
        this.gui = gui;
    }
    
    @Override
    public void onMessage(Message msg) {
        try {
            TextMessage message = (TextMessage) msg;
            System.out.println("Message received: " + message.getText());
            LoanRequest loanRequest = new Gson().fromJson(message.getText(), LoanRequest.class);
            gui.add(loanRequest);
        } catch (JMSException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

}
