package main.soundfiles;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

/**
 * SoundClipLoader finds sound clip files on disk.
 */
public class SoundClipLoader {

	public static Set<SoundClip> loadSoundClips(String path) {
		assert path!= null && !path.isEmpty();
		Set<SoundClip> set = new HashSet<>();

		if (path == null) return set;

		File f = new File(path);
		if (!f.isDirectory()) return set;

		addSoundClipsToSet(f, set);
		return set;
	}

	private static void addSoundClipsToSet(File folder, Set<SoundClip> set) {
		for (File f : findWAVFiles(folder)) {
			System.out.println("Loading... " + f.getAbsolutePath());
			set.add(new SoundClip(f));
		}
		for (File g: findSubFolders(folder)) {
			addSoundClipsToSet(g, set);
		}
	}

	private static File[] findWAVFiles(File folder) {
		return folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				int i = name.lastIndexOf ('.');
				return "wav".equals(name.substring(i+1).toLowerCase());
			}
		});
	}

	private static File[] findSubFolders(File folder) {
		return folder.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return (pathname.isDirectory()); 
			}
		});
	}
}
