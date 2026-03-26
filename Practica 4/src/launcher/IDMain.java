package launcher;

import java.util.ArrayList;
import java.util.List;

import monitors.LockIDMonitor;
import monitors.MonitorID;

import util.ThreadMonitor;

class IDMain {

    public static void main(String[] args) {
    	
    	int k = Integer.parseInt(args[0]);
    	List<ThreadMonitor> lista = new ArrayList<>();
    	MonitorID monitor = new LockIDMonitor(0);
    	
    	ThreadMonitor hilo0 = new ThreadMonitor(0, k, monitor);
    	ThreadMonitor hilo1 = new ThreadMonitor(1, k, monitor);
		ThreadMonitor hilo2 = new ThreadMonitor(2, k, monitor);
    	
    	lista.add(hilo0);
    	lista.add(hilo1);
		lista.add(hilo2);
	
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