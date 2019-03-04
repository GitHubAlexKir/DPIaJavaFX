package controller;

import com.google.gson.Gson;
import domain.client.ClientReply;
import domain.client.ClientRequest;
import domain.item.Item;
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

public class ClientController {

    @FXML
    public TableView<Item> ItemTableView;
    @FXML
    public TableColumn ItemIDColumn;
    @FXML
    public TableColumn ItemNameColumn;


    @FXML
    public TableView<Item> ClientTableView;
    @FXML
    public TableColumn ItemPriceColumn;
    @FXML
    public TableColumn ItemRequestNameColumn;
    @FXML
    public TableColumn SellerColumn;



    @FXML
    public ImageView ProductImageView;


    private ClientRepository repo;
    private MessageSenderGateway sender = new MessageSenderGateway("ClientRequest");
    private MessageReceiverGateway receiver = new MessageReceiverGateway("ClientReply");
    private Gson gson = new Gson();


    private List<Item> items;

    private Item selectedItem;

    private List<Item> requestItems;

    public ClientController() throws NamingException, JMSException {
        repo = new ClientRepository();
        items = new ArrayList<>();
        requestItems = new ArrayList<>();
    }
    @FXML
    private void selectedItem()
    {
        if (ItemTableView.getSelectionModel().getSelectedItem() != null) {
            selectedItem = ItemTableView.getSelectionModel().getSelectedItem();
            Image image = new Image("placeholder2.png");
            switch (Math.toIntExact(selectedItem.getId())) {
                case 1:
                    image = new Image("1.png");
                    break;
                case 2:
                    image = new Image("2.png");
                    break;
                case 3:
                    image = new Image("3.png");
                    break;
            }
            this.ProductImageView.setImage(image);
        }
        else
        {
            selectedItem = null;
            Image image = new Image("placeholder.png");
            this.ProductImageView.setImage(image);
        }
    }

    @FXML
    public void GetPricePressed()
    {
       if (selectedItem != null)
       {
           String corrolationId = UUID.randomUUID().toString();
           ClientRequest request = new ClientRequest(corrolationId,selectedItem.getName());
          try {
              sender.send(request);
          } catch (JMSException e) {
              e.printStackTrace();
          }
           Item item = new Item();
           item.setCorrelationID(corrolationId);
           item.setName(selectedItem.getName());
           requestItems.add(item);
           reloadRequests();
       }
    }

    private void reloadRequests() {
        ClientTableView.getItems().clear();
        ItemRequestNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        ItemPriceColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("price"));
        SellerColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("seller"));
        if (!requestItems.isEmpty()) {
            for (Item i : requestItems) {
                ClientTableView.getItems().add(i);
            }
        }
    }

    public void getProducts()
    {
        ItemTableView.getItems().clear();
        ItemNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        ItemIDColumn.setCellValueFactory(new PropertyValueFactory<Item,String>("id"));

        items = repo.findAll();
        if (!items.isEmpty()) {
            for (Item i : items) {
                ItemTableView.getItems().add(i);
            }
        }
    }

   public void loadMQRecieveFromBroker()
   {
       try {
           receiver.getConsumer().setMessageListener(msg -> {
               if (msg instanceof TextMessage) {
                   try {
                       String Json = ((TextMessage) msg).getText();
                       ClientReply ClientReply = gson.fromJson(Json, domain.client.ClientReply.class);
                       add(ClientReply);
                   } catch (JMSException  e) {
                       e.printStackTrace();
                   }
               }
           });

       } catch (JMSException e) {
           e.printStackTrace();
       }

   }

    private void add(ClientReply clientReply) {
        System.out.println("incomming client reply " + clientReply.getCorrolationID() + " " +  clientReply.getSeller() +  " " + clientReply.getPrice());
        System.out.println("size of request items " + requestItems.size());
        for (int i = 0; i < requestItems.size(); i++){
            Item rr = requestItems.get(i);
            System.out.println(rr.toString());
            if (rr.getCorrelationID().equals(clientReply.getCorrolationID())){
                System.out.println("MAtch found");
                rr.setPrice(String.valueOf(clientReply.getPrice()));
                rr.setSeller(clientReply.getSeller());
                reloadRequests();
            }
        }
    }

}
