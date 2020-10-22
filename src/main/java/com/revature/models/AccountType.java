package com.revature.models;

public class AccountType {
	  private int typeId; // primary key
	  private String type; // not null, unique
	  
	  public AccountType(int typeId, String type) {
		  this.typeId = typeId;
		  this.type = type;
	  }
	  
	  public AccountType(int typeId) {
		  this.typeId = typeId;
		  switch(typeId) {
		  case 1:
			  type = "Checkings";
			  break;
		  case 2:
			  type = "Savings";
			  break;
		  }
	  }
	  
	  public AccountType() {}
	  
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
