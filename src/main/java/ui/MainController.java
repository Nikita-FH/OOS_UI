package ui;

import bank.PrivateBank;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.PrivateBankAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainController {
    private Path p = Paths.get(".\\src\\main\\resources\\PrivateBanken\\Sparkasse");
    private PrivateBank bank;

    @FXML
    private ListView<String> accountList;

    public void initialize() {
        try {
            if (p.toFile().exists()){
                bank = new PrivateBank("Sparkasse",0.1,0.1,true);
            }
            else bank = new PrivateBank("Sparkasse",0.1,0.1,false);
        } catch (PrivateBankAlreadyExistsException | IOException e) {throw new RuntimeException(e);}

        try{
            bank.createAccount("Nikita");
            bank.createAccount("Tom");
        }
        catch (AccountAlreadyExistsException e) {}
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        bank.getAccountsToTransactions().forEach((k,v)->{
            accountList.getItems().add(k);
        });
    }
}
