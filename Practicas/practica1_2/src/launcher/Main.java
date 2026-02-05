package launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.Hilo;
import utils.Entero;

/* ---OBJETIVOS---
 * 1. Dos tipos de procesos en los que cada tipo hace una operacion distinta al otro tipo, x numero ode veces
 * 2. Se crea una clase Entero, que sera el tipo de la variable que todos los procesos modificaran
 * 3. El objetivo es que se vea claramente que al usar numeros relativamente grandes, hay CARRERA DE DATOS
 */


public class Main {

	
	public static void main(String[] args) {
		//Vamos a necesitar leer por consola la cantidad de hilos que vamos a crear
		Scanner scan = new Scanner(System.in);
		int numHilos;
		int numOper;
		
		//Ahora imprimiremos por consola un mensaje para que el usuario introduza un numero que 
		//correspondera con el numero de hilos a crear
		System.out.print("Introducir número de hilos a crear de cada tipo (M) ");
		//Coge la cadena introducida y la castea a int. Si no es posible -> ERROR
		numHilos = scan.nextInt();
		
		System.out.print("Introducir el numero de operaciones a realizar por cada proceso (N) ");		
		numOper = scan.nextInt();
		
		
		scan.close(); //Ya no vamos a leer mas asi que cerramos el scanner
		
		
		//Vamos a hacer una lista de procesos.
		List<Hilo> lista = new ArrayList<Hilo>();
		Entero ent = new Entero(0);
		
		for(int i = 0; i < numHilos; i++) {
			Hilo hilo0 = new Hilo(0, ent, numOper);
			Hilo hilo1 = new Hilo(1, ent, numOper);
			
			lista.add(hilo0);
			lista.add(hilo1);
			
		}
	
		for(Hilo i :lista)
			i.start();
		
		for(Hilo h:lista) {
			try {
				h.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		System.out.println(ent.get_valor());
		
	}

}

