package main.albums;

import main.soundfiles.SoundClip;
import java.util.*;

public class Album extends AbstractAlbum{
    private Set<Album> subAlbums = new HashSet<>();
    private Album parentAlbum;

    //constructor som tar albumets namn som parameter.
    public Album(String albumName) {
        super(albumName);

        assert (this.albumName.matches(albumName));
    }

    private boolean isRoot() {
        return parentAlbum == null;
    }

    private Album getRoot() {
        if (isRoot())
            return this;

        return parentAlbum.getRoot();
    }

    public Album getParentAlbum() {
        return parentAlbum;
    }

    @Override
    public String toString() {
        return getAlbumName();
    }

    public String getAlbumName() {
        return albumName;
    }

    private boolean isProperSubAlbum(Album album) {
        assert album != null;

        return (subAlbums.contains(album) && (album.parentAlbum.equals(this)));
    }


    public void addSubAlbum(Album album) {
        assert (album != null && !album.equals(this) && !containsAlbumRecursive(album) && album.subAlbums.size() == 0 && album.soundClips.size() == 0); //förvillkor

        subAlbums.add(album);
        album.parentAlbum = this;

        assert isProperSubAlbum(album);
    }

    public void removeSubAlbum(Album album) {
        assert (album != null && !album.equals(this) && containsAlbum(album)); //förvillkor

        subAlbums.remove(album);
        album.parentAlbum = null;

        assert !isProperSubAlbum(album); //eftervillkor
    }

    public boolean containsAlbum(Album album) {
        assert (album != null && !album.equals(this)); //förvillkor

        return subAlbums.contains(album);
    }

    public boolean containsAlbumRecursive(Album album) {
        assert (album != null && !album.equals(this));

        if (subAlbums.contains(album))
            return true;

        for (Album subAlbum : subAlbums)
            if (subAlbum.containsAlbumRecursive(album)) {
                return true;
            }

        return false;
    }

    public Set<Album> getSubAlbumsCopy() {
        return new HashSet<>(subAlbums);
    }

    public void addSoundClip(SoundClip clip) {
        assert (clip != null);

        soundClips.add(clip);
        if (parentAlbum != null)
            parentAlbum.addSoundClip(clip);

        assert containsSoundClip(clip);
    }

    public void addSoundClips(Set<SoundClip> clips) {
        assert (clips != null);

        if (clips.isEmpty())
            return;

        soundClips.addAll(clips);

        if (parentAlbum != null)
            parentAlbum.addSoundClips(clips);

        assert containsSoundClips(clips);
    }

    public void removeSoundClip(SoundClip clip) {
        assert (clip != null);

        if (isRoot())
            return;

        soundClips.remove(clip);

        for (Album subAlbum : subAlbums) {
            subAlbum.removeSoundClip(clip);
        }

        assert !containsSoundClip(clip);
    }

    public void removeSoundClips(Set<SoundClip> clips) {
        assert (clips != null);

        if (isRoot())
            return;

        soundClips.removeAll(clips);

        for (Album subAlbum : subAlbums) {
            subAlbum.removeSoundClips(clips);
        }

        assert !containsSoundClips(clips);
    }

    public boolean containsSoundClip(SoundClip clip) {
        assert clip != null;

        return soundClips.contains(clip);
    }

    public boolean containsSoundClips(Set<SoundClip> clips) {
        assert clips != null && !clips.isEmpty();

        return soundClips.containsAll(clips);
    }

    public Set<SoundClip> getSoundClipsCopy() {
        return new HashSet<>(soundClips);
    }
}