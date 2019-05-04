package core.client_inteface;

import Bank.*;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;
import com.zeroc.IceInternal.Ex;
import core.config.ClientConfig;
import core.helpers.AccountCreationHelper;
import core.helpers.CreditRequestHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ClientInterface {

    private static int INTERFACE_WIDTH = 120;

    private Communicator communicator;
    private String bank;
    private String loggedClientPESEL;
    private AccountManagerPrx accountManager;
    private BankAccountPrx standardAccount;
    private PremiumBankAccountPrx premiumAccount;
    private HashMap <String,String> context = new HashMap<>();

    public void start(){
        int choice = 0;
        do {
            this.printInitialMenu();
            choice = this.readOptionChoice();
            this.dispatchInitialChoice(choice);
        } while(choice != 0);
    }

    private void printInitialMenu() {
        this.printHeader();
        String[] options = {"EXIT", "CHOOSE BANK"};
        for(int i = 0; i < options.length; i++){
            System.out.println(i + ". " + options[i]);
        }
    }

    private int readOptionChoice() {
        System.out.println("Put choice: ");
        Scanner inputScanner = new Scanner(System.in);
        return inputScanner.nextInt();
    }

    private void dispatchInitialChoice(int choice) {
        switch (choice){
            case 0:
                this.exit(0);
                break;
            case 1:
                this.proceedBankChoosing();
                break;
        }
    }

    private void proceedBankChoosing() {
        int choice;
        do {
            this.printBankChoiceMenu();
            choice = this.readOptionChoice();
            this.dispatchBankChoice(choice);
        } while (choice < 0 || choice > 2);
    }

    private void dispatchBankChoice(int choice) {
        if(choice > 0 && choice < 3){
            String bankConfig = ClientConfig.getConfigForBank(choice);
            this.initializeCommunication(bankConfig);
            this.bank = "bank " + choice;
            this.startBankOperationLoop();
        } else if(choice == 0){
            this.cleanSession();
        }
    }

    private void startBankOperationLoop() {
        int choice;
        do {
            this.printInitialBankOperationMenu();
            choice = this.readOptionChoice();
            this.dispatchInitialBankOperation(choice);
        } while (choice < 1 || choice > 2);
    }

    private void printInitialBankOperationMenu() {
        this.printHeader();
        String[] options = {"CREATE ACCOUNT", "LOG-IN", "BACK"};
        for(int i = 0; i < options.length; i++){
            System.out.println(i + ". " + options[i]);
        }
    }

    private void dispatchInitialBankOperation(int choice) {
        if(choice == 0){
            this.proceedAccountCreating();
        } else if(choice == 1){
            this.proceedLogIn();
        }
    }

    private void proceedAccountCreating() {
        String choice;
        do {
            choice = "y";
            this.printHeader();
            System.out.println("Creating account");
            Scanner scanner = new Scanner(System.in);
            System.out.print("PESEL: ");
            String PESEL = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Income level: ");
            float incomeLevel = scanner.nextFloat();
            AccountCreationData accountCreationData = AccountCreationHelper.prepareAccountCreationData(PESEL, password, incomeLevel);
            try {
                this.accountManager.createAccount(accountCreationData);
            } catch (AccountCreationException e) {
                System.out.println("Error: " + e.reason);
            }
            choice = scanner.nextLine();
            System.out.println("DONE: [Y/n]");
            choice = scanner.nextLine();
        } while (!choice.toLowerCase().equals("y"));

    }

    private void proceedLogIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("PESEL: ");
        String PESEL = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        try {
            LoginResponse response = this.accountManager.login(PESEL, password);
            this.context.put("token", response.secretToken);
            this.standardAccount = response.account;
            if(response.accountType == AccountType.PREMIUM){
                this.premiumAccount = PremiumBankAccountPrx.checkedCast(response.account);
            }
            this.loggedClientPESEL = PESEL;
            this.proceedLoggedUserOperations();
        } catch (AuthenticationException e) {
            System.out.println("Error: " + e.reason);
        }

    }

    private void proceedLoggedUserOperations() {
        int choice;
        do {
            this.printLoggedOperations();
            choice = this.readOptionChoice();
            this.dispatchLoggedUserOperation(choice);
        } while (choice != 0);
        this.logout();
    }

    private void printLoggedOperations() {
        this.printHeader();
        String[] options = {"LOGOUT", "GET STATE", "PUT MONEY", "WITHDRAW MONEY", null};
        if(this.premiumAccount != null){
            options[options.length - 1] = "GET CREDIT";
        }
        for(int i = 0; i < options.length; i++){
            if(options[i] == null){
                continue;
            }
            System.out.println(i + ". " + options[i]);
        }
    }


    private void dispatchLoggedUserOperation(int choice) {
        switch (choice){
            case 1:
                this.proceedGettingState();
                break;
            case 2:
                this.proceedMoneyPut();
                break;
            case 3:
                this.proceedMoneyWithdraw();
        }
        if(choice == 4 && this.premiumAccount != null){
            this.proceedCreditRequest();
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("PRESS [RETURN] to continue.");
        scanner.nextLine();
    }

    private void proceedGettingState() {
        this.printHeader();
        try {
            float state = this.standardAccount.getState(this.context);
            System.out.println("Account state: " + state);
        } catch (AuthorizationException e) {
            System.out.println(e.reason);
        }
    }

    private void proceedMoneyPut() {
        Scanner scanner = new Scanner(System.in);
        this.printHeader();
        System.out.print("Value to put: ");
        float value = scanner.nextFloat();
        try {
            float state = this.standardAccount.putMoney(value, this.context);
            System.out.println("State: " + state);
        } catch (AuthorizationException e) {
            System.out.println(e.reason);
        } catch (OperationException e) {
            System.out.println(e.reason);
        }
    }

    private void proceedMoneyWithdraw() {
        Scanner scanner = new Scanner(System.in);
        this.printHeader();
        System.out.print("Value to withdraw: ");
        float value = scanner.nextFloat();
        try {
            float state = this.standardAccount.withdrawMoney(value, this.context);
            System.out.println("State: " + state);
        } catch (AuthorizationException e) {
            System.out.println(e.reason);
        } catch (OperationException e) {
            System.out.println(e.reason);
        }
    }

    private void proceedCreditRequest() {
        Scanner scanner = new Scanner(System.in);
        this.printHeader();
        System.out.println("Choose currency:");
        int idx = 0;
        for (Currency currency : Currency.values()){
            System.out.println(idx + ". " + currency);
            idx += 1;
        }
        int currencyChoice = this.readOptionChoice();
        if(currencyChoice < 0 || currencyChoice >= Currency.values().length){
            System.out.println("Bad choice.");
            return;
        }
        System.out.println("Put value: ");
        float value = scanner.nextFloat();
        System.out.println("Put length in months: ");
        int lengthInMonths = scanner.nextInt();
        CreditRequest request = CreditRequestHelper.prepareCreditRequest(value, Currency.valueOf(currencyChoice), lengthInMonths);
        try {
            CreditOffer creditOffer = this.premiumAccount.requestCredit(request, this.context);
            System.out.println("COST IN PLN: " + creditOffer.costInPLN);
            try {
                System.out.println("COST IN " + creditOffer.getForeignCurrency() + ": " + creditOffer.getCostInForeignCurrency());
            } catch (java.util.NoSuchElementException ignored){}
        } catch (AuthorizationException e) {
            System.out.println("Error: " + e.reason);
        } catch (OperationException e) {
            System.out.println("Error: " + e.reason);
        }
    }


    private void initializeCommunication(String bankConfig) {
        this.communicator = Util.initialize(null, bankConfig);
        ObjectPrx baseProxy = communicator.propertyToProxy("LoginManager.Proxy");
        this.accountManager = AccountManagerPrx.checkedCast(baseProxy);
        if (this.accountManager == null){
            this.exit(13);
        }
    }

    private void printBankChoiceMenu() {
        this.printHeader();
        String[] options = {"BACK", "BANK 1", "BANK 2"};
        for(int i = 0; i < options.length; i++){
            System.out.println(i + ". " + options[i]);
        }
    }

    private void exit(int status) {
        cleanSession();
        System.exit(status);
    }

    private void logout(){
        if(this.standardAccount != null){
            this.accountManager.logout(this.context);
        }
        this.standardAccount = null;
        this.premiumAccount = null;
        this.loggedClientPESEL = null;
        this.context = new HashMap<>();
    }

    private void cleanSession(){
        this.logout();
        if(this.communicator != null){
            this.communicator.shutdown();
        }
        this.communicator = null;
        this.bank = null;
        this.accountManager = null;
    }

    private void printHeader() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for (int i = 0; i < ClientInterface.INTERFACE_WIDTH; i++){
            System.out.print('*');
        }
        System.out.println();
        System.out.println(this.generateHeaderContent());
        for (int i = 0; i < ClientInterface.INTERFACE_WIDTH; i++){
            System.out.print('*');
        }
        System.out.println();
    }

    private String generateHeaderContent(){
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append("BANK: ");
        if(this.bank != null){
            headerBuilder.append(this.bank);
        } else {
            headerBuilder.append("(not selected)");
        }
        headerBuilder.append(" | ");
        headerBuilder.append("CLIENT: ");
        if(this.loggedClientPESEL != null){
            headerBuilder.append(this.loggedClientPESEL);
        } else {
            headerBuilder.append("(not logged)");
        }
        headerBuilder.append(" | ");
        headerBuilder.append("CLIENT STATUS: ");
        if(this.loggedClientPESEL != null){
            if(this.premiumAccount != null){
                headerBuilder.append("premium");
            } else {
                headerBuilder.append("standard");
            }
        } else {
            headerBuilder.append("N/A");
        }
        int spacesLeft = ClientInterface.INTERFACE_WIDTH - headerBuilder.length() - 2;
        if(spacesLeft > 0) {
            int leftSidedSpaces = spacesLeft / 2;
            int rightSidedSpaces = spacesLeft - leftSidedSpaces;
            for(int i = 0; i < rightSidedSpaces; i++){
                headerBuilder.append(" ");
            }
            headerBuilder.append("*");
            StringBuilder leftSideBuilder = new StringBuilder();
            leftSideBuilder.append("*");
            for(int i = 0; i < leftSidedSpaces; i++){
                leftSideBuilder.append(" ");
            }
            leftSideBuilder.append(headerBuilder);
            return leftSideBuilder.toString();
        } else {
            return "* " + headerBuilder.toString() + " *";
        }
    }


}
