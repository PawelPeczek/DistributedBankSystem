#ifndef BANK_INTERFACE_ICE
#define BANK_INTERFACE_ICE

module Bank
{

  enum AccountType {
    STANDARD,
    PREMIUM
  };

  exception OperationException {
    string reason = "Invalid value";
  }

  exception AuthorizationException {
    string reason = "Invalid token";
  }

  interface BankAccount {
    float getState() throws AuthorizationException;
    float putMoney(float value) throws OperationException, AuthorizationException;
    float withdrawMoney(float value) throws OperationException, AuthorizationException;
  }

  enum Currency {
    PLN,
    EUR,
    GBP,
    CHF
  }

  class CreditRequest {
    int monthsLength;
    float value;
    Currency currency;
  }

  class CreditOffer {
    float costInPLN;
    optional(1) float costInForeignCurrency;
    optional(2) Currency foreignCurrency;
  }

  interface PremiumBankAccount extends BankAccount {
    CreditOffer requestCredit(CreditRequest request) throws OperationException, AuthorizationException;
  }

  class LoginResponse {
    BankAccount* account;
    AccountType accountType;
    string secretToken;
  }

  class AccountCreationData {
    string PESEL;
    string password;
    float incomeLevel;
  }

  exception AccountCreationException {
    string reason = "Invalid token";
  }

  exception AuthenticationException {
    string reason = "Invalid token";
  }

  interface AccountManager {
    void createAccount(AccountCreationData creationData) throws AccountCreationException;
    LoginResponse login(string PESEL, string password) throws AuthenticationException;
    void logout();
  }



};

#endif
