package messaging;

import com.google.gson.Gson;
import gui.LoanBrokerFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import model.BankInterestReply;
import model.LoanRequest;

public class BankMessageListener implements MessageListener {
    final static Logger LOGGER = Logger.getLogger(ClientMessageListener.class.getName());
    
    private final LoanBrokerFrame gui;
    
    public BankMessageListener(LoanBrokerFrame gui) {
        this.gui = gui;
    }
    
    @Override
    public void onMessage(Message msg) {
        try {
            TextMessage message = (TextMessage) msg;
            BankInterestReply bankInterestReply = new Gson().fromJson(message.getText(), BankInterestReply.class);
            System.out.println("Correlation reply id: " + message.getJMSCorrelationID());
            LoanRequest request = gui.correlations.get(message.getJMSCorrelationID());
            //JMSSender sender = new JMSSender();
            //sender.send(new BankInterestRequest(loanRequest.getAmount(), loanRequest.getTime()));
            gui.add(request, bankInterestReply);
        } catch (JMSException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

}
