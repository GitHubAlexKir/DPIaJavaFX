package controller;

import com.google.gson.Gson;
import domain.client.ClientReply;
import domain.client.ClientRequest;
import domain.item.Item;
import domain.item.ItemReply;
import domain.item.ItemRequest;
import gateway.MessageReceiverGateway;
import gateway.MessageSenderGateway;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.jms.*;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;

public class BrokerController {

    @FXML
    public TableView<Item> TableView;
    @FXML
    public TableColumn NameColumn;
    @FXML
    public TableColumn ProductIDColumn;
    @FXML
    public TableColumn PriceColumn;
    @FXML
    public TableColumn SellerColumn;

    private MessageReceiverGateway clientReceiver = new MessageReceiverGateway("ClientRequest");
    private MessageSenderGateway itemServiceSender = new MessageSenderGateway("ItemRequest");
    private MessageSenderGateway clientSender = new MessageSenderGateway("ClientReply");
    private MessageReceiverGateway itemServiceReceiver = new MessageReceiverGateway("ItemReply");
    private Gson gson = new Gson();


    private List<Item> items;

    public BrokerController() throws NamingException, JMSException {
        items = new ArrayList<>();
    }

    private void reloadItems() {
        TableView.getItems().clear();
        NameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        ProductIDColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("productID"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("price"));
        SellerColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("seller"));
        if (!items.isEmpty()) {
            for (Item i : items) {
                TableView.getItems().add(i);
            }
        }
    }

   public void loadMQRecieveFromItemService()
   {
       try {
           itemServiceReceiver.getConsumer().setMessageListener(msg -> {
               if (msg instanceof TextMessage) {
                   try {
                       String Json = ((TextMessage) msg).getText();
                       ItemReply itemReply = gson.fromJson(Json, ItemReply.class);
                       add(itemReply);
                   } catch (JMSException  e) {
                       e.printStackTrace();
                   }
               }
           });

       } catch (JMSException e) {
           e.printStackTrace();
       }

   }

    private void add(ItemReply itemReply) {
        for (int i = 0; i < items.size(); i++){
            Item rr = items.get(i);
            if (rr.getCorrelationID().equals(itemReply.getCorrolationID())){
                rr.setProductID(itemReply.getProductID());
                rr.setSeller("testing");
                rr.setPrice("29.99");
                reloadItems();
                try {
                    clientSender.send(new ClientReply(rr.getCorrelationID(),rr.getSeller(),Double.valueOf(rr.getPrice())));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadMQRecieveFromClient()
    {
        try {
            clientReceiver.getConsumer().setMessageListener(msg -> {
                if (msg instanceof TextMessage) {
                    try {
                        String Json = ((TextMessage) msg).getText();
                        ClientRequest clientRequest = gson.fromJson(Json, ClientRequest.class);
                        add(clientRequest);
                    } catch (JMSException  e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    private void add(ClientRequest clientRequest) {
        Item item = new Item();
        item.setCorrelationID(clientRequest.getCorrolationId());
        item.setName(clientRequest.getItemName());
        items.add(item);
        reloadItems();
        ItemRequest itemRequest = new ItemRequest(item.getCorrelationID(),item.getName());
        try {
            itemServiceSender.send(itemRequest);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
