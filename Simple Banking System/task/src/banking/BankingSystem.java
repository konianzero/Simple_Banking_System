package banking;

import banking.account.Account;
import banking.account.AccountService;

import java.util.Optional;
import java.util.Scanner;

public class BankingSystem {
    private static final String MAIN_MENU = "\n1. Create an account\n" +
                                            "2. Log into account\n" +
                                            "0. Exit";

    private static final String ACCOUNT_MENU = "\n1. Balance\n" +
                                               "2. Log out\n" +
                                               "0. Exit";

    private static final String CARD_CREATED = "\nYour card has been created";
    private static final String ENTER_NUMBER = "\nEnter your card number:";
    private static final String ENTER_PIN = "Enter your PIN:";
    private static final String WRONG_NUMBER = "\nWrong card number or PIN!";
    private static final String SUCCESS_LOGIN = "\nYou have successfully logged in!";
    private static final String BALANCE = "\nBalance: %d";
    private static final String SUCCESS_LOGOUT = "\nYou have successfully logged out!";
    private static final String EXIT_MSG = "\nBye!";

    private Scanner scanner;
    private AccountService accountService;

    public BankingSystem(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.println(MAIN_MENU);
            switch (scanner.nextInt()) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    logIn();
                    break;
                case 0:
                    exit();
                    break;
            }
        }
    }

    public void setService(AccountService accountService) {
        this.accountService = accountService;
    }

    private void createAccount() {
        Account account = accountService.createAccount();

        System.out.println(CARD_CREATED);
        System.out.println(account);
    }

    private void logIn() {
        System.out.println(ENTER_NUMBER);
        String number = scanner.next();
        System.out.println(ENTER_PIN);
        String pin = scanner.next();

        Optional<Account> accountOptional = accountService.getAccount(number, pin);

        if (accountOptional.isPresent()) {
            accountMenu(accountOptional.get());
        } else {
            System.out.println(WRONG_NUMBER);
            return;
        }
    }

    private void accountMenu(Account account) {
        System.out.println(SUCCESS_LOGIN);
        while (true) {
            System.out.println(ACCOUNT_MENU);
            switch (scanner.nextInt()) {
                case 1:
                    System.out.println(String.format(BALANCE, account.getBalance()));
                    break;
                case 2:
                    System.out.println(SUCCESS_LOGOUT);
                    return;
                case 0:
                    exit();
                    break;
            }
        }
    }

    private void exit() {
        System.out.println(EXIT_MSG);
        System.exit(0);
    }
}

