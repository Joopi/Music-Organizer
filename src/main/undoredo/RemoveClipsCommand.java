package main.undoredo;

import main.albums.Album;
import main.organizer.MusicOrganizerWindow;
import main.soundfiles.SoundClip;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RemoveClipsCommand implements UndoableCommand {

    private Album album;
    private Set<SoundClip> soundClips;
    private Map<Album, Set<SoundClip>> removedClips = new HashMap<>();

    private Set<Album> getAllSubAlbums(Album album) {
        Set<Album> result = new HashSet<>();
        result.add(album);
        album.getSubAlbumsCopy().forEach(subAlbum -> result.addAll(getAllSubAlbums(subAlbum)));
        return result;
    }

    public RemoveClipsCommand(Album album, Set<SoundClip> soundClips) {
        this.album = album;
        this.soundClips = soundClips;

        Set<Album> albums = getAllSubAlbums(album);
        for (Album subAlbum : albums) {
            Set<SoundClip> removedSoundClips = subAlbum.getSoundClipsCopy();
            removedSoundClips.retainAll(soundClips);
            removedClips.put(subAlbum, removedSoundClips);
        }
    }
    
    @Override
    public Album execute(MusicOrganizerWindow view) {
        album.removeSoundClips(soundClips);
        return album;
    }

    @Override
    public Album undo(MusicOrganizerWindow view) {
        removedClips.forEach(Album::addSoundClips);
        return album;
    }

    @Override
    public Album redo(MusicOrganizerWindow view) {
        return execute(view);
    }
}
