package ui;

import bank.PrivateBank;
import bank.exceptions.AccountAlreadyExistsException;
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

/**
 * Controller der sich um die AddAccountView kümmert
 * @author Nikita
 */
public class AddAccountController extends Controller{

    @FXML
    public TextField nameNewAccount;
    @FXML
    public Label addAccountInfo;

    /**
     * Die Bank in die wir den Account einfügen wollen
     */
    private PrivateBank bank;

    /**
     * Öffnet die AddAccountView
     * @param event ActionEvent das die hinzufügen Aktion angestoßen hat
     * @param mainController MainController Objekt zu dem die View gehören soll
     */
    @FXML
    public void addAccountView(ActionEvent event,MainController mainController) {
            Stage dialog = new Stage();

            dialog.initOwner(((Node)event.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Add Account");
            dialog.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
                mainController.updateAccountList();
            });
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(AddAccountController.class.getClassLoader().getResource("AddAccountView.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                dialog.setScene(new Scene(root));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dialog.show();
    }

    /**
     * Funktion die das Hinzufügen des Accounts realisiert
     * @param actionEvent ActionEvent das die Aktion angestoßen hat
     */
    @FXML
    public void createAccount(ActionEvent actionEvent) {
        bank = PrivateBankModel.getInstance().getBank();
        try {
            bank.createAccount(nameNewAccount.getText());
            addAccountInfo.setText("Account created");
        }
        catch (AccountAlreadyExistsException e) {
            addAccountInfo.setText("Account already exists");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
