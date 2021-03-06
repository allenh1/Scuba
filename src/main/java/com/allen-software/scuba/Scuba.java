//    SCUBA: A SCUBA diving plugin for Minecraft
//    Copyright (C) 2018  Hunter L. Allen
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License along
//    with this program; if not, write to the Free Software Foundation, Inc.,
//    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

package com.allen_software.scuba;

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
        for (OfflinePlayer p : getServer().getOfflinePlayers()) {
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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        getLogger().info("Scuba: Beginning clean logoff...");
        /**
         * This code allows a player to sign off without crashing
         * the plugin. Not my wisest coding practice...
         */
        //find the correct diver
        int index = 0;
        while (divers.get(index).getPlayerName() != event.getPlayer().getName())
            index++;
        divers.get(index).stop();
        divers.remove(index);
    }//handles when a player leaves the server (to avoid a thread exception)

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("fillTank")) {
            if (args.length != 1) {
                sender.sendMessage("Invalid Arguments. Try /fillTank <player>");
                return false;
            }//end if

            int index = 0;

            for (index = 0; index < divers.size(); index++) {
                if (divers.get(index).getPlayerName().equalsIgnoreCase(args[0]))
                    break;
            }//find the diver
            if (index == divers.size() - 1 && index != 0) {
                sender.sendMessage("Diver not registered!");
                return false;
            }//end if

            divers.get(index).fillTank();

            return true;
        } else if (cmd.getName().equalsIgnoreCase("checkTank")) {
            if (args.length == 0) {
                String playerName = sender.getName();
                for (int x = 0; x < divers.size(); x++) {
                    if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName())) {
                        sender.sendMessage(divers.get(x).getFillLevel());
                        return true;
                    }//find the player and send tank information.
                }//end for x

                sender.sendMessage("Diver not registered!");
                return false;
            } else if (args.length == 1) {
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
        } else if (cmd.getName().equalsIgnoreCase("makeDiveLight")) {
            if (args.length == 0) {
                String playerName = sender.getName();
                for (int x = 0; x < divers.size(); x++) {
                    if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName())) {
                        divers.get(x).getDiveLight();
                        return true;
                    }//find the player and send tank information.
                }//end for x
                sender.sendMessage("Diver not registered!");
                return false;
            } else if (args.length == 1) {
                String playerName = args[0];
                for (int x = 0; x < divers.size(); x++) {
                    if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName())) {
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
        } else if (cmd.getName().equalsIgnoreCase("enableDiveLight")) {
            if (args.length != 0)
                sender.sendMessage("No player arguments required!");
            else {
                String playerName = sender.getName();
                for (int x = 0; x < divers.size(); x++) {
                    if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName())) {
                        divers.get(x).enableLight();
                        return true;
                    }//find the player and send tank information.
                }//end for x
                sender.sendMessage("Diver not registered!");
                return false;
            }
        } else if (cmd.getName().equalsIgnoreCase("disableDiveLight")) {
            if (args.length != 0)
                sender.sendMessage("No player arguments required!");
            else {
                String playerName = sender.getName();
                for (int x = 0; x < divers.size(); x++) {
                    if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName())) {
                        divers.get(x).disableLight();
                        return true;
                    }//find the player and send tank information.
                }//end for x
                sender.sendMessage("Diver not registered!");
                return false;
            }
        }
        else if (cmd.getName().equalsIgnoreCase("checkDiveLight")) {
            if (args.length == 0) {
                String playerName = sender.getName();
                for (int x = 0; x < divers.size(); x++) {
                    if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName())) {
                        sender.sendMessage(divers.get(x).getLightRemaining());
                        return true;
                    }//find the player and send tank information.
                }//end for x
                sender.sendMessage("Diver not registered!");
                return false;
            } else if (args.length == 1) {
                String playerName = args[0];
                for (int x = 0; x < divers.size(); x++) {
                    if (playerName.equalsIgnoreCase(divers.get(x).getPlayerName())) {
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
