package ui;

import bank.PrivateBank;
import bank.PrivateBankHolder;
import bank.Transaction;
import bank.exceptions.AccountDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountController {

    @FXML
    public Label accountLabel;
    @FXML
    public Label kontostandLabel;
    @FXML
    public ContextMenu deleteContext;
    @FXML
    public MenuButton sortOptionMenu;
    @FXML
    public ListView<Label> transactionList;

    private List<Transaction> accountTransactions;
    private PrivateBank bank;
    private String name;

    public void getMainData(String accountName) {
        bank = PrivateBankHolder.getInstance().getBank();
        try {
            accountTransactions = bank.getTransactions(accountName);
        } catch (AccountDoesNotExistException e) {
            throw new RuntimeException(e);
        }
        name = accountName;

        accountLabel.setText(name);
        try {
            kontostandLabel.setText(""+bank.getAccountBalance(name));
        } catch (AccountDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTransactionList(String name){
        bank = PrivateBankHolder.getInstance().getBank();
        try {
            accountTransactions = bank.getTransactions(name);
        } catch (AccountDoesNotExistException e) {
            throw new RuntimeException(e);
        }

        ObservableList<String> items = FXCollections.observableArrayList();
        accountTransactions.forEach(transaction -> items.add(transaction.toString()));
        ObservableList<Label> itemLabels = FXCollections.observableArrayList();

        items.forEach(item -> itemLabels.add(new Label(item)));

        transactionList.setItems(itemLabels);
    }

    @FXML
    public void changeToMainView(ActionEvent actionEvent) {
    }
    @FXML
    public void openAddTransaktion(ActionEvent actionEvent) {
    }
}
