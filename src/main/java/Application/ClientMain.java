package Application;

import controller.ClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Client - Webshop");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        ClientController controller = fxmlLoader.<ClientController>getController();
        controller.getProducts();
        controller.loadMQRecieveFromBroker();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
