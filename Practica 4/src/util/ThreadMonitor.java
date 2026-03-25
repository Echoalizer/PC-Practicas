package util;

import monitors.MonitorIncDec;

public class ThreadMonitor extends Thread {
    private final int id;
    private final int iterations;
    private final MonitorIncDec monitor;


    public ThreadMonitor(int id, int iterations, MonitorIncDec m) {
        this.id = id;
        this.iterations = iterations;
        this.monitor = m;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            if(this.id % 2 == 0) {
            	this.monitor.increment();
            }
            else {
            	this.monitor.decrement();
            }           
        }
    }

}
