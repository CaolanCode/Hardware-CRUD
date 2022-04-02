package itcarlow.ie;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class HashPassword {

    // hashing password method
    // PBKDF2 algorithm
    public static String hashPassword(String password) {
        // iteration for algorithm
        int iterations = 65536;
        try {
            // create salt for hashing password
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            // generate random salt
            random.nextBytes(salt);
            // create PDEKeySpec and SecretKeyFactory
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 64 * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            try {
                // generate hash
                byte[] hash = factory.generateSecret(spec).getEncoded();
                // return hashed password
                return iterations + ":" +intoHex(salt) + ":" + intoHex(hash);
            } catch (InvalidKeySpecException ikse) {
                throw new RuntimeException(ikse);
            }
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }
    }// hashPassword

    public static boolean checkPassword(String password, String DBPassword) {
        // split stored hash into iteration, salt, hash
        String[] parts = DBPassword.split(":");
        // retrieve iteration
        int iterations = Integer.parseInt(parts[0]);
        // retrieve salt
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            // generate hash
            byte[] testHash = factory.generateSecret(spec).getEncoded();

            int diff = hash.length ^ testHash.length;
            for(int i=0; i<hash.length && i < testHash.length; i++){
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        }catch (InvalidKeySpecException ikse) {
                throw new RuntimeException(ikse);
            } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }

    }//end checkPassword

    // convert hashing to hexadecimal
    private static String intoHex(byte[] array){
        BigInteger bigInteger = new BigInteger(1,array);
        String hexStr = bigInteger.toString(16);
        int paddingLen = (array.length*2) - hexStr.length();
        if(paddingLen>0){
            return String.format("%0"+paddingLen+"d",0) + hexStr;
        }else{
            return hexStr;
        }
    }// end intHex

    // convert hexadecimal to byte[]
    private static byte[] fromHex(String hex){
        byte[] bytes = new byte[hex.length()/2];
        for(int i=0; i<bytes.length; i++){
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2*i + 2), 16);
        }
        return bytes;
    }
}

