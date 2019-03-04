package domain.item;

import java.io.Serializable;

public class ItemRequest implements Serializable {
    private String corrolationID;
    private String itemName;

    public ItemRequest(String corrolationID, String itemName) {
        this.corrolationID = corrolationID;
        this.itemName = itemName;
    }

    public String getCorrolationID() {
        return corrolationID;
    }

    public void setCorrolationID(String corrolationID) {
        this.corrolationID = corrolationID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
