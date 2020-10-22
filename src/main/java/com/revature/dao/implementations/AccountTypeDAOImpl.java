package com.revature.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.revature.services.ConnectionService;
import com.revature.dao.interfaces.GenericDAO;
import com.revature.models.AccountType;

public class AccountTypeDAOImpl implements GenericDAO<AccountType>{

	Connection connection = ConnectionService.getConnection();

	public AccountType create(AccountType accountType) {
		// TODO Auto-generated method stub
		return null;
	}

	public AccountType get(int id) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from accounttypes where id = ?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				AccountType type = new AccountType(rs.getInt("id"),
												   rs.getString("type"));
				return type;
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public AccountType update(AccountType accountType) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(AccountType accountType) {
		// TODO Auto-generated method stub
	}

	public List<AccountType> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccountType> getAccountsByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccountType> getAccountsByStatusId(int statusId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccountType> getAccountsByType(int typeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccountType> getAccountsByUserIdAndTypeId(int userId, int typeId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
