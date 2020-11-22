package banking.account;

public class Account {
    private final String cardNumber;
    private final String pin;
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

    public boolean verifyAccess(String cardNumber, String pin) {
        return this.cardNumber.equals(cardNumber) && this.pin.equals(pin);
    }

    public long getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Your card number:\n"
               + cardNumber
               + "\nYour card PIN:\n"
               + pin;
    }
}

