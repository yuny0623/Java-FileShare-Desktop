import Key.Key;
import Server.Connection;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("ImageGhostClient");

        System.out.println("1. create key pair as string");
        System.out.println("2. create key pair as pem file.");
        System.out.println("3. send file to server.");
        System.out.println("4. exit.");
        System.out.println();

        Scanner sc = new Scanner(System.in);

        while(true){
            int input = sc.nextInt();

            if(input == 1){
                Key key = new Key();
                key.generateKeyPair();

                System.out.println("**Public Key**");
                System.out.println(key.getPublicKey());
                System.out.println();
                System.out.println("**Private Key**");
                System.out.println(key.getPrivateKey());
                System.out.println();

            }else if(input == 2){
                Connection connection = new Connection();
                connection.connectServer();
            }else if(input == 3){
                System.exit(0);
            }else{
                System.out.println("1. create key pair.");
                System.out.println("2. send file to server.");
                System.out.println("3. exit");
                System.out.println();
            }
        }
    }
}