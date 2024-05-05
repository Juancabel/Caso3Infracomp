import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;

public class ProtocoloServidor {

    private Random rnd = new Random();
    private static final BigInteger p = calcNumeros.P;
    private static final BigInteger g = calcNumeros.G;
    private BigInteger x = new BigInteger(1024, rnd);


    private BigInteger y = g.modPow(x, p);


    public static void procesar(BufferedReader pIn, PrintWriter pOut) throws IOException,Exception{
        


    }
}
