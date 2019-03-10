package Application;

import controller.Seller3Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Seller3Main extends Application {

    private String sellerName = "Seller3";

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/seller3.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle(this.sellerName + " - Webshop");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        Seller3Controller controller = fxmlLoader.<Seller3Controller>getController();
        controller.loadMQRecieveFromBroker();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
