import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;

public class Servidor {
    public static final int PUERTO = 3400;

    public static void main(String[] args) throws IOException{
        ServerSocket ss = null;
        boolean continuar = true;

        System.out.println("Main Server...");

        try{
            ss = new ServerSocket(PUERTO);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        int numThreads = 0;

        while (continuar){

            //continuar=false;
            //Crea el socket del lado del servidor
            //Queda bloqueado hasta que llega un cliente

            Socket socket = ss.accept();

            ThreadServidor thread = new ThreadServidor(socket, numThreads);
            numThreads++;

            thread.start();
        }
    }
    
}
