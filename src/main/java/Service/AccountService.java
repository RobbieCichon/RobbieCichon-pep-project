package Service;

import java.util.List;
import java.util.ArrayList;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }

    public Account addAccount(Account account){
        List<Account> currentAccounts = getAllAccounts();
        for (int i = 0; i < currentAccounts.size(); i++){
            if (account.getUsername() == "" || account.getPassword().length() <= 3 || currentAccounts.get(i).getUsername() == account.getUsername()) return null;
        }
            
        return accountDAO.insertAccount(account);
    }

    public Account accessAccount(Account account){
        List<Account> currentAccounts = getAllAccounts();
        return accountDAO.accessAccount(account, currentAccounts);
    }
}
