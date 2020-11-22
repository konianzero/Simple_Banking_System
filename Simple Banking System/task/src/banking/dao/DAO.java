package banking.dao;

import banking.account.Account;

import java.util.List;

public interface DAO {
    List<Account> getAll();
    boolean isExist(String number);
    boolean create(Account account);
    Account get(String number, String pin);
    boolean transfer(Account from, Account to);
    boolean update(Account account);
    boolean delete(Account account);
}

