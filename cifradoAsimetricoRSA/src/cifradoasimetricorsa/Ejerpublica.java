
package cifradoasimetricorsa;

import java.io.File; //Representa archivos
import java.io.FileInputStream; //Leer bytes de un archivo
import java.io.FileNotFoundException; // Excepción si no existe el archivo
import java.io.FileOutputStream; // Escribir bytes a un archivo
import java.io.PrintWriter; // Escribir texto en un archivo
import java.security.KeyFactory; // Convertir claves entre forma4tos
import java.security.KeyPair; // Contiene un par de claves
import java.security.KeyPairGenerator; // Genera pares de claves
import java.security.spec.RSAPublicKeySpec; // Parámetros de clave pública
import javax.crypto.Cipher; // Cifrado/descifrado de datos

public class Ejerpublica{

    public static void main(String[] args) throws FileNotFoundException {
        try {
            //Se crea un generador de claves RSA
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            
            //Se genera las claves (publica y privada)
            KeyPair keypair = keygen.generateKeyPair();
            
            //Se crea un objeto Cipher para el cifrado RSA
            Cipher desCipher = Cipher.getInstance("RSA");
            //Inicia el cifrador en modo encriptar usando la clave privada
            desCipher.init(Cipher.ENCRYPT_MODE, keypair.getPrivate());
            //Abir el archivo que queremos cifrar
            File inf = new File("src/cifradoasimetricorsa/fichero_prueba.txt");
            FileInputStream is = new FileInputStream(inf);
            //Abrir el archivo de salida para guardar el archivo cifrado
            try (FileOutputStream os = new FileOutputStream("fichero_cifrado_con_RSA")) {

                byte[] buffer = new byte[64]; //Leer el archivo en bloques de 64 bytes
                int bytes_leidos = is.read(buffer);
                while (bytes_leidos != -1) { //Cifrar el bloque leido
                    byte[] cbuf = desCipher.doFinal(buffer, 0, bytes_leidos);
                    os.write(cbuf); //Escribir el bloque cifrado en el archivo de salida
                    bytes_leidos = is.read(buffer); //Leer el siguiente bloque
                }
            }

            KeyFactory keyfac = KeyFactory.getInstance("RSA"); //Crear un KeyFactory para trabajar con las claves RSA 
            System.out.println("Generando keyspec");
            //Obtener los componentes de la clave publica con el modulo y exponente
            RSAPublicKeySpec publicKeySpec = keyfac.getKeySpec(keypair.getPublic(), RSAPublicKeySpec.class);
            //Guardar la clave publica en un archivo de texto
            FileOutputStream cos = new FileOutputStream("clave_publica");
            PrintWriter pw = new PrintWriter(cos);
            pw.println(publicKeySpec.getModulus()); //Guardar el modulo
            pw.println(publicKeySpec.getPublicExponent()); //Guardar el exponente
            pw.close();
        } catch (Exception e) {
            //Revisar y mostrar cualquier error
            e.printStackTrace();
        }

    }
}
