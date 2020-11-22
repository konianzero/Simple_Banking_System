package banking.account;

public class Account {
    private String cardNumber;
    private String pin;
    private long balance;

    public Account(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        balance = 0;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public long getBalance() {
        return balance;
    }
}

