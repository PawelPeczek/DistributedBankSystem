package core.helpers;

import Bank.AccountCreationData;

public class AccountCreationHelper {

    public static AccountCreationData prepareAccountCreationData(String PESEL, String password, float incomeLevel){
        AccountCreationData creationData = new AccountCreationData();
        creationData.PESEL = PESEL;
        creationData.password = password;
        creationData.incomeLevel = incomeLevel;
        return creationData;
    }
}
