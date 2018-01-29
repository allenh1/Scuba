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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;


public class Diver extends Thread
{
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
        World w = player.getWorld();
        Location l = player.getLocation();
        l.setY(l.getY() + 1);
        return !notRegistered &&
            (w.getBlockAt(l).getType() == Material.WATER ||
             w.getBlockAt(l).getType() == Material.STATIONARY_WATER);
        
    }//check if the player is underwater

    public void run()
    {
        for (; true; ) {
            if (notRegistered && Bukkit.getServer().getPlayer(player_offline.getName()) != null) {
                player = Bukkit.getServer().getPlayer(player_offline.getName());
                Bukkit.getServer().getLogger().info("Diver " + player.getName() +" is now online.");
                notRegistered = false;
            } else if (!notRegistered) {
                if (diveLight.lightOn() && !isUnderwater())
                    diveLight.turnOff();

                if (diveLight.lightOn() && diveLight.isEnabled()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 5000, 2, true));
                    try {
                        sleep(500);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

                else {
                    if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    try {
                        sleep(500);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                if (!isUnderwater() && !tank.isFull()) {
                    tank.fill(0.0001);
                }
                if (isUnderwater() && !diveLight.deadBattery() && diveLight.isTurnedOn())
                    diveLight.turnOn();
                if (!tank.isEmpty() && isUnderwater()) {
                    /* 5 percent drain if underwater and tank has air */
                    tank.drain(0.0005); player.setRemainingAir(400);
                }
            }
            try {
                sleep(100);
            } catch (java.lang.InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void fillTank()
    { tank.fill(); }

    public String getFillLevel()
    { return tank.toString(); }

    public boolean hasLightEnabled()
    { return diveLight.isEnabled(); }

    public String getLightRemaining()
    { return diveLight.getTimeLeft(); }

    public void enableLight()
    { diveLight.switch_on(); }

    public void disableLight()
    { diveLight.switch_off(); }

    public void getDiveLight()
    {
        if (hasLightEnabled())
            return;

        final int glowDustID = 348;
        final int costOfLight = 5;

        ItemStack glowMin = new ItemStack(glowDustID, costOfLight);
        if (player.getInventory().contains(glowMin)) {
            Bukkit.broadcastMessage("5 Glowstone Dust will be removed from your inventory!");
            player.getInventory().remove(glowMin);
            diveLight.enable(); //start the divelight thread
        } else {
            Bukkit.broadcastMessage("You need a stack of exactly 5 Glowstone Dust to make a Dive Light.");
        }
    }//Turn on the dive light
}
