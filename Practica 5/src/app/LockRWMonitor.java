package app;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockRWMonitor implements RW {
	private final ReentrantLock l;
	private final Condition okToRead;
	private final Condition okToWrite;

	private int nr;
	private int nw;
	
	public LockRWMonitor() {
		l = new ReentrantLock(true);
		okToRead = l.newCondition();
		okToWrite = l.newCondition();
		nr = 0;
		nw = 0;
	}
	
	@Override
	public void request_read() throws InterruptedException {
		l.lock();
		
		while(nw > 0)
			okToRead.await();
		nr++;
		
		l.unlock();
	}

	@Override
	public void request_write() throws InterruptedException {
		l.lock();
		
		while(nr > 0 || nw > 0)
			okToWrite.await();
		nw++;
		
		l.unlock();
	}

	@Override
	public void release_read() throws InterruptedException {
		l.lock();
		
		nr--;
		if(nr== 0)
			okToWrite.signal();
		
		l.unlock();
	}

	@Override
	public void release_write() throws InterruptedException {
		l.lock();
		
		nw--;
		okToWrite.signal();
		okToRead.signalAll();
		
		l.unlock();
	}

}
