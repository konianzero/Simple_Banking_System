package banking.dao;

import banking.account.Account;

import java.util.List;

public interface DAO {
    List<Account> getAll();
    void create(Account account);
}

