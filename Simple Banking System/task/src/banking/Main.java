package banking;

import banking.service.AccountService;
import banking.dao.AccountDAO;
import banking.dao.DAO;
import banking.view.BankingSystem;

import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BankingSystem bankingSystem = new BankingSystem(scanner);
        AccountService accountService = new AccountService();

        String databaseName = getDBName(args).orElse("");
        DAO accountDAO = new AccountDAO(databaseName);

        bankingSystem.setService(accountService);
        accountService.setController(accountDAO);

        bankingSystem.start();
    }

    private static Optional<String> getDBName(String[] args) {
        if (args[0].equals("-fileName")) {
            return Optional.ofNullable(args[1]);
        }
        return Optional.empty();
    }
}

