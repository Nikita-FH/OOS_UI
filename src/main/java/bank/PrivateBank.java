package bank;

import bank.exceptions.*;
import bank.utils.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Eine Klasse die eine Bank mit ihren Funktionen realisiert
 * @author Nikita
 * @implements {@link Bank}
 * */
public class PrivateBank implements Bank {
    /**Der Pfad zum Order indem alle Banken liegen*/
    private static final Path directoryRoot = Paths.get(".\\src\\main\\resources\\PrivateBanken\\");
    /**Name der Bank*/
    private String name;
    /**Ordner Name im Dateisystem*/
    private Path directory;
    /**Zinsen fuer Einzahlungen zwischen 0 und 1 bzw. 0% und 100%*/
    private double incomingInterest;
    /**Zinsen fuer Auszahlungen zwischen 0 und 1 bzw. 0% und 100% */
    private double outgoingInterest;
    /**Eine Abildung aller Nutzer der Bank und deren Transaktionen*/
    private Map<String, List<Transaction>> accountsToTransactions = new HashMap<String, List<Transaction>>();


    /**
     * gibt den{@link PrivateBank#directory} der Ban zurueck
     * @return String Name des Ordners
     */
    public String getDirectory() {
        return directory.toString();
    }

    /**
     * setzt das {@link PrivateBank#directory} in dem die Bank liegt, Name des directory ist {@link PrivateBank#name} der Bank
     * @throws PrivateBankAlreadyExistsException wenn die Bank mit dem Namen schon existiert, da Name = Directory Pfad
     */
    private void setDirectory() throws PrivateBankAlreadyExistsException {
        Path newPath = Paths.get(directoryRoot + "\\"+ name);

        if(new File(newPath.toString()).exists()) {
            throw new PrivateBankAlreadyExistsException();
        }
        this.directory = newPath;
        this.directory.toFile().mkdirs();
        this.accountsToTransactions.forEach((k,v)->{

            try {
                this.writeAccount(k);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * kopiert die Transacitions aus dem Pfad in dem die Bank liegen sollte
     * @throws IOException wenn es Fehler beim lesen der Accounts gibt
     */
    private void copyDirectory() throws IOException {
        this.directory = Paths.get(directoryRoot + "\\"+ name);
        this.accountsToTransactions = new HashMap<String,List<Transaction>>();
        this.readAccounts();
    }

    /**
     * gibt den {@link PrivateBank#name} zurueck
     * @return String Name der Bank*/
    public String getName() {
        return this.name;
    }

    /**
     * gibt {@link PrivateBank#incomingInterest} der Bank zurueck
     * @return Double Zinsatz der Eingehnden Zahlungen
     */
    public double getIncomingInterest() {
        return this.incomingInterest;
    }

    /**
     * gibt {@link PrivateBank#outgoingInterest} der Bank zurueck
     * @return Double Zinssatz der Ausgehenden Zahlungen
     */
    public double getOutgoingInterest() {
        return this.outgoingInterest;
    }

    /**
     * setzt {@link PrivateBank#name} string der Bank, da der Name der Bank auch der Name des Ordners ist, wird der Ordner auch umbenannt
     * @param name Namen den die Bank annehmen soll
     * @throws PrivateBankAlreadyExistsException Wenn der Name der Bank schon existiert
     */
    public void setName(String name) throws PrivateBankAlreadyExistsException {
        this.name = name;
        this.directory = Paths.get(directoryRoot + "\\"+ name);
        this.setDirectory();
    }

    /**
     * setzt {@link PrivateBank#incomingInterest} der Bank
     * @param incomingInterest double Zinssatz fuer eingehende Zahlungen
     * @throws TransactionAttributeException wenn Interest nicht zwischen 0-1
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException{
        if (incomingInterest >= 0 && incomingInterest <= 1)
            this.incomingInterest = incomingInterest;
        else
            throw new TransactionAttributeException("Interest nicht 0 <= X <=1");
    }

    /**
     * setzt {@link PrivateBank#setOutgoingInterest} der Bank
     * @param outgoingInterest double Zinssatz fuer ausgehende Zahlungen
     * @throws TransactionAttributeException wenn Interest nicht zwischen 0-1
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException{
        if (outgoingInterest >= 0 && outgoingInterest <= 1)
            this.outgoingInterest = outgoingInterest;
        else
            throw new TransactionAttributeException("Interest nicht 0 <= X <=1");
    }

    /**
     * Konstruktor fuer {@link PrivateBank}
     * @param name String name der Bank
     * @param incomingInterest double Zinssatz fuer einkommende Zahlungen
     * @param outgoingInterest double Zinssatz fuer ausgehende Zahlungen
     * @param load boolean true wenn PrivateBank erstellt werden soll, true wenn PrivateBank geladen werden soll
     * @throws PrivateBankAlreadyExistsException wenn man nicht eine Bank läd, und der Name schon gegeben ist
     * @throws IOException wenn es ein Fehler beim lesen oder schreiben gab
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest, boolean load) throws PrivateBankAlreadyExistsException, IOException {
        this.name = name;
        this.setIncomingInterest(incomingInterest);
        this.setOutgoingInterest(outgoingInterest);
        if(!load) this.setDirectory();
        else this.copyDirectory();
    }

    /**
     * Copy Konstruktor fuer {@link PrivateBank}, {@link PrivateBank#accountsToTransactions} werden nicht kopiert. Kopierte Bank trägt den Namen der Original Bank + "-Copy"
     * @param bank Die Daten der Bank die kopiert werden sollen
     * @throws IOException wenn es ein Fehler beim lesen oder schreiben gab
     */
    public PrivateBank(PrivateBank bank) throws IOException {
        this.incomingInterest = bank.getIncomingInterest();
        this.outgoingInterest = bank.getOutgoingInterest();
        this.accountsToTransactions = new HashMap<String, List<Transaction>>();
        this.name = bank.getName();
        this.copyDirectory();
        boolean made = false;
        String copy = "-Copy";
        while (!made) {
            try {
                this.setName(bank.getName() + copy);
                made = true;
            } catch (PrivateBankAlreadyExistsException e) {
                copy += copy;
            }
        }

        if (!directory.toFile().exists())
           this.directory.toFile().mkdirs();
        this.accountsToTransactions.forEach(
                (k,v) ->{
                    try {
                        this.writeAccount(k);
                    } catch (IOException e) {
                        //Darf nicht passieren
                        throw new RuntimeException(e);
                    }
                }
        );

    }

    /**
     *
     * @return {@link PrivateBank} Klasse als String in Format:
     * <p>Bank: String</p>
     * <p>Zinssatz-Ein: double</p>
     * <p>Zinssatz-Aus: double</p>
     * <p>Transaktionen: List<{@link Transaction#toString()}></p>
     */
    @Override
    public String toString() {
        final String[] zwischen = {"","","","",""};


        zwischen[0] += "Bank: " + this.name + "\n";
        zwischen[1] += "Incoming Interest: " + this.incomingInterest + "\n";
        zwischen[2] += "Outgoing Interest: " + this.outgoingInterest + "\n";
        zwischen[3] += "Accounts: ";
        this.accountsToTransactions.forEach( (k,v) -> {
            zwischen[4] += k + ": " + v + "\n";
        });
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            res.append(zwischen[i]);
        }

        return res.toString();
    }

    /**
     * Ueberprueft ob die Objekte der Klasse {@link PrivateBank} auf alle Attribute gleich sind, BIS AUF {@link PrivateBank#name}
     * @param t Object das auf gleichheit zu this ueberprueft werden soll
     * @return boolean true wenn gleich, false wenn falsch
     */
    @Override
    public boolean equals(Object t){
        if(!(t instanceof PrivateBank)) return false;
        if(!(this.incomingInterest == ((PrivateBank)t).getIncomingInterest())) return false;
        if(!(this.outgoingInterest == ((PrivateBank)t).getOutgoingInterest())) return false;
        if(!this.accountsToTransactions.equals(((PrivateBank)t).accountsToTransactions)) return false;

        return true;
    }

    /**
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     * @throws IOException wenn es beim lesen oder schreiben ein Problem gab
     */
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException {
        if (this.accountsToTransactions.containsKey(account)) throw new AccountAlreadyExistsException(account);

        accountsToTransactions.put(account, new ArrayList<>());
        this.writeAccount(account);
    }

    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     * @throws IOException wenn es Problem beim lesen oder schreiben gab
     */
    public void createAccount(String account, List<? extends Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAttributeException, IOException {
            this.createAccount(account);

            if(transactions == null) throw new TransactionAttributeException(null);

        try {
            for(Transaction t : transactions){
                if (t == null) throw new TransactionAttributeException(null);
                this.addTransaction(account,t);
            }
        } catch (AccountDoesNotExistException e) {
            //Darf nicht passieren
            throw new RuntimeException(e);
        }
        this.writeAccount(account);
    }

    /**
     * Adds a transaction to an already existing account.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     * @throws IOException wenn es Probleme beim lesen oder schreiben der Transaktion gab
     */
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {

            if (!(this.accountsToTransactions.containsKey(account))) throw new AccountDoesNotExistException(account);
            if (transaction == null) throw new TransactionAttributeException("null");
            this.accountsToTransactions.forEach( (k,v) -> {
                if(k.equals(account)){
                    v.forEach( (j)->{
                        if(j.equals(transaction)) throw new TransactionAlreadyExistException(transaction.toString());
                    });
                }
            });
            if (transaction instanceof Payment){
                ((Payment) transaction).setIncomingInterest(this.incomingInterest);
                ((Payment) transaction).setOutgoingInterest(this.outgoingInterest);
            }
            this.accountsToTransactions.get(account).add(transaction);
            this.writeAccount(account);
   }

    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     * @throws IOException Wenn es probleme beim entfernen einer Transaktion in der Json gab
     */
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {

        if (!(this.accountsToTransactions.containsKey(account))) throw new AccountDoesNotExistException(account);
        List<Transaction> l = accountsToTransactions.get(account);

        if(!l.contains(transaction)) throw new TransactionDoesNotExistException(transaction.toString());
        l.remove(transaction);
        this.writeAccount(account);
    }

    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     * @throws AccountDoesNotExistException Wenn es den Account nicht gibt
     */
    public boolean containsTransaction(String account, Transaction transaction) throws AccountDoesNotExistException{
         if(!(this.accountsToTransactions.containsKey(account))) throw new AccountDoesNotExistException(account);

         List<Transaction> l = accountsToTransactions.get(account);
         return l.contains(transaction);
    }

    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     * @throws AccountDoesNotExistException wenn es den Account nicht gibt
     */
    public double getAccountBalance(String account) throws AccountDoesNotExistException{
        double balance  = 0;
        if(!(this.accountsToTransactions.containsKey(account))) throw new AccountDoesNotExistException(account);

        List<Transaction> l = accountsToTransactions.get(account);

        for(Transaction t : l){
            balance += t.calculateBill();
        }

        return balance;
    }

    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of all transactions for the specified account
     * @throws AccountDoesNotExistException wenn es den Account nicht gibt
     */
    public List<Transaction> getTransactions(String account) throws AccountDoesNotExistException{
        if(!(this.accountsToTransactions.containsKey(account))) throw new AccountDoesNotExistException(account);

        return accountsToTransactions.get(account);
    }

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return the sorted list of all transactions for the specified account
     * @throws AccountDoesNotExistException wenn es den Account nicht gibt
     */
    public List<Transaction> getTransactionsSorted(String account, boolean asc) throws AccountDoesNotExistException{
        if(!(this.accountsToTransactions.containsKey(account))) throw new AccountDoesNotExistException(account);

        List<Transaction> l = new ArrayList<Transaction>(accountsToTransactions.get(account));
        l.sort(Transaction::compareTo);

        if(asc) Collections.reverse(l);

        return l;
    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of all transactions by type
     * @throws AccountDoesNotExistException wenn es den Account nicht gibt
     */
    public List<Transaction> getTransactionsByType(String account, boolean positive) throws AccountDoesNotExistException{
        if(!(this.accountsToTransactions.containsKey(account))) throw new AccountDoesNotExistException(account);

        List<Transaction> l = new ArrayList<Transaction>(accountsToTransactions.get(account));

        l.removeIf((var)->{
            double res = var.calculateBill();

            if(positive) {
                return !(res > 0);
            }
            else{
                return !(res < 0);
            }
        });

        return l;
    }

    /**
     * Schreibt die Transaktion eines Account in die dazugehörige Json Datei einer Bank
     * @param account der Account der geschrieben werden soll
     * @throws IOException Wenn es Probleme bei oder schreiben des Accounts gab
     */

    private void writeAccount(String account) throws  IOException {
        List<Transaction> transactionList = null;
        try {
            transactionList = this.getTransactions(account);
        }
        catch (AccountDoesNotExistException e) {
            //Wir bekommen einen Account zum schreiben, wenn der Fehler auftritt gab es nen massiven Fehler
            throw new RuntimeException();
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonSerializer<Transaction> serializer = new TransactionSerializer();
        gsonBuilder.registerTypeAdapter(Transaction.class,serializer);

        Gson gson = gsonBuilder.setPrettyPrinting().create();
        JsonArray transactionListJson = new JsonArray(transactionList.size());
        for(Transaction transaction : transactionList){
            transactionListJson.add(gson.toJsonTree(transaction, Transaction.class));
        }

        File f = new File(this.directory +"\\" + account + ".json");

        f.createNewFile();

        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(gson.toJson(transactionListJson));
        bw.close();

    }

    /**
     * Liest alle Accounts aus der Bank mit deren dazugehörigen Transaktionen
     * @throws IOException wenn es Problem beim lesen oder schreiben gab
     */
    private void readAccounts() throws IOException {
        File[] files = this.directory.toFile().listFiles();

        GsonBuilder gsonBuilder = new GsonBuilder();


        Gson gson = gsonBuilder.create();

        JsonArray transactionListJson = new JsonArray();

        if(files == null) return;
        for(File f : files) {
            FileReader fr = new FileReader(f);
            transactionListJson = gson.fromJson(new JsonReader(fr), JsonArray.class);
            String account = f.getName().replace(".json", "");
            try{
                this.createAccount(account);
            }
            catch (AccountAlreadyExistsException e){
                //Kopieren einer Bank, nicht weiter Schlimm, da wir ja Transactions einem Account zuweisen
            }
            for (JsonElement jsonElement : transactionListJson) {
                try {
                    switch (jsonElement.getAsJsonObject().get("CLASSNAME").getAsString()) {
                        case "IncomingTransfer" -> {
                            JsonDeserializer<IncomingTransfer> deserializer = new IncomingTransferDeserializer();
                            gsonBuilder = new GsonBuilder().registerTypeAdapter(IncomingTransfer.class, deserializer);
                            gson = gsonBuilder.create();

                            IncomingTransfer transaction = gson.fromJson(jsonElement.getAsJsonObject().get("INSTANCE"), IncomingTransfer.class);

                            this.addTransaction(account, transaction);
                        }
                        case "OutgoingTransfer" -> {
                            JsonDeserializer<OutgoingTransfer> deserializer = new OutgoingTransferDeserializer();
                            gsonBuilder = new GsonBuilder().registerTypeAdapter(OutgoingTransfer.class, deserializer);
                            gson = gsonBuilder.create();

                            bank.OutgoingTransfer transaction = gson.fromJson(jsonElement.getAsJsonObject().get("INSTANCE"), OutgoingTransfer.class);
                            this.addTransaction(account, transaction);
                        }
                        case "Payment" -> {
                            JsonDeserializer<Payment> deserializer = new PaymentDeserializer();
                            gsonBuilder = new GsonBuilder().registerTypeAdapter(Payment.class, deserializer);
                            gson = gsonBuilder.create();

                            Payment transaction = gson.fromJson(jsonElement.getAsJsonObject().get("INSTANCE"), Payment.class);
                            this.addTransaction(account, transaction);
                        }
                    }
                }
                catch (AccountDoesNotExistException e){
                    //Dies hätte nicht passieren dürfen
                    throw new RuntimeException();
                }

            }
            fr.close();
        }
    }
}

