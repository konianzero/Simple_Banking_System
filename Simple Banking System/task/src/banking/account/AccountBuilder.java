package banking.account;

import java.util.Random;

public class AccountBuilder {
    private static Random random = new Random();

    public static Account build() {
        return new Account(generateCardNumber(), generatePin());
    }

    private static String generateCardNumber() {
        return new StringBuilder("400000")
                .append(getInt())
                .append(getInt())
                .append(getInt())
                .append(getInt())
                .append(getInt())
                .append(getInt())
                .append(getInt())
                .append(getInt())
                .append(getInt())
                .append(getInt()) // checksum
                .toString();
    }

    private static String generatePin() {
        return new StringBuilder("")
                .append(getInt())
                .append(getInt())
                .append(getInt())
                .append(getInt())
                .toString();
    }

    private static int getInt() {
        return random.nextInt(10);
    }
}

