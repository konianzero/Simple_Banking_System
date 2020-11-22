package banking.dao;

import banking.account.Account;
import banking.account.AccountBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends AbstractDAO {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS card ("
                                                + "id INTEGER PRIMARY KEY ASC,"
                                                + "number TEXT NOT NULL,"
                                                + "pin TEXT NOT NULL,"
                                                + "balance INTEGER DEFAULT 0)";
    private static final String SELECT_ALL = "SELECT * FROM card";
    private static final String CREATE_ACCOUNT = "INSERT INTO card (number, pin) values (?, ?)";
    private static final String GET_ACCOUNT = "SELECT * FROM card WHERE number = ? AND pin = ?";
    private static final String IS_EXIST = "SELECT number FROM card WHERE number = ?";
    private static final String UPDATE_BALANCE = "UPDATE card SET balance = ? WHERE number = ?";
    private static final String TRANSFER = "UPDATE card SET balance = balance + ? WHERE number = ?";
    private static final String DELETE_ACCOUNT = "DELETE FROM card WHERE number = ?";

    private static final String ROLL_BACK_MSG = "Transaction is being rolled back";

    public AccountDAO(String databaseName) {
        super(databaseName);
        createTable();
    }

    private void createTable() {
        try (Connection con = createConnection();
             PreparedStatement ps = con.prepareStatement(CREATE_TABLE)) {

            ps.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();

        try (Connection con = createConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                accounts.add(AccountBuilder.build(rs.getString("number"),
                                                  rs.getString("pin"),
                                                  rs.getInt("balance"))
                );
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return accounts;
    }

    @Override
    public boolean create(Account account) {
        try (Connection con = createConnection();
             PreparedStatement ps = con.prepareStatement(CREATE_ACCOUNT)) {

            ps.setString(1, account.getCardNumber());
            ps.setString(2, account.getPin());

            ps.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Account get(String number, String pin) {
        Account account = null;
        try (Connection con = createConnection();
             PreparedStatement ps = con.prepareStatement(GET_ACCOUNT)) {

            ps.setString(1, number);
            ps.setString(2, pin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    account = AccountBuilder.build(rs.getString("number"),
                                                   rs.getString("pin"),
                                                   rs.getInt("balance"));
                }
            }
        } catch (SQLException se) {
            System.err.println(se.getMessage());
        }

        return account;
    }

    @Override
    public boolean update(Account account) {
        try (Connection con = createConnection()) {
            con.setAutoCommit(false);
            Savepoint savepoint = con.setSavepoint();

            try (PreparedStatement ps = con.prepareStatement(UPDATE_BALANCE)) {

                ps.setInt(1, account.getBalance());
                ps.setString(2, account.getCardNumber());

                ps.executeUpdate();
            } catch (SQLException e) {
                if (con != null) {
                    try {
                        System.err.print(ROLL_BACK_MSG);
                        con.rollback(savepoint);
                    } catch (SQLException se) {
                        System.err.println(se.getMessage());
                        return false;
                    }
                }
            }

            con.commit();
        } catch (SQLException se) {
            System.err.println(se.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean transfer(Account from, Account to) {
        try (Connection con = createConnection()) {
            con.setAutoCommit(false);
            Savepoint savepoint = con.setSavepoint();

            try (PreparedStatement transferFrom = con.prepareStatement(UPDATE_BALANCE);
                 PreparedStatement transferTo = con.prepareStatement(TRANSFER)) {

                transferFrom.setInt(1, from.getBalance());
                transferFrom.setString(2, from.getCardNumber());
                transferFrom.executeUpdate();

                transferTo.setInt(1, to.getBalance());
                transferTo.setString(2, to.getCardNumber());
                transferTo.executeUpdate();

            } catch (SQLException e) {
                if (con != null) {
                    try {
                        System.err.print(ROLL_BACK_MSG);
                        con.rollback(savepoint);
                    } catch (SQLException se) {
                        System.err.println(se.getMessage());
                        return false;
                    }
                }
            }

            con.commit();
        } catch (SQLException se) {
            System.err.println(se.getMessage());
            return false;
        }
        return true;
    }

    public boolean isExist(String number) {
        try (Connection con = createConnection();
             PreparedStatement ps = con.prepareStatement(IS_EXIST)) {

            ps.setString(1, number);

            return ps.executeQuery().next();

        } catch (SQLException se) {
            System.err.println(se.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Account account) {
        try (Connection con = createConnection()) {
            con.setAutoCommit(false);
            Savepoint savepoint = con.setSavepoint();

            try (PreparedStatement ps = con.prepareStatement(DELETE_ACCOUNT)) {

                ps.setString(1, account.getCardNumber());

                ps.executeUpdate();
            } catch (SQLException e) {
                if (con != null) {
                    try {
                        System.err.print(ROLL_BACK_MSG);
                        con.rollback(savepoint);
                    } catch (SQLException se) {
                        System.err.println(se.getMessage());
                        return false;
                    }
                }
            }

            con.commit();
        } catch (SQLException se) {
            System.err.println(se.getMessage());
            return false;
        }
        return true;
    }
}

