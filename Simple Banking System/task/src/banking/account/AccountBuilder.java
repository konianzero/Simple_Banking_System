package banking.account;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class AccountBuilder {
    private static final String BIN = "400000";
    private static Random random = new Random();

    public static Account build() {
        return new Account(generateCardNumber(), generatePin());
    }

    private static String generateCardNumber() {
        int[] cardNumber = new int[16];
        IntStream.range(0, cardNumber.length)
                 .peek(i -> setBIN(cardNumber, i))
                 .skip(6)
                 .forEach(i -> cardNumber[i] = getInt());

        return addCheckSum(cardNumber);
    }

    private static void setBIN(int[] cardNumber, int index) {
        if (index < BIN.length()) {
            cardNumber[index] = Character.getNumericValue(BIN.charAt(index));
        }
    }

    /**
     * Generates a check sum with Luhn algorithm.
     * @param cardNumber the array with card number digits without checksum
     * @return card number with checksum
     */
    private static String addCheckSum(int[] cardNumber) {
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
        cardNumber[cardNumber.length - 1] = checkSum == 10 ? 0 : checkSum;

        return arrayToString(cardNumber);
    }

    private static String generatePin() {
        int[] pin = new int[] {getInt(), getInt(), getInt(), getInt()};
        return arrayToString(pin);
    }

    private static String arrayToString(int[] array) {
        return Arrays.stream(array)
                     .mapToObj(i -> String.valueOf(i))
                     .collect(Collectors.joining());
    }

    private static int getInt() {
        return random.nextInt(10);
    }
}

