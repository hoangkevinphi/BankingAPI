package com.revature.dao.interfaces;

import java.util.List;

public interface GenericDAO <T>{
	public T create(T t);
	public T get(int id);
	public T update(T t);
	public void delete(T t);
	public List<T> getAll();
	
	//Used for AccountDAO, may be unimplemented in other DAOs
	public List<T> getAccountsByUserId(int userId);
	public List<T> getAccountsByStatusId(int statusId);
	public List<T> getAccountsByType(int typeId);
	public List<T> getAccountsByUserIdAndTypeId(int userId, int typeId);
}
