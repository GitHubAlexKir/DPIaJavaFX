package domain.Seller;

import java.io.Serializable;

public class SellerReply implements Serializable {
    private String correlationID;
    private double price;
    private String sellerName;

    public SellerReply(String correlationID, double price, String sellerName) {
        this.correlationID = correlationID;
        this.price = price;
        this.sellerName = sellerName;
    }

    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
