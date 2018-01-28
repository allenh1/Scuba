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
