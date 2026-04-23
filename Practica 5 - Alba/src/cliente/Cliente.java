package cliente;

import mensajes.Conexion;
import mensajes.DesconexionCliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {


    private final String IP;
    private final int puerto;

    private final Socket s;
    private ObjectOutputStream fout;
    private ObjectInputStream fin;

    private OyenteServidor oyente;

    private Scanner in;

    public Cliente(int puerto, String IP, Socket s, ObjectOutputStream fout, ObjectInputStream fin, Scanner in, OyenteServidor oyente) {
        this.puerto = puerto;
        this.IP = IP;
        this.s = s;
        this.fout = fout;
        this.fin = fin;
        this.in = in;
        this.oyente = oyente;
    }


    public static void main(String args[]) {

        //Hay que crear el nuevo cliente -> Por lo que hay que pedir su info y crear el oyenteServidor
        Scanner in = new Scanner(System.in);
        //System.out.println("Introduce el puerto del servidor");
        //int puertoServidor = Integer.parseInt(in.nextLine());

        //System.out.println("Introduce la IP del cliente");
        String IPCliente = "localhost";

        Socket s = null;

        try {
            s = new Socket("localhost", 99);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ObjectOutputStream fout = null;
        ObjectInputStream fin = null;
        try {
            fout = new ObjectOutputStream(s.getOutputStream());
            fin = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        OyenteServidor oyente = null;

        try {
            oyente = new OyenteServidor("OS1", fout, fin);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Cliente cl = new Cliente(99, IPCliente, s, fout, fin, in, oyente);

        oyente.start();
        cl.run();

        //Ahora lo que hay que hacer es que ejecute el menu


    }


    public void run() {
        //Lo primero que va a hacer el cliente cuando se cree, es mandar un mensaje de que se ha conectado al servidor
        try {
            fout.writeObject(new Conexion());

        } catch (IOException e) {
            System.err.println("El cliente no ha podido mandar el mensaje de CONEXION");
        }


        //Menu
        try {
            menu();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //Se cierra el socket y los canales
        try {
            fin.close();
            fout.close();
            s.close();
            in.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void menu() throws ClassNotFoundException {

        int option = 0;

        while (option != -1) {

            System.out.println("Escoge una de las opciones: ");
            System.out.println("1. DESCONEXION CLIENTE");
            String str = in.nextLine();
            option = Integer.parseInt(str);

            switch (option) {

                case 1: //DECONEXION CLIENTE
                    System.out.println("El cliente se quiere desconectar");
                    try {
                        fout.writeObject(new DesconexionCliente());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    boolean desconectado = this.oyente.getDesconectado();
                    while (desconectado) {
                        desconectado = this.oyente.getDesconectado();
                    }


                    //Una vez se ha recibido por parte del servidor que se va a desconectar el cliente -> Se cambia de opcion
                    //para asi salir dle bucle y cerrar los sockets y todo
                    option = -1;


            }

        }


    }


}
