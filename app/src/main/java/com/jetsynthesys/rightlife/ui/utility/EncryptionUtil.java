package com.jetsynthesys.rightlife.ui.utility;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding"; // AES with CBC mode and PKCS5 padding

    public static String getEncryptedPassword(String password) throws Exception {
        // Convert the key and IV from Base64
        byte[] keyBytes = Utils.BETTER_RIGHT_LIFE_KEY.getBytes("UTF-8");
        byte[] ivBytes = Base64.decode(Utils.BETTER_RIGHT_LIFE_IV, Base64.DEFAULT); // Use android.util.Base64

        // Create the secret key and IV parameter spec
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // Create the cipher
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        // Encrypt the password
        byte[] encryptedBytes = cipher.doFinal(password.getBytes("UTF-8"));

        // Return the encrypted password as a Base64 encoded string
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }
}