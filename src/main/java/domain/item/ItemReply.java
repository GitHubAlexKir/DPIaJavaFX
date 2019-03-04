package domain.item;

import java.io.Serializable;

public class ItemReply implements Serializable {
    private String corrolationID;
    private String productID;

    public ItemReply(String corrolationID, String productID) {
        this.corrolationID = corrolationID;
        this.productID = productID;
    }

    public String getCorrolationID() {
        return corrolationID;
    }

    public void setCorrolationID(String corrolationID) {
        this.corrolationID = corrolationID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
