package com.revature.models;

import java.util.ArrayList;

public class Account {
	private int accountId; // primary key
	private double balance;  // not null
	private AccountStatus status;
	private AccountType type;
	  
	public Account(int accountId, double balance, AccountStatus status, AccountType type) {
		this.accountId = accountId;
		this.balance = balance;
		this.status = status;
		this.type = type;
	}
	
	public Account(double balance, AccountStatus status, AccountType type) {
		this.balance = balance;
		this.status = status;
		this.type = type;
	}
	
	public Account() {}
	  
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public AccountStatus getStatus() {
		return status;
	}
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
	}
}
