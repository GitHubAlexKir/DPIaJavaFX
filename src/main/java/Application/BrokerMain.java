package Application;

import controller.BrokerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class BrokerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/broker.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Broker - Webshop");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        BrokerController controller = fxmlLoader.<BrokerController>getController();
        controller.loadMQRecieveFromClient();
        controller.loadMQRecieveFromItemService();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
