import bank.PrivateBank;
import bank.exceptions.PrivateBankAlreadyExistsException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {

    public Main(){super();};

    public static void main(String[] args) {
        launch(args);
    }

    Path p = Paths.get(".\\src\\main\\resources\\PrivateBanken\\Sparkasse");
    PrivateBank bank;

    @Override
    public void init(){
        try {
            if (p.toFile().exists()){
                bank = new PrivateBank("Sparkasse",0.1,0.1,true);
            }
            else bank = new PrivateBank("Sparkasse",0.1,0.1,false);
        } catch (PrivateBankAlreadyExistsException | IOException e) {throw new RuntimeException(e);}
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
}
