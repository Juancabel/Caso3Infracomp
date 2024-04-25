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

        while (continuar){

            //continuar=false;
            //Crea el socket del lado del servidor
            //Queda bloqueado hasta que llega un cliente

            Socket socket = ss.accept();

            try {
                PrintWriter escritor = new PrintWriter(socket.getOutputStream(),true);
                BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //Ejecuta el protocolo del lado del servidor
                ProtocoloServidor.procesar(lector, escritor);

                //ProtocoloServidor.procesar(lector, escritor);

                escritor.close();
                lector.close();
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
