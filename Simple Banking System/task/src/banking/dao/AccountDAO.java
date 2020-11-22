package banking.dao;

import banking.account.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends AbstractDAO {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS card ("
                                                + "id INTEGER PRIMARY KEY ASC,"
                                                + "number TEXT NOT NULL,"
                                                + "pin TEXT NOT NULL,"
                                                + "balance INTEGER DEFAULT 0)";
    private static final String SELECT_ALL = "SELECT * FROM card";
    private static final String INSERT_INTO = "INSERT INTO card (number, pin) values (%s, %s)";

    public AccountDAO(String databaseName) {
        super(databaseName);
        createTable();
    }

    private void createTable() {
        statementPreparation();

        try {
            update(CREATE_TABLE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        statementCompletion();
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        statementPreparation();

        try (ResultSet rs = select(SELECT_ALL)) {
            while (rs.next()) {
                accounts.add(new Account(rs.getString("number"), rs.getString("pin")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        statementCompletion();
        return accounts;
    }

    @Override
    public void create(Account account) {
        statementPreparation();

        try {
            update(String.format(INSERT_INTO, account.getCardNumber(), account.getPin()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        statementCompletion();
    }
}

