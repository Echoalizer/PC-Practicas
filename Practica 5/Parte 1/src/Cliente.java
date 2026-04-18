import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    public static void main (String[] args) {
        String IP = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            Socket s = new Socket(IP, port);
            System.out.printf("open at %s:%d\n", s.getLocalAddress(), s.getLocalPort());
            System.out.printf("connected to %s:%d\n", s.getInetAddress(), s.getPort());

            BufferedReader cin = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter cout = new PrintWriter(s.getOutputStream());

            String msg = cin.readLine();

            System.out.println(msg);

            cout.println("adios");
            cout.flush();

            cout.close();
            cin.close();
            s.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
