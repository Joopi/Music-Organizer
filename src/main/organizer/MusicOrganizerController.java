package main.organizer;

import main.undoredo.*;
import main.soundfiles.SoundClip;
import main.soundfiles.SoundClipBlockingQueue;
import main.soundfiles.SoundClipPlayer;
import main.soundfiles.SoundClipLoader;
import main.albums.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MusicOrganizerController {

	private MusicOrganizerWindow view;
	private SoundClipBlockingQueue queue;
	private Album root;
	private UndoCommandController undoController;
	private RatedAlbum ratedAlbum;
	private FlaggedAlbum flaggedAlbum;

	public MusicOrganizerController() {
		
		// Create the root album for all sound clips
		root = new Album("All Sound Clips");

		ratedAlbum = new RatedAlbum("Great Sound Clips");

		flaggedAlbum = new FlaggedAlbum("Flagged Sound Clips");
		
		// Create the View in Model-View-Controller
		view = new MusicOrganizerWindow(this);
		
		// Create the blocking queue
		queue = new SoundClipBlockingQueue();

		// Create undo/redo controller
		undoController = new UndoCommandController(10, view);

		// Create a separate thread for the sound clip player and start it
		(new Thread(new SoundClipPlayer(queue))).start();
	}

	/**
	 * Load the sound clips found in all subfolders of a path on disk. If path is not
	 * an actual folder on disk, has no effect.
	 */
	public Set<SoundClip> loadSoundClips(String path) {
		Set<SoundClip> clips = SoundClipLoader.loadSoundClips(path);

		root.addSoundClips(clips);

		return clips;
	}
	
	/**
	 * Returns the root album
	 */
	public Album getRootAlbum(){
		return root;
	}
	
	/**
	 * Adds an album to the Music Organizer
	 */
	public void addNewAlbum(Album parentAlbum){
		if (parentAlbum == null) {
			view.showMessage("Please choose an album before adding a sub album");
			return;
		}

		String albumName = view.promptForAlbumName();
		if (albumName == null || albumName.isEmpty()) {
			view.showMessage("Please enter a valid name for the album");
			return;
		}

		Album subAlbum = new Album(albumName);
		undoController.addCommandAndExecute(new AddAlbumCommand(parentAlbum, subAlbum));
	}
	
	/**
	 * Removes an album from the Music Organizer
	 */
	public void deleteAlbum(Album album){
		if (album == null) {
			view.showMessage("What?");
			return;
		}

		Album parentAlbum = album.getParentAlbum();
		if (parentAlbum == null) {
			view.showMessage("Do not delete the parent album");
			return;
		}
		undoController.addCommandAndExecute(new RemoveAlbumCommand(parentAlbum, album));
	}
	
	/**
	 * Adds sound clips to an album
	 */
	public void addSoundClips(Album album, List<SoundClip> clips) {
		if (album == null)
			return;

		Set<SoundClip> soundClips = new LinkedHashSet<>(clips);
		soundClips.removeAll(album.getSoundClipsCopy());

		//så att vi inte skapar onödiga undo/redo entries ifall att albumet redan innehåller alla clips.
		if (soundClips.isEmpty())
			return;

		undoController.addCommandAndExecute(new AddClipsCommand(album, soundClips)); //addCommand automatically executes the command
	}
	
	/**
	 * Removes sound clips from an album
	 */
	public void removeSoundClips(Album album, List<SoundClip> clips) {
		if (album == null)
			return;

		Album parentAlbum = album.getParentAlbum();
		if (parentAlbum == null) {
			view.showMessage("Do not remove sound clips from 'All Sound Clips'");
			return;
		}

		Set<SoundClip> soundClips = new LinkedHashSet<>(clips);

		//RetainAll så att vi inte kan "trolla fram" nya ljudfiler i ett album när vi senare trycker på undo.
		soundClips.retainAll(album.getSoundClipsCopy());

		if (soundClips.isEmpty())
			return;

		undoController.addCommandAndExecute(new RemoveClipsCommand(album, soundClips));
	}

	public void undo() {
		undoController.undo();
	}

	public void redo(){
		undoController.redo();
	}

	public RatedAlbum getRatedAlbum() { return ratedAlbum; }

	public FlaggedAlbum getFlaggedAlbum() { return flaggedAlbum; }

	public void flagSoundClips() {
		Set<SoundClip> soundClips = new HashSet<>(view.getSelectedSoundClips());
		undoController.addCommandAndExecute(new FlagCommand(soundClips, view.getSelectedAlbum(), flaggedAlbum));
	}

	public void rateSoundClips() {
		Set<SoundClip> soundClips = new HashSet<>(view.getSelectedSoundClips());
		if (!soundClips.isEmpty()) {
			int rating = view.promptForRating();
			if (rating > -1 && rating < 6) {
				undoController.addCommandAndExecute(new RateCommand(soundClips, rating, view.getSelectedAlbum(), ratedAlbum));
			}
		}
	}
	
	/**
	 * Puts the selected sound clips on the queue and lets
	 * the sound clip player thread play them. Essentially, when
	 * this method is called, the selected sound clips in the 
	 * SoundClipTable are played.
	 */
	public void playSoundClips(){
		List<SoundClip> l = view.getSelectedSoundClips();
		for(int i=0;i<l.size();i++)
			queue.enqueue(l.get(i));
	}
}
