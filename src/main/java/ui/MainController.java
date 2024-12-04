package ui;

import bank.PrivateBank;
import bank.PrivateBankHolder;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.PrivateBankAlreadyExistsException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainController {
    @FXML
    public Button createAccountBttn;
    @FXML
    public Label addAccountInfo;
    @FXML
    private ListView<Label> accountList;
    @FXML
    private Button addAccountBttn;

    private Path p = Paths.get(".\\src\\main\\resources\\PrivateBanken\\Sparkasse");
    private PrivateBank bank;

    @FXML
    public void initialize() {
        try {
            if (p.toFile().exists()){
                bank = new PrivateBank("Sparkasse",0.1,0.1,true);
            }
            else bank = new PrivateBank("Sparkasse",0.1,0.1,false);
        } catch (PrivateBankAlreadyExistsException | IOException e) {throw new RuntimeException(e);}

        PrivateBankHolder privateBankHolder =  PrivateBankHolder.getInstance();
        privateBankHolder.setBank(bank);

        updateAccountList();
    }

    @FXML
    public void openAddAccount(ActionEvent event) {
        AddAccountController addAccountController = new AddAccountController();
        addAccountController.addAccountView(event, this);
    }

    public void updateAccountList() {
        bank = PrivateBankHolder.getInstance().getBank();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem editItem = new MenuItem("Account ansehen");

        deleteItem.setOnAction(e->{
            ConfirmDeleteController confirmDeleteController = new ConfirmDeleteController();
            Label label = accountList.getSelectionModel().getSelectedItem();
            confirmDeleteController.confirmDeleteView((Stage) accountList.getScene().getWindow(), this, label.getText());
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

    public void changeToAccountView(String accountName) {
        AccountController accountController = new AccountController();
        Stage root = (Stage) accountList.getScene().getWindow();
        accountController.openAccountView(accountName,root);
    }
}
