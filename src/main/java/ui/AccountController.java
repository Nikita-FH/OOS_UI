package ui;

import bank.PrivateBank;
import bank.Transaction;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionDoesNotExistException;
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
import java.util.List;

/**
 * Controller Klasse für die AccountView
 * @author Nikita
 */
public class AccountController extends Controller{

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

    /**Liste aller Transactions Elemente die angezeigt werden sollen*/
    private List<Transaction> accountTransactions;
    /**Die Bank in der wir uns befinden*/
    private PrivateBank bank;
    /**Der Name des Accounts mit dem wir arbeiten*/
    private String name;
    /**
     * Die Sortierung die Momentan ausgewählt ist
     */
    private String currentSort = "Keine Sortierung";

    /**
     * initialisiere die View, hier die möglichkeiten zum Sortieren einfügen
     */
    public void initialize() {
        sortOptionMenu.getItems().clear();
        MenuItem noSort = new MenuItem("Keine Sortierung");
        noSort.setOnAction((ActionEvent event) -> sortNoSort());
        MenuItem auf = new MenuItem("Aufsteigend");
        auf.setOnAction((ActionEvent event) -> sortAuf());
        MenuItem ab = new MenuItem("Absteigend");
        ab.setOnAction((ActionEvent event) -> sortAb());
        MenuItem pos = new MenuItem("Positiv");
        pos.setOnAction((ActionEvent event) -> sortPos());
        MenuItem neg = new MenuItem("Negativ");
        neg.setOnAction((ActionEvent event) -> sortNeg());
        sortOptionMenu.getItems().addAll(noSort, auf, ab, pos,neg);
    }

    /**
     * keine Sortierung
     */
    private void sortNoSort() {
        updateTransactionList(name);
        currentSort = "Keine Sortierung";
    };

    /**
     * sortiere die Transaktionen des Accounts aufsteigend
     */
    private void sortAuf() {
        try {
            accountTransactions = bank.getTransactionsSorted(name,true);
        } catch (AccountDoesNotExistException e) {throw new RuntimeException(e);}
        currentSort = "Aufsteigend";
        insertViewList(accountTransactions);
    };

    /**
     * sortiere die Transaktionen des Accounts absteigend
     */
    private void sortAb(){
        try {
            accountTransactions = bank.getTransactionsSorted(name,false);
        } catch (AccountDoesNotExistException e) {throw new RuntimeException(e);}
        currentSort = "Absteigend";
        insertViewList(accountTransactions);
    };

    /**
     * sortiere die Transaktionen des Accounts nach Positiv
     */
    private void sortPos(){
        try {
            accountTransactions = bank.getTransactionsByType(name,true);
        } catch (AccountDoesNotExistException e) {throw new RuntimeException(e);}
        currentSort = "Positiv";
        insertViewList(accountTransactions);
    };

    /**
     * sortiere die Transaktionen des Accounts Negativ
     */
    private void sortNeg() {
        try {
            accountTransactions = bank.getTransactionsByType(name,false);
        } catch (AccountDoesNotExistException e) {throw new RuntimeException(e);}
        currentSort = "Negativ";
        insertViewList(accountTransactions);
    };

    /**
     * Füge die Transaktionsliste in die Anzeige ein in Reihenfolge der Liste
     * @param list List der Transaktionen
     */
    private void insertViewList(List<Transaction> list) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");

        //Die Aktionen die ausgeführt werden sollen, wenn Delete gedrückt wird
        deleteItem.setOnAction(e->{
            ConfirmActionController confirmActionController = new ConfirmActionController();
            Label label = transactionList.getSelectionModel().getSelectedItem();
            confirmActionController.confirmActionView((Stage) transactionList.getScene().getWindow(),
                    onCloseAction -> {
                        updateTransactionList(name);
                        updateKontostand();
                    },
                    //onConfirmAction
                    () -> {
                        try{
                            for (Transaction transaction : bank.getTransactions(name)) {
                                if (transaction.toString().equals(label.getText())) {
                                    bank.removeTransaction(name, transaction);
                                }
                            }
                        }
                        catch (AccountDoesNotExistException | TransactionDoesNotExistException | IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        updateTransactionList(name);
                        updateKontostand();
                    });
        });
        contextMenu.getItems().addAll(deleteItem);

        ObservableList<String> items = FXCollections.observableArrayList();
        list.forEach(transaction -> items.add(transaction.toString()));
        ObservableList<Label> itemLabels = FXCollections.observableArrayList();

        items.forEach(item -> {
                Label label = new Label(item);
                label.setContextMenu(contextMenu);
                itemLabels.add(label);
            }
        );

        transactionList.setItems(itemLabels);
    }

    /**
     * Pipline Funktion zum erhalten der notwendigen Daten vom MainView Controller
     * @param accountName
     */
    public void getMainData(String accountName) {
        bank = PrivateBankModel.getInstance().getBank();
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

    /**
     * Aktualisiere die Transaktionsliste
     * @param name String des Accounts zu dem die Transaktionen angezegit werden müssen
     */
    public void updateTransactionList(String name){
        bank = PrivateBankModel.getInstance().getBank();
        try {
            accountTransactions = bank.getTransactions(name);
        } catch (AccountDoesNotExistException e) {
            throw new RuntimeException(e);
        }

        insertViewList(accountTransactions);

        switch (currentSort) {
            case "Aufsteigend":
                sortAuf();
                break;
            case "Absteigend":
                sortAb();
                break;
            case "Positiv":
                sortPos();
                break;
            case "Negativ":
                sortNeg();
                break;
            default:
                insertViewList(accountTransactions);
        }
    }

    /**
     * Funktion zum wechseln in die MainView
     * @param actionEvent
     */
    @FXML
    public void changeToMainView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AccountController.class.getClassLoader().getResource("MainView.fxml"));
            Parent newScene = fxmlLoader.load();
            Stage root = (Stage) transactionList.getScene().getWindow();

            root.setTitle("MainView");
            root.setScene(new Scene(newScene));
            root.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funktion die die AddTransaktionView anlegt
     * @param actionEvent das Event das die Methode angestoßen hat
     */
    @FXML
    public void openAddTransaktion(ActionEvent actionEvent) {
        AddTransactionController addTransactionController = new AddTransactionController();
        addTransactionController.addAccountView(actionEvent, this,name);
    }

    /**
     * aktualisiert den Kontostand
     */
    @FXML
    public void updateKontostand() {
        try {
            kontostandLabel.setText(""+bank.getAccountBalance(name));
        } catch (AccountDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }
}


