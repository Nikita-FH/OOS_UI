package ui;

import bank.*;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionAttributeException;
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
import java.util.Calendar;

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
    private static String  name;
    private static AccountController accountController;

    @FXML
    public void addAccountView(ActionEvent event, AccountController accountController, String name) {
        AddTransactionController.name = name;
        AddTransactionController.accountController = accountController;

        Stage dialog = new Stage();

        dialog.initOwner(((Node)event.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Transaction");
        dialog.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
            accountController.updateKontostand();
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
        PrivateBank bank = PrivateBankModel.getInstance().getBank();
        Payment payment;
        try{
            payment = new Payment(Calendar.getInstance().getTime().toString(),Double.parseDouble(paymentMengeField.getText()), paymentDescField.getText());
            bank.addTransaction(name, payment);
            paymentInfoLabel.setText("Payment added successfully");

        } catch (TransactionAlreadyExistException e){
            paymentInfoLabel.setText("Die Transaktion ist schon vorhanden");
        }
        catch (TransactionAttributeException e){
            paymentInfoLabel.setText("Fehler beim erstellen der Transaktion");
        }
        catch (AccountDoesNotExistException e){
            paymentInfoLabel.setText(name + "Account existiert nicht");
        }
        catch (IOException e){
            paymentInfoLabel.setText("Fehler beim speichern der Transaktion");
        }
        catch(NumberFormatException e){
            paymentInfoLabel.setText("Menge ist kein gültiger Wert");
        }
        accountController.updateTransactionList(name);
    }

    public void createTransaction(ActionEvent actionEvent) {
        PrivateBank bank = PrivateBankModel.getInstance().getBank();
        OutgoingTransfer outgoingTransfer;
        IncomingTransfer incomingTransfer;
        try{
            outgoingTransfer = new OutgoingTransfer(Calendar.getInstance().getTime().toString(),Double.parseDouble(transferMengeField.getText()), transferDescField.getText(),name, transferRecipientField.getText());
            incomingTransfer = new IncomingTransfer(Calendar.getInstance().getTime().toString(),Double.parseDouble(transferMengeField.getText()), transferDescField.getText(),transferRecipientField.getText(),name);
            bank.addTransaction(transferRecipientField.getText(), incomingTransfer);
            bank.addTransaction(name, outgoingTransfer);
        }
        catch (TransactionAlreadyExistException e){
            transferInfoLabel.setText("Die Transaktion ist schon vorhanden");
        }
        catch (TransactionAttributeException e){
            transferInfoLabel.setText("Fehler beim erstellen der Transaktion");
        }
        catch (AccountDoesNotExistException e){
            transferInfoLabel.setText(transferInfoLabel.getText() + " Account existiert nicht");
        }
        catch (IOException e){
            transferInfoLabel.setText("Fehler beim speichern der Transaktion");
        }
        catch(NumberFormatException e){
            transferInfoLabel.setText("Menge ist kein gültiger Wert");
        }
        accountController.updateTransactionList(name);
    }
}
