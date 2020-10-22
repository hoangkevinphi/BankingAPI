package com.revature.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.revature.dao.interfaces.GenericDAO;
import com.revature.models.AccountStatus;
import com.revature.services.ConnectionService;

public class AccountStatusDAOImpl implements GenericDAO<AccountStatus>{

	Connection connection = ConnectionService.getConnection();
	
	public AccountStatus create(AccountStatus accountStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	public AccountStatus get(int id) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from accountstatus where id = ?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				AccountStatus status = new AccountStatus(rs.getInt("id"),
														 rs.getString("status"));
				return status;
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public AccountStatus update(AccountStatus accountStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(AccountStatus accountStatus) {
		// TODO Auto-generated method stub
	}

	public List<AccountStatus> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccountStatus> getAccountsByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccountStatus> getAccountsByStatusId(int statusId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccountStatus> getAccountsByType(int typeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccountStatus> getAccountsByUserIdAndTypeId(int userId, int typeId) {
		// TODO Auto-generated method stub
		return null;
	}

}
