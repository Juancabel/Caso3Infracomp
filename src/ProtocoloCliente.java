import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ProtocoloCliente {
    public static void procesar(BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut)
    throws IOException{
        //Recibe el mensaje del usuario
        System.out.println("Escriba el mensaje a enviar:");
        String fromUser = stdIn.readLine();

        //Envia por la red
        pOut.println(fromUser);

        String fromServer = "";

        //Lee lo que llega del servidor
        //Verifica que no sea nulo
        if((fromServer=pIn.readLine()) != null){
            System.out.println("Respuesta del servidor " + fromServer);
        }
    }
}
