import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ProtocoloServidor {

    public static void procesar(BufferedReader pIn, PrintWriter pOut)
        throws IOException{
    String inputLine;
    String outputLine;

    //Lee el flujo de entrada
    inputLine = pIn.readLine();
    Integer num = Integer.parseInt(inputLine);
    System.out.println("Entrada a procesar: " + num);

    //Procesa la entrada
    outputLine = String.valueOf(num-1);

    //Escribe en el flujo de salida
    pOut.println(outputLine);
    System.out.println("Salida procesada: " + outputLine);

    }
}
