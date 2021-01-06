package main.albums;

import java.util.Set;
import main.soundfiles.SoundClip;

public class RatedAlbum extends AbstractAlbum{
    public RatedAlbum(String albumName) {
        super(albumName);
    }

    @Override
    public String toString() {
        return albumName;
    }

    @Override
    public void addSoundClip(SoundClip soundClip) {
        assert soundClip.getRating() >= 4;
        this.soundClips.add(soundClip);
    }

    @Override
    public void removeSoundClip(SoundClip soundClip) {
        assert soundClip.getRating() < 4;
        this.soundClips.remove(soundClip);
    }
    
    @Override
    public void addSoundClips(Set<SoundClip> soundClips) {
        for (SoundClip soundClip : soundClips) {
            assert soundClip.getRating() >= 4;

            this.soundClips.add(soundClip);
        }
    }

    @Override
    public void removeSoundClips(Set<SoundClip> soundClips) {
        for (SoundClip soundClip : soundClips) {
            assert soundClip.getRating() < 4;

            this.soundClips.remove(soundClip);
        }
    }
}
