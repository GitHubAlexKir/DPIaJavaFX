package Application;

import controller.Seller1Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Seller1Main extends Application {

    private String sellerName = "Seller1";

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/seller1.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle(this.sellerName + " - Webshop");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        Seller1Controller controller = fxmlLoader.<Seller1Controller>getController();
        controller.loadMQRecieveFromBroker();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
