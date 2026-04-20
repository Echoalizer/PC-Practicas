package models.readersWriters;

import control.ReadWriteController;

public class SyncRWMonitor implements ReadWriteController {
	private int nr = 0;
	private int nw = 0;
	
	@Override
	public synchronized void request_read() throws InterruptedException {
		while(nw > 0)
			wait();
		nr++;
	}

	@Override
	public synchronized void request_write() throws InterruptedException {
		while(nr > 0 || nw > 0)
			wait();
		nw++;
	}

	@Override
	public synchronized void release_read() {
		nr--;
		if(nr == 0)
			notifyAll();
	}

	@Override
	public synchronized void release_write() {
		nw--;
		notifyAll();
	}
}
