package listeners;

import entities.Property;
import integradores.IntegradorCorreo;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.MessagingException;

@JMSDestinationDefinition(name = "java:app/jms/myQueue", interfaceName = "javax.jms.Queue", resourceAdapter = "jmsra", destinationName = "myQueue")
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "java:app/jms/myQueue")
    ,
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:app/jms/myQueue")
    ,
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable")
    ,
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "java:app/jms/myQueue")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    ,
        @ActivationConfigProperty(propertyName = "addressList", propertyValue = "10.192.12.42")
})
public class ListenerCorreo implements MessageListener {
    @EJB
    private IntegradorCorreo integradorCorreo;
  
    public ListenerCorreo() {
        
    }
    
    @Override
    public void onMessage(Message message) {
        System.err.println("nuevo mensaje");
        Property p = null;
        try {
            p = (Property) ((ObjectMessage)message).getObject();
            integradorCorreo.sendMail(p);
        } catch (JMSException ex) {
            Logger.getLogger(ListenerCorreo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ListenerCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
