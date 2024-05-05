import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Random;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.*;

public class ProtocoloServidor {

    private Random rnd = new Random();
    private static final BigInteger p = calcNumeros.P;
    private static final BigInteger g = calcNumeros.G;
    private BigInteger x = new BigInteger(1024, rnd);


    private BigInteger y = g.modPow(x, p);


    public static void procesar(BufferedReader pIn, PrintWriter pOut) throws IOException,Exception{
        
        String privKey = pIn.readLine();
        PrivateKey privateKey = null;
        
        try {
            String privateK = privKey;
            byte[] privateBytes = Base64.getDecoder().decode(privateK);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        System.out.println(privateKey);

        
        
    }
}
