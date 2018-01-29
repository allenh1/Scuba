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

import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.*;
import org.bukkit.event.player.*;


public final class Scuba extends JavaPlugin
{
    private HashMap<String, Diver> divers;

    @Override
    public void onEnable()
    {
        getLogger().info("SCUBA plugin enabled!");
        divers = new HashMap<String, Diver>();
        /** This code updates the players tank **/
        for (OfflinePlayer p : getServer().getOfflinePlayers()) {
            String name = p.getName();
            Diver d = new Diver(name);
            d.start();                
            divers.put(p.getName(), d);
            getLogger().info("Added new Diver: " + name);
        }
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
        String new_name = event.getPlayer().getName();
        Diver nd = new Diver(new_name); nd.start();
        divers.put(new_name, nd);
        getLogger().info("Added new Diver: " + new_name);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        getLogger().info("SCUBA: Beginning clean logoff...");
        /**
         * This code allows a player to sign off without crashing
         * the plugin. Not my wisest coding practice...
         */
        //find the correct diver
        String to_find = event.getPlayer().getName();
        Diver d = divers.remove(to_find);
        if (d != null)
            d.stop();
        else
            getLogger().info("Someone I don't know just left...");
    }//handles when a player leaves the server (to avoid a thread exception)

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("fillTank")) {
            String name = args.length == 0 ? sender.getName() : args[0];
            Diver d = divers.get(name);
            if (null == d) {
                sender.sendMessage("Diver not registered!");
                return false;
            }
            d.fillTank();
            return true;
        } else if (cmd.getName().equalsIgnoreCase("checkTank")) {
            String name = args.length == 0 ? sender.getName() : args[0];
            Diver d = divers.get(name);
            if (null == d) {
                sender.sendMessage("Diver not registered!");
                return false;
            }
            sender.sendMessage(d.getFillLevel());
            return true;
        } else if (cmd.getName().equalsIgnoreCase("makeDiveLight")) {
            if (args.length != 0) {
                sender.sendMessage("Invalid Arguments! Please use /makeDiveLight");
                return false;
            }
            // TODO(allenh1): it would be cool to gift a dive light.
            Diver d = divers.get(sender.getName());
            if (null == d) {
                sender.sendMessage("Diver not registered!");
                return false;
            }
            d.getDiveLight();
            return true;
        } else if (cmd.getName().equalsIgnoreCase("enableDiveLight")) {
            if (args.length != 0) {
                sender.sendMessage("Invalid Arguments! Please use /enableDiveLight!");
                return false;
            }
            Diver d = divers.get(sender.getName());
            if (null == d) {
                sender.sendMessage("Diver not registered");
                return false;
            }
            d.enableLight();
            return true;
        } else if (cmd.getName().equalsIgnoreCase("disableDiveLight")) {
            if (args.length != 0) {
                sender.sendMessage("Invalid Arguments! Please use /disablediveLight!");
                return false;
            }
            Diver d = divers.get(sender.getName());
            if (null == d) {
                sender.sendMessage("Diver not registered");
                return false;
            }
            d.disableLight();
            return true;
        } else if (cmd.getName().equalsIgnoreCase("checkDiveLight")) {
            String name = args.length == 0 ? sender.getName() : args[0];
            Diver d = divers.get(name);
            if (null == d) {
                sender.sendMessage("Diver not registered!");
                return false;
            }
            sender.sendMessage(d.getLightRemaining());
            return true;
        }
        sender.sendMessage("Invalid Synatax! Please use /checkDiveLight [player]");
        return false;
    }
}
