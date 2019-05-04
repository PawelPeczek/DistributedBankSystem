package core.config;

public class ClientConfig {

    private static String FIRST_BANK_CONFIG = "config-1.client";
    private static String SECOND_BANK_CONFIG = "config-2.client";

    public static String getConfigForBank(int bankNumber){
        if(bankNumber == 1) {
            return ClientConfig.FIRST_BANK_CONFIG;
        } else if (bankNumber == 2){
            return ClientConfig.SECOND_BANK_CONFIG;
        } else {
            throw new RuntimeException("Cannot find config");
        }

    }

}
