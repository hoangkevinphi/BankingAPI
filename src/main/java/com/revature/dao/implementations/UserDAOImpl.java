package com.revature.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.dao.interfaces.GenericDAO;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.ConnectionService;

public class UserDAOImpl implements GenericDAO<User>{
	
	Connection connection = ConnectionService.getConnection();
	
	@Override
	public User create(User user) {
		try {
			//Insert the account into the database.
			PreparedStatement ps = connection.prepareStatement
					("insert into users (username,password,firstname,lastname,email,roleid) values (?,?,?,?,?,?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getFirstName());
			ps.setString(4, user.getLastName());
			ps.setString(5, user.getEmail());
			ps.setInt(6, user.getRole().getRoleId());
			ps.executeUpdate();
			//Set the generated ID to the account created in Java.
			ResultSet keys = ps.getGeneratedKeys();
			keys.next();
			user.setUserId(keys.getInt(1));
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User get(int id) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from users where id = ?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			GenericDAO<Role> roleDAO = new RoleDAOImpl();
			if(rs.next()) {
				User user = new User (rs.getInt("id"),
					     			  rs.getString("username"),
					     			  rs.getString("password"),
					     			  rs.getString("firstname"),
					     			  rs.getString("lastname"),
					     			  rs.getString("email"),
					     			  roleDAO.get(rs.getInt("roleid")));
				return user;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User update(User user) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("update users set username = ?, password = ?, firstname = ?, lastname = ?, email = ?, roleid = ? where id = ?;");
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getFirstName());
			ps.setString(4, user.getLastName());
			ps.setString(5, user.getEmail());
			ps.setInt(6, user.getRole().getRoleId());
			ps.setInt(7, user.getUserId());
			ps.executeUpdate();
			return user;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void delete(User user) {
		try {
			PreparedStatement ps = connection.prepareStatement
					("delete from users where id = ?;");
			ps.setInt(1, user.getUserId());
			ps.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<User> getAll() {
		List<User> users = new ArrayList<User>();
		try {
			PreparedStatement ps = connection.prepareStatement
					("select * from users"); 
			ResultSet rs = ps.executeQuery();
			GenericDAO<Role> roleDAO = new RoleDAOImpl();
			while(rs.next()) {
				User user = new User (rs.getInt("id"),
		     			  			  rs.getString("username"),
		     			  			  rs.getString("password"),
		     			  			  rs.getString("firstname"),
		     			  			  rs.getString("lastname"),
		     			  			  rs.getString("email"),
		     			  			  roleDAO.get(rs.getInt("roleid")));
				
				users.add(user);
			}
			return users;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<User> getAccountsByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<User> getAccountsByStatusId(int statusId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<User> getAccountsByType(int typeId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<User> getAccountsByUserIdAndTypeId(int userId, int typeId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
