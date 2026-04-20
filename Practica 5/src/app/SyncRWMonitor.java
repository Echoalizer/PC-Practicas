package app;

public class SyncRWMonitor implements RW {
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
	public synchronized void release_read() throws InterruptedException {
		nr--;
		if(nr == 0)
			notifyAll();
	}

	@Override
	public synchronized void release_write() throws InterruptedException {
		nw--;
		notifyAll();
	}
}
