import bank.*;
import bank.exceptions.PrivateBankAlreadyExistsException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.MainController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {

    public Main(){super();};

    MainController controller = new MainController();

    @Override
    public void init(){
        controller.initialize();
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("MainView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            primaryStage.setTitle("MainView");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}
