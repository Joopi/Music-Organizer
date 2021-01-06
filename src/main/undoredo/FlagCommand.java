package main.undoredo;

import main.albums.AbstractAlbum;
import main.albums.FlaggedAlbum;
import main.organizer.MusicOrganizerWindow;
import main.soundfiles.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FlagCommand implements UndoableCommand {

    private AbstractAlbum album;
    private FlaggedAlbum flaggedAlbum;
    private Map<SoundClip, Boolean> soundClipWasFlagged;

    public FlagCommand(Set<SoundClip> soundClips, AbstractAlbum currentAlbum, FlaggedAlbum flaggedAlbum) {
        soundClipWasFlagged = new HashMap<>();
        soundClips.forEach(soundClip -> soundClipWasFlagged.put(soundClip, soundClip.isFlagged()));
        this.album = currentAlbum;
        this.flaggedAlbum = flaggedAlbum;
    }

    private void updateFlaggedAlbum(SoundClip soundClip, boolean add) {
        if (add) {
            flaggedAlbum.addSoundClip(soundClip);
        } else {
            flaggedAlbum.removeSoundClip(soundClip);
        }
    }

    @Override
    public AbstractAlbum execute(MusicOrganizerWindow view) {
        Set<Map.Entry<SoundClip, Boolean>> entries = soundClipWasFlagged.entrySet();
        for (Map.Entry<SoundClip, Boolean> entry : entries) {
            boolean newFlag = !entry.getValue();

            SoundClip soundClip = entry.getKey();
            soundClip.setFlag(newFlag);

            updateFlaggedAlbum(soundClip, newFlag);
        }
        return album;
    }

    @Override
    public AbstractAlbum undo(MusicOrganizerWindow view) {
        Set<Map.Entry<SoundClip, Boolean>> entries = soundClipWasFlagged.entrySet();
        for (Map.Entry<SoundClip, Boolean> entry : entries) {
            boolean flag = entry.getValue();

            SoundClip soundClip = entry.getKey();
            soundClip.setFlag(flag);

            updateFlaggedAlbum(soundClip, flag);
        }
    
        return album;
    }

    @Override
    public AbstractAlbum redo(MusicOrganizerWindow view) {
        return execute(view);
    }
}
