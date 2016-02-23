package com.gmail.gugol95.cicero;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CiceroPlayer {
	UUID player;
	long firstJoin;
	int notVisitedYet;
	HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
	
	CiceroPlayer(UUID player, long firstJoin, int notVisitedYet, ArrayList<Town> towns){
		int i=0;
		this.player=player;
		this.firstJoin=firstJoin;
		this.notVisitedYet=notVisitedYet;  
		for(i=0; i<notVisitedYet; i++){
			this.visited.put(towns.get(i).getName(), false);
		}
	}
	
	CiceroPlayer(UUID player, long firstJoin, int notVisitedYet, HashMap<String, Boolean> towns){
		this.player=player;
		this.firstJoin=firstJoin;
		this.notVisitedYet=notVisitedYet;
		visited.putAll(towns);
	}
	
	boolean hasVisited(Town town){
		return visited.get(town.getName());
	}
	
	Player getPlayer(){
		return Bukkit.getPlayer(player);
	}
	
	OfflinePlayer getOfflinePlayer(){
		return Bukkit.getOfflinePlayer(player);
	}
	
	long getFirstJoined(){
		return this.firstJoin;
	}
}
