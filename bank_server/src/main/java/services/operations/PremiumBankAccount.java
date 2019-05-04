package services.operations;

import Bank.*;
import currency.client.CurrencyNotAvailableException;
import currency.client.CurrencyServiceNotAvalableException;
import currency.exchange.gen.Currency;
import com.zeroc.Ice.Current;
import currency.client.CurrencyServiceClient;
import persistance.model.BankClient;
import services.access_controll.TokenManager;
import services.access_controll.TokenNotPresentInContextException;

public class PremiumBankAccount extends BankAccount implements Bank.PremiumBankAccount {

    private final CurrencyServiceClient currencyServiceClient;
    private static float MONTHLY_PRECENTAGE = (float)0.06;

    public PremiumBankAccount(BankClient client, CurrencyServiceClient currencyServiceClient){
        super(client);
        this.currencyServiceClient = currencyServiceClient;
    }

    @Override
    public CreditOffer requestCredit(CreditRequest request, Current current) throws AuthorizationException, OperationException {
        this.authorizeOperation(current);
        return this.prepareCreditOffer(request);
    }

    private void authorizeOperation(Current current) throws AuthorizationException {
        TokenManager tokenManager = new TokenManager();
        try {
            String token = tokenManager.getTokenFromContext(current);
            if(!tokenManager.isPremiumTokenValid(token)){
                throw new AuthorizationException();
            }
        } catch (TokenNotPresentInContextException e) {
            throw new AuthorizationException();
        }
    }

    private CreditOffer prepareCreditOffer(CreditRequest request) throws OperationException {
        Bank.Currency creditCurrency = request.currency;
        CreditOffer offer = new CreditOffer();
        float toExchange;
        if(!creditCurrency.equals(Bank.Currency.PLN)){
            offer.setForeignCurrency(creditCurrency);
            toExchange = request.value;
            toExchange = calculateCost(toExchange, request.monthsLength);
            offer.setCostInForeignCurrency(toExchange);
            toExchange = calculateCostInPLN(toExchange, creditCurrency);
        } else {
            toExchange = request.value;
        }
        offer.costInPLN = toExchange;
        return offer;
    }

    private float calculateCostInPLN(float value, Bank.Currency creditCurrency) throws OperationException {
        Currency exchangeCurrency = this.getExchangeCurrency(creditCurrency);
        try {
            float exchangeValue = this.currencyServiceClient.getCurrencyValue(exchangeCurrency);
            return value * exchangeValue;
        } catch (CurrencyNotAvailableException | CurrencyServiceNotAvalableException e) {
            throw new OperationException(e.getMessage());
        }

    }

    private Currency getExchangeCurrency(Bank.Currency currency) throws OperationException {
        Currency exchangeCurrency;
        switch (currency) {
            case CHF:
                exchangeCurrency = Currency.CHF;
                break;
            case EUR:
                exchangeCurrency = Currency.EUR;
                break;
            case GBP:
                exchangeCurrency = Currency.GBP;
                break;
            default:
                throw new OperationException("Unrecognized currency");
        }
        return exchangeCurrency;
    }

    private float calculateCost(float value, int lengthInMonths){
        return value + lengthInMonths * value * PremiumBankAccount.MONTHLY_PRECENTAGE;
    }

}
