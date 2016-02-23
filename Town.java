package com.gmail.gugol95.cicero;

import org.bukkit.Location;

public class Town {
	String name;
	Location loc;
	
	Town(String name, Location loc){
		this.name=name;
		this.loc=loc;
	}
	
	String getName(){
		return this.name;
	}
	
	Location getLocation(){
		return this.loc;
	}
}
