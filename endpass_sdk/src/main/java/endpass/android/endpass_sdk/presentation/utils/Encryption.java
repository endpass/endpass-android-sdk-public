package endpass.android.endpass_sdk.presentation.utils;

import android.util.Base64;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Encryption {

    private static final String KEY = "DONTHACKMEPLEASE";
    private static final String INIT_VECTOR = "thli1s1ilsSparTa";

    public static String encryptData(String text) {
        return encrypt(text);
    }

    public static String decryptData(String text) {
        return decrypt(text);
    }

    private static String encrypt(String value) {
        try {
            byte[] data = value.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);

            return base64;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.NO_WRAP));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}