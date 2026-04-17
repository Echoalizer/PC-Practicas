import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main (String[] args) {
//        while (true) {
            try {
                ServerSocket listen = new ServerSocket(99);
                System.out.println("server ready");
                Socket ss = listen.accept();
                System.out.println("connection successful");


                // Codigo oyente
                PrintWriter fout = new PrintWriter(ss.getOutputStream());
                BufferedReader fin = new BufferedReader(new InputStreamReader(ss.getInputStream()));

                fout.println("hola");
                fout.flush();
                String msg = fin.readLine();

                System.out.println(msg);

                fout.close();
                fin.close();
                ss.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//        }

    }
}
