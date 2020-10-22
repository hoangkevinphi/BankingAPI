package com.revature.services;

public class InterestService {
	private static final double RATE = 0.0025;
	
	public static double AccrueInterest(double balance, int numMonths) {
		if(numMonths <= 0)
			return balance;
		else
			return AccrueInterest(balance + (balance * RATE), numMonths - 1);
	}
}
