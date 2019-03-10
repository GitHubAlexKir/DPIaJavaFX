package gateway;

import com.google.gson.Gson;
import domain.Seller.SellerReply;
import domain.Seller.SellerRequest;
import domain.client.ClientReply;
import domain.client.ClientRequest;
import domain.item.ItemReply;
import domain.item.ItemRequest;
import service.MQConnection;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.naming.NamingException;

public class MessageSenderGateway {
    private MQConnection mqConnection;
    private MessageProducer producer;

    public MessageSenderGateway(String name) throws JMSException, NamingException {
        this.mqConnection = new MQConnection(name);
        this.producer = this.mqConnection.getSession().createProducer((Destination) mqConnection.getJndiContext().lookup(name));
    }

    public void send(String json) throws JMSException {
        TextMessage msg = this.mqConnection.getSession().createTextMessage(json);
        producer.send(msg);
    }
}
