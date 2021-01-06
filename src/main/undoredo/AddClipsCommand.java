package main.undoredo;

import main.albums.Album;
import main.organizer.MusicOrganizerWindow;
import main.soundfiles.SoundClip;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddClipsCommand implements UndoableCommand {

    private Album album;
    private Set<SoundClip> soundClips;
    private Map<Album, Set<SoundClip>> addedClips = new HashMap<>();

    public AddClipsCommand(Album album, Set<SoundClip> soundClips) {
        this.album = album;
        this.soundClips = soundClips;

        Album parent = album;
        while (parent.getParentAlbum() != null) {

            Set<SoundClip> addedClipsCopy = new HashSet<>(soundClips);
            addedClipsCopy.removeAll(parent.getSoundClipsCopy());
            addedClips.put(parent, addedClipsCopy);

            parent = parent.getParentAlbum();
        }
    }

    @Override
    public Album execute(MusicOrganizerWindow view) {
        album.addSoundClips(soundClips);
		return album;
    }

    @Override
    public Album undo(MusicOrganizerWindow view) {
        Set<Album> parents = addedClips.keySet();
        parents.forEach(parent -> parent.removeSoundClips(addedClips.get(parent)));
        return album;
    }

    @Override
    public Album redo(MusicOrganizerWindow view) {
        return execute(view);
    }

}
