package PGPConnection;

public class PGP {
    /*
        PGP Communication Structure Implementation - yuny0623
     */

    /*
        Alice:
           1. generate MAC
           2. encrypt MAC with Alice's private key
           3. add original message to result of 2
           4. generate new symmetric key
           5. encrypt 3 with the result of 4. the symmetric key
           6. put symmetric key in E.E
           7. encrypt E.E with Bob's public key
           8. add all result and send to Bob

        Bob:
            reverse it.
     */
}
