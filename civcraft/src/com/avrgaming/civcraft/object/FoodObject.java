package com.avrgaming.civcraft.object;

import com.avrgaming.civcraft.exception.CivException;

public class FoodObject {
	
	private String foodName;
	private Double foodCount = 0.0;
//	private Double principalAmount = 0.0;
	private SQLObject holder;
	
	public FoodObject(SQLObject holder) {
		this.holder = holder;
	}
	
	public String getFoodName() {
		return foodName;
	}
	
	public void setFoodName(String name) {
		this.foodName = name;
	}
	
	public double getFoodCount() {
		foodCount = Math.floor(foodCount);
		synchronized (foodCount) {
			return foodCount;
		}
	}
	
	public void setFoodCount(double amount) {
		this.setFoodCount(amount, true);
	}
	
	public void setFoodCount(double amount, boolean save) {
		if (amount < 0) {
			amount = 0;
		}
		
		amount = Math.floor(amount);
		synchronized (foodCount) {
			foodCount = amount;
		}
		
		if (save) {
			holder.save();
		}
	}
	
	public void giveFood(double amount) {
		if (amount < 0) {
			amount = 0;
		}
		amount = Math.floor(amount);
		this.giveFood(amount, true);
	}
	
	public void giveFood(double amount, boolean save) {
		if (amount < 0) {
			amount = 0;
		}
		
		amount = Math.floor(amount);
		synchronized (foodCount) {
			foodCount += amount;
		}
		
		if (save) {
			holder.save();
		}
	}
	
	public void takeFood(double amount) {
		if (amount < 0) {
			amount = 0;
		}
		amount = Math.floor(amount);
		this.takeFood(amount, true);
	}
	
	public void takeFood(double amount, boolean save) {
		if (amount < 0) {
			amount = 0;
		}
		
		amount = Math.floor(amount);
		/* Update the principal we use to calculate interest,
		 * if our current balance dips below the principal,
		 * then we subtract from the principal. */
/*		synchronized(principalAmount) {
			if (principalAmount > 0) {
				double currentBalance = this.getFoodCount();
				double diff = currentBalance - principalAmount;
				diff -= amount;
				
				if (diff < 0) {
					principalAmount -= (-diff);
				}
			}
		}*/
		
		synchronized(foodCount) {
			foodCount -= amount;
		}
		
		if (save) {
			holder.save();
		}
	}
	
	public boolean hasEnoughFood(double amount) {
		amount = Math.floor(amount);
		synchronized (foodCount) {
			if (foodCount >= amount) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean payTo(FoodObject objToPay, double amount) {
		if (!this.hasEnoughFood(amount)) {
			return false;
		} else {
			this.takeFood(amount);
			objToPay.giveFood(amount);
			return true;
		}
	}
	
	public double payToCreditor(FoodObject objToPay, double amount) {
		double total = 0;
		if (this.hasEnoughFood(amount)) {
			this.takeFood(amount);
			objToPay.giveFood(amount);
			return amount;
		} else {
			/* Do not have enough to pay */
			try {
				throw new CivException("Cannot pay this enough, cancelling.");
			} catch (CivException e) {
				e.printStackTrace();
			}
		}
		return total;
	}
	
/*	public double getPrincipalAmount() {
		return principalAmount;
	}
	
	public void setPrincipalAmount(double interestAmount) {
		this.principalAmount = interestAmount;
	}*/
}
