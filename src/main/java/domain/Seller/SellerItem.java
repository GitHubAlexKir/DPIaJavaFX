package domain.Seller;

public class SellerItem {
    private String correlationID;
    private String productID;
    private String name;
    private double price;

    public SellerItem(String correlationID, String productID, String name, double price) {
        this.correlationID = correlationID;
        this.productID = productID;
        this.name = name;
        this.price = price;
    }

    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
