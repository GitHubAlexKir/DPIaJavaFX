package ItemService;

import com.google.gson.Gson;
import domain.*;
import gateway.MessageReceiverGateway;
import gateway.MessageSenderGateway;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import repository.ClientRepository;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemController {
    @FXML
    public TableView<Item> TableView;
    @FXML
    public TableColumn NameColumn;
    @FXML
    public TableColumn ProductIDColumn;

    private ClientRepository repo;
    private MessageSenderGateway sender = new MessageSenderGateway("ItemReply");
    private MessageReceiverGateway receiver = new MessageReceiverGateway("ItemRequest");
    private Gson gson = new Gson();
    private ItemReply itemReply = new ItemReply(null,null);

    public ItemController() throws NamingException, JMSException {
    }

   public void loadMQRecieveFromBroker()
   {
       try {
           receiver.getConsumer().setMessageListener(msg -> {
               if (msg instanceof TextMessage) {
                   try {
                       String Json = ((TextMessage) msg).getText();
                       ItemRequest itemRequest = gson.fromJson(Json, ItemRequest.class);
                       this.itemReply = new ItemReply(itemRequest.getCorrolationID(),"Error");
                       switch (itemRequest.getItemName()){
                           case "Samsung TV":
                               itemReply.setProductID("AQE49Q6FNALXXN");
                               break;
                           case "Acer Laptop":
                               itemReply.setProductID("BQE49Q6FNALXXN");
                               break;
                           case "Samsung S5 telefoon":
                               itemReply.setProductID("CQE49Q6FNALXXN");
                               break;
                       }
                       Item item = new Item();
                       item.setProductID(itemReply.getProductID());
                       item.setName(itemRequest.getItemName());
                       add(item);
                   } catch (JMSException  e) {
                       e.printStackTrace();
                   }
               }
           });

       } catch (JMSException e) {
           e.printStackTrace();
       }

   }
   @FXML
   public void send()
   {
       System.out.println("sending");
       try {
           sender.send(itemReply);
       } catch (JMSException e) {
           e.printStackTrace();
       }
   }
    private void add(Item item) {
        NameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        ProductIDColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("productID"));
        TableView.getItems().add(item);
    }
}
