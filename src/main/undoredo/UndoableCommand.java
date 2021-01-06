package main.undoredo;

import main.albums.AbstractAlbum;
import main.organizer.MusicOrganizerWindow;

public interface UndoableCommand {
    AbstractAlbum execute(MusicOrganizerWindow view);
    AbstractAlbum undo(MusicOrganizerWindow view);
    AbstractAlbum redo(MusicOrganizerWindow view);
}
