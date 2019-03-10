package Application;

import controller.Seller2Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Seller2Main extends Application {

    private String sellerName = "Seller2";

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/seller2.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle(this.sellerName + " - Webshop");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        Seller2Controller controller = fxmlLoader.<Seller2Controller>getController();
        controller.loadMQRecieveFromBroker();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
