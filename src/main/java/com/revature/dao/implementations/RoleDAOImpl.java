package com.revature.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.revature.services.ConnectionService;
import com.revature.dao.interfaces.GenericDAO;
import com.revature.models.Role;

public class RoleDAOImpl implements GenericDAO<Role>{

	Connection connection = ConnectionService.getConnection();

	public Role create(Role role) {
		// TODO Auto-generated method stub
		return null;
	}

	public Role get(int id) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from roles where id = ?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				Role role = new Role(rs.getInt("id"),
									 rs.getString("role"));
				return role;
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Role update(Role role) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(Role role) {
		// TODO Auto-generated method stub
	}

	public List<Role> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Role getRoleByString(String string) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from roles where role = ?;");
			ps.setString(1, string);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				Role role = new Role(rs.getInt("id"),
									 rs.getString("role"));
				return role;
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Role> getAccountsByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Role> getAccountsByStatusId(int statusId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Role> getAccountsByType(int typeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Role> getAccountsByUserIdAndTypeId(int userId, int typeId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
