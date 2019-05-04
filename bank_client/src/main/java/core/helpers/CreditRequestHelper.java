package core.helpers;

import Bank.CreditRequest;
import Bank.Currency;

public class CreditRequestHelper {

    public static CreditRequest prepareCreditRequest(float value, Currency currency, int lengthInMonths){
        CreditRequest request = new CreditRequest();
        request.currency = currency;
        request.value = value;
        request.monthsLength = lengthInMonths;
        return request;
    }
}
