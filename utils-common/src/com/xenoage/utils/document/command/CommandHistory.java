package com.xenoage.utils.document.command;

import java.util.ArrayList;
import java.util.List;

import static com.xenoage.utils.kernel.Range.rangeReverse;

/**
 * A history of {@link Command}s executed on the same {@link CommandPerformer}.
 * 
 * @author Andreas Wenger
 */
public class CommandHistory {

	private ArrayList<Command> history = new ArrayList<>();
	private int historyPosition = -1;


	/**
	 * Resets the history.
	 */
	public void clear() {
		history.clear();
		historyPosition = -1;
	}

	/**
	 * Adds the given {@link Command} to the top of the stack and sets it as the
	 * last executed command.
	 */
	public void addCommand(Command command) {
		history.add(command);
		forward();
	}

	/**
	 * Sets the next {@link Command} as the last executed command.
	 */
	public void forward() {
		historyPosition++;
	}

	/**
	 * Sets the last {@link Command} as the last executed command.
	 */
	public void back() {
		historyPosition--;
	}

	/**
	 * Gets the last executed {@link Command}, or null if there is nothing to be undone.
	 */
	public Command getLastCommand() {
		if (historyPosition > -1) {
			return history.get(historyPosition);
		}
		else {
			return null;
		}
	}

	/**
	 * Gets the last undone {@link Command}, or null if there is nothing to be redone.
	 */
	public Command getLastUndoneCommand() {
		if (history.size() > historyPosition + 1) {
			return history.get(historyPosition + 1);
		}
		else {
			return null;
		}
	}

	/**
	 * Delete all commands that follow the current command.
	 */
	public void removeFollowingCommands() {
		int numberToRemove = history.size() - historyPosition - 1;
		for (int i = 0; i < numberToRemove; i++) {
			history.remove(history.size() - 1);
		}
	}

	/**
	 * Gets a list of the undoable commands. The first one is the command, that must be undone first.
	 */
	public List<Command> getUndoableCommands() {
		ArrayList<Command> ret = new ArrayList<>();
		for (int i : rangeReverse(historyPosition, 0)) {
			ret.add(history.get(i));
		}
		return ret;
	}

}
