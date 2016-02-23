package com.gmail.gugol95.cicero;

import java.io.*;
import java.util.*;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Cicero extends JavaPlugin{
	public static Cicero main;
	ArrayList<Town> Towns = new ArrayList<Town>();
	ArrayList<CiceroPlayer> PlayerList = new ArrayList<CiceroPlayer>();
	HashMap<String, Boolean> townMap = new HashMap<String, Boolean>();
	Town Spawn = null, t;															//Spawn del server
	UUID player;
	CiceroPlayer cp = null;	
	File townFile, playerFile;	
	String pluginName = "Cicero", line, lineF, lineL, name;
	World w;
	Location loc;
	double x, y, z;
	int ciceroTime = 1, nTown = 0, nPlayer = 0, i, j, notVisited;								//Ore dopo le quali un utente viene disabilitato dal cicero, Numero di città registrate e Numero totale di utenti abilitati al cicero
	long date;
	
	@Override
	public void onEnable() {
		main = this;
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		townFile = new File(this.getDataFolder().getPath() + "/Towns.trs");
		playerFile = new File(this.getDataFolder().getPath() + "/Players.trs");
		getCommand("cicero").setExecutor(new CommandHandler(main));
		
		if(!getDataFolder().exists()){
			saveDefaultConfig();
			
			try{
				if(townFile.createNewFile()){
			        System.out.println("File is created!");
				}
				else{
			        System.out.println("File already exists.");
			    }
			}
			catch(IOException e){
				e.printStackTrace();
			}
			
			try{
				if(playerFile.createNewFile()){
			        System.out.println("File is created!");
				}
				else{
			        System.out.println("File already exists.");
			    }
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		else{
			pluginName = getConfig().getString("PLUGINNAME");
			ciceroTime = getConfig().getInt("CICEROTIME");
			
			try(BufferedReader tf = new BufferedReader(new FileReader(townFile))) {  	
			   	if((line = tf.readLine()) != null){
			   		lineF = lineL = line;
			   		lineF = lineF.substring(0, line.lastIndexOf(":"));
				   	lineL = lineL.substring(line.lastIndexOf(":") + 1, line.length());
				   	
				   	if(lineF.equals("Numero di Citta")){
			    		nTown = Integer.parseInt(lineL);
			    	}
				   	
				   	line = tf.readLine();
				   	for(i=0;i<nTown+1 && line != null; i++){
				   		for(j=0; j<5 && line != null; j++){
				   			if(line != null){
					   			lineF = lineL = line;
					   			lineF = lineF.substring(0, line.lastIndexOf(":"));
					   			lineL = lineL.substring(line.lastIndexOf(":") + 1, line.length());
							   	
					   			switch(lineF){
					   				case "Spawn":
					   					name = lineL;
					   					break;
					   				case "Name":
					   					name = lineL;
					   					break;
					   				case "world":
					   					w = Bukkit.getWorld(lineL);
					   					break;
					   				case "x":
					   					x = Double.parseDouble(lineL);
					   					break;
					   				case "y":
					   					y = Double.parseDouble(lineL);
					   					break;
					   				case "z":
					   					z = Double.parseDouble(lineL);
					   					loc = new Location(w, x, y, z);
								   		if(name.equals("Spawn")){
									   		Spawn = new Town(name, loc);
								   		}
								   		else{
								   			t = new Town(name, loc);
								   			Towns.add(t);
								   		}
					   					break;
					   			}
				   			}
				   			line = tf.readLine();
				   		}	   		
				   	}	   	
			   	}  
			    tf.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			
			try(BufferedReader pf = new BufferedReader(new FileReader(playerFile))) {
				if ((line = pf.readLine()) != null){
					lineF = lineL = line;
					lineF = lineF.substring(0, line.lastIndexOf(":"));
					lineL = lineL.substring(line.lastIndexOf(":") + 1, line.length());
			    	
			    	if(lineF.equals("Numero di Utenti")){
			    		nPlayer = Integer.parseInt(lineL);
			    	}
			    	
			    	line = pf.readLine();
			    	for(i=0; i<nPlayer && line != null; i++){
			    		for(j=0; j<nTown+4 && line != null; j++){
			    			if(line != null){
				    			lineF = lineL = line;
				    			lineF = lineF.substring(0, line.lastIndexOf(":"));
				    			lineL = lineL.substring(line.lastIndexOf(":") + 1, line.length());
				    			switch(lineF){
				    				case "Name":
				    					break;
				    				case "UUID":
				    					player = UUID.fromString(lineL);
				    					break;
				    				case "Date":
				    					date = Long.parseLong(lineL);
				    					break;
				    				case "notVisited":
				    					notVisited = Integer.parseInt(lineL);
				    					break;
				    				default:
				    					townMap.put(lineF, Boolean.valueOf(lineL));
				    			}
			    			}
			    			line = pf.readLine();
			    		}
    					cp = new CiceroPlayer(player, date, notVisited, townMap);
			    		PlayerList.add(cp);
			    	}
			    }		    
			    pf.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}	
		}
	}

	@Override
	public void onDisable() {
		townFile = new File(this.getDataFolder().getPath() + "/Towns.trs");
		playerFile = new File(this.getDataFolder().getPath() + "/Players.trs");
		
		int i;
		CiceroPlayer p;
		
		getConfig().set("PLUGINNAME", pluginName);
		getConfig().set("CICEROTIME", ciceroTime);
		saveDefaultConfig();
		
		try(FileWriter ew = new FileWriter(townFile, false)){
			ew.write("");
			ew.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		try(FileWriter tfw = new FileWriter(townFile, true)){
			tfw.write("Numero di Citta:" + nTown + "\n");
			tfw.write("Spawn:" + Spawn.getName() + "\nworld:" + Spawn.getLocation().getWorld().getName() + "\nx:" 
					+ Spawn.getLocation().getX() + "\ny:" + Spawn.getLocation().getY() + "\nz:" + Spawn.getLocation().getZ() + "\n");
			for(i=0; i<nTown; i++){
				tfw.write("Name:" + Towns.get(i).getName() + "\nworld:" + Towns.get(i).getLocation().getWorld().getName() + "\nx:" + Towns.get(i).getLocation().getX() 
						+ "\ny:" + Towns.get(i).getLocation().getY() + "\nz:" + Towns.get(i).getLocation().getZ() + "\n");
			}
			tfw.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		try(FileWriter ew = new FileWriter(playerFile, false)){
			ew.write("");
			ew.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		try(FileWriter pfw = new FileWriter(playerFile, true)){
			pfw.write("Numero di Utenti:" + nPlayer);
			
			for(i=0; i<nPlayer; i++){
				p = PlayerList.get(i);
				pfw.write("\nName:" + p.getOfflinePlayer().getName() + "\nUUID:" + p.getOfflinePlayer().getUniqueId() + "\nDate:" + p.getFirstJoined() 
						+ "\nnotVisited:" + p.notVisitedYet);
				for(j=0; j<nTown; j++){
					pfw.write("\n" + Towns.get(j).getName() + ":" + p.visited.get(Towns.get(j).getName()));
				}
			}	
			pfw.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public final class JoinListener implements Listener {
		int i;
		@EventHandler
		public void onLogin(PlayerJoinEvent event) {
			if(!event.getPlayer().hasPlayedBefore()) {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "[" + pluginName + "]" + ChatColor.GREEN + "Benvenuto nel server da Cicero!");
				cp= new CiceroPlayer(event.getPlayer().getUniqueId(), System.currentTimeMillis(), nTown, Towns);
				PlayerList.add(cp);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "[" + pluginName + "]" + ChatColor.GREEN + "Sei stato aggiuto a Cicero!"); getLogger().info(event.getPlayer().getName() + " e' stato aggiuto a Cicero!");
				nPlayer++;
			}
			else{
				if(!PlayerList.isEmpty()){
					for(i=0; i<nPlayer; i++){
						if(event.getPlayer().equals(PlayerList.get(i))){
							if(System.currentTimeMillis() - PlayerList.get(i).getFirstJoined() > 3600000 * ciceroTime){
								PlayerList.remove(PlayerList.get(i));
								nPlayer--;
							}
						}
					}
				}
			}
		}
	}	
}
