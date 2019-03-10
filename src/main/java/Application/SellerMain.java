package Application;

import controller.ClientController;
import controller.SellerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SellerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/seller.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Seller - Webshop");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        SellerController controller = fxmlLoader.<SellerController>getController();
        controller.loadMQRecieveFromBroker();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
