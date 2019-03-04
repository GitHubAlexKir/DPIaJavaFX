package domain;

import java.io.Serializable;

public class ClientReply implements Serializable {
    private String corrolationID;
    private String seller;
    private double price;

    public ClientReply(String corrolationID, String seller, double price) {
        this.corrolationID = corrolationID;
        this.seller = seller;
        this.price = price;
    }

    public String getCorrolationID() {
        return corrolationID;
    }

    public void setCorrolationID(String corrolationID) {
        this.corrolationID = corrolationID;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
