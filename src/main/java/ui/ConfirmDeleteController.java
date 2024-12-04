package ui;

import bank.PrivateBank;
import bank.PrivateBankHolder;
import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ConfirmDeleteController {
    @FXML
    public Button deleteYesBttn;
    @FXML
    public Button delteNoBttn;

    private static String accountName;
    private static MainController mainController;
    private PrivateBank bank;

    public void confirmDeleteView(Stage stage, MainController mainController, String accountName) {
        Stage dialog = new Stage();
        ConfirmDeleteController.accountName = accountName;
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Confirm Delete");
        dialog.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
            mainController.updateAccountList();
        });
        ConfirmDeleteController.mainController = mainController;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AddAccountController.class.getClassLoader().getResource("ConfirmDeleteView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            dialog.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dialog.show();
    }

    @FXML
    public void confirmDelte(ActionEvent actionEvent){
        bank = PrivateBankHolder.getInstance().getBank();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        try {
            bank.deleteAccount(accountName);
            mainController.updateAccountList();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        stage.close();

    }
    @FXML
    public void dontDelte(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
