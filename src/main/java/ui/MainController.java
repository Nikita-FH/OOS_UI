package ui;

import bank.PrivateBank;
import bank.PrivateBankModel;
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

    private PrivateBank bank;

    @FXML
    public void initialize() {
        updateAccountList();
    }

    @FXML
    public void openAddAccount(ActionEvent event) {
        AddAccountController addAccountController = new AddAccountController();
        addAccountController.addAccountView(event, this);
    }

    public void updateAccountList() {
        bank = PrivateBankModel.getInstance().getBank();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem editItem = new MenuItem("Account ansehen");

        deleteItem.setOnAction(e -> {
            ConfirmActionController confirmActionController = new ConfirmActionController();
            Label label = accountList.getSelectionModel().getSelectedItem();
            confirmActionController.confirmActionView((Stage) accountList.getScene().getWindow(), this,
            onCloseAction -> {
                updateAccountList();
            },
            onConfirmAction -> {
                try {
                    bank.deleteAccount(label.getText());
                    updateAccountList();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return null;
            });

        });

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
