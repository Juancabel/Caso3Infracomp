import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Key;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.util.Scanner;

import javax.crypto.Cipher;

public class borrarJAVA {

    private static final String ALGORITMO = "RSA";
    public static void main(String[] args) {
        new borrarJAVA();
    }

    public borrarJAVA(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Escriba un mensaje de texto: ");
        String textoUsuario = sc.nextLine();
        sc.close();

        System.out.println("Input en texto plano: " + textoUsuario);
        
        System.out.println("Input en bytes[]: ");
        System.out.println(textoUsuario.getBytes());
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITMO);
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        PublicKey llavePublica = keyPair.getPublic();
        PrivateKey llavePrivada = keyPair.getPrivate();

        byte[] textoCifrado = cifrarAsimetrico(llavePublica, ALGORITMO, textoUsuario);
        System.out.println("Input cifrado en RSA con Llaves de 1024 bits en byte[]: ");
        imprimir(textoCifrado);

        byte[] textoDescifrado = desifrarAsimetrico(llavePrivada, ALGORITMO, textoCifrado);
        System.out.println("Input descifrado en bytes[]: ");
        imprimir(textoDescifrado);

        String textoString = new String(textoDescifrado);
        System.out.println("Input descifrado convertido a texto plano: " + textoString);

    }

    public static void imprimir(byte[] contenido){
        int i = 0;
        for(;i < contenido.length-1;i++){
            System.out.print(contenido[i] + " ");
        }System.out.println(contenido[i] + " ");
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
