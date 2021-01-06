package main.soundfiles;

import java.util.LinkedList;
import java.util.List;

public class SoundClipBlockingQueue {

	private List<SoundClip> queue = new LinkedList<SoundClip>();

	public synchronized void enqueue(SoundClip item){
		queue.add(item);
	    notifyAll();
	}

	public synchronized SoundClip dequeue() throws InterruptedException{
		while(queue.size() == 0){
			wait();
		}
	    return queue.remove(0);
	}
}
