package banking.account;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountBuilder {
    private static final String BIN = "400000";
    private static final Random random = new Random();

    public static BankAccount buildNew() {
        return new BankAccount(generateCardNumber(), generatePin(), 0);
    }

    public static BankAccount build(String cardNumber, String pin, int balance) {
        return new BankAccount(cardNumber, pin, balance);
    }

    public static BankAccount buildTransferAccount(String number, int money) {
        return new BankAccount(number, null, money);
    }

    private static class BankAccount implements Account {
        private final String cardNumber;
        private final String pin;
        private int balance;

        public BankAccount(String cardNumber, String pin, int balance) {
            this.cardNumber = cardNumber;
            this.pin = pin;
            this.balance = balance;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public String getPin() {
            return pin;
        }

        public int getBalance() {
            return balance;
        }

        public void addToBalance(int income) {
            this.balance += income;
        }

        public boolean verifyAccess(String cardNumber, String pin) {
            return this.cardNumber.equals(cardNumber) && this.pin.equals(pin);
        }

        public String info() {
            return "Your card number:\n"
                    + cardNumber
                    + "\nYour card PIN:\n"
                    + pin;
        }
    }

    private static String generateCardNumber() {
        int[] cardNumber = new int[16];
        IntStream.range(0, cardNumber.length)
                 .peek(i -> setBIN(cardNumber, i))
                 .skip(6)
                 .forEach(i -> cardNumber[i] = getInt());

        cardNumber[cardNumber.length - 1] = calcCheckSum(cardNumber);
        return arrayToString(cardNumber);
    }

    private static void setBIN(int[] cardNumber, int index) {
        if (index < BIN.length()) {
            cardNumber[index] = Character.getNumericValue(BIN.charAt(index));
        }
    }

    /**
     * Generates a check sum with Luhn algorithm.
     * @param cardNumber the array with card number digits
     * @return checksum
     */
    public static int calcCheckSum(int[] cardNumber) {
        int sum = IntStream.range(0, cardNumber.length - 1)
                           .map(i -> {
                                int digit = cardNumber[i];
                                if (i % 2 == 0) {
                                    digit *= 2;
                                    if (digit > 9) {
                                        return digit - 9;
                                    }
                                }
                                return digit;
                            }).sum();

        int checkSum = 10 - (sum % 10);
        return checkSum == 10 ? 0 : checkSum;
    }

    private static String generatePin() {
        int[] pin = new int[] {getInt(), getInt(), getInt(), getInt()};
        return arrayToString(pin);
    }

    private static String arrayToString(int[] array) {
        return Arrays.stream(array)
                     .mapToObj(String::valueOf)
                     .collect(Collectors.joining());
    }

    private static int getInt() {
        return random.nextInt(10);
    }
}
