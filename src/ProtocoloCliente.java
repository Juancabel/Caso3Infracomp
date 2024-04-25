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
    throws IOException,Exception{
        //Recibe el mensaje del usuario
        System.out.println("Comenzando Proceso...");

        //Envia la palabra Reto
        pOut.println("Reto");

        String fromServer = pIn.readLine();

        if((fromServer) != null){
            //Verifica que se devuelva Reto con la llave publica del servidor. 
            pOut.println("OK");
        }

        //Recibe P,G y Y para Diffie Hellman
        fromServer = pIn.readLine();
        String [] data = fromServer.split(",");
        int p = Integer.parseInt(data[0]);
        int g = Integer.parseInt(data[1]);
        int y_servidor = Integer.parseInt(data[2]);
        int iv = Integer.parseInt(data[3]); // No estoy seguro si esto es un int xd 

        //Genera su propio X y su propio Y para Diffie Hellman

        int x =0;
        int y_propio =0;

        //Envia su propio Y y calcula la clave simetrica Z
        pOut.println(y_propio);
        int z = 0;

        //Hace un hash de Z: La mitad es para la llave de cifrado y la otra para el mensaje de autenticacion
        int K_AB1 =0;
        int K_AB2 =0;

        fromServer=pIn.readLine();
        if(!fromServer.equals("CONTINUAR")){
            throw new Exception("Error");
        }

        String login = "login";
        String contrasena = "contrasena";

        String login_cifrado = "";
        String contrasena_cifrado = "";

        //Envia la contraseña y login cifrados simetricamente con K_AB1
        pOut.println(login_cifrado + "," + contrasena_cifrado);

        fromServer=pIn.readLine();
        if(!fromServer.equals("OK")){
            throw new Exception("Error");
        }

        //Envia la consulta cifrada con K_AB1 y el HMAC con K_AB2
        String consulta_cifrada = "";
        String hmac_cifrado = "";
        pOut.println(consulta_cifrada + "," + hmac_cifrado);

        //Recibe la respues y el HMAC de la consulta y los descifra 
        String respuesta = pIn.readLine();
        String[] resp = respuesta.split(",");

        String respuesta_cons = resp[0];
        String respuesta_hmac = resp[1];

        System.out.println(respuesta_cons);












    }
}
