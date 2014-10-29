/*
* NPGuys - Bukkit plugin for better NPC interaction
* Copyright (C) 2014 Adam Gotlib <Goldob>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package pl.ragecraft.npguys.editor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import pl.ragecraft.npguys.DialogueManager;
import pl.ragecraft.npguys.ElementManager;
import pl.ragecraft.npguys.NPGuy;
import pl.ragecraft.npguys.NPGuyData;
import pl.ragecraft.npguys.NPGuys;
import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.commands.CommandUtils;
import pl.ragecraft.npguys.conversation.NPCMessage;
import pl.ragecraft.npguys.conversation.PlayerMessage;
import pl.ragecraft.npguys.editor.EditorSession.NoDialogueSelectedException;
import pl.ragecraft.npguys.editor.EditorSession.NoNPGuySelectedException;
import pl.ragecraft.npguys.exception.ActionMissingException;
import pl.ragecraft.npguys.exception.InvalidCommandException;
import pl.ragecraft.npguys.exception.MessageNotFoundException;
import pl.ragecraft.npguys.exception.NPGuyAlreadyExistsException;
import pl.ragecraft.npguys.exception.NPGuyNotFoundException;
import pl.ragecraft.npguys.exception.RequirementMissingException;
import pl.ragecraft.npguys.requirement.Requirement;

public class DialogueEditor {
	private static Map<CommandSender, EditorSession> activeSessions;
	private static CommandHandler commandHandler;
	
	private DialogueEditor() {}
	
	public static void init(NPGuys plugin) {
		activeSessions = new HashMap<CommandSender, EditorSession>();
		commandHandler = new CommandHandler();
	}
	
	public static EditorSession getSession(CommandSender sender) {
		if(!activeSessions.containsKey(sender)) {
			activeSessions.put(sender, new EditorSession(sender));
		}
		return activeSessions.get(sender);
	}
	
	public static CommandHandler getCommandHandler() {
		return commandHandler;
	}
	
	public static void closeAllSessions() {
		for(EditorSession session : activeSessions.values()) {
			session.forceClose();
		}
		activeSessions.clear();
	}
	
	public static class CommandHandler extends CommandUtils implements CommandExecutor {
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label,
				String[] arguments) {
			switch(label.toLowerCase()) {
			case("dialogue"):
				dialogueCommand(sender, arguments);
				break;
			case("npguy"):
				npguyCommand(sender, arguments);
				break;
			case("npg"):
				npguyCommand(sender, arguments);
				break;
			default:
				reportFailure(sender, COMMAND_NOT_RECOGNIZED);
			}
			return true;
		}
		
		private void dialogueCommand(CommandSender sender, String[] arguments) {
			EditorSession session = getSession(sender);
			if(arguments.length == 0) {
				// TODO Print dialogue data
				return;
			}
			switch(arguments[0].toLowerCase()) {
			case("list"):
				// TODO
				break;
			case("select"):
				if(assertMinArgsLength(sender, 2, arguments)) {
					String dialogue = joinStrings(arguments, 1);
					try {
						session.selectDialogue(dialogue);
						String feedback = "You selected dialogue: %dialogue @ NPGuy: %npguy";
						feedback = feedback.replaceAll("%dialogue", ChatColor.AQUA.toString()+dialogue+ChatColor.GREEN.toString());
						feedback = feedback.replaceAll("%npguy", ChatColor.AQUA.toString()+session.getSelectedNPGuy().getName());
						reportSuccess(sender, feedback);
					} catch (MessageNotFoundException | NoNPGuySelectedException e) {
						reportFailure(sender, e.getMessage());
					}
				}
				break;
			case("shortcut"):
				if(assertDialogueEditable(session) && assertMinArgsLength(sender, 2, arguments)) {
					String shortcut = joinStrings(arguments, 1);
					try {
						session.getSelectedDialogue().setShortcut(shortcut);
						reportSuccess(sender, "Shortcut changed!");
					} catch (NoDialogueSelectedException e) {
						reportFailure(sender, e.getMessage());
					}
				}
				break;
			case("message"):
			case("msg"):
				if(assertDialogueEditable(session) && assertMinArgsLength(sender, 3, arguments)) {
					try {
						PlayerMessage playerMsg = session.getSelectedDialogue();
						if(arguments[1].equalsIgnoreCase("set")) {
							playerMsg.setMessage(joinStrings(arguments, 2));
							reportSuccess(sender, "Message changed!");
						} else if(arguments[1].equalsIgnoreCase("add")) {
							playerMsg.setMessage(playerMsg.getMessage()+joinStrings(arguments, 2));
							reportSuccess(sender, "Message changed!");
						} else {
							reportFailure(sender, COMMAND_NOT_RECOGNIZED);
						}
					} catch(NoDialogueSelectedException e) {
						reportFailure(sender, e.getMessage());
					}	
				}
				break;
			case("npcmsg"):
				if(assertDialogueEditable(session) && assertMinArgsLength(sender, 3, arguments)) {
					try {
						NPCMessage npcMsg = session.getSelectedDialogue().getNPCMessage();
						if(arguments[1].equalsIgnoreCase("set")) {
							npcMsg.setMessage(joinStrings(arguments, 2));
							reportSuccess(sender, "NPC message changed!");
						} else if(arguments[1].equalsIgnoreCase("add")) {
							npcMsg.setMessage(npcMsg.getMessage()+joinStrings(arguments, 2));
							reportSuccess(sender, "NPC message changed!");
						} else {
							reportFailure(sender, COMMAND_NOT_RECOGNIZED);
						}
					} catch(NoDialogueSelectedException e) {
						reportFailure(sender, e.getMessage());
					}	
				}
				break;
			case("response"):
				if(assertDialogueEditable(session) && assertMinArgsLength(sender, 3, arguments)) {
					try {
						NPCMessage npcMsg = session.getSelectedDialogue().getNPCMessage();
						String dialogue = joinStrings(arguments, 1);
						if(arguments[1].equalsIgnoreCase("add")) {
							npcMsg.getPossibleResponses().add(dialogue);
							reportSuccess(sender, "Response added!");
						} else if(arguments[1].equalsIgnoreCase("remove")) {
							if(npcMsg.getPossibleResponses().remove(dialogue)) {
								reportSuccess(sender, "Response removed!");
							} else {
								reportFailure(sender, "Couldn't find the dialogue on the response list. Sorry :(");
							}
						} else {
							reportFailure(sender, COMMAND_NOT_RECOGNIZED);
						}
					} catch(NoDialogueSelectedException e) {
						reportFailure(sender, e.getMessage());
					}	
				}
				break;
			case("action"):
				if(assertDialogueEditable(session) && assertMinArgsLength(sender, 3, arguments)) {
					try {
						PlayerMessage playerMsg = session.getSelectedDialogue();
						if(arguments[1].equalsIgnoreCase("add")) {
							Action newAction = ElementManager.newAction(arguments[2]);
							newAction.fromCommand(Arrays.copyOfRange(arguments, 3, arguments.length));
							playerMsg.getActions().add(newAction);
							reportSuccess(sender, "Action added!");
						} else if(arguments[1].equalsIgnoreCase("remove")) {
							if(assertArgsLengthEqual(sender, 4, arguments)) {
								int actionIndex = Integer.valueOf(arguments[3]);
								playerMsg.getActions().remove(actionIndex);
								reportSuccess(sender, "Action removed!");
							}
						} else {
							reportFailure(sender, COMMAND_NOT_RECOGNIZED);
						}
					} catch(NoDialogueSelectedException | ActionMissingException | InvalidCommandException e) {
						reportFailure(sender, e.getMessage());
					} catch(NumberFormatException e) {
						reportFailure(sender, "Invalid action index! Index must be an integer!");
					} catch(IndexOutOfBoundsException e) {
						reportFailure(sender, "Invalid action index! It's either too big or too small (can't be smaller than 0)!");
					}
				}
				break;
			case("requirement"):
			case("req"):
				if(assertDialogueEditable(session) && assertMinArgsLength(sender, 3, arguments)) {
					try {
						PlayerMessage playerMsg = session.getSelectedDialogue();
						if(arguments[1].equalsIgnoreCase("add") || arguments[1].equalsIgnoreCase("addr") || arguments[1].equalsIgnoreCase("addreversed")) {
							Requirement newRequirement = ElementManager.newRequirement(arguments[2]);
							newRequirement.fromCommand(Arrays.copyOfRange(arguments, 3, arguments.length));
							if(arguments[1].equalsIgnoreCase("addr") || arguments[1].equalsIgnoreCase("addreversed")) {
								newRequirement.setReversed(true);
							}
							reportSuccess(sender, "Action added!");
						} else if(arguments[1].equalsIgnoreCase("remove")) {
							if(assertArgsLengthEqual(sender, 4, arguments)) {
								int actionIndex = Integer.valueOf(arguments[3]);
								playerMsg.getActions().remove(actionIndex);
								reportSuccess(sender, "Action removed!");
							}
						} else {
							reportFailure(sender, COMMAND_NOT_RECOGNIZED);
						}
					} catch(NoDialogueSelectedException | RequirementMissingException | InvalidCommandException e) {
						reportFailure(sender, e.getMessage());
					} catch(NumberFormatException e) {
						reportFailure(sender, "Invalid requirement index! Index must be an integer!");
					} catch(IndexOutOfBoundsException e) {
						reportFailure(sender, "Invalid requirement index! It's either too big or too small (can't be smaller than 0)!");
					}
				}
				break;
			case("create"):
				if(assertMinArgsLength(sender, 2, arguments) && assertNPGuyEditable(session)) {
					try {
						NPGuyData npguy = session.getSelectedNPGuy();
						String dialogue = joinStrings(arguments, 1);
						if(npguy.getDialogues().containsKey(dialogue)) {
							reportFailure(sender, "Dialogue already exists!");
						}
						else {
							npguy.getDialogues().put(dialogue, DialogueManager.getDefaultMessage(dialogue));
							session.selectDialogue(dialogue);
							reportSuccess(sender, "Dialogue created and selected!");
						}
					} catch(NoNPGuySelectedException | MessageNotFoundException e) {
						reportFailure(sender, e.getMessage());
					}
				}
				break;
			case("remove"):
				if(assertArgsLengthEqual(sender, 1, arguments) && assertDialogueEditable(session)) {
					try {
						session.getSelectedNPGuy().getDialogues().remove(session.getSelectedDialogue().getName());
						reportSuccess(sender, "Dialogue removed!");
					} catch (NoDialogueSelectedException | NoNPGuySelectedException e) {
						reportFailure(sender, e.getMessage());
					}
				}
				break;
			default:
				reportFailure(sender, COMMAND_NOT_RECOGNIZED);
			}
		}
		
		private void npguyCommand(CommandSender sender, String[] arguments) {
			EditorSession session = getSession(sender);
			if(arguments.length == 0) {
				// TODO Print NPGuy data
				return;
			}
			switch(arguments[0].toLowerCase()) {
			case("list"):
				// TODO
				break;
			case("select"):
				if(assertMinArgsLength(sender, 2, arguments)) {
					String npguy = joinStrings(arguments, 1);
					try {
						session.selectNPGuy(npguy);
						String feedback = "You selected NPGuy: %npguy";
						feedback = feedback.replaceAll("%npguy", ChatColor.AQUA.toString()+npguy);
						reportSuccess(sender, feedback);
					} catch (NPGuyNotFoundException e) {
						reportFailure(sender, e.getMessage());
					}
				}
				break;
			case("attach"):
				if(assertArgsLengthEqual(sender, 2, arguments)) {
					try {
						int npcID = Integer.valueOf(arguments[1]);
						NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);
						if(npc != null) {
							if(npc.hasTrait(NPGuy.class)) {
								NPGuy npguy = npc.getTrait(NPGuy.class);
								npguy.setUID(session.getSelectedNPGuy().getName());
								reportSuccess(sender, "NPGuy attached!");
							}
							else {
								reportFailure(sender, "Selected NPC does not have NPGuy trait!");
							}
						}
						else {
							reportFailure(sender, "NPC not found! Check ID and try again!");
						}
					} catch(NoNPGuySelectedException e) {
						reportFailure(sender, e.getMessage());
					} catch (NumberFormatException e) {
						reportFailure(sender, "Invalid NPC ID! ID must be an integer!");
					}
				}
				break;
			case("attached"):
				if(assertArgsLengthEqual(sender, 2, arguments)) {
					try {
						int npcID = Integer.valueOf(arguments[1]);
						NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);
						if(npc != null) {
							if(npc.hasTrait(NPGuy.class)) {
								NPGuy npguy = npc.getTrait(NPGuy.class);
								String feedback = "NPGuy attached to npc with ID %id is: %npguy";
								feedback = feedback.replaceAll("%id", ChatColor.AQUA.toString()+String.valueOf(npcID)+ChatColor.GREEN.toString());
								feedback = feedback.replaceAll("%npguy", ChatColor.AQUA.toString()+npguy.getUID());
								reportSuccess(sender, feedback);
							}
							else {
								reportFailure(sender, "Selected NPC does not have NPGuy trait!");
							}
						}
						else {
							reportFailure(sender, "NPC not found! Check ID and try again!");
						}
					} catch (NumberFormatException e) {
						reportFailure(sender, "Invalid NPC ID! ID must be a positive integer!");
					}
				}
				break;
			case("activate"):
				if(assertArgsLengthEqual(sender, 1, arguments)) {
					try {
						DialogueManager.setActive(session.getSelectedNPGuy().getName(), true);
						reportSuccess(sender, "NPGuy activated!");
					} catch (NPGuyNotFoundException | NoNPGuySelectedException e) {
						reportFailure(sender, e.getMessage());
					}
				}
				break;
			case("deactivate"):
				if(assertArgsLengthEqual(sender, 1, arguments)) {
					try {
						DialogueManager.setActive(session.getSelectedNPGuy().getName(), false);
						reportSuccess(sender, "NPGuy deactivated! Don't forget to activate him when you're done editing.");
					} catch (NPGuyNotFoundException | NoNPGuySelectedException e) {
						reportFailure(sender, e.getMessage());
					}
				}
				break;
			case("create"):
				if(assertMinArgsLength(sender, 2, arguments)) {
					try {
						String name = joinStrings(arguments, 1);
						DialogueManager.createNPGuy(name, false);
						session.selectNPGuy(name);
						reportSuccess(sender, "NPGuy created and selected!");
					} catch (NPGuyAlreadyExistsException | NPGuyNotFoundException e) {
						reportFailure(sender, e.getMessage());
					}
				}
				break;
			case("remove"):
				if(assertArgsLengthEqual(sender, 1, arguments) && assertNPGuyEditable(session)) {
					try {
						DialogueManager.removeData(session.getSelectedNPGuy().getName());
						reportSuccess(sender, "NPGuy removed!");
					} catch (NPGuyNotFoundException | NoNPGuySelectedException e) {
						reportFailure(sender, e.getMessage());
					}
				}
				break;
			default:
				reportFailure(sender, COMMAND_NOT_RECOGNIZED);
			}
		}
		
		private boolean assertDialogueEditable(EditorSession session) {
			if(assertNPGuyEditable(session)) {
				try {
					session.getSelectedDialogue();
					return true;
				} catch (NoDialogueSelectedException e) {
					reportFailure(session.getUser(), e.getMessage());
				}
			}
			return false;
		}
		
		private boolean assertNPGuyEditable(EditorSession session) {
			try {
				if(DialogueManager.isActive(session.getSelectedNPGuy().getName())) {
					reportFailure(session.getUser(), "You need to deactivate the selected NPGuy before editing!");
					return false;
				}
			} catch (NoNPGuySelectedException | NPGuyNotFoundException e) {
				reportFailure(session.getUser(), e.getMessage());
				return false;
			}
			 return true;
		}
	}	
}
