package main.undoredo;

import main.organizer.MusicOrganizerWindow;
import main.albums.AbstractAlbum;

public class UndoCommandController {
    private MusicOrganizerWindow view;
    private UndoableCommand[] commands;
    private int current;

    public UndoCommandController(int size, MusicOrganizerWindow view) {
        commands = new UndoableCommand[size];
        current = 0;
        this.view = view;
    }

    public int getSize() {
        return commands.length;
    }

    private void shiftCommands() {
        int l = Math.min(commands.length-current, commands.length-1);
        System.arraycopy(commands, current, commands, 1, l);
        while (l++ < commands.length - 1) {
            commands[l] = null;
        }
    }

    private void onExecute(AbstractAlbum affectedAlbum) {
        if (view != null) {
            view.onClipsUpdated(affectedAlbum);
            view.setSelectedTreeNode(affectedAlbum);
            view.setUndo(canUndo());
            view.setRedo(canRedo());
        }
    }

    public void addCommandAndExecute(UndoableCommand command) {
        shiftCommands();
        current = 0;
        commands[0] = command;
        onExecute(command.execute(view));
    }

    private boolean canUndo() {
        return (current < commands.length && commands[current] != null);
    }

    private boolean canRedo() {
        return (current > 0 && commands[current -1] != null);
    }


    public void undo() {
        if (canUndo()) {
            onExecute(commands[current++].undo(view));
            System.out.println(current);
        }
    }

    public void redo() {
        if (canRedo()) {
            current -= 1;
            onExecute(commands[current].redo(view));        
        }
    }

    public int getCurrent() {
        return current;
    }

    public UndoableCommand[] getCommandsCopy() {
        return commands.clone();
    }
}
