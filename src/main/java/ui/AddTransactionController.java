package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class AddTransactionController {
    @FXML
    public TextField paymentMengeField;
    @FXML
    public TextField paymentDescField;
    @FXML
    public Label paymentInfoLabel;
    @FXML
    public TextField transferMengeField;
    @FXML
    public TextField transferDescField;
    @FXML
    public TextField transferRecipientField;
    @FXML
    public Label transferInfoLabel;

    @FXML
    public void addAccountView(ActionEvent event, AccountController accountController, String name) {
        Stage dialog = new Stage();

        dialog.initOwner(((Node)event.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Transaction");
        dialog.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
            accountController.updateTransactionList(name);
        });
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AddAccountController.class.getClassLoader().getResource("AddTransactionView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            dialog.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dialog.show();
    }

    public void createPayment(ActionEvent actionEvent) {
    }

    public void createTransaction(ActionEvent actionEvent) {
    }
}
