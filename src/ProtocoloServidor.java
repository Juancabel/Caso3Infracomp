import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ProtocoloServidor {

    private static final Random rnd = new Random();
    private static final BigInteger p = calcNumeros.P;
    private static final BigInteger g = calcNumeros.G;
    private static BigInteger z;
    private static BigInteger y_cliente;
    private static final BigInteger x = new BigInteger(1024, rnd);
    private static final BigInteger iv = new BigInteger(128,rnd);
    private static byte [] k_ab1;
    private static byte [] k_ab2;
    private static String login_guardado = "Juanito";
    private static String contra_guardado = "1234";



    private static final BigInteger y = g.modPow(x, p);

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

    public static void procesar(BufferedReader pIn, PrintWriter pOut) throws IOException,Exception{
        
        String line;
        PrivateKey privateKey = null;

        while ((line = pIn.readLine()) != null) {
            long startTime = System.nanoTime();
            String privKey = line;
            try {
                String privateK = privKey;
                byte[] privateBytes = Base64.getDecoder().decode(privateK);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                privateKey = keyFactory.generatePrivate(keySpec);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            long endTime = System.nanoTime() - startTime;
            System.out.println("Tiempo en nanosegundos para generar la firma: " + endTime + "\n");
            break;
         }
         while ((line = pIn.readLine()) != null){
            String reto = line;
            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(privateKey);
            privateSignature.update(reto.getBytes(StandardCharsets.UTF_8));
    
            byte[] signature = privateSignature.sign();
            String message = convertByteString(signature);
            pOut.println(message);
            break;
        }
        while ((line = pIn.readLine()) != null){
            if(line.equals("OK")){
                pOut.println(p+","+g+","+y+","+iv);
                String message = p.toString();
                Cipher encryptCipher = Cipher.getInstance("RSA");
                encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);
                byte[] cipherText = encryptCipher.doFinal(message.getBytes());
                String diffie = convertByteString(cipherText);
                pOut.println(diffie);
                String message_2 = g + "," + iv;
                Cipher encryptCipher_2 = Cipher.getInstance("RSA");
                encryptCipher_2.init(Cipher.ENCRYPT_MODE, privateKey);
                byte[] cipherText_2 = encryptCipher.doFinal(message_2.getBytes());
                String diffie_2 = convertByteString(cipherText_2);
                pOut.println(diffie_2);
                String message_3 = y.toString();
                Cipher encryptCipher_3 = Cipher.getInstance("RSA");
                encryptCipher_3.init(Cipher.ENCRYPT_MODE, privateKey);
                byte[] cipherText_3 = encryptCipher.doFinal(message_3.getBytes());
                String diffie_3 = convertByteString(cipherText_3);
                pOut.println(diffie_3);
                break;
            }
            else if(line.equals("ERROR")){
                throw new Exception("Hubo un error en de verificacion");
            }
        }
        while ((line = pIn.readLine()) != null){
            if (line=="ERROR"){
                throw new Exception("Hubo problemas de verificacion");
            }
            break;
        }
        while ((line = pIn.readLine()) != null){
            y_cliente = new BigInteger(line);
            z = y_cliente.modPow(x, p);
            break;
        }
        try { 
            MessageDigest md = MessageDigest.getInstance("SHA-512"); 
            byte[] messageDigest = md.digest(z.toString().getBytes()); 
            k_ab1 = new byte[32];
            k_ab2 = new byte[32];
            System.arraycopy(messageDigest, 0, k_ab1, 0, 32);
            System.arraycopy(messageDigest, 16, k_ab2, 0, 32);
            pOut.println("CONTINUAR");
        } catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
        while ((line = pIn.readLine()) != null){
            String message = line;
            String [] logincontra = message.split(";");
            SecretKeySpec secretKeySpec = new SecretKeySpec(k_ab1, "AES");
            byte[] bte = iv.toByteArray();
            byte[] bytes = new byte[16];
            System.arraycopy(bte, 0, bytes, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bytes);
            byte [] login = convertSringByte(logincontra[0]);
            byte [] contra = convertSringByte(logincontra[1]);
            Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] login_descifrado = cipherDecrypt.doFinal(login);
            byte[] contra_descifrado = cipherDecrypt.doFinal(contra);
            String login_real = new String(login_descifrado, StandardCharsets.UTF_8);
            String contra_real = new String(contra_descifrado, StandardCharsets.UTF_8);
            if(login_real.equals(login_guardado)|| contra_real.equals(contra_guardado)){
                pOut.println("OK");
                break;
            }
            else{
                pOut.println("ERROR");
            }
        }
        while ((line = pIn.readLine()) != null){
            
            SecretKeySpec secretKeySpec = new SecretKeySpec(k_ab1, "AES");
            byte[] bte = iv.toByteArray();
            byte[] bytes = new byte[16];
            System.arraycopy(bte, 0, bytes, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bytes);
            String qh = line;
            String [] queryhmac = qh.split(";");
            byte [] query_cifrado = convertSringByte(queryhmac[0]);


            long startTime = System.nanoTime();
            Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
            long endTime = System.nanoTime() - startTime;
            System.out.println("Tiempo en nanosegundos para descifrar la consulta: " + endTime + "\n");


            cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decrypted = cipherDecrypt.doFinal(query_cifrado);
            String query_real = new String(decrypted);
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(k_ab2, "HmacSHA256");
            sha256_HMAC.init(secret_key);

            startTime = System.nanoTime();
            byte[] hmac_real = sha256_HMAC.doFinal(query_real.getBytes());
            if(convertByteString(hmac_real).equals(queryhmac[1])){
                Integer ans = Integer.parseInt(query_real) - 2;
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
                byte[] ans_cifrado = cipher.doFinal(ans.toString().getBytes());
                String ans_msg = convertByteString(ans_cifrado);
                byte[] ans_hmac = sha256_HMAC.doFinal(ans.toString().getBytes());
                String hmac_msg = convertByteString(ans_hmac);
                pOut.println(ans_msg+";"+hmac_msg);
            }
            else{
                throw new Exception("Hubo un error de verificacion con el HMAC del mensaje");
            }

            endTime = System.nanoTime() - startTime;
            System.out.println("Tiempo en nanosegundos para verificar el codigo de autenticación: " + endTime + "\n");
        }
    }
}
