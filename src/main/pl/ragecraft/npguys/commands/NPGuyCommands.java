package pl.ragecraft.npguys.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import pl.ragecraft.npguys.ElementManager;
import pl.ragecraft.npguys.NPGuy;
import pl.ragecraft.npguys.NPGuyData;
import pl.ragecraft.npguys.DialogueManager;
import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.conversation.PlayerMessage;
import pl.ragecraft.npguys.exception.ActionMissingException;
import pl.ragecraft.npguys.exception.InvalidCommandException;
import pl.ragecraft.npguys.exception.NPGuyAlreadyExistsException;
import pl.ragecraft.npguys.exception.NPGuyNotFoundException;
import pl.ragecraft.npguys.exception.RequirementMissingException;
import pl.ragecraft.npguys.requirement.Requirement;

public class NPGuyCommands implements CommandExecutor {
	DialogueCommands dialogueCommands = new DialogueCommands();
	private final String UNKNOWN_COMMAND = "Unknown command. Type /npguy for command list.";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		NPGuyData data = null;
		switch (args.length) {
		case 0:
			//TODO Display command list
			sender.sendMessage("Placeholder - command list :)");
			return true;
		case 1:
			try {
				data = DialogueManager.getData(args[0]);
				
				StringBuilder display = new StringBuilder();
				display.append("npguy: ").append(data.name).append('\n');
				display.append("welcome_message: ").append(data.welcomeMessage).append('\n');
				display.append("dialogues: ");
				for (String dialogue : data.dialogues.keySet()) {
					display.append('\n').append("- ").append(dialogue);
				}
				
				sender.sendMessage(display.toString());
			}
			catch (NPGuyNotFoundException e) {
				sender.sendMessage(e.getMessage());
			}
			return true;
		default:
			if (args[1].equalsIgnoreCase("create")) {
				if (sender.hasPermission("npguys.management.npc")) {
					if (args.length != 2) {
						sender.sendMessage(UNKNOWN_COMMAND);
						return true;
					}
					data = new NPGuyData();
					
					String name = args[0];
					data.name = name;
					data.welcomeMessage = "default";
					
					data.dialogues.put("default", DialogueManager.getDefaultMessage());
					
					
					
					try {
						DialogueManager.putData(data.name, data);
						sender.sendMessage("NPGuy created!");
					} catch (NPGuyAlreadyExistsException e) {
						sender.sendMessage(e.getMessage());
					}
				}
				else {
					sender.sendMessage(ChatColor.RED+"You don't have permissions. You need npguys.management.npc to do this.");
				}
				return true;
			}
			else {
				try {
					data = DialogueManager.getData(args[0]);
				}
				catch (NPGuyNotFoundException e) {
					sender.sendMessage(e.getMessage());
					return true;
				}
			}
			if (args[1].equalsIgnoreCase("remove")) {
				if (!sender.hasPermission("npguys.management.npc")) {
					sender.sendMessage(ChatColor.RED+"You don't have permissions. You need npguys.management.npc to do this.");
					return true;
				}
				try {
					DialogueManager.removeData(data.name);
					sender.sendMessage("NPGuy data removed!");
				}
				catch (NPGuyNotFoundException e) {
					sender.sendMessage(e.getMessage()); 
				}
				return true;
			}
			if (args[1].equalsIgnoreCase("attach")) {
				if (!sender.hasPermission("npguys.management.npc")) {
					sender.sendMessage(ChatColor.RED+"You don't have permissions. You need npguys.management.npc to do this.");
					return true;
				}
				if (args.length != 3) {
					sender.sendMessage(UNKNOWN_COMMAND);
					return true;
				}
				try {
					int i = Integer.valueOf(args[2]);
					NPC npc = CitizensAPI.getNPCRegistry().getById(i);
					if (npc != null) {
						if(npc.hasTrait(NPGuy.class)) {
							NPGuy npguy = npc.getTrait(NPGuy.class);
							npguy.setUID(
									data.name);
							sender.sendMessage("NPGuy attached!");
						}
						else {
							sender.sendMessage("Selected NPC does not have NPGuy trait!");
						}
					}
					else {
						sender.sendMessage("NPC not found! Check id and try again!");
					}
				}
				catch (NumberFormatException e) {
					sender.sendMessage("Invalid NPC id! ID must be a valid integer!");
				}
				return true;
			}
			if (args[1].equalsIgnoreCase("dialogue")) {
				if (!sender.hasPermission("npguys.management.dialogues")) {
					sender.sendMessage(ChatColor.RED+"You don't have permissions. You need npguys.management.dialogues to do this.");
					return true;
				}
				
				List<String> newArgs = new ArrayList<String>();
				for (int i = 2; i < args.length; i++) {
					newArgs.add(args[i]);
				}
				dialogueCommands.handleCommand(sender, data, newArgs.toArray(new String[newArgs.size()]));
				return true;
			}
		}
		return false;
	}
	
	private class DialogueCommands implements NPGuyCommandHandler {

		@Override
		public void handleCommand(CommandSender sender, NPGuyData data, String[] args) {
			PlayerMessage message = null;
			switch(args.length) {
			case 0:
				sender.sendMessage(UNKNOWN_COMMAND);
				return;
			case 1:
				message = data.dialogues.get(args[0]);
				if (message == null) {
					sender.sendMessage("Message not found!");
					return;
				}
				else {
					StringBuilder display = new StringBuilder();
					display.append("npguy: ").append(data.name).append('\n');
					display.append("dialogue: ").append(args[0]).append('\n');;
					display.append("shortcut: ").append(message.getShortcut()).append('\n');;
					display.append("message: ").append(message.getMessage()).append('\n');;
					display.append("requirements: {");
					
					int i = 0;
					for (Requirement requirement : message.getRequirements()) {
						if (i != 0) {
							display.append("; ");
						}
						display.append(i).append(":");
						if (requirement.isReversed()) {
							display.append("!");
						}
						display.append(requirement.getType()).append("(").append(requirement.getData()).append(")");
						i++;
					}
					display.append("}").append('\n');
					
					display.append("actions: {");
					
					i = 0;
					for (Action action : message.getActions()) {
						if (i != 0) {
							display.append("; ");
						}
						display.append(i).append(":").append(action.getType()).append("(").append(action.getData()).append(")");
						i++;
					}
					display.append("}").append('\n');
					
					display.append("npc_message: ").append(message.getNPCMessage().getMessage()).append('\n');
					display.append("possible_responses:");
					
					for (String response : message.getNPCMessage().getPossibleResponses()) {
						display.append('\n').append("- ").append(response);
					}
					
					sender.sendMessage(display.toString());
					return;
				}
			default:
				if (args.length < 2) {
					return;
				}
				if (args[1].equalsIgnoreCase("create")) {
					if (args.length == 2) {
						message = DialogueManager.getDefaultMessage();
						if(data.dialogues.containsKey(args[0])) {
							sender.sendMessage("Dialogue already exists!");
						}
						else {
							data.dialogues.put(args[0], message);
							sender.sendMessage("Dialogue created!");
						}
					}
					else {
						sender.sendMessage(UNKNOWN_COMMAND);
					}
					return;
				}
				else {
					message = data.dialogues.get(args[0]);
					if (message == null) {
						sender.sendMessage("Message not found! Check spelling and try again!");
						return;
					}
					if(args.length == 2 && args[1].equalsIgnoreCase("welcomemsg")) {
						data.welcomeMessage = args[0];
						sender.sendMessage("Welcome message changed!");
						return;
					}
					if(args.length == 3 && args[1].equalsIgnoreCase("shortcut")) {
						message.setShortcut(args[2]);
						sender.sendMessage("Shortcut changed!");
						return;
					}
					if(args.length > 3 && args[1].equalsIgnoreCase("msg")) {
						if (args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("set")) {
							StringBuilder msg = new StringBuilder();
							if (args[2].equalsIgnoreCase("add")) {
								msg.append(message.getMessage());	
							}
							for (int i = 3; i < args.length; i++) {
								if (i!=3) {
									msg.append(" ");
								}
								msg.append(args[i]);
							}
							message.setMessage(msg.toString());
							
							sender.sendMessage("Message changed!");
						}
						else {
							sender.sendMessage(UNKNOWN_COMMAND);
						}
						return;
					}
					if(args.length > 2 && args[1].equalsIgnoreCase("npcmsg")) {
						if (args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("set")) {
							StringBuilder msg = new StringBuilder();
							if (args[2].equalsIgnoreCase("add")) {
								msg.append(message.getNPCMessage().getMessage());	
							}
							for (int i = 3; i < args.length; i++) {
								if (i!=3) {
									msg.append(" ");
								}
								msg.append(args[i]);
							}
							message.getNPCMessage().setMessage(msg.toString());
							
							sender.sendMessage("NPC Message changed!");
						}
						else {
							sender.sendMessage(UNKNOWN_COMMAND);
						}
						return;
					}
					if(args[1].equalsIgnoreCase("response")) {
						if (args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("remove") && args.length == 4) {
							if (args[2].equalsIgnoreCase("add")) {
								message.getNPCMessage().getPossibleResponses().add(args[3]);
								sender.sendMessage("Response added!");
							}
							else {
								if (!message.getNPCMessage().getPossibleResponses().contains(args[3])) {
									sender.sendMessage("Response not found! Check spelling and try again!");
								}
								else {
									message.getNPCMessage().getPossibleResponses().remove(args[3]);
									sender.sendMessage("Response removed!");
								}
							}
						}
						else {
							sender.sendMessage(UNKNOWN_COMMAND);
						}
						return;
					}
					if(args[1].equalsIgnoreCase("requirement")) {
						if (args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("addreversed")) {
							if (args[2].equalsIgnoreCase("remove")) {
								if (args.length == 4) {
									try {
										int i = Integer.valueOf(args[3]);
										if (message.getRequirements().size()-1 < i) {
											sender.sendMessage("Requirement not found!");
										}
										else {
											message.getRequirements().remove(i);
											sender.sendMessage("Requirement removed!");
										}
									}
									catch (NumberFormatException e) {
										sender.sendMessage("Invalid requirement id. ID must be a valid integer!");
									}
								}
								else {
									sender.sendMessage(UNKNOWN_COMMAND);
								}
							}
							else {
								try {
									Requirement requirement = ElementManager.newRequirement(args[3]);
									
									List<String> reqData = new ArrayList<String>();
									for (int i = 4; i < args.length; i++) {
										reqData.add(args[i]);
									}
									requirement.fromCommand(reqData.toArray(new String[reqData.size()]));
									
									if (args[2].equalsIgnoreCase("addreversed")) {
										requirement.setReversed(true);
									}
									
									message.getRequirements().add(requirement);
								} catch (RequirementMissingException e) {
									sender.sendMessage(e.getMessage());
								} catch (InvalidCommandException e) {
									sender.sendMessage(e.getMessage());
								}
							}
						}
						else {
							sender.sendMessage(UNKNOWN_COMMAND);
						}
						return;
					}
					if(args[1].equalsIgnoreCase("action")) {
						if (args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("remove")) {
							if (args[2].equalsIgnoreCase("remove")) {
								if (args.length == 4) {
									try {
										int i = Integer.valueOf(args[3]);
										if (message.getActions().size()-1 < i) {
											sender.sendMessage("Action not found!");
										}
										else {
											message.getActions().remove(i);
											sender.sendMessage("Action removed!");
										}
									}
									catch (NumberFormatException e) {
										sender.sendMessage("Invalid action id. ID must be a valid integer!");
									}
								}
								else {
									sender.sendMessage(UNKNOWN_COMMAND);
								}
							}
							else {
								try {
									Action action = ElementManager.newAction(args[3]);
									
									List<String> actData = new ArrayList<String>();
									for (int i = 4; i < args.length; i++) {
										actData.add(args[i]);
									}
									action.fromCommand(actData.toArray(new String[actData.size()]));
									
									message.getActions().add(action);
								} catch (ActionMissingException e) {
									sender.sendMessage(e.getMessage());
								} catch (InvalidCommandException e) {
									sender.sendMessage(e.getMessage());
								}
							}
						}
						else {
							sender.sendMessage(UNKNOWN_COMMAND);
						}
						return;
					}
				}
				sender.sendMessage(UNKNOWN_COMMAND);
			}
				
		}

		@Override
		public Map<String, String> listCommands() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	private interface NPGuyCommandHandler {
		public void handleCommand(CommandSender sender, NPGuyData data, String[] args);
		
		public Map<String,String> listCommands();
	}
}