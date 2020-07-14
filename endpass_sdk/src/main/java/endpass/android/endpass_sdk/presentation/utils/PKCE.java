package endpass.android.endpass_sdk.presentation.utils;

import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PKCE {


    public static void createCodeVerifier() throws  NoSuchAlgorithmException {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        String verifier = Base64.encodeToString(code, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);


        byte[] bytes = verifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        //Use Apache "Commons Codec" dependency. Import the Base64 class
        //import org.apache.commons.codec.binary.Base64;
        String challenge = encodeBase64URLSafeString(digest);
        challenge = challenge.substring(0, challenge.length() - 2);

        AppSingleton.INSTANCE.setCodeVerifier(verifier);
        AppSingleton.INSTANCE.setCodeChallenge(challenge);


    }


    private static String encodeBase64URLSafeString(byte[] binaryData) {

        return Base64.encodeToString(binaryData, Base64.URL_SAFE);

    }
}
