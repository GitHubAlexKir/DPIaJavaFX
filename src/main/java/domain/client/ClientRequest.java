package domain.client;

import java.io.Serializable;

public class ClientRequest implements Serializable {
    private String corrolationID;
    private String itemName;

    public ClientRequest(String corrolationID, String itemName) {
        this.corrolationID= corrolationID;
        this.itemName = itemName;
    }

    public String getCorrolationId() {
        return corrolationID;
    }

    public void setCorrolationId(String corrolationId) {
        this.corrolationID = corrolationId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
