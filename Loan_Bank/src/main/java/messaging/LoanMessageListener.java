package messaging;

import com.google.gson.Gson;
import gui.JMSBankFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import model.BankInterestRequest;

public class LoanMessageListener implements MessageListener {

    final static Logger LOGGER = Logger.getLogger(LoanMessageListener.class.getName());

    private final JMSBankFrame gui;

    public LoanMessageListener(JMSBankFrame gui) {
        this.gui = gui;
    }

    @Override
    public void onMessage(Message msg) {
        try {
            TextMessage message = (TextMessage) msg;
            System.out.println("Message received: " + message.getText());
            BankInterestRequest interestRequest = new Gson().fromJson(message.getText(), BankInterestRequest.class);
            gui.add(interestRequest, message.getJMSMessageID());
        } catch (JMSException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

}
