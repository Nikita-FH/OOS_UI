package ui;

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

/**
 * View die eine Aktion bestätigt und diese dann ausführt
 */
public class ConfirmActionController{
    @FXML
    static Button YesBttn;
    @FXML
    static Button NoBttn;
    /**
     *
     */
    static Runnable myAction;

    /**
     * Erstellt View in der eine Aktion bestätigt werden soll
     * @param stage Der Besitzer dieser Aktion
     * @param OnCloseEvent EventHandler Event das ausgeführt werden soll, wenn die View schließt
     * @param myAction Runnable Funktion die bei bestätigung ausgeführt werden soll
     */
    public void confirmActionView(Stage stage, EventHandler<WindowEvent> OnCloseEvent, Runnable myAction)  {
        Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Confirm Action");
        dialog.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,OnCloseEvent);

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
        myAction.run();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    public void denyAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
