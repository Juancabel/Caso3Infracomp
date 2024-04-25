import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static final int PUERTO = 3400;
    public static final String SERVIDOR = "localhost";
    
    public static void main(String[] args) throws Exception{
        Socket socket = null;
        PrintWriter escritor = null;
        BufferedReader lector = null;

        System.out.println("Cliente ...");

        try {
            //Crea el socket del lado del cliente
            socket = new Socket(SERVIDOR,PUERTO);

            //Se conectan los flujos, tanto de salida como de entrada
            escritor = new PrintWriter(socket.getOutputStream(),true);
            lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        //Se ejecuta el protocolo por el lado del cliente
        ProtocoloCliente.procesar(stdIn,lector,escritor);

        //ProtocoloCliente.procesar(stdIn,lector,escritor);

        //Se cierran los flujos y el socket
        socket.close();
        escritor.close();
        lector.close();

    }
}
