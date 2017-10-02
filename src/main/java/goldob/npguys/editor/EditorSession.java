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

package goldob.npguys.editor;

import org.bukkit.command.CommandSender;

import goldob.npguys.DialogueManager;
import goldob.npguys.NPGuyData;
import goldob.npguys.conversation.PlayerMessage;
import goldob.npguys.exception.MessageNotFoundException;
import goldob.npguys.exception.NPGuyNotFoundException;

public class EditorSession {
	private CommandSender user;
	private NPGuyData selectedNPGuy = null;
	private PlayerMessage selectedDialogue = null;
	
	public EditorSession(CommandSender user) {
		this.user = user;
	}
	
	public void close() {
		// TODO
	}
	
	public void forceClose() {
		// TODO
	}
	
	public void selectNPGuy(String npguy) throws NPGuyNotFoundException {
		selectedNPGuy = DialogueManager.getData(npguy);
		selectedDialogue = null;
	}
	
	public void selectDialogue(String dialogue) throws MessageNotFoundException, NoNPGuySelectedException {
		try {
			selectedDialogue = DialogueManager.getPlayerMessage(getSelectedNPGuy().getName(), dialogue);
		} catch (NPGuyNotFoundException e) {
			throw new NoNPGuySelectedException();
		}
	}
	
	public CommandSender getUser() {
		return user;
	}
	
	public NPGuyData getSelectedNPGuy() throws NoNPGuySelectedException {
		if(selectedNPGuy == null) {
			throw new NoNPGuySelectedException();
		}
		return selectedNPGuy;
	}
	
	public PlayerMessage getSelectedDialogue() throws NoDialogueSelectedException {
		if(selectedDialogue == null) {
			throw new NoDialogueSelectedException();
		}
		return selectedDialogue;
	}
	
	public class NoNPGuySelectedException extends Exception {
		private static final long serialVersionUID = 1L;
		
		@Override
		public String getMessage() {
			return "You need to select an NPGuy first!";
		}
	}
	
	public class NoDialogueSelectedException extends Exception {
		private static final long serialVersionUID = 1L;
		
		@Override
		public String getMessage() {
			return "You need to select a dialogue first!";
		}
	}
}
