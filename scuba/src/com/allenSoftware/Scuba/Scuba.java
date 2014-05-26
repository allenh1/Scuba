package com.allenSoftware.Scuba;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public final class Scuba extends JavaPlugin 
{
	private ArrayList<Diver> divers;
	
	@Override
	public void onEnable()
	{
		getLogger().info("SCUBA plugin enabled!");
		
		divers = new ArrayList<Diver>();
	    /** This code updates the players tank **/
		
		for (OfflinePlayer p : getServer().getOfflinePlayers())
		{
			divers.add(new Diver(p.getName())); //add diver to the list
		
			getLogger().info("Added new Diver: " + p.getName());
		}
		
		for (int x = 0; x < divers.size(); x++)
			divers.get(x).start();
	}
 
	@Override
	public void onDisable()
	{
		getLogger().info("SCUBA plugin disabled!");
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) 
	{
		getLogger().info("Call to login method.");
	    /** This code updates the players tank **/
		
		divers.add(new Diver(event.getPlayer().getName())); //add diver to the list
		
		getLogger().info("Added new Diver: " + event.getPlayer().getName());
		
		divers.get(divers.size() - 1).start();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{			
		if (cmd.getName().equalsIgnoreCase("fillTank"))
		{
			if (args.length != 1)
			{	
				sender.sendMessage("Invalid Arguments. Try /fillTank <player>");
				return false;
			}//end if
			
			int index = 0; 
			
			for (index = 0; index < divers.size(); index++)
			{
				if (divers.get(index).getPlayerName().equalsIgnoreCase(args[0]))
					break;
			}//find the diver
			if (index == divers.size() - 1 && index != 0)
			{
				sender.sendMessage("Diver not registered!");
				return false;
			}//end if
			

			divers.get(index).fillTank();

			return true;
		}//Fill a tank (administrators only, likely) 
		
		else if (cmd.getName().equalsIgnoreCase("checkTank"))
		{
			if (args.length == 0)
			{
				String playerName = sender.getName();
				
				for (int x = 0; x < divers.size(); x++)
				{
					if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName()))
					{
						sender.sendMessage(divers.get(x).getFillLevel());
						return true;
					}//find the player and send tank information.
				}//end for x
				
				sender.sendMessage("Diver not registered!");
				return false;
			}//if no 
			
			else if (args.length == 1)
			{
				String playerName = args[0];
				
				for (int x = 0; x < divers.size(); x++)
				{
					if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName()))
					{
						sender.sendMessage(divers.get(x).getFillLevel());
						return true;
					}//find the player and send tank information.
				}//end for x
				
				sender.sendMessage("Diver not registered!");
					return false;
			}//check for a specific player
			
			else 
				sender.sendMessage("Invalid Synatax! Please use /checkTank <player>");
			return true;
		}//Check the PSI of the tank
		
		else if (cmd.getName().equalsIgnoreCase("makeDiveLight"))
		{
			if (args.length == 0)
			{
				String playerName = sender.getName();
				
				for (int x = 0; x < divers.size(); x++)
				{
					if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName()))
					{
						divers.get(x).getDiveLight();
						return true;
					}//find the player and send tank information.
				}//end for x
				
				sender.sendMessage("Diver not registered!");
				return false;
			}//if no 
			
			else if (args.length == 1)
			{
				String playerName = args[0];
				
				for (int x = 0; x < divers.size(); x++)
				{
					if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName()))
					{
						divers.get(x).getDiveLight();
						return true;
					}//find the player and send tank information.
				}//end for x
				
				sender.sendMessage("Diver not registered!");
					return false;
			}//check for a specific player
			
			else 
				sender.sendMessage("Invalid Synatax! Please use /makeDiveLight <player>");
			return true;
		}//make dive light
		
		else if (cmd.getName().equalsIgnoreCase("enableDiveLight"))
		{
			if (args.length != 0)
				sender.sendMessage("No player arguments required!");
			else
			{
				String playerName = sender.getName();
				
				for (int x = 0; x < divers.size(); x++)
				{
					if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName()))
					{
						divers.get(x).enableLight();
						return true;
					}//find the player and send tank information.
				}//end for x
				
				sender.sendMessage("Diver not registered!");
				return false;
			}
		}
		
		else if (cmd.getName().equalsIgnoreCase("disableDiveLight"))
		{
			if (args.length != 0)
				sender.sendMessage("No player arguments required!");
			else
			{
				String playerName = sender.getName();
				
				for (int x = 0; x < divers.size(); x++)
				{
					if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName()))
					{
						divers.get(x).disableLight();
						return true;
					}//find the player and send tank information.
				}//end for x
				
				sender.sendMessage("Diver not registered!");
				return false;
			}
		}
		
		else if (cmd.getName().equalsIgnoreCase("checkDiveLight"))
		{
			if (args.length == 0)
			{
				String playerName = sender.getName();
				
				for (int x = 0; x < divers.size(); x++)
				{
					if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName()))
					{
						sender.sendMessage(divers.get(x).getLightRemaining());
						return true;
					}//find the player and send tank information.
				}//end for x
				
				sender.sendMessage("Diver not registered!");
				return false;
			}//if no 
			
			else if (args.length == 1)
			{
				String playerName = args[0];
				
				for (int x = 0; x < divers.size(); x++)
				{
					if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName()))
					{
						sender.sendMessage(divers.get(x).getLightRemaining());
						return true;
					}//find the player and send tank information.
				}//end for x
				
				sender.sendMessage("Diver not registered!");
					return false;
			}//check for a specific player
			
			else 
				sender.sendMessage("Invalid Synatax! Please use /checkTank <player>");
			return true;
		}//Check dive light
		
		return false; //no commands made
	}
}
