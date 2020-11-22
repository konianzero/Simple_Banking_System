package banking.account;

import banking.dao.DAO;

import java.util.Optional;

public class AccountService {
    private DAO accountDAO;

    public void setController(DAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account createAccount() {
        Account account = AccountBuilder.build();
        accountDAO.create(account);
        return account;
    }

    public Optional<Account> getAccount(String number, String pin) {
        return accountDAO.getAll()
                         .stream()
                         .filter(a -> a.verifyAccess(number, pin))
                         .findFirst();
    }
}

