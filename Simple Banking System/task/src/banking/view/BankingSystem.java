package banking.view;

import banking.account.Account;
import banking.service.AccountService;

import java.util.Optional;
import java.util.Scanner;

public class BankingSystem {
    private static final String MAIN_MENU = "\n1. Create an account\n" +
                                            "2. Log into account\n" +
                                            "0. Exit";

    private static final String ACCOUNT_MENU = "\n1. Balance\n" +
                                               "2. Add income\n" +
                                               "3. Do transfer\n" +
                                               "4. Close account\n" +
                                               "5. Log out\n" +
                                               "0. Exit";

    private static final String CARD_CREATED = "\nYour card has been created";
    private static final String ENTER_NUMBER = "\nEnter your card number:";
    private static final String ENTER_PIN = "Enter your PIN:";
    private static final String SUCCESS_LOGIN = "\nYou have successfully logged in!";
    private static final String BALANCE = "\nBalance: %d";
    private static final String ENTER_INCOME = "\nEnter income:";
    private static final String INCOME_ADDED = "Income was added!";
    private static final String TRANSFER = "\nTransfer";
    private static final String TRANSFER_CARD_NUM = "Enter card number:";
    private static final String TRANSFER_MONEY = "Enter how much money you want to transfer:";
    private static final String TRANSFER_SUCCESS = "Success!";
    private static final String ACCOUNT_CLOSED = "\nThe account has been closed!";
    private static final String SUCCESS_LOGOUT = "\nYou have successfully logged out!";
    private static final String EXIT_MSG = "\nBye!";

    private static final String WRONG_CARD_DATA = "\nWrong card number or PIN!";
    private static final String SAME_ACCOUNT = "You can't transfer money to the same account!";
    private static final String WRONG_CARD_NUM = "Probably you made mistake in the card number. Please try again!";
    private static final String CARD_NOT_EXIST = "Such a card does not exist.";
    private static final String NOT_ENOUGH_MONEY = "Not enough money!";

    private final Scanner scanner;
    private AccountService accountService;
    private Account account;

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
        Optional<Account> accountOptional = accountService.createAccount();

        if (accountOptional.isPresent()) {
            System.out.println(CARD_CREATED);
            System.out.println(accountOptional.get().info());
        }
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
            System.out.println(WRONG_CARD_DATA);
            return;
        }
    }

    private void accountMenu(Account a) {
        this.account = a;
        System.out.println(SUCCESS_LOGIN);
        while (true) {
            System.out.println(ACCOUNT_MENU);
            switch (scanner.nextInt()) {
                case 1:
                    printBalance();
                    break;
                case 2:
                    addIncome();
                    break;
                case 3:
                    doTransfer();
                    break;
                case 4:
                    closeAccount();
                    return;
                case 5:
                    logOut();
                    return;
                case 0:
                    exit();
                    break;
            }
        }
    }

    private void printBalance() {
        System.out.println(String.format(BALANCE, account.getBalance()));
    }

    private void addIncome() {
        System.out.println(ENTER_INCOME);
        int income = scanner.nextInt();
        
        account.addToBalance(income);
        
        if (accountService.updateAccount(account)) {
            account = accountService.getAccount(account.getCardNumber(), account.getPin()).get();
            System.out.println(INCOME_ADDED);
        }
    }

    private void doTransfer() {
        System.out.println(TRANSFER);

        System.out.println(TRANSFER_CARD_NUM);
        String transferCardNumber = scanner.next();
        if (account.getCardNumber().equals(transferCardNumber)) {
            System.out.println(SAME_ACCOUNT);
            return;
        }
        if (!accountService.checkNumber(transferCardNumber)) {
            System.out.println(WRONG_CARD_NUM);
            return;
        }
        if (!accountService.isExist(transferCardNumber)) {
            System.out.println(CARD_NOT_EXIST);
            return;
        }

        System.out.println(TRANSFER_MONEY);
        int money = scanner.nextInt();
        if (account.getBalance() < money) {
            System.out.println(NOT_ENOUGH_MONEY);
            return;
        }

        account.addToBalance(-money);
        
        if (accountService.transfer(account, transferCardNumber, money)) {
            account = accountService.getAccount(account.getCardNumber(), account.getPin()).get();
            System.out.println(TRANSFER_SUCCESS);
        }
    }

    private void closeAccount() {
        if (accountService.deleteAccount(account)) {
            System.out.println(ACCOUNT_CLOSED);
            account = null;
        }
    }

    private void logOut() {
        System.out.println(SUCCESS_LOGOUT);
        account = null;
    }

    private void exit() {
        System.out.println(EXIT_MSG);
        System.exit(0);
    }
}

