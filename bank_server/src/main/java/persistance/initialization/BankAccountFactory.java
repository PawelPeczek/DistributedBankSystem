package persistance.initialization;

import currency.client.CurrencyServiceClient;
import persistance.model.BankClient;
import services.operations.BankAccount;
import services.operations.PremiumBankAccount;

public class BankAccountFactory {

    public static BankAccount initalizeBankAccount(BankClient client){
        return new BankAccount(client);
    }

    public static PremiumBankAccount initializePremiumBankAccount(BankClient client, CurrencyServiceClient currencyClient){
        return new PremiumBankAccount(client, currencyClient);
    }
}
