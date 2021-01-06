package main.undoredo;

import main.albums.Album;
import main.organizer.MusicOrganizerWindow;

public class AddAlbumCommand implements UndoableCommand {

    private Album parent, album;

    public AddAlbumCommand(Album parent, Album album) {
        this.parent = parent;
        this.album = album;
    }

    @Override
    public Album execute(MusicOrganizerWindow view) {
        parent.addSubAlbum(album);
		view.onAlbumAdded(album);
        return album;
    }

    @Override
    public Album undo(MusicOrganizerWindow view) {
        parent.removeSubAlbum(album);
        view.onAlbumRemoved(album);
        return parent;
    }

    @Override
    public Album redo(MusicOrganizerWindow view) {
        return execute(view);
        
    }
}
