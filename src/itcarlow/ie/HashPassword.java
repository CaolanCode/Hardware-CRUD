package itcarlow.ie;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class HashPassword {

    // static salt variable
    public static byte[] salt;
    // hashing password method
    // PBKDF2 algorithm
    public static byte[] hashPassword(String password){
        // create salt for hashing password
        SecureRandom random = new SecureRandom();
        salt = new byte[16];
        // generate random salt
        random.nextBytes(salt);
        try{
            // create PDEKeySpec and SecretKeyFactory
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536,128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            try{
                // generate hash
                byte[] hash = factory.generateSecret(spec).getEncoded();
                return hash;
            } catch (InvalidKeySpecException ikse){
                throw new RuntimeException(ikse);
            }
        } catch (NoSuchAlgorithmException nsae){
            throw new RuntimeException(nsae);
        }
    }
}
