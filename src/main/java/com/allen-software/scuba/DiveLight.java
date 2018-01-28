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
        for (; secondsLeft > 0; ) {
            if (isOn) {
                try {
                    sleep(1000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } // sleep for one second
				
                secondsLeft--;
            } // only drain battery when on.
        }
		
        enabled = false; isOn = false; secondsLeft = 0;
    }//end void
}
