package ui;

import bank.PrivateBank;
import bank.PrivateBankModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.function.Function;

public class ConfirmActionController{
    @FXML
    public Button YesBttn;
    @FXML
    public Button NoBttn;
    static Controller controller;
    static Function myAction;


    public void confirmActionView(Stage stage, Controller controller , EventHandler<WindowEvent> OnCloseEvent, Function myAction)  {
        Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Confirm Action");
        dialog.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,OnCloseEvent);

        ConfirmActionController.controller = controller;
        ConfirmActionController.myAction = myAction;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ConfirmActionController.class.getClassLoader().getResource("ConfirmActionView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            dialog.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dialog.show();
    }

    @FXML
    public void confirmAction(ActionEvent actionEvent){
        myAction.apply(null);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    public void denyAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
