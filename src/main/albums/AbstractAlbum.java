package main.albums;
import java.util.LinkedHashSet;
import java.util.Set;
import main.soundfiles.SoundClip;

public abstract class AbstractAlbum {
    protected Set<SoundClip> soundClips;
    protected String albumName;

    public AbstractAlbum(String albumName) {
        this.albumName = albumName;
        soundClips = new LinkedHashSet<>();
    }

    abstract void addSoundClips(Set<SoundClip> soundClips);
    abstract void removeSoundClips(Set<SoundClip> soundClips);

    abstract void addSoundClip(SoundClip soundClip);
    abstract void removeSoundClip(SoundClip soundClip);

    public Set<SoundClip> getSoundClipsCopy() {
        return new LinkedHashSet<>(soundClips);
    }

}