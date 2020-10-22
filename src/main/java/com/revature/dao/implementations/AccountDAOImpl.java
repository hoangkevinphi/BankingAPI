package com.revature.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.dao.interfaces.GenericDAO;
import com.revature.models.Account;
import com.revature.models.AccountStatus;
import com.revature.models.AccountType;
import com.revature.models.User;
import com.revature.services.ConnectionService;

public class AccountDAOImpl implements GenericDAO<Account>{

	Connection connection = ConnectionService.getConnection();
	
	public Account create(Account account) {
		try {
			//Insert the account into the database.
			PreparedStatement ps = connection.prepareStatement
					("insert into accounts (balance,statusid,typeid) values (?,?,?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setDouble(1, account.getBalance());
			ps.setInt(2, account.getStatus().getStatusId());
			ps.setInt(3, account.getType().getTypeId());
			ps.executeUpdate();
			//Set the generated ID to the account created in Java.
			ResultSet keys = ps.getGeneratedKeys();
			keys.next();
			account.setAccountId(keys.getInt(1));
			return account;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void create(Account account, User user) {
		try {
			//Insert the account into the database.
			PreparedStatement ps = connection.prepareStatement
					("insert into accounts (balance,statusid,typeid) values (?,?,?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setDouble(1, account.getBalance());
			ps.setInt(2, account.getStatus().getStatusId());
			ps.setInt(3, account.getType().getTypeId());
			ps.executeUpdate();
			//Set the generated ID to the account created in Java.
			ResultSet keys = ps.getGeneratedKeys();
			keys.next();
			account.setAccountId(keys.getInt(1));
			
			//Insert account and user into usersaccounts table
			PreparedStatement usersAccountsPs = connection.prepareStatement
					("insert into usersaccounts values (?,?);");
			usersAccountsPs.setInt(1, user.getUserId());
			usersAccountsPs.setInt(2, account.getAccountId());
			usersAccountsPs.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Account get(int id) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from accounts where id = ?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			GenericDAO<AccountStatus> statusDAO = new AccountStatusDAOImpl();
			GenericDAO<AccountType> typeDAO = new AccountTypeDAOImpl();
			if(rs.next()) {
				Account account = new Account (rs.getInt("id"),
					     					   rs.getDouble("balance"),
					     					   statusDAO.get(rs.getInt("statusid")),
					     					   typeDAO.get(rs.getInt("typeid")));
				return account;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Account update(Account account) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("update accounts set balance = ?, statusid = ?, typeid = ? where id = ?;");
			ps.setDouble(1, account.getBalance());
			ps.setInt(2, account.getStatus().getStatusId());
			ps.setInt(3, account.getType().getTypeId());
			ps.setInt(4, account.getAccountId());
			ps.executeUpdate();
			return account;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void delete(Account account) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("delete from accounts where id = ?;");
			ps.setInt(1, account.getAccountId());
			ps.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Account> getAll() {
		List<Account> accounts = new ArrayList<Account>();
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from accounts;"); 
			ResultSet rs = ps.executeQuery();
			GenericDAO<AccountStatus> statusDAO = new AccountStatusDAOImpl();
			GenericDAO<AccountType> typeDAO = new AccountTypeDAOImpl();
			while(rs.next()) {
				Account account = new Account (rs.getInt("id"),
  					   rs.getDouble("balance"),
  					   statusDAO.get(rs.getInt("statusid")),
  					   typeDAO.get(rs.getInt("typeid")));
				
				accounts.add(account);
			}
			return accounts;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Account> getAccountsByStatusId(int statusId) {
		List<Account> accounts = new ArrayList<Account>();
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from accounts where statusid = ?;"); 
			ps.setInt(1, statusId);
			ResultSet rs = ps.executeQuery();
			GenericDAO<AccountStatus> statusDAO = new AccountStatusDAOImpl();
			GenericDAO<AccountType> typeDAO = new AccountTypeDAOImpl();
			while(rs.next()) {
				Account account = new Account (rs.getInt("id"),
  					   rs.getDouble("balance"),
  					   statusDAO.get(rs.getInt("statusid")),
  					   typeDAO.get(rs.getInt("typeid")));
				
				accounts.add(account);
			}
			return accounts;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Account> getAccountsByUserId(int userId){
		List<Account> accounts = new ArrayList<Account>();
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from accounts left join usersaccounts on accounts.id = usersaccounts.accountid where usersaccounts.userid = ?;"); 
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			GenericDAO<AccountStatus> statusDAO = new AccountStatusDAOImpl();
			GenericDAO<AccountType> typeDAO = new AccountTypeDAOImpl();
			while(rs.next()) {
				Account account = new Account (rs.getInt("id"),
  					   						   rs.getDouble("balance"),
  					   						   statusDAO.get(rs.getInt("statusid")),
  					   						   typeDAO.get(rs.getInt("typeid")));
				
				accounts.add(account);
			}
			return accounts;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Account> getAccountsByType(int typeId){
		List<Account> accounts = new ArrayList<Account>();
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from accounts where typeid = ?"); 
			ps.setInt(1, typeId);
			ResultSet rs = ps.executeQuery();
			GenericDAO<AccountStatus> statusDAO = new AccountStatusDAOImpl();
			GenericDAO<AccountType> typeDAO = new AccountTypeDAOImpl();
			while(rs.next()) {
				Account account = new Account (rs.getInt("id"),
  					   						   rs.getDouble("balance"),
  					   						   statusDAO.get(rs.getInt("statusid")),
  					   						   typeDAO.get(rs.getInt("typeid")));
				
				accounts.add(account);
			}
			return accounts;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Account> getAccountsByUserIdAndTypeId(int userId, int typeId){
		List<Account> accounts = new ArrayList<Account>();
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from accounts left join usersaccounts on accounts.id = usersaccounts.accountid where usersaccounts.userid = ? and typeid = ?;"); 
			ps.setInt(1, userId);
			ps.setInt(2, typeId);
			ResultSet rs = ps.executeQuery();
			GenericDAO<AccountStatus> statusDAO = new AccountStatusDAOImpl();
			GenericDAO<AccountType> typeDAO = new AccountTypeDAOImpl();
			while(rs.next()) {
				Account account = new Account (rs.getInt("id"),
  					   						   rs.getDouble("balance"),
  					   						   statusDAO.get(rs.getInt("statusid")),
  					   						   typeDAO.get(rs.getInt("typeid")));
				
				accounts.add(account);
			}
			return accounts;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
