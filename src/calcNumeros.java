import java.math.BigInteger;
import java.security.*;
import javax.crypto.*;

public class calcNumeros {

    private static final String ALGORITMO_ASIMETRICO = "RSA";
    
    private static String STRING_P = "00:a8:82:fa:56:2f:55:59:51:04:4c:1d:5f:8f:b1:"+
    "c7:92:5f:51:13:ca:a7:86:93:e6:07:4e:86:cb:ff:"+
    "8a:c7:ae:0e:49:b2:19:3b:83:03:4e:b9:12:76:ff:"+
    "ed:2f:76:45:03:aa:ce:8f:af:2a:0f:cc:8a:67:9d:"+
    "e1:3c:63:66:b9:39:01:0d:8f:f3:a1:55:d7:a4:69:"+
    "33:f3:f3:d7:bc:12:5f:2c:44:5b:fe:54:dd:7a:2c:"+
    "cb:07:7c:06:58:a6:46:b2:95:ee:7e:a6:4e:2c:51:"+
    "f5:4e:f6:84:88:06:61:8f:dd:62:13:30:c1:be:7e:"+
    "cf:74:0a:0f:9d:2b:94:f9:27";

    private static String STRING_G ="02";

    public static final BigInteger P = new BigInteger(deHexaByte(STRING_P));
    public static final BigInteger G = new BigInteger(deHexaByte(STRING_G));

    private static byte[] deHexaByte(String pHex){
        String[] pList = pHex.split(":");
        byte[] rta = new byte[pList.length];

        for (int i=0;i<rta.length;i++){

            String hexNum = pList[i];

            int firstDigit = toDigit(hexNum.charAt(0));
            int secondDigit = toDigit(hexNum.charAt(1));

            byte posP = (byte) ((firstDigit << 4) + secondDigit);
            rta[i]=posP;
        }
        return rta;
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
              "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

    public static byte[] cifrarAsimetrico(Key llave, String algoritmo, String texto){
        byte[] textoCifrado;

        try {
            Cipher cifrador = Cipher.getInstance(algoritmo);
            byte[] textoClaro = texto.getBytes();

            cifrador.init(Cipher.ENCRYPT_MODE, llave);
            textoCifrado = cifrador.doFinal(textoClaro);

            return textoCifrado;
        } catch (Exception e) {
            System.out.println("Excepción: " + e.getMessage());
            return null;
        }
    }

    public static byte[] desifrarAsimetrico(Key llave, String algoritmo, byte[] texto){
        byte[] textoClaro;

        try {
            Cipher cifrador = Cipher.getInstance(algoritmo);
            cifrador.init(Cipher.DECRYPT_MODE, llave);
            textoClaro = cifrador.doFinal(texto);
        } catch (Exception e) {
            System.out.println("Excepción: " + e.getMessage());
            return null;
        }
        return textoClaro;
    }

}
