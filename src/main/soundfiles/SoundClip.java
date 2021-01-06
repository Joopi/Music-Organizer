package main.soundfiles;

import java.io.File;

/**
 * SoundClip is a class representing a digital
 * sound clip file on disk.
 */
public class SoundClip {

	private final File file;
	private boolean isFlagged;
	private int rating;
	
	/**
	 * Make a SoundClip from a file.
	 * Requires file != null.
	 */
	public SoundClip(File file) {
		assert file != null;
		isFlagged = false;
		rating = -1;
		this.file = file;
	}

	/**
	 * @return the file containing this sound clip.
	 */
	public File getFile() {
		return file;
	}
	
	public String toString(){
		String name = file.getName();
		
		if (rating > -1)
			name += "   " + rating + "/5";

		if (isFlagged)
			name += "   F";
		
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		return 
			obj instanceof SoundClip
			&& ((SoundClip)obj).file.equals(file);
	}
	
	@Override
	public int hashCode() {
		return file.hashCode();
	}

	public void setFlag(boolean flag) {
		isFlagged = flag;
	}

	public boolean isFlagged() {
		return isFlagged;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getRating() {
		return this.rating;
	}

}
