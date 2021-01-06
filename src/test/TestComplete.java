package test;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.jupiter.api.Test;
import main.albums.Album;
import main.soundfiles.SoundClip;

public class TestComplete {

    @Test
    public void rootSoundClip() {
        Album rootAlbum = new Album("All Sound Clips");

        SoundClip clip = new SoundClip(new File("hubbabubba"));
        rootAlbum.addSoundClip(clip);

        assertTrue(rootAlbum.containsSoundClip(clip));

        Album subAlbum = new Album("sub");
        rootAlbum.addSubAlbum(subAlbum);

        assertFalse(subAlbum.containsSoundClip(clip));
    }

    @Test
    public void albums() {
        Album rootAlbum = new Album("All Sound Clips");

        Album movieClips = new Album("Movie clips");
        Album hardBass = new Album("Hard bass");

        rootAlbum.addSubAlbum(movieClips);
        rootAlbum.addSubAlbum(hardBass);

        assertTrue(rootAlbum.containsAlbumRecursive(hardBass));

        Album titanic = new Album("Titanic");
        movieClips.addSubAlbum(titanic);

        assertTrue(rootAlbum.containsAlbumRecursive(titanic) && movieClips.containsAlbumRecursive(titanic));
        assertFalse(hardBass.containsAlbumRecursive(titanic));
    }

    @Test
    public void soundClips() {
        Album rootAlbum = new Album("All Sound Clips");

        Album movieClips = new Album("Movie clips");
        Album hardBass = new Album("Hard bass");

        rootAlbum.addSubAlbum(movieClips);
        rootAlbum.addSubAlbum(hardBass);

        Album titanic = new Album("Titanic");
        movieClips.addSubAlbum(titanic);

        SoundClip soundClip = new SoundClip(new File("f"));
        titanic.addSoundClip(soundClip);

        assertTrue(rootAlbum.containsSoundClip(soundClip)
                && movieClips.containsSoundClip(soundClip)
                && titanic.containsSoundClip(soundClip));

        assertFalse(hardBass.containsSoundClip(soundClip));

        //test removal
        titanic.removeSoundClip(soundClip);
        assertTrue(rootAlbum.containsSoundClip(soundClip));
        assertTrue(movieClips.containsSoundClip(soundClip));
        assertFalse(titanic.containsSoundClip(soundClip));
        //this should never have contained it
        assertFalse(hardBass.containsSoundClip(soundClip));

        /**
             o
            / \
           o   o
                \
                 o

         */
    }

    @Test
    public void connectFilledAlbum() {
        Album rootAlbum = new Album("All Sound Clips");

        Album movieClips = new Album("Movie clips");
        Album hardBass = new Album("Hard bass");

        rootAlbum.addSubAlbum(hardBass);

        SoundClip soundClip = new SoundClip(new File("f"));
        rootAlbum.addSubAlbum(movieClips);
        movieClips.addSoundClip(soundClip);


        /**
            o f.wav
           / \
          o   o f.wav

         **/
        assertTrue(rootAlbum.containsSoundClip(soundClip));
        assertFalse(hardBass.containsSoundClip(soundClip));

        movieClips.removeSoundClip(soundClip);

        /**
            o f.wav
           / \
          o   o

         **/

        assertTrue(rootAlbum.containsSoundClip(soundClip));
        assertFalse(movieClips.containsSoundClip(soundClip));
        assertFalse(hardBass.containsSoundClip(soundClip));
    }

    @Test
    public void testSize() {
        Album rootAlbum = new Album("All Sound Clips");
        Album movieClips = new Album("Movie clips");

        SoundClip soundClip = new SoundClip(new File("f"));
        SoundClip secondClip = new SoundClip(new File("e"));

        rootAlbum.addSubAlbum(movieClips);

        movieClips.addSoundClip(soundClip);
        movieClips.addSoundClip(secondClip);

        assertEquals(2, movieClips.getSoundClipsCopy().size());

        movieClips.removeSoundClip(secondClip);

        assertEquals(1, movieClips.getSoundClipsCopy().size());
        assertEquals(2, rootAlbum.getSoundClipsCopy().size());
    }

}
