package gateway;

import com.google.gson.Gson;
import domain.ClientRequest;
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
}
