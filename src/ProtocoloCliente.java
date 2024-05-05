import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Random;
import java.security.*;
import javax.crypto.*;

public class ProtocoloCliente {


    public static void procesar(BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut) throws IOException,Exception{

        //CREATE DATA
        String dataString = "Data to be encrypted";
        byte[] dataBytes  = dataString.getBytes();
    
        //CREATE KEYS
        KeyPairGenerator keyPairGenerator;
    
        try{
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        }
        catch (Exception e ){
            throw new Exception("Algo sucedio");
        }
    
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        //Recibe el mensaje del usuario
        System.out.println("Comenzando Proceso...");
        String privKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        
        pOut.println(privKey);

    }
}
