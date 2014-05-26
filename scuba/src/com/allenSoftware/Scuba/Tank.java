package com.allenSoftware.Scuba;

public class Tank 
{
	private double fillLevel; //goes from 0 (empty) to 1 (full)
	
	public Tank()
	{ fillLevel = 0; }
	
	public void fill()
	{ fillLevel = 1.0; }//fill tank completely
	
	public void fill(double amount)
	{ 
		if (fillLevel + amount > 1.0)
			amount -= (fillLevel + amount - 1);
		fillLevel += amount;
	}//don't overfill!
	
	public void drain(double amount)
	{ fillLevel -= amount; }
	
	public boolean isFull()
	{ return fillLevel == 1.0; }
	
	public void setFill(double amount)
	{ fillLevel = amount; }//set the fill level
	
	public double getFillLevel()
	{ return fillLevel; }//get the fill level
	
	public boolean isEmpty()
	{ return fillLevel <= 0; }
	
	public String toString()
	{ return Double.toString(fillLevel * 100) + "%"; }//toString()
}//end class
