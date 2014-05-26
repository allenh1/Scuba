package com.allenSoftware.Scuba;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public class Diver extends Thread{
	private Tank tank;
	private OfflinePlayer player_offline;
	private String name;
	private Player player;
	private DiveLight diveLight;
	private boolean notRegistered;
	
	public Diver(String playerName)
	{
		tank = new Tank();
		diveLight = new DiveLight();
		player_offline = Bukkit.getServer().getOfflinePlayer(playerName);
		notRegistered = true;
		name = playerName;
	}//end constructor
	
	public boolean hasEmptyTank()
	{ return tank.isEmpty(); }//check tank level
	
	public void drainTank(double amount)
	{ tank.drain(amount); }
	
	public void setRemainingAir(int ticks)
	{ player.setRemainingAir(ticks); }
	
	public String getPlayerName()
	{ return name; }
	
	private boolean isUnderwater() 
	{
		if (!notRegistered)
		{
		World w = player.getWorld();
		Location l = player.getLocation();		
		l.setY(l.getY() + 1);
			if(w.getBlockAt(l).getType() == Material.WATER || w.getBlockAt(l).getType() == Material.STATIONARY_WATER){
			return true;
			} 
			else {
			return false;
			}
		}
		
		return false;
	}//check if the player is underwater
	
	public void run()
	{
		while (true)
		{			
			if (notRegistered && Bukkit.getServer().getPlayer(player_offline.getName()) != null)
			{ 
				player = Bukkit.getServer().getPlayer(player_offline.getName());
				Bukkit.getServer().getLogger().info("Diver " + player.getName() +" is now online.");
				notRegistered = false;
			}//end if
			
			else if (!notRegistered)
			{		
				if (diveLight.lightOn() && !isUnderwater())
					diveLight.turnOff();
				
				if (diveLight.lightOn() && diveLight.isEnabled())
				{
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 5000, 2, true));

					try {
				    	sleep(500);
					} catch(InterruptedException ex) {
				    	Thread.currentThread().interrupt();
					}
				}
				
				else
				{
					if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
						player.removePotionEffect(PotionEffectType.NIGHT_VISION);
					
					try {
				    	sleep(500);
					} catch(InterruptedException ex) {
				    	Thread.currentThread().interrupt();
					}
				}
				
			
				if (!isUnderwater() && !tank.isFull())
				{ tank.fill(0.0001); }
				
				if (diveLight.isEnabled() && isUnderwater() && !diveLight.deadBattery())
					diveLight.turnOn();
				
				if (!tank.isEmpty() && isUnderwater())
				{ tank.drain(0.0005); player.setRemainingAir(400); }//5 percent drain if underwater and tank has air
			}//end else
		}//end while
	}//run void
	
	public void fillTank()
	{ tank.fill(); }
	
	public String getFillLevel()
	{ return tank.toString(); }
	
	public boolean hasLightEnabled()
	{ return diveLight.isEnabled(); }
	
	public String getLightRemaining()
	{ return diveLight.getTimeLeft(); }
	
	public void enableLight()
	{ diveLight.turnOn(); }
	
	public void disableLight()
	{ diveLight.turnOff(); }
	
	public void getDiveLight()
	{
		if (hasLightEnabled())
			return;
		
		final int glowDustID = 348; 
		final int costOfLight = 5; 
		
		ItemStack glowMin = new ItemStack(glowDustID, costOfLight);
		if (player.getInventory().contains(glowMin))
		{
			Bukkit.broadcastMessage("5 Glowstone Dust will be removed from your inventory!");
			player.getInventory().remove(glowMin);
			
			diveLight.enable(); //start the divelight thread
		}//end if
		else
			Bukkit.broadcastMessage("You need a stack of exactly 5 Glowstone Dust to make a Dive Light.");
	}//Turn on the dive light
}
