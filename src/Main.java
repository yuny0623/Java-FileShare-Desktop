import FileController.CipherHandler;
import FileController.FileHandler;
import Key.KeyMaker;
import Server.Connection;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception{
        System.out.println("ImageGhostClient");

        System.out.println("1. create key pair as string");
        System.out.println("2. create key pair as pem file.");
        System.out.println("3. upload file.");
        System.out.println("3. send file to server.");
        System.out.println("4. exit.");
        System.out.println();

        Scanner sc = new Scanner(System.in);

        while(true){
            int input = sc.nextInt();

            if(input == 1){
                KeyMaker keyMaker = new KeyMaker();
                keyMaker.generateKeyPair();

                System.out.println("**Public Key**");
                System.out.println(keyMaker.getPublicKey());
                System.out.println();
                System.out.println("**Private Key**");
                System.out.println(keyMaker.getPrivateKey());
                System.out.println();

            }else if(input == 2){
                Connection connection = new Connection();
                connection.connectServer();

            }else if(input == 3){
                System.out.println("Enter file path: ");
                String filePath = sc.next();
                FileHandler fileHandler = new FileHandler();
                // File file = fileHandler.uploadFile(filePath);
                System.out.println("1. -----------------------------------");
                //File file = new File("C:\\Users\\dkapq\\OneDrive\\바탕 화면\\ImageGhost\\ImageGhostClient\\Image\\cat.jpg");
                File file = new File("./Image/cat.jpg");

                String result = fileHandler.transferFile2String(file);
                System.out.println(result);
                System.out.println("-----------------------------------");
                System.out.println();
                System.out.println();

                System.out.println("2. -----------------------------------");
                System.out.println(fileHandler.transferString2File(result, "./", "cat2.jpg").getName());
                System.out.println();
                System.out.println("-----------------------------------");

                // *****************************************************
                String plainText = result;

                SecretKey seckey = CipherHandler.getSecretEncryptionKey();    // 대칭키를 받는다.
                byte[] cipherText = CipherHandler.encryptText(plainText, seckey); // 평문을 암호화

                String decryptedText = CipherHandler.decryptText(cipherText, seckey); // 복호화
                System.out.println("Original Text : " + plainText);
                System.out.println();
                System.out.println();
                System.out.println("AES Key (Hex) : " + seckey.getEncoded());
                System.out.println("Encrypted Text (Hex) : " + cipherText);
                System.out.println("Descrypted Text : " + decryptedText);
            }
            else if(input == 4){
                System.exit(0);
            }else{
                System.out.println("1. create key pair.");
                System.out.println("2. send file to server.");
                System.out.println("3. exit");
                System.out.println();
            }
        }
        // test code 실행해봅시다.

    }
}