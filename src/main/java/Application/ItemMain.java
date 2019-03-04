package Application;

import controller.ItemController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ItemMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ItemService.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("ItemService - Webshop");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        ItemController controller = fxmlLoader.<ItemController>getController();
        controller.loadMQRecieveFromBroker();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
