package main.undoredo;

import main.albums.Album;
import main.organizer.MusicOrganizerWindow;

public class RemoveAlbumCommand implements UndoableCommand {

    private Album parent, album;

    public RemoveAlbumCommand(Album parent, Album album) {
        this.parent = parent;
        this.album = album;
    }

    private void updateView(Album album, MusicOrganizerWindow view) {
        view.onAlbumAdded(album);
        album.getSubAlbumsCopy().forEach(subAlbum -> updateView(subAlbum, view));
    }

    @Override
    public Album undo(MusicOrganizerWindow view) {
        parent.addSubAlbum(album);
        updateView(album, view);
        return album;
    }

    @Override
    public Album execute(MusicOrganizerWindow view) {
        parent.removeSubAlbum(album);
        view.onAlbumRemoved(album);
		return parent;
    }

    @Override
    public Album redo(MusicOrganizerWindow view) {
        return execute(view);
    }
}
