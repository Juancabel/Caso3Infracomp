import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;

public class ProtocoloCliente {

    //Esto es un place Holder
    private Random rnd = new Random();
    private static final BigInteger p = calcNumeros.P;
    private static final BigInteger g = calcNumeros.G;
    private BigInteger x = new BigInteger(1024, rnd);


    private BigInteger y = g.modPow(x, p);


    private ProtocoloCliente(){
        System.out.println(y);
    }


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
