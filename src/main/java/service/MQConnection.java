package service;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class MQConnection {

    private javax.jms.Connection connection;
    private Context jndiContext;

    public MQConnection(String queue) throws NamingException, JMSException {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        // connect to the Destination called “loanReply”
        props.put(("queue." + queue), queue);
        this.jndiContext = new InitialContext(props);
        ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                .lookup("ConnectionFactory");
        this.connection = connectionFactory.createConnection();
    }

    public Session getSession() throws JMSException {
        return this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public Context getJndiContext()
    {
        return this.jndiContext;
    }

    public void start() throws JMSException {
        this.connection.start();
    }
}
