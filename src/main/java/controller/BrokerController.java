package controller;

import com.google.gson.Gson;
import domain.Seller.SellerReply;
import domain.Seller.SellerRequest;
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
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

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
    private MessageSenderGateway seller1Sender = new MessageSenderGateway("Seller1Request");
    private MessageSenderGateway seller2Sender = new MessageSenderGateway("Seller2Request");
    private MessageSenderGateway seller3Sender = new MessageSenderGateway("Seller3Request");
    private MessageSenderGateway clientSender = new MessageSenderGateway("ClientReply");
    private MessageReceiverGateway itemServiceReceiver = new MessageReceiverGateway("ItemReply");
    private MessageReceiverGateway sellerReceiver = new MessageReceiverGateway("SellerReply");
    private Gson gson = new Gson();


    private List<Item> items;
    private List<SellerReply> sellerReplies;

    public BrokerController() throws NamingException, JMSException {
        items = new ArrayList<>();
        sellerReplies = new ArrayList<>();
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

    private void checkReplies(SellerReply sellerReply){
        List<SellerReply> sharedReplies = new ArrayList<>();
        sharedReplies.add(sellerReply);
        for (int i = 0; i < this.sellerReplies.size(); i++) {
            if (sellerReplies.get(i).getCorrelationID().equals(sellerReply.getCorrelationID()))
            {
                sharedReplies.add(sellerReplies.get(i));
            }

        }
        if(sharedReplies.size() == 3)
        {
            SellerReply minPriceReply = sharedReplies
                    .stream()
                    .min(Comparator.comparing(SellerReply::getPrice))
                    .orElseThrow(NoSuchElementException::new);
            add(minPriceReply);
        }
        else {
            sellerReplies.add(sellerReply);
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
    public void loadMQRecieveFromSeller()
    {
        try {
            sellerReceiver.getConsumer().setMessageListener(msg -> {
                if (msg instanceof TextMessage) {
                    try {
                        String Json = ((TextMessage) msg).getText();
                        SellerReply sellerReply = gson.fromJson(Json, SellerReply.class);
                        checkReplies(sellerReply);
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
            itemServiceSender.send(gson.toJson(itemRequest));
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    private void add(ItemReply itemReply) {
        for (int i = 0; i < items.size(); i++){
            Item rr = items.get(i);
            if (rr.getCorrelationID().equals(itemReply.getCorrolationID())){
                rr.setProductID(itemReply.getProductID());
                reloadItems();
                try {
                    System.out.println("Sending to Seller");
                    seller1Sender.send(gson.toJson(new SellerRequest(rr.getCorrelationID(),rr.getProductID())));
                    seller2Sender.send(gson.toJson(new SellerRequest(rr.getCorrelationID(),rr.getProductID())));
                    seller3Sender.send(gson.toJson(new SellerRequest(rr.getCorrelationID(),rr.getProductID())));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void add(SellerReply sellerReply) {
        for (int i = 0; i < items.size(); i++){
            Item rr = items.get(i);
            if (rr.getCorrelationID().equals(sellerReply.getCorrelationID())){
                rr.setPrice(String.valueOf(sellerReply.getPrice()));
                rr.setSeller(sellerReply.getSellerName());
                reloadItems();
                try {
                    clientSender.send(gson.toJson(new ClientReply(rr.getCorrelationID(),rr.getSeller(),Double.valueOf(rr.getPrice()))));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
