package banking.service;

import banking.account.Account;
import banking.account.AccountBuilder;
import banking.dao.DAO;

import java.util.List;
import java.util.Optional;

public class AccountService {
    private DAO accountDAO;

    public void setController(DAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Optional<Account> createAccount() {
        Account account = AccountBuilder.buildNew();
        if (accountDAO.create(account)) {
            return Optional.of(account);
        }
        return Optional.empty();
    }

    public Optional<Account> getAccount(String number, String pin) {
//        return accountDAO.getAll()
//                         .stream()
//                         .filter(a -> a.verifyAccess(number, pin))
//                         .findFirst();
        return Optional.ofNullable(accountDAO.get(number, pin));
    }

    public boolean updateAccount(Account account) {
        return accountDAO.update(account);
    }

    public boolean transfer(Account from, String number, int money) {
        Account to = AccountBuilder.buildTransferAccount(number, money);
        return accountDAO.transfer(from, to);
    }

    public boolean checkNumber(String number) {
        int[] array = number.chars().map(Character::getNumericValue).toArray();
        return array[array.length - 1] == AccountBuilder.calcCheckSum(array);
    }

    public boolean isExist(String number) {
        return accountDAO.isExist(number);
    }

    public boolean deleteAccount(Account account) {
        return accountDAO.delete(account);
    }
}

