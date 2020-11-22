package banking.account;

public interface Account {
    String getCardNumber();
    String getPin();
    int getBalance();
    void addToBalance(int income);
    boolean verifyAccess(String cardNumber, String pin);
    String info();
}

