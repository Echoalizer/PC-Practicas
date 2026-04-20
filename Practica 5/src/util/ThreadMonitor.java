package util;

import monitors.MonitorID;

public class ThreadMonitor extends Thread {
    private final int id;
    private final int iterations;
    private final MonitorID monitor;


    public ThreadMonitor(int id, int iterations, MonitorID m) {
        this.id = id;
        this.iterations = iterations;
        this.monitor = m;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            if (id == 2) {
                System.out.println(monitor.getValue());
            }
            else if (this.id % 2 == 0) {
            	this.monitor.increment();
            }
            else {
            	this.monitor.decrement();
            }           
        }
    }

}
