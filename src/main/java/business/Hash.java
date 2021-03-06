package business;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public static String SHA(String password)  {
        byte[] data1 = password.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            return new String(sha1.digest(data1));
        }catch (NoSuchAlgorithmException e){
            return null;
        }
    }
}