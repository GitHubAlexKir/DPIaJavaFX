package gateway;

import service.MQConnection;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.naming.NamingException;

public class MessageReceiverGateway {
    private MQConnection mqConnection;
    private MessageConsumer consumer;

    public MessageReceiverGateway(String name) throws JMSException, NamingException {
        this.mqConnection = new MQConnection(name);
        this.consumer = this.mqConnection.getSession().createConsumer((Destination) mqConnection.getJndiContext().lookup(name));
        this.mqConnection.start();
    }

    public MessageConsumer getConsumer() {
        return this.consumer;
    }
}
