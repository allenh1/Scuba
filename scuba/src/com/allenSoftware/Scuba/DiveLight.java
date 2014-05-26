package com.allenSoftware.Scuba;

public class DiveLight extends Thread
{
	private boolean isOn;
	private boolean enabled;
	private int secondsLeft;
	
	public DiveLight()
	{
		isOn = false;
		enabled = false;
		secondsLeft = 1800; //30 minutes
	}//dive light
	
	public void turnOn()
	{ isOn = true; }
	
	public void turnOff()
	{ isOn = false; }
	
	public void enable()
	{ enabled = true; secondsLeft = 1800; start(); }
	
	public boolean isEnabled()
	{ return enabled; }
	
	public String getTimeLeft()
	{
		return Integer.toString(secondsLeft / 60) + ":" + Integer.toString(secondsLeft % 60);
	}//get the remaining time
	
	public boolean lightOn()
	{ return isOn; }
	
	public boolean deadBattery()
	{ return secondsLeft <= 0; }
	
	public void run()
	{
		while (secondsLeft > 0)
		{
			if (isOn)
			{
				try {
			    	sleep(1000);
				} catch(InterruptedException ex) {
			    	Thread.currentThread().interrupt();
				}//sleep for one second
				
				secondsLeft--;
			}//only drain battery when on.
		}//end while
		
		enabled = false; isOn = false; secondsLeft = 0;
	}//end void
}
