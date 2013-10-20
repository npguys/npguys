package pl.ragecraft.npguys.quester;

import com.gmail.molnardad.quester.commandbase.QCommand;
import com.gmail.molnardad.quester.commandbase.QCommandContext;
import com.gmail.molnardad.quester.commandbase.exceptions.QCommandException;
import com.gmail.molnardad.quester.elements.Objective;
import com.gmail.molnardad.quester.elements.QElement;
import com.gmail.molnardad.quester.storage.StorageKey;

@QElement("CUSTOM")
public final class NPGuysObjective extends Objective {
	
	@Override
	public int getTargetAmount() {
		return 1;
	}
	
	protected static Objective load(StorageKey key) {
		return new NPGuysObjective();
	}
	
	@QCommand(min = 0, max = 0, usage = "")
	public static Objective fromCommand(QCommandContext context) throws QCommandException {
		return new NPGuysObjective();
	}

	@Override
	protected String info() {
		return "";
	}

	@Override
	protected void save(StorageKey arg0) {
		//Do nothing...
	}
	
	@Override
	protected String show(int arg0) {
		return "This is custom NPGuys objective. You can only complete it by talking with NPCs.";
	}
}
