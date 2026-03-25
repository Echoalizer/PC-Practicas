package launcher;

import java.util.ArrayList;
import java.util.List;

import monitors.MonitorSync;
import monitors.MonitorLC;
import monitors.MonitorIncDec;

import util.ThreadMonitor;

class IncrementDecrementSync {

    public static void main(String[] args) {
    	
    	int k = Integer.parseInt(args[0]);
    	List<ThreadMonitor> lista = new ArrayList<ThreadMonitor>();
    	MonitorIncDec monitor = new MonitorLC(0);
    	
    	ThreadMonitor hilo0 = new ThreadMonitor(0, k, monitor);
    	ThreadMonitor hilo1 = new ThreadMonitor(1, k, monitor);
    	
    	lista.add(hilo0);
    	lista.add(hilo1);
	
    	for(var i : lista) {
    		i.start();
    	}
    	
    	
    	for(var i : lista) {
    		try {
				i.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
		System.out.println(monitor.getValue());

    	
    	
    }
}