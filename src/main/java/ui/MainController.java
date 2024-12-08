package ui;

import bank.PrivateBank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController extends Controller{
    @FXML
    public Button createAccountBttn;
    @FXML
    public Label addAccountInfo;
    @FXML
    private ListView<Label> accountList;
    @FXML
    private Button addAccountBttn;
    /**
     * Die Bank in der wir die Accounts managen
     */
    private PrivateBank bank;

    /**
     * initialisiere die MainView
     */
    @FXML
    public void initialize() {
        updateAccountList();
    }

    /**
     * öffnet die AddAccountView
     * @param event ActionEvent das die Aktion angestoßen hat
     */
    @FXML
    public void openAddAccount(ActionEvent event) {
        AddAccountController addAccountController = new AddAccountController();
        addAccountController.addAccountView(event, this);
    }

    /**
     * Funktion die die Accounts aktualisiert
     */
    public void updateAccountList() {
        bank = PrivateBankModel.getInstance().getBank();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem editItem = new MenuItem("Account ansehen");

        //Fügt jedem Account eine delete Aktion hinzu
        deleteItem.setOnAction(e -> {
            ConfirmActionController confirmActionController = new ConfirmActionController();
            Label label = accountList.getSelectionModel().getSelectedItem();
            confirmActionController.confirmActionView((Stage) accountList.getScene().getWindow(),
                    onCloseAction -> {
                        updateAccountList();
                    },
                    //OnConfirmAction
                    () -> {
                        try {
                            bank.deleteAccount(label.getText());
                            updateAccountList();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    });
        });

        //Fügt jedem Account eine Editier funktion hinzu
        editItem.setOnAction(e -> {
            Label label = accountList.getSelectionModel().getSelectedItem();
            changeToAccountView(label.getText());
        });

        contextMenu.getItems().addAll(deleteItem, editItem);


        ObservableList<String> items = FXCollections.observableArrayList();
        bank.getAccountsToTransactions().forEach((k,v) -> items.add(k));
        ObservableList<Label> options = FXCollections.observableArrayList();

        for (String item : items) {
            Label label = new Label(item);
            label.setContextMenu(contextMenu);
            options.add(label);
        }

        accountList.setItems(options);
    }

    /**
     * Funktion die die AccountView öffnet zum bearbeiten des Accounts
     * @param accountName Name des Accounts den wir bearbeiten wollen
     */
    @FXML
    public void changeToAccountView(String accountName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AccountController.class.getClassLoader().getResource("AccountView.fxml"));
            Parent newScene = fxmlLoader.load();
            Stage root = (Stage) accountList.getScene().getWindow();

            root.setTitle(accountName);
            root.setScene(new Scene(newScene));
            root.show();
            AccountController controller = fxmlLoader.getController();
            controller.getMainData(accountName);
            controller.updateTransactionList(accountName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
