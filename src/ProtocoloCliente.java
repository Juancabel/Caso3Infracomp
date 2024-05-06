import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public class ProtocoloCliente {

    private static final Random rnd = new Random();
    private static BigInteger p;
    private static BigInteger g;
    private static BigInteger y;
    private static BigInteger z;
    private static BigInteger iv;
    private static BigInteger p_moc;
    private static BigInteger g_moc;
    private static BigInteger y_moc;
    private static BigInteger iv_moc;
    private static BigInteger x;

    private static byte [] k_ab1;
    private static byte [] k_ab2;

    public static byte[] convertSringByte(String string){
        String [] separated = string.split(",");
        byte [] signature_bytes = new byte [separated.length];
        for (int i =0;i< separated.length;i++){
            int a = Integer.parseInt(separated[i]);
            signature_bytes[i] = (byte) a;
        }
        return signature_bytes;
    }
    public static String convertByteString(byte[] bytes){
        String message = "";
            for (int i=0;i<bytes.length;i++){
                if (i !=bytes.length-1){
                    message = message + bytes[i]+",";
                }
                else{
                    message = message + bytes[i]; 
                }
            }
        return message;
    }

    public static void procesar(BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut) throws IOException,Exception{

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

        
        System.out.println("Comenzando Proceso...");
        String privKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        pOut.println(privKey);
        pOut.println("Reto");
        
        String line;
        while ((line = pIn.readLine()) != null){
            long startTime = System.nanoTime();
            String signature = line;
            byte[] signature_bytes = convertSringByte(signature);
            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            publicSignature.initVerify(publicKey);
            publicSignature.update("Reto".getBytes(StandardCharsets.UTF_8));
            boolean isCorrect = publicSignature.verify(signature_bytes);
            if(isCorrect){
                pOut.println("OK");
            }
            else{
                pOut.println("ERROR");;                
            }
            long endTime = System.nanoTime() - startTime;
            System.out.println("Tiempo en nanosegundos para verificar la firma: " + endTime + "\n");
            break;
        }
        while ((line = pIn.readLine()) != null){
            String data = line;
            String[] data_partida = data.split(",");
            p_moc = new BigInteger(data_partida[0]);
            g_moc = new BigInteger(data_partida[1]);
            y_moc = new BigInteger(data_partida[2]);
            iv_moc = new BigInteger(data_partida[3]);
            break;
        }

        while ((line = pIn.readLine()) != null){
            String response = line;
            byte[] response_bytes = convertSringByte(response);
            Cipher decriptCipher = Cipher.getInstance("RSA");
            decriptCipher.init(Cipher.DECRYPT_MODE, publicKey);
            String decipheredMessage = new String(decriptCipher.doFinal(response_bytes), StandardCharsets.UTF_8);
            p = new BigInteger(decipheredMessage);
            break;
        }

        while ((line = pIn.readLine()) != null){
            String response = line;
            byte[] response_bytes = convertSringByte(response);
            Cipher decriptCipher = Cipher.getInstance("RSA");
            decriptCipher.init(Cipher.DECRYPT_MODE, publicKey);
            String decipheredMessage = new String(decriptCipher.doFinal(response_bytes), StandardCharsets.UTF_8);
            String [] divided = decipheredMessage.split(",");
            g = new BigInteger(divided[0]);
            iv = new BigInteger(divided[1]);
            break;
        }
        while ((line = pIn.readLine()) != null){
            String response = line;
            byte[] response_bytes = convertSringByte(response);
            Cipher decriptCipher = Cipher.getInstance("RSA");
            decriptCipher.init(Cipher.DECRYPT_MODE, publicKey);
            String decipheredMessage = new String(decriptCipher.doFinal(response_bytes), StandardCharsets.UTF_8);
            y = new BigInteger(decipheredMessage);
            break;
        }
        x = new BigInteger(1024, rnd);
        if (p.equals(p_moc) && g.equals(g_moc) && y.equals(y_moc) && iv.equals(iv_moc)){
            pOut.println("OK");

            long startTime = System.nanoTime();
            pOut.println(g.modPow(x, p));
            long endTime = System.nanoTime() - startTime;
            System.out.println("Tiempo en nanosegundos para calcular Gy: " + endTime + "\n");

            z = y.modPow(x, p);
        }
        else{
            pOut.println("ERROR");
        }
        try { 
            MessageDigest md = MessageDigest.getInstance("SHA-512"); 
            byte[] messageDigest = md.digest(z.toString().getBytes()); 
            k_ab1 = new byte[32];
            k_ab2 = new byte[32];
            System.arraycopy(messageDigest, 0, k_ab1, 0, 32);
            System.arraycopy(messageDigest, 16, k_ab2, 0, 32);

        } catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        }
        while ((line = pIn.readLine()) != null){
            SecretKeySpec secretKeySpec = new SecretKeySpec(k_ab1, "AES");
            byte[] bte = iv.toByteArray();
            byte[] bytes = new byte[16];
            System.arraycopy(bte, 0, bytes, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] login_encrypted = cipher.doFinal("Juanito".getBytes());
            byte[] contra_encrypted = cipher.doFinal("1234".getBytes());
            String login = convertByteString(login_encrypted);
            String contra = convertByteString(contra_encrypted);
            String message = login + ";" + contra;
            pOut.println(message);
            break;
        }
        while ((line = pIn.readLine()) != null){
            if(line.equals("ERROR")){
                throw new Exception("Hubo un error en la verificacion:");
            }
            Scanner in = new Scanner(System.in);
            System.out.println("Ingrese el numero que desea restarle 2: ");
            String query = in.nextLine().strip();
            SecretKeySpec secretKeySpec = new SecretKeySpec(k_ab1, "AES");
            byte[] bte = iv.toByteArray();
            byte[] bytes = new byte[16];
            System.arraycopy(bte, 0, bytes, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            long startTime = System.nanoTime();
            byte[] query_cifrado = cipher.doFinal(query.getBytes());
            long endTime = System.nanoTime() - startTime;
            System.out.println("Tiempo en nanosegundos para cifrar la consulta: " + endTime + "\n");

            String query_message = convertByteString(query_cifrado);
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(k_ab2, "HmacSHA256");
            sha256_HMAC.init(secret_key);

            startTime = System.nanoTime();
            byte[] hmac_cifrado = sha256_HMAC.doFinal(query.getBytes());
            endTime = System.nanoTime() - startTime;
            System.out.println("Tiempo en nanosegundos para generar el codigo de autenticación: " + endTime + "\n");

            String hmac_message = convertByteString(hmac_cifrado);
            pOut.println(query_message+";"+hmac_message);
            break;
        }
        while ((line = pIn.readLine()) != null){
            String message = line;
            String [] rta_hmac = message.split(";");
            byte [] rta_cifrada = convertSringByte(rta_hmac[0]);
            SecretKeySpec secretKeySpec = new SecretKeySpec(k_ab1, "AES");
            byte[] bte = iv.toByteArray();
            byte[] bytes = new byte[16];
            System.arraycopy(bte, 0, bytes, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bytes);
            Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] rta_real = cipherDecrypt.doFinal(rta_cifrada);
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(k_ab2, "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hmac_real = sha256_HMAC.doFinal(rta_real);
            if (convertByteString(hmac_real).equals(rta_hmac[1])){
                System.out.println("La respuesta recibida del servidor es: " + new String(rta_real));
            }
            else{
                System.out.println("Hubo un error de verificacion con el HMAC del mensaje");
            }
            break;
        }
    }
}