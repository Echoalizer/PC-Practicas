package launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import swift.Hilo;


public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int n;
		
		System.out.print("Introducir valor n: ");
		n = scan.nextInt();
		double[][] matriz1 = new double[n][n];
		double[][] matriz2 = new double[n][n];
		double[][] matriz3 = new double[n][n];
		
		System.out.println("Ingrese los valores de la matriz:");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print("Elemento [" + i + "][" + j + "]: ");
				matriz1[i][j] = scan.nextInt();
			}
		}
		
		
		System.out.println("Ingrese los valores de la matriz:");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print("Elemento [" + i + "][" + j + "]: ");
				matriz2[i][j] = scan.nextInt();
			}
		}
		
		scan.close();
		
		List<Hilo> lista = new ArrayList<Hilo>();
		for(int i = 0; i < n;i++) {
			Hilo hilo = new Hilo(matriz1,matriz2,matriz3,n,i) ;
			hilo.start();
			lista.add(hilo);
		}
		
		for(Hilo h: lista) {
			try {
				h.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		for (int i = 0; i < matriz1.length; i++) {  // Recorre filas
            for (int j = 0; j < matriz1[i].length; j++) {  // Recorre columnas
                System.out.print(matriz1[i][j] + "\t"); // Imprime con tabulación
            }
            System.out.println(); // Salto de línea para la siguiente fila
        }
		
		for (int i = 0; i < matriz2.length; i++) {  // Recorre filas
            for (int j = 0; j < matriz2[i].length; j++) {  // Recorre columnas
                System.out.print(matriz2[i][j] + "\t"); // Imprime con tabulación
            }
            System.out.println(); // Salto de línea para la siguiente fila
        }
		
		
		for (int i = 0; i < matriz3.length; i++) {  // Recorre filas
            for (int j = 0; j < matriz3[i].length; j++) {  // Recorre columnas
                System.out.print(matriz3[i][j] + "\t"); // Imprime con tabulación
            }
            System.out.println(); // Salto de línea para la siguiente fila
        }
		
	}

}
