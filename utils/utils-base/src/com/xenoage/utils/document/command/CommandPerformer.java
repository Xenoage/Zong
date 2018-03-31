package com.xenoage.utils.document.command;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.document.Document;
import com.xenoage.utils.document.exceptions.CancelledException;
import com.xenoage.utils.document.exceptions.PropertyAlreadySetException;
import com.xenoage.utils.document.exceptions.UselessException;

import java.util.ArrayList;
import java.util.List;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;

/**
 * This class performs {@link Command}s on a {@link Document} and supports undoing and redoing them.
 * 
 * It allows to register {@link CommandListener}s to monitor the actions.
 * 
 * @author Andreas Wenger
 */
public class CommandPerformer {

	@MaybeNull private Document document;
	private CommandHistory history = new CommandHistory();
	private List<CommandListener> listeners = new ArrayList<>();


	/**
	 * Creates a new {@link CommandPerformer}.
	 * @param document   the {@link Document} this performer is working on.
	 *                   May be null for an app-wide command performer.
	 */
	public CommandPerformer(@MaybeNull Document document) {
		this.document = document;
	}

	/**
	 * Gets the {@link Document} this performer is working on.
	 * May be null for an app-wide command performer.
	 */
	@MaybeNull public Document getDocument() {
		return document;
	}

	/**
	 * Executes the given {@link Command} and updates the command history according to
	 * its {@link Undoability}.
	 */
	public void execute(Command command) {
		log(remark(command.getClass().getName() + " is executed..."));
		try {
			command.execute();
			//non-transparent commands break the chain of redoable commands
			//(already undone commands can not be redone any more as soon as a new command was executed)
			Undoability undo = command.getUndoability();
			if (undo != Undoability.Transparent) {
				history.removeFollowingCommands();
			}
			//add undoable command to history
			if (undo == Undoability.Undoable) {
				history.addCommand(command);
			}
			//stopper commands clear the history
			else if (undo == Undoability.Stopper) {
				history.clear();
			}
			//notify listeners
			for (CommandListener l : listeners)
				l.commandExecuted(document, command);
		} catch (CancelledException ex) {
			//user cancelled the action.
			log(remark("Command cancelled."));
		} catch (UselessException ex) {
			//the action was cancelled because it has no effect.
			log(remark("Command was cancelled, because it has no effect."));
		} catch (PropertyAlreadySetException ex) {
			//no problem. we just ignore that exception.
			log(remark("Command was cancelled, because property was already set."));
		}
	}

	/**
	 * Returns true, if the last executed command can be undone. 
	 */
	public boolean isUndoPossible() {
		return (history.getLastCommand() != null);
	}

	/**
	 * Undoes the last command, when possible.
	 */
	public void undo() {
		if (isUndoPossible()) {
			Command command = history.getLastCommand();
			log(remark(command.getClass().getName() + " is undone..."));
			//undo the command
			command.undo();
			//go back in history
			history.back();
			//notify listeners
			for (CommandListener l : listeners)
				l.commandUndone(document, command);
		}
	}

	/**
	 * Undoes the given number of commands in the history, if possible.
	 */
	public void undoMultipleSteps(int steps) {
		if (steps < 1)
			return;
		log(remark("Multiple undo (" + steps + " steps)..."));
		Command command = null;
		for (int i = 0; i < steps && isUndoPossible(); i++) {
			command = history.getLastCommand();
			log(remark(command.getClass().getName() + " is undone..."));
			//undo the command
			command.undo();
			//go back in history
			history.back();
		}
		//notify listeners
		for (CommandListener l : listeners)
			l.commandUndone(document, command);
	}

	/**
	 * Returns true, if the last undone command can be executed again. 
	 */
	public boolean isRedoPossible() {
		return (history.getLastUndoneCommand() != null);
	}

	/**
	 * Executes the last undone command again, when possible.
	 */
	public void redo() {
		if (isRedoPossible()) {
			Command command = history.getLastUndoneCommand();
			log(remark(command.getClass().getName() + " is redone..."));
			//execute the command
			command.execute();
			//go forward in history
			history.forward();
			//notify listeners
			for (CommandListener l : listeners)
				l.commandExecuted(document, command);
		}
	}

	/**
	 * Registers the given {@link CommandListener}.
	 */
	public void addCommandListener(CommandListener listener) {
		listeners.add(listener);
	}

	/**
	 * Unregisters the given {@link CommandListener}.
	 */
	public void removeCommandListener(CommandListener listener) {
		listeners.remove(listener);
	}

}
