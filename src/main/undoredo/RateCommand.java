package main.undoredo;

import main.albums.AbstractAlbum;
import main.albums.RatedAlbum;
import main.organizer.MusicOrganizerWindow;
import main.soundfiles.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RateCommand implements UndoableCommand {

    private AbstractAlbum album;
    private RatedAlbum ratedAlbum;
    private Map<SoundClip, Integer> soundClipPreviousRating;
    private int rating;

    public RateCommand(Set<SoundClip> soundClips, int rating, AbstractAlbum currentAlbum, RatedAlbum ratedAlbum) {
        soundClipPreviousRating = new HashMap<>();
        soundClips.forEach(soundClip -> soundClipPreviousRating.put(soundClip, soundClip.getRating()));
        this.rating = rating;
        this.album = currentAlbum;
        this.ratedAlbum = ratedAlbum;
    }

    private void updateRatedAlbum(SoundClip soundClip, boolean add) {
        if (add) {
            ratedAlbum.addSoundClip(soundClip);
        } else {
            ratedAlbum.removeSoundClip(soundClip);
        }
    }

    @Override
    public AbstractAlbum execute(MusicOrganizerWindow view) {
        Set<SoundClip> soundClips = soundClipPreviousRating.keySet();
        for (SoundClip soundClip : soundClips) {

            soundClip.setRating(rating);
            updateRatedAlbum(soundClip, rating >= 4);
        }
        return album;
    }

    @Override
    public AbstractAlbum undo(MusicOrganizerWindow view) {
        Set<Map.Entry<SoundClip, Integer>> entries = soundClipPreviousRating.entrySet();
        for (Map.Entry<SoundClip, Integer> entry : entries) {
            SoundClip soundClip = entry.getKey();

            int oldRating = entry.getValue();
            soundClip.setRating(oldRating);

            updateRatedAlbum(soundClip, oldRating >= 4);
        }
        return album;
    }

    @Override
    public AbstractAlbum redo(MusicOrganizerWindow view) {
        return execute(view);
    }
}
