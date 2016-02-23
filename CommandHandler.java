package com.gmail.gugol95.cicero;

import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor{
	
	private Cicero cicero = null;
	
	CommandHandler(Cicero main){
		this.cicero = main;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p;
		Town town;
		CiceroPlayer cp = null;
		UUID u;
		int i=0, j=0;
		
		if (cmd.getName().equalsIgnoreCase("cicero")){
			if(args.length<1){
				if(sender instanceof Player){
					p = Bukkit.getPlayer(sender.getName());
					u = p.getUniqueId();
					if(!cicero.PlayerList.isEmpty()){
						for(i=0; i<cicero.nPlayer; i++){
							if(cicero.PlayerList.get(i).getOfflinePlayer().getUniqueId().equals(u)){ 
								if(System.currentTimeMillis() - cicero.PlayerList.get(i).getFirstJoined() <= 3600000 * cicero.ciceroTime){ 
									for(j=0; j<cicero.nTown; j++){
										if(!(cicero.PlayerList.get(i).hasVisited(cicero.Towns.get(j)))){
											sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "Stai visitando " + cicero.Towns.get(j).getName() + "!");
											p.teleport(cicero.Towns.get(j).getLocation());
											Bukkit.broadcastMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + p.getName() + " sta visitando " 
													+ cicero.Towns.get(j).getName() + "!");
											cicero.PlayerList.get(i).visited.put(cicero.Towns.get(j).getName(), true);
											cicero.PlayerList.get(i).notVisitedYet--;
											return true;
										}
									}
									if(cicero.PlayerList.get(i).notVisitedYet == 0){
										p.teleport(cicero.Spawn.getLocation());
										p.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "Hai visitato tutte le citta', sei stato riportato allo spawn!");
										cicero.PlayerList.remove(cicero.PlayerList.get(i));
										cicero.nPlayer--;
									}
								}
								else{
									p.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "Il tuo tempo di abilitazione a Cicero e' scaduto!");
									cicero.PlayerList.remove(cicero.PlayerList.get(i));
									cicero.nPlayer--;
								}
								return true;
							}
						}
					}
					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Non sei abilitato a Cicero!");
				}
				else{
					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Devi essere un utente online per eseguire questo comando");
				}
			}
			else{
				switch(args[0].toLowerCase()){
				 	case "player":
				 		if(args.length>1){
					 		switch(args[1].toLowerCase()){
					 			case "add":	               //DOVREBBE ESSERE FINITO E APPOSTO
					 				if(sender.hasPermission("cicero.player.add")){
						 				if(args.length>2){
						 					if(Bukkit.getOfflinePlayer(args[2]).isOnline()){
						 						p = Bukkit.getPlayer(args[2]);
						 						u = p.getUniqueId();
						 						cp = new CiceroPlayer(u, System.currentTimeMillis(), cicero.nTown, cicero.Towns);
							 					if(!cicero.PlayerList.isEmpty()){
								 					for(i=0; i<cicero.nPlayer; i++){
									 					if(u.equals(cicero.PlayerList.get(i).getOfflinePlayer().getUniqueId())){
									 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "L'utente " + Bukkit.getPlayer(u).getName() + " e' gia' presente!");
									 						return true;
									 					}
								 					}
							 					}
							 					cicero.PlayerList.add(cp);
							 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "L'utente " + Bukkit.getPlayer(u).getName() + " e' stato aggiunto a Cicero!");
							 					p.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "Sei stato aggiunto al Cicero!");
							 					cicero.nPlayer++;
						 					}
						 					else{
						 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Utente non registrato o non online!");
						 					}
						 				}
						 				else{
						 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Usa /cicero player add <nome>");
						 				}
					 				}
					 				else{
					 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Non hai i permessi necessari!");
					 				}
					 				break;
					 			case "add+":               //DOVREBBE ESSERE FINITO E APPOSTO
					 				if(sender.hasPermission("cicero.player.*")){
						 				if(args.length>2){
						 					if(Bukkit.getOfflinePlayer(args[2]).isOnline()){
							 					u=Bukkit.getPlayer(args[2]).getUniqueId();
							 					cp= new CiceroPlayer(Bukkit.getPlayer(u).getUniqueId(), System.currentTimeMillis(), cicero.nTown, cicero.Towns);
							 					cicero.PlayerList.add(cp);
							 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "L'utente " + args[2] + " e' stato aggiunto a Cicero!");
							 					cicero.nPlayer++;
						 					}
						 					else{
						 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Utente non registrato o non online!");
						 					}
						 				}
						 				else{
						 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Usa /cicero player add+ <nome>");
						 				}
					 				}
						 			else{
						 				sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Non hai i permessi necessari!");
						 			}
					 				break;
					 			case "remove":             //DOVREBBE ESSERE FINITO E APPOSTO
					 				if(sender.hasPermission("cicero.player.remove")){
						 				if(args.length>2){
						 					if(Bukkit.getOfflinePlayer(args[2]).hasPlayedBefore()){
						 						u=Bukkit.getOfflinePlayer(args[2]).getUniqueId();
						 						if(!cicero.PlayerList.isEmpty()){
								 					for(i=0; i<cicero.nPlayer; i++){
									 					if(Bukkit.getOfflinePlayer(args[2]).getUniqueId().equals(cicero.PlayerList.get(i).getOfflinePlayer().getUniqueId())){
									 						cicero.PlayerList.remove(cicero.PlayerList.get(i));
									 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "L'utente " + args[2] + " e' stato rimosso da Cicero!");
									 						cicero.nPlayer--;
									 						return true;
									 					}
								 					}	
								 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "L'utente " + args[2] + " non e' presente!");
						 						}
						 						else{
						 							sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.YELLOW + "Al momento nessun utente e' abilitato!");
						 						}
						 					}
						 					else{
						 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Utente non registrato!");
						 					}
						 				}
						 				else{
						 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Usa /cicero player remove <nome>");			 					
						 				}
					 				}
						 			else{
						 				sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Non hai i permessi necessari!");
						 			}
					 				break;
					 			case "remove+":             //DOVREBBE ESSERE FINITO E APPOSTO
					 				if(sender.hasPermission("cicero.player.remove")){
					 					if(args.length>2){
					 						u=UUID.fromString(args[2]);
					 						if(!cicero.PlayerList.isEmpty()){
						 						for(i=0; i<cicero.nPlayer; i++){
								 					if(u.equals(cicero.PlayerList.get(i).getPlayer().getUniqueId())){
								 						cicero.PlayerList.remove(cicero.PlayerList.get(i));
								 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "L'utente con UUID: " + args[2] + " e' stato rimosso da Cicero!");
								 						cicero.nPlayer--;
								 						return true;
								 					}
							 					}	
							 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "L'utente con UUID: " + args[2] + " non e' presente!");
					 						}
					 					}
					 				}
					 				else{
						 				sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Non hai i permessi necessari!");
						 			}
					 				break;
					 			case "list":				//DOVREBBE ESSERE FINITO E APPOSTO
					 				if(sender.hasPermission("cicero.player.list")){
					 					if(cicero.nPlayer!=0){
					 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "Utenti abilitati a Cicero:");
						 					for(i=0; i<cicero.nPlayer; i++){
						 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + cicero.PlayerList.get(i).getOfflinePlayer().getName() + ";");
						 					}
					 					}
					 					else{
					 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.YELLOW + "Al momento nessun utente e' abilitato!");
					 					}
					 				}
						 			else{
						 				sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Non hai i permessi necessari!");
						 			}
					 				break;
					 			default:
					 				sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED 
					 						+ "Usa:\n- cicero player add <nome>;\n- cicero player add+ <nome>;\n- cicero player remove <nome>;\n- cicero player list;\n");
					 		}
				 		}
				 		else{
				 			sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED 
				 					+ "Usa:\n- cicero player add <nome>;\n- cicero player add+ <nome>;\n- cicero player remove <nome>;\n- cicero player list;\n");
				 		}
				 		break;
				 	case "town":
				 		if(args.length>1){
					 		switch(args[1].toLowerCase()){
					 			case "add":					//DOVREBBE ESSERE FINITO E APPOSTO
					 				if(sender.hasPermission("cicero.town.add") && sender instanceof Player){
						 				if(args.length>2){
						 					if(!cicero.Towns.isEmpty()){
							 					for(j=0; j<cicero.nTown; j++){
							 						if(cicero.Towns.get(j).getName().equals(args[2])){
							 							sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "La citta' e' gia' presente!");
							 							return true;
							 						}
							 					}
						 					}
						 					p = Bukkit.getPlayer(sender.getName());
						 					town = new Town(args[2], p.getLocation());
						 					cicero.Towns.add(town);
						 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "La citta' " + args[2] + " e' stata aggiunta a Cicero!");
						 					cicero.nTown++;
						 				}
						 				else{
						 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Usa /cicero town add <nome>");
						 				}
					 				}
				 					else{
				 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Non hai i permessi necessari!");
				 					}
					 				break;
					 			case "add+":					//DOVREBBE ESSERE FINITO E APPOSTO
					 				if(sender.hasPermission("cicero.town.*")){
						 				if(args.length>2){
						 					p = Bukkit.getPlayer(sender.getName());
					 						town = new Town(args[2], p.getLocation());
					 						if(!cicero.Towns.isEmpty()){
						 						for(j=0; j<cicero.nTown; j++){
							 						if(cicero.Towns.get(j).getName().equals(args[2])){
							 							cicero.Towns.get(j).loc=p.getLocation();
							 							sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "La citta' " + args[2] + " e' stata aggiornata!");
							 							return true;
							 						}
							 					}
					 						}
					 						cicero.Towns.add(town);
						 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "La citta' " + args[2] + " e' stata aggiunta a Cicero!");
						 					cicero.nTown++;
						 				}
						 				else{
						 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Usa /cicero town add+ <nome>");
						 				}
				 					}
			 						else{
			 							sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Non hai i permessi necessari!");
			 						}
					 				break;
					 			case "remove":					//DOVREBBE ESSERE FINITO E APPOSTO
					 				if(sender.hasPermission("cicero.town.remove")){
						 				if(args.length>2){
						 					if(!cicero.Towns.isEmpty()){
							 					for(j=0; j<cicero.nTown; j++){
							 						if(cicero.Towns.get(j).getName().equals(args[2])){
							 							cicero.Towns.remove(cicero.Towns.get(j));
							 							sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "La citta' "+ args[2] + " e'stata rimossa da Cicero!");
							 							cicero.nTown--;
							 							return true;
							 						}
							 					}
						 					}
						 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "La citta' " + args[2] + " non e' presente!");
						 				}
						 				else{
						 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Usa /cicero town remove <nome>");
						 				}
									}
	 								else{
	 									sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Non hai i permessi necessari!");
	 								}
					 				break;
					 			case "list":					//DOVREBBE ESSERE FINITO E APPOSTO
					 				if(sender.hasPermission("cicero.town.list")){
					 					if(cicero.nTown!=0){
					 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "Citta' raggiunte da Cicero:");
						 					for(j=0; j<cicero.nTown; j++){
						 						sender.sendMessage(ChatColor.GREEN + cicero.Towns.get(j).getName() + ChatColor.GREEN + ": "
						 								+ ChatColor.WHITE + "(" + cicero.Towns.get(j).getLocation().getWorld().getName() + ChatColor.WHITE + ", "
						 								+ ChatColor.WHITE + cicero.Towns.get(j).getLocation().getBlockX() + ChatColor.WHITE + ", "
						 								+ ChatColor.WHITE + cicero.Towns.get(j).getLocation().getBlockY() + ChatColor.WHITE + ", "
						 								+ ChatColor.WHITE + cicero.Towns.get(j).getLocation().getBlockZ()
						 								+ ");");
						 					}
					 					}
					 					else{
					 						sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.YELLOW + "Al momento nessuna citta' e' raggiunta da Cicero!");
					 					}
									}
 									else{
 										sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Non hai i permessi necessari!");
 									}
					 				break;
					 			default:
					 				sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Usa:\n- cicero town add <nome>;\n- cicero town add+ <nome>;\n- cicero town remove <nome>;\n- cicero town list;\n");
					 		}
				 		}
				 		else{
			 				sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.RED + "Usa:\n- cicero town add <nome>;\n- cicero town add+ <nome>;\n- cicero town remove <nome>;\n- cicero town list;\n");

				 		}
				 		break;
				 	case "setspawn":
		 				if(sender.hasPermission("cicero.*")){
		 					p=Bukkit.getPlayer(sender.getName());
		 					cicero.Spawn = new Town("Spawn", p.getLocation());
		 					sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "Spawn impostato!");
		 				}
		 				break;
				 	case "help":								//DOVREBBE ESSERE FINITO E APPOSTO
				 		sender.sendMessage(ChatColor.DARK_RED + "[" + cicero.pluginName + "] " + ChatColor.GREEN + "Comandi per il plguin Cicero:\n");
				 		sender.sendMessage(ChatColor.GREEN + "- cicero: " + ChatColor.WHITE + "se l'utente e' abilitato, tp a una nuova citta';\n");
				 		sender.sendMessage(ChatColor.GREEN + "- cicero help: " + ChatColor.WHITE + "mostra i comandi del cicero;\n");
				 		if(sender.hasPermission("cicero.player.add")) sender.sendMessage(ChatColor.GREEN + "- cicero player add <name>: " + ChatColor.WHITE + "aggiunge un nuovo utente alla lista cicero;\n");
				 		if(sender.hasPermission("cicero.player.*")) sender.sendMessage(ChatColor.GREEN + "- cicero player add+ <name>: " + ChatColor.WHITE + "forza l'aggiunta di un utente alla lista cicero (deve essere rimosso manualmente);\n");
				 		if(sender.hasPermission("cicero.player.remove")) sender.sendMessage(ChatColor.GREEN + "- cicero player remove <name>: " + ChatColor.WHITE + "rimuove un utente dalla lista cicero;\n");
				 		if(sender.hasPermission("cicero.player.remove")) sender.sendMessage(ChatColor.GREEN + "- cicero player remove+ <UUID>: " + ChatColor.WHITE + "rimuove un utente dalla lista cicero tramite UUID;\n");
				 		if(sender.hasPermission("cicero.player.list")) sender.sendMessage(ChatColor.GREEN + "- cicero player list: " + ChatColor.WHITE + "visualizza utenti abilitati;\n");
				 		if(sender.hasPermission("cicero.*")) sender.sendMessage(ChatColor.GREEN + "- cicero setspawn: "+ ChatColor.WHITE + "posiziona lo spawn (ultima citta' raggiunta dopo aver esplorato tutte le altre)");
				 		if(sender.hasPermission("cicero.town.add")) sender.sendMessage(ChatColor.GREEN + "- cicero town add <name>: " + ChatColor.WHITE + "aggiunge una citta' al cicero alle coordinate correnti, se <name> e' gia' presente da errore;\n");
				 		if(sender.hasPermission("cicero.town.*")) sender.sendMessage(ChatColor.GREEN + "- cicero town add+ <name>: " + ChatColor.WHITE + "aggiunge una citta' al cicero alle coordinate correnti, se <name> e' gia' presente sovrascrive le coordinate;\n");
				 		if(sender.hasPermission("cicero.town.remove")) sender.sendMessage(ChatColor.GREEN + "- cicero town remove <name>: " + ChatColor.WHITE + "rimuove una citta' dal cicero;\n");
				 		if(sender.hasPermission("cicero.town.list")) sender.sendMessage(ChatColor.GREEN + "- cicero town list: " + ChatColor.WHITE + "visualizza citta' aggiunte al cicero;");
				 		break;
				}
			}
			return true;
		}
		return false; 
	}
}
