package messaging;

import com.google.gson.Gson;
import gui.LoanBrokerFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import model.BankInterestRequest;
import model.LoanRequest;

public class ClientMessageListener implements MessageListener {
    final static Logger LOGGER = Logger.getLogger(ClientMessageListener.class.getName());
    
    private final LoanBrokerFrame gui;
    
    public ClientMessageListener(LoanBrokerFrame gui) {
        this.gui = gui;
    }
    
    @Override
    public void onMessage(Message msg) {
        try {
            TextMessage message = (TextMessage) msg;
            LoanRequest loanRequest = new Gson().fromJson(message.getText(), LoanRequest.class);
            gui.add(loanRequest);
            BrokerToBank sender = new BrokerToBank(gui);
            BankInterestRequest interestRequest = new BankInterestRequest(loanRequest.getAmount(), loanRequest.getTime());
            sender.send(loanRequest, interestRequest, message.getJMSMessageID());
            gui.add(loanRequest, interestRequest);
        } catch (JMSException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

}
