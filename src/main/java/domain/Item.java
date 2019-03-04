package domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Transient
    private String price;
    @Transient
    private String seller;
    @Transient
    private String correlationID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        if (price != null) {
            return price;
        }
        else {
            return "Waiting..";
        }
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSeller() {
        if (seller != null) {
            return seller;
        }
        else {
            return "Waiting..";
        }
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
