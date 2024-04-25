import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ProtocoloServidor {


    public static void procesar(BufferedReader pIn, PrintWriter pOut)
        throws IOException,Exception{
    String inputLine;
    String outputLine;

    //Lee el flujo de entrada
    inputLine = pIn.readLine();
    
    //Codifica la palabra "Reto" con su llave privada.
    outputLine = "Codificado";

    //Envia la palabra codificada para su verificacion. 
    pOut.println(outputLine);
    
    //Recibe la respuesta del cliente, si es "OK" continua, si no no. 
    inputLine = pIn.readLine();
    if (!inputLine.equals("OK")){
        throw new Exception("Hubo un error de verificacion");
    }

    //Genera G, P, X y su propio Y para Diffie-Hellman y los comunica. 
    int g = 0;
    int p = 0;
    int x =0;
    int y_propio =0;
    int iv = 0; //No estoy seguro si esto es un int 
    String codif="g,p y y_propio codificados";
    
    //Envia G,P y Y para Diffie-Hellman (Tambien en via)
    pOut.println(g + "," + p + "," + y_propio + "," + iv + "," + codif);
    
    //Recibe el Y del cliente y genera la clave simetrica z. 
    int y_cliente = Integer.parseInt(pIn.readLine());
    int z = 0;

    //Hace un hash de Z: La mitad es para la llave de cifrado K_AB1 y la otra para el mensaje de autenticacion K_AB2
    int K_AB1 =0;
    int K_AB2 =0;

    pOut.println("CONTINUAR");

    //Recibe el input y la contraseña cifrados con K_AB1, los descifra y verifica. 
    inputLine = pIn.readLine();
    String [] login_contra = inputLine.split(",");
    String login = login_contra[0];
    String contra = login_contra[1];

    pOut.println("OK");

    //Recibe la consulta cifrada con K_AB1 y el HMAC cifrado con K_AB2, las descifra y responde la consulta
    inputLine=pIn.readLine();
    String [] data = inputLine.split(",");
    String consulta = data[0];
    String HMAC = data[1];

    //Envia la respuesta y el HMAC cifrados con su respectiva llave de respuesta
    String respuesta_cons = "";
    String respuesta_HMAC = "";

    pOut.println(respuesta_cons + "," + respuesta_HMAC);






    }
}
