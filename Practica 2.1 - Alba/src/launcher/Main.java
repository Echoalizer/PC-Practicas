package launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import swift.LockRompeEmpate;
import swift.MiHilo;
import utils.Entero;

public class Main {
	
	public static void main(String[] args) {	
		Entero num = new Entero(0);
		Scanner scan = new Scanner(System.in);
		int numOperaciones;
		
		System.out.print("Introducir cantidad de operaciones ");
		numOperaciones = scan.nextInt();
		
		scan.close();
		
		List<MiHilo> lista = new ArrayList<MiHilo>();
		LockRompeEmpate lock= new LockRompeEmpate();
		
		MiHilo hilo0 = new MiHilo(0,num,numOperaciones,lock);
		MiHilo hilo1 = new MiHilo(1,num,numOperaciones,lock);
	
		lista.add(hilo0);
		lista.add(hilo1);
			
		
		
		for(MiHilo i : lista) {
			i.start();
		}
		

		for(MiHilo h : lista) {
			try {
				h.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(num.get_valor());
		
	}


}
