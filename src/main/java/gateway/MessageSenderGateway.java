package gateway;

import com.google.gson.Gson;
import domain.ClientReply;
import domain.ClientRequest;
import domain.ItemReply;
import domain.ItemRequest;
import service.MQConnection;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.naming.NamingException;

public class MessageSenderGateway {
    private MQConnection mqConnection;
    private MessageProducer producer;
    private Gson gson = new Gson();

    public MessageSenderGateway(String name) throws JMSException, NamingException {
        this.mqConnection = new MQConnection(name);
        this.producer = this.mqConnection.getSession().createProducer((Destination) mqConnection.getJndiContext().lookup(name));
    }

    public void send(ClientRequest request) throws JMSException {
        TextMessage msg = this.mqConnection.getSession().createTextMessage(gson.toJson(request));
        producer.send(msg);
    }

    public void send(ItemRequest request) throws JMSException {
        TextMessage msg = this.mqConnection.getSession().createTextMessage(gson.toJson(request));
        producer.send(msg);
    }

    public void send(ClientReply reply) throws JMSException {
        TextMessage msg = this.mqConnection.getSession().createTextMessage(gson.toJson(reply));
        producer.send(msg);
    }

    public void send(ItemReply reply) throws JMSException {
        TextMessage msg = this.mqConnection.getSession().createTextMessage(gson.toJson(reply));
        producer.send(msg);
    }
}
