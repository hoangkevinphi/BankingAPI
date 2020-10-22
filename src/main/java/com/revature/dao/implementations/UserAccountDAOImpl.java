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
import com.revature.models.User;
import com.revature.models.UserAccount;
import com.revature.services.ConnectionService;

public class UserAccountDAOImpl implements GenericDAO<UserAccount>{
	
	Connection connection = ConnectionService.getConnection();
	
	@Override
	public UserAccount create(UserAccount userAccount) {
		try {
			//Insert the account into the database.
			PreparedStatement ps = connection.prepareStatement
					("insert into usersaccounts (userid,accountid) values (?,?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userAccount.getUser().getUserId());
			ps.setInt(2, userAccount.getAccount().getAccountId());
			ps.executeUpdate();
			return userAccount;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UserAccount get(int accountId) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from usersaccounts where accountid = ?;");
			ps.setInt(1, accountId);
			ResultSet rs = ps.executeQuery();
			GenericDAO<User> userDAO = new UserDAOImpl();
			GenericDAO<Account> accountDAO = new AccountDAOImpl();
			if(rs.next()) {
				UserAccount userAccount = new UserAccount (userDAO.get(rs.getInt("userid")),
					     			  		   			   accountDAO.get(rs.getInt("accountid")));
				return userAccount;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*public UserAccount get(int userId, int accountId) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from usersaccounts where userid = ? and accountid = ?;");
			ps.setInt(1, userId);
			ps.setInt(2, accountId);
			ResultSet rs = ps.executeQuery();
			GenericDAO<User> userDAO = new UserDAOImpl();
			GenericDAO<Account> accountDAO = new AccountDAOImpl();
			if(rs.next()) {
				UserAccount userAccount = new UserAccount (userDAO.get(rs.getInt("userid")),
					     			  		   			   accountDAO.get(rs.getInt("accountid")));
				return userAccount;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

	@Override
	public UserAccount update(UserAccount userAccount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(UserAccount userAccount) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("delete from usersaccounts where accountid = ?;");
			ps.setInt(1, userAccount.getAccount().getAccountId());
			ps.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<UserAccount> getAll() {
		List<UserAccount> usersAccounts = new ArrayList<UserAccount>();
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from usersaccounts"); 
			ResultSet rs = ps.executeQuery();
			GenericDAO<User> userDAO = new UserDAOImpl();
			GenericDAO<Account> accountDAO = new AccountDAOImpl();
			if(rs.next()) {
				UserAccount userAccount = new UserAccount (userDAO.get(rs.getInt("userid")),
					     			  		   			   accountDAO.get(rs.getInt("accountid")));
				usersAccounts.add(userAccount);
			}
			return usersAccounts;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<UserAccount> getAccountsByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<UserAccount> getAccountsByStatusId(int statusId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<UserAccount> getAccountsByType(int typeId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<UserAccount> getAccountsByUserIdAndTypeId(int userId, int typeId) {
		// TODO Auto-generated method stub
		return null;
	}
}
