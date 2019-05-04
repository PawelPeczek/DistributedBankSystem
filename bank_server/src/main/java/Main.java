import services.BankSystem;

import java.io.File;

public class Main {

    public static void main(final String[] args){
        String bankName = Main.getBankNameFromArgs(args);
        BankSystem system = new BankSystem(bankName);
        system.start();
        system.awaitTermination();
    }

    private static String getBankNameFromArgs(String[] args){
        if(args.length < 1){
            throw new RuntimeException("Not enough argument passed.");
        }
        return args[0];
    }

}