package domain;

import java.io.Serializable;

public class SellerRequest implements Serializable {
    private String correlationID;
    private long productID;

    public SellerRequest(String correlationID, long productID) {
        this.correlationID = correlationID;
        this.productID = productID;
    }

    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }
}
