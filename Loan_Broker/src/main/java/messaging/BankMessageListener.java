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

            //JMSSender sender = new JMSSender();
            //sender.send(new BankInterestRequest(loanRequest.getAmount(), loanRequest.getTime()));
            //gui.add((LoanRequest)rr.getRequest(), (BankInterestReply)rr.getReply());
        } catch (JMSException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

}
