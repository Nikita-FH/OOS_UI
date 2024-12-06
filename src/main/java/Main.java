import bank.*;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.PrivateBankAlreadyExistsException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

public class Main extends Application {
    private Path p = Paths.get(".\\src\\main\\resources\\PrivateBanken\\Sparkasse");
    private PrivateBank bank;

    public Main(){super();};

    @Override
    public void init(){
        try {
            if (p.toFile().exists()){
                bank = new PrivateBank("Sparkasse",0.1,0.1,true);
            }
            else bank = new PrivateBank("Sparkasse",0.1,0.1,false);
        } catch (PrivateBankAlreadyExistsException | IOException e) {throw new RuntimeException(e);}

        try{
            bank.createAccount("Tom");
            Payment p1 = new Payment(Calendar.getInstance().getTime().toString(),1,"p1",0.1,0.1);
            IncomingTransfer it = new IncomingTransfer(Calendar.getInstance().getTime().toString(),10,"it","Nikita","Tom");
            OutgoingTransfer ot = new OutgoingTransfer(Calendar.getInstance().getTime().toString(),5,"ot","Tom","Nikita");
            bank.addTransaction("Tom",p1);
            bank.addTransaction("Tom",it);
            bank.addTransaction("Tom",ot);
        } catch (AccountAlreadyExistsException e) {
        } catch (IOException e) {
            throw new RuntimeException();
        } catch (AccountDoesNotExistException e) {
            throw new RuntimeException(e);
        }

        try{
            bank.createAccount("Nikita");
            Payment p1 = new Payment(Calendar.getInstance().getTime().toString(),100,"p1",0.1,0.1);
            OutgoingTransfer ot = new OutgoingTransfer(Calendar.getInstance().getTime().toString(),10,"it","Nikita","Tom");
            IncomingTransfer it = new IncomingTransfer(Calendar.getInstance().getTime().toString(),5,"ot","Tom","Nikita");
            bank.addTransaction("Nikita",p1);
            bank.addTransaction("Nikita",it);
            bank.addTransaction("Nikita",ot);
        }
        catch (AccountAlreadyExistsException e) {
        } catch (IOException e) {
            throw new RuntimeException();
        } catch (AccountDoesNotExistException e) {
            throw new RuntimeException(e);
        }

        PrivateBankModel privateBankHolder = PrivateBankModel.getInstance();
        privateBankHolder.setBank(bank);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("MainView.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            primaryStage.setTitle("MainView");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}
