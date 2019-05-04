package services;

import currency.exchange.gen.Currency;

import java.util.LinkedList;

public class SystemConfig {

    private static String FIRST_BANK_NAME = "bank1";
    private static String SECOND_BANK_NAME = "bank2";
    private static String CURRENCY_EXCHANGE_HOST = "currency-exchange";
    private static int CURRENCY_EXCHANGE_PORT = 50051;
    private static String FIRST_BANK_HIBERNATE_CFG = "configs/hibernate_1.cfg.xml";
    private static String SECOND_BANK_HIBERNATE_CFG = "configs/hibernate_2.cfg.xml";

    public static LinkedList<Currency> getCurrencyToSubscribe(String bankName){
        LinkedList<Currency> currencyToSubscribe = new LinkedList<>();
        if(bankName.equals(SystemConfig.FIRST_BANK_NAME)){
            currencyToSubscribe.add(Currency.EUR);
            currencyToSubscribe.add(Currency.GBP);
            return currencyToSubscribe;
        } else if (bankName.equals(SystemConfig.SECOND_BANK_NAME)){
            currencyToSubscribe.add(Currency.EUR);
            currencyToSubscribe.add(Currency.CHF);
            return currencyToSubscribe;
        } else {
            throw new RuntimeException("Wrong bank name. Use one from " + FIRST_BANK_NAME + " | " + SECOND_BANK_NAME);
        }
    }

    public static String getCurrencyExchangeHost() {
        return SystemConfig.CURRENCY_EXCHANGE_HOST;
    }

    public static int getCurrencyExchangePort(){
        return SystemConfig.CURRENCY_EXCHANGE_PORT;
    }

    public static String getHibernateConfigPath(String bankName){
        if(bankName.equals(SystemConfig.FIRST_BANK_NAME)){
            return SystemConfig.FIRST_BANK_HIBERNATE_CFG;
        } else if (bankName.equals(SystemConfig.SECOND_BANK_NAME)){
            return SystemConfig.SECOND_BANK_HIBERNATE_CFG;
        } else {
            throw new RuntimeException("Wrong bank name. Use one from " + FIRST_BANK_NAME + " | " + SECOND_BANK_NAME);
        }
    }

}
