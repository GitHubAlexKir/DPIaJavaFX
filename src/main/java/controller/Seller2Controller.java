package controller;

import com.google.gson.Gson;
import domain.Seller.SellerItem;
import domain.Seller.SellerReply;
import domain.Seller.SellerRequest;
import gateway.MessageReceiverGateway;
import gateway.MessageSenderGateway;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Seller2Controller {

    @FXML
    public TableView<SellerItem> ItemTableView;
    @FXML
    public TableColumn ItemProductIDColumn;
    @FXML
    public TableColumn ItemNameColumn;
    @FXML
    public TableColumn ItemPriceColumn;
    @FXML
    public ImageView ProductImageView;
    @FXML
    public Text PriceText;


    private MessageSenderGateway sender = new MessageSenderGateway("SellerReply");
    private MessageReceiverGateway receiver = new MessageReceiverGateway("Seller2Request");
    private Gson gson = new Gson();

    private SellerItem selectedItem;

    private List<SellerItem> requestItems;

    private int selectedDiscount = 0;
    private double price;
    private String sellerName = "Seller2";

    public Seller2Controller() throws NamingException, JMSException {
        requestItems = new ArrayList<>();
    }
    @FXML
    private void selectedItem()
    {
        if (ItemTableView.getSelectionModel().getSelectedItem() != null) {
            selectedItem = ItemTableView.getSelectionModel().getSelectedItem();
            Image image = new Image("placeholder2.png");
            switch (selectedItem.getProductID()) {
                case "AQE49Q6FNALXXN":
                    image = new Image("1.png");
                    break;
                case "BQE49Q6FNALXXN":
                    image = new Image("2.png");
                    break;
                case "CQE49Q6FNALXXN":
                    image = new Image("3.png");
                    break;
            }
            this.ProductImageView.setImage(image);
            setPriceText();
        }
        else
        {
            selectedItem = null;
            Image image = new Image("placeholder.png");
            this.ProductImageView.setImage(image);
        }
    }
    @FXML
    private void setDiscountZero(){
        this.selectedDiscount = 0;
        setPriceText();
    }

    @FXML
    private void setDiscountTen(){
        this.selectedDiscount = 10;
        setPriceText();
    }
    @FXML
    private void setDiscountTwenty(){
        this.selectedDiscount = 20;
        setPriceText();
    }

    private void setPriceText()
    {
        if (selectedItem != null)
        {
            this.price = selectedItem.getPrice() * (100 - this.selectedDiscount) / 100;
            BigDecimal bd = new BigDecimal(Double.toString(this.price));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            this.price = bd.doubleValue();
            this.PriceText.setText("Prijs: " + this.price);
        }
    }

    @FXML
    public void SendPricePressed()
    {
        //add(new SellerRequest("dgg","AQE49Q6FNALXXN"));
        //add(new SellerRequest("ddvgg","BQE49Q6FNALXXN"));
        //add(new SellerRequest("ddvdgg","CQE49Q6FNALXXN"));
       if (selectedItem != null)
       {

           SellerReply sellerReply = new SellerReply(selectedItem.getCorrelationID(),this.price,this.sellerName);
          try {
              sender.send(gson.toJson(sellerReply));
          } catch (JMSException e) {
              e.printStackTrace();
          }
           reloadRequests();
       }
    }

    private void reloadRequests() {
        ItemTableView.getItems().clear();
        ItemProductIDColumn.setCellValueFactory(new PropertyValueFactory<SellerItem, String>("ProductID"));
        ItemNameColumn.setCellValueFactory(new PropertyValueFactory<SellerItem, String>("name"));
        ItemPriceColumn.setCellValueFactory(new PropertyValueFactory<SellerItem, String>("price"));
        if (!requestItems.isEmpty()) {
            for (SellerItem i : requestItems) {
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
                       System.out.println("incomming");
                       String Json = ((TextMessage) msg).getText();
                       SellerRequest sellerRequest = gson.fromJson(Json, SellerRequest.class);
                       add(sellerRequest);
                   } catch (JMSException  e) {
                       e.printStackTrace();
                   }
               }
           });

       } catch (JMSException e) {
           e.printStackTrace();
       }

   }

    private void add(SellerRequest sellerRequest) {
        System.out.println("incomming Seller Request " + sellerRequest.getCorrelationID() + " " +  sellerRequest.getProductID());
        System.out.println("size of request items " + requestItems.size());
        requestItems.add(getSellerItem(sellerRequest));
        reloadRequests();
    }

    private SellerItem getSellerItem(SellerRequest sellerRequest) {
        return new SellerItem(sellerRequest.getCorrelationID(),sellerRequest.getProductID(),
                getProductName(sellerRequest.getProductID()),getProductPrice(sellerRequest.getProductID()));
    }

    private double getProductPrice(String productID) {
        double price = 1;
        switch (productID) {
            case "AQE49Q6FNALXXN":
                price = 359.99;
                break;
            case "BQE49Q6FNALXXN":
                price = 539.99;
                break;
            case "CQE49Q6FNALXXN":
                price = 289.99;
                break;
        }
        return price;
    }

    private String getProductName(String productID) {
        String productName = "error";
        switch (productID) {
            case "AQE49Q6FNALXXN":
                productName = "Samsung TV";
                break;
            case "BQE49Q6FNALXXN":
                productName = "Acer Laptop";
                break;
            case "CQE49Q6FNALXXN":
                productName = "Samsung S5 telefoon";
                break;
        }
        return productName;
    }
}
