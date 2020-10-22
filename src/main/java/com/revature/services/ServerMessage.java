package com.revature.services;

public class ServerMessage {
	private String message;
	
	public ServerMessage(String message) {
		this.message = message;
	}
	public ServerMessage() {}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
