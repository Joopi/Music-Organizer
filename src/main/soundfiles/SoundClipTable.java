package main.soundfiles;

import main.albums.AbstractAlbum;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JList;

public class SoundClipTable extends JList {

	private List<SoundClip> clips;
	
	
	public SoundClipTable() {
		super();
		clips = new ArrayList<SoundClip>();
	}

	public void display(AbstractAlbum a){
		this.clearTable();

		if (a == null)
			return;

		clips.addAll(a.getSoundClipsCopy());
		
		Object[] data = new Object[clips.size()];
		Iterator<SoundClip> it = clips.iterator();
		int i = 0;
		while(it.hasNext()){
			SoundClip s = it.next();
			data[i++] = s.toString();
		}
		this.setListData(data);
		invalidate();
		validate();
		doLayout();
		repaint();
	}

	private void clearTable(){
		clips.removeAll(clips);
		this.setListData(new String[0]);
	}

	public List<SoundClip> getClips(int[] indices){
		List<SoundClip> l = new ArrayList<SoundClip>();
		for(int i=0;i<indices.length;i++){
			l.add(clips.get(indices[i]));
		}
		return l;
	}

}
