package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AccountDAO {
    /**
     * Retrieves all accounts registered in the SQL database.
     * @return An ArrayList of all accounts on the social media site.
     */
    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try{
            String sql = "SELECT * FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    /**
     * Registers a new user to the SQL database.
     * @param account The Account Object to be added.
     * @return an Account Object now containing the newly created account_id, null if failed.
     */
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Checks for valid credentials to login to the site as a specific user.
     * @param account Account object with username and password given by the front end user.
     * @param currentAccounts An ArrayList of all currently registered accounts.
     * @return An Account object if there exists one with the same credentials passed through account, null if not.
     */
    public Account accessAccount(Account account, List<Account> currentAccounts){
        String username = account.getUsername();
        String password = account.getPassword();

        for (int i = 0; i < currentAccounts.size(); i++){
            if (currentAccounts.get(i).getUsername().equals(username) && currentAccounts.get(i).getPassword().equals(password)){ 
                return currentAccounts.get(i);
            } 
        }
        return null;
    }
}
