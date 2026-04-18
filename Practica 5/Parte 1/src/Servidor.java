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
                System.out.printf("server reachable at %s:%d\n", listen.getInetAddress(), listen.getLocalPort());
                Socket ss = listen.accept();
                System.out.printf("connection successful with %s:%d\n", ss.getInetAddress(), ss.getPort());


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
