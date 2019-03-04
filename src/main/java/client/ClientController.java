package client;

import domain.Item;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;

public class ClientController {

    @FXML
    public TableView<Item> ItemTableView;
    @FXML
    public TableColumn ItemPriceColumn;
    @FXML
    public TableColumn ItemNameColumn;
    @FXML
    public ImageView ProductImageView;

    private ClientRepository repo;

    private List<Item> items;

    private Item selectedItem;

    public ClientController() {
        repo = new ClientRepository();
        items = new ArrayList<>();
        //getProducts();
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
           System.out.println(selectedItem.toString());
       }
    }

    public void getProducts()
    {
        ItemTableView.getItems().clear();
        ItemNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        ItemPriceColumn.setCellValueFactory(new PropertyValueFactory<Item,String>("price"));
        items = repo.findAll();
        if (!items.isEmpty()) {
            for (Item i : items) {
                ItemTableView.getItems().add(i);
            }
        }
    }

}
